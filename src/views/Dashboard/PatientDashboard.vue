<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useUserDataStore } from '@/stores/userData';

// ── API endpoints ────────────────────────────────────────────────────────────
// NOTE: /api/patients and /api/appointments are ASSUMED paths based on patterns
// used elsewhere in this project. If your actual endpoints differ, update these.
const PATIENTS_URL      = 'http://localhost:8080/api/patients';
const APPOINTMENTS_URL  = 'http://localhost:8080/api/appointment';
const SOA_PATIENT_URL   = 'http://localhost:8080/api/billing/soa/patient';
const INSTALLMENTS_URL  = 'http://localhost:8080/api/billing/installments';

// ── IMPORTANT: this needs to come from your actual login/session system.
// Hardcoded for now so the page has something to load — replace with the
// real logged-in patient's ID once auth is wired up.
const userStore = useUserDataStore();

// Pulls the real logged-in patient's ID from the auth store instead of a
// hardcoded value. If the store hasn't loaded a user yet (e.g. page was
// refreshed directly), init() restores it from localStorage.
if (!userStore.user) {
    userStore.init();
}
const patientId = computed(() => userStore.user?.patientID ?? null);

const activeTab = ref('calendar');
const patientName = ref('');
const lastLogin = ref(new Date().toLocaleString('en-US', {
    weekday: undefined, hour: 'numeric', minute: '2-digit', hour12: true
}));

const loading = ref(false);

// ── Dashboard stats — now populated from real data ───────────────────────────
const stats = ref({
    upcomingAppointments: 0,
    medicalRecords: 0,
    outstandingBalance: 0,
    servicesUsed: 0
});

const appointments = ref([]);
const soaDetails = ref(null);
const installments = ref([]);

// Safely parses an installment's serviceBreakdown JSON for display —
// returns [] if missing/unparsable rather than throwing.
function parseBreakdown(installment) {
    if (!installment.serviceBreakdown) return [];
    try {
        return JSON.parse(installment.serviceBreakdown);
    } catch (e) {
        return [];
    }
}

// Every unique service/fee name pulled out of the patient's payment history —
// this becomes both "Services Used" count and the Records/Services tab content.
const serviceHistory = computed(() => {
    const items = [];
    installments.value.forEach((installment) => {
        if (!installment.serviceBreakdown) return;
        try {
            const parsed = JSON.parse(installment.serviceBreakdown);
            parsed.forEach((entry) => {
                const name = (entry.name || '').trim();
                // Skip placeholder entries like "No services" / "SOA Service"
                // that aren't real clinical services.
                const isPlaceholder = !name || name.toLowerCase() === 'no services' || name.toLowerCase() === 'soa service';
                if (!entry.isDiscount && !isPlaceholder) {
                    items.push({
                        name: entry.name,
                        amount: entry.amount,
                        date: installment.paymentDate
                    });
                }
            });
        } catch (e) {
            // ignore unparsable breakdowns
        }
    });
    return items;
});

const uniqueServiceNames = computed(() => {
    return [...new Set(serviceHistory.value.map(s => s.name))];
});

// ── Appointment form ──────────────────────────────────────────────────────────
const appointmentForm = ref({
    date: '',
    serviceType: '',
    notes: ''
});

const serviceTypes = ref([
    'Prenatal Checkup', 'Postnatal Care', 'Family Planning',
    'Immunization', 'General Checkup', 'Normal Delivery', 'Other'
]);

function formatCurrency(amount) {
    return '₱' + Number(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function setActiveTab(tab) {
    activeTab.value = tab;
}

// ── Load real data ────────────────────────────────────────────────────────────
async function fetchPatient() {
    try {
        const res = await axios.get(`${PATIENTS_URL}/${patientId.value}`);
        const p = res.data;
        patientName.value = `${p.fName || p.FName || ''} ${p.lName || p.LName || ''}`.trim() || 'Patient';
    } catch (error) {
        console.error('Failed to load patient info', error);
        patientName.value = 'Patient';
    }
}

async function fetchAppointments() {
    try {
        const res = await axios.get(APPOINTMENTS_URL);
        appointments.value = (res.data || []).filter(a => a.patientID === patientId.value);
        stats.value.upcomingAppointments = appointments.value.filter(a => {
            if (!a.appointmentDate) return false;
            return new Date(a.appointmentDate) >= new Date(new Date().setHours(0, 0, 0, 0));
        }).length;
    } catch (error) {
        console.error('Failed to load appointments', error);
    }
}

async function fetchBilling() {
    try {
        const res = await axios.get(`${SOA_PATIENT_URL}/${patientId.value}`);
        soaDetails.value = res.data;
        stats.value.outstandingBalance = res.data.balanceAmount || 0;

        if (res.data.soaId) {
            const instRes = await axios.get(`${INSTALLMENTS_URL}/soa/${res.data.soaId}`);
            installments.value = instRes.data || [];
        }
    } catch (error) {
        console.error('Failed to load billing info', error);
    }
}

async function loadAll() {
    if (!patientId.value) {
        console.warn('No logged-in patient found — skipping data load. Is the user logged in as a Patient?');
        return;
    }
    loading.value = true;
    await Promise.all([fetchPatient(), fetchAppointments(), fetchBilling()]);
    stats.value.servicesUsed = uniqueServiceNames.value.length;
    stats.value.medicalRecords = serviceHistory.value.length;
    loading.value = false;
}

// ── Submit a real appointment request ────────────────────────────────────────
async function submitAppointment() {
    if (!appointmentForm.value.date || !appointmentForm.value.serviceType) {
        alert('Please fill in all required fields (Date and Service Type)');
        return;
    }

    try {
        const [fName, ...rest] = patientName.value.split(' ');
        await axios.post(APPOINTMENTS_URL, {
            patientID: patientId.value,
            fName: fName || '',
            lName: rest.join(' ') || '',
            appointmentDate: appointmentForm.value.date,
            serviceType: appointmentForm.value.serviceType,
            notes: appointmentForm.value.notes
        });

        alert('Appointment request submitted successfully!');
        appointmentForm.value = { date: '', serviceType: '', notes: '' };
        await fetchAppointments();
        setActiveTab('calendar');
    } catch (error) {
        console.error('Failed to submit appointment', error);
        const detail = error.response?.data
            ? (typeof error.response.data === 'string' ? error.response.data : JSON.stringify(error.response.data))
            : error.message;
        alert('Failed to submit appointment: ' + detail);
    }
}

// ── Calendar logic (unchanged, just now fed by real appointments) ───────────
const currentDate = ref(new Date());
const currentMonth = computed(() => currentDate.value.getMonth());
const currentYear = computed(() => currentDate.value.getFullYear());

const monthNames = ["January","February","March","April","May","June","July","August","September","October","November","December"];
const dayNames = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];

const daysInMonth = computed(() => {
    const year = currentYear.value;
    const month = currentMonth.value;
    const date = new Date(year, month, 1);
    const days = [];
    const firstDayIndex = date.getDay();
    for (let i = 0; i < firstDayIndex; i++) days.push({ empty: true, key: `empty-${i}` });
    const numDays = new Date(year, month + 1, 0).getDate();
    for (let i = 1; i <= numDays; i++) {
        days.push({ date: i, fullDate: new Date(year, month, i), empty: false, key: `day-${i}` });
    }
    return days;
});

function nextMonth() { currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1); }
function prevMonth() { currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1); }

// Real appointment events for the calendar — replaces the old mock data
const calendarEvents = computed(() => {
    return appointments.value
        .filter(a => a.appointmentDate)
        .map(a => ({
            date: new Date(a.appointmentDate),
            title: a.serviceType || 'Appointment',
            type: (a.serviceType || '').toLowerCase().includes('delivery') ? 'labor' : 'checkup'
        }));
});

function getEventsForDay(day) {
    if (day.empty) return [];
    return calendarEvents.value.filter(e =>
        e.date.getDate() === day.date &&
        e.date.getMonth() === currentMonth.value &&
        e.date.getFullYear() === currentYear.value
    );
}

function isToday(day) {
    if (day.empty) return false;
    const today = new Date();
    return day.fullDate.getDate() === today.getDate() &&
           day.fullDate.getMonth() === today.getMonth() &&
           day.fullDate.getFullYear() === today.getFullYear();
}

const upcomingEventsList = computed(() => {
    return calendarEvents.value
        .slice()
        .sort((a, b) => a.date - b.date)
        .filter(e => e.date >= new Date(new Date().setHours(0, 0, 0, 0)));
});

onMounted(() => {
    loadAll();
});
</script>

<template>
    <div v-if="!patientId" class="min-h-screen bg-gray-50 flex items-center justify-center">
        <div class="text-center p-8 bg-white rounded-lg shadow-sm border border-gray-200">
            <p class="text-gray-700 font-semibold">No logged-in patient found.</p>
            <p class="text-gray-500 text-sm mt-1">Please log in with a Patient account to view this dashboard.</p>
        </div>
    </div>
    <div v-else class="min-h-screen bg-gray-50">
        <!-- Header -->
        <div class="bg-white border-b border-gray-200 px-8 py-6">
            <div class="flex items-start justify-between">
                <div>
                    <h1 class="text-4xl font-bold text-gray-800">Welcome, {{ patientName || '...' }}</h1>
                    <p class="text-gray-600 mt-2">You can manage your appointments, view medical records, and track payments here.</p>
                </div>
                <div class="text-right">
                    <p class="text-sm text-gray-600">Last Login</p>
                    <p class="text-lg font-semibold text-gray-800">{{ lastLogin }}</p>
                </div>
            </div>
        </div>

        <!-- Stats Cards -->
        <div class="px-8 py-8 bg-gray-100">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Upcoming Appointments</p>
                            <p class="text-4xl font-bold text-gray-800">{{ loading ? '—' : stats.upcomingAppointments }}</p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Services Availed (Records)</p>
                            <p class="text-4xl font-bold text-gray-800">{{ loading ? '—' : stats.medicalRecords }}</p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Outstanding Balance</p>
                            <p class="text-4xl font-bold text-red-600">{{ loading ? '—' : formatCurrency(stats.outstandingBalance) }}</p>
                        </div>
                        <div class="bg-red-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Distinct Services Used</p>
                            <p class="text-4xl font-bold text-gray-800">{{ loading ? '—' : stats.servicesUsed }}</p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabs -->
        <div class="px-8 py-4 bg-white border-b border-gray-200 overflow-x-auto">
            <div class="flex space-x-1 min-w-max">
                <button @click="setActiveTab('calendar')"
                    :class="['px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'calendar' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100']">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    Calendar
                </button>
                <button @click="setActiveTab('appointment')"
                    :class="['px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'appointment' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100']">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    Appointment
                </button>
                <button @click="setActiveTab('records')"
                    :class="['px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'records' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100']">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                    </svg>
                    Records
                </button>
                <button @click="setActiveTab('payments')"
                    :class="['px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'payments' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100']">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                    </svg>
                    Payments
                </button>
                <button @click="setActiveTab('services')"
                    :class="['px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'services' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100']">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                    Services
                </button>
            </div>
        </div>

        <!-- Content Area -->
        <div class="px-8 py-8">

            <!-- Calendar Tab -->
            <div v-if="activeTab === 'calendar'" class="bg-white rounded-lg shadow-sm p-8">
                <div class="flex items-center justify-between mb-6">
                    <div>
                        <h2 class="text-2xl font-bold text-gray-800">My Schedule</h2>
                        <p class="text-gray-600">View your upcoming checkups and expected day of labor</p>
                    </div>
                    <div class="flex space-x-4">
                        <div class="flex items-center space-x-2">
                            <span class="w-3 h-3 rounded-full bg-blue-500"></span>
                            <span class="text-sm font-medium text-gray-600">Checkup</span>
                        </div>
                        <div class="flex items-center space-x-2">
                            <span class="w-3 h-3 rounded-full bg-pink-500"></span>
                            <span class="text-sm font-medium text-gray-600">Delivery</span>
                        </div>
                    </div>
                </div>

                <div class="flex items-center justify-between mb-4 bg-gray-50 p-4 rounded-lg border border-gray-200">
                    <button @click="prevMonth" class="p-2 hover:bg-gray-200 rounded-full transition-colors">
                        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                        </svg>
                    </button>
                    <h3 class="text-xl font-bold text-gray-800">{{ monthNames[currentMonth] }} {{ currentYear }}</h3>
                    <button @click="nextMonth" class="p-2 hover:bg-gray-200 rounded-full transition-colors">
                        <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                        </svg>
                    </button>
                </div>

                <div class="grid grid-cols-7 gap-px bg-gray-200 border border-gray-200 rounded-lg overflow-hidden">
                    <div v-for="day in dayNames" :key="day" class="bg-gray-100 text-center py-3 font-semibold text-sm text-gray-700">
                        {{ day }}
                    </div>
                    <div v-for="day in daysInMonth" :key="day.key" class="bg-white min-h-[120px] p-2 transition-colors"
                        :class="{'bg-gray-50': day.empty}">
                        <div v-if="!day.empty" class="h-full flex flex-col">
                            <div class="flex justify-between items-start mb-2">
                                <span class="text-sm font-medium w-8 h-8 flex items-center justify-center rounded-full"
                                    :class="isToday(day) ? 'bg-blue-600 text-white shadow-md' : 'text-gray-700'">
                                    {{ day.date }}
                                </span>
                            </div>
                            <div class="flex-1 overflow-y-auto space-y-1">
                                <div v-for="(event, index) in getEventsForDay(day)" :key="index"
                                    class="text-xs px-2 py-1.5 rounded truncate font-medium"
                                    :class="event.type === 'labor' ? 'bg-pink-100 text-pink-700 border border-pink-200' : 'bg-blue-100 text-blue-700 border border-blue-200'"
                                    :title="event.title">
                                    {{ event.title }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="mt-8">
                    <h3 class="text-lg font-bold text-gray-800 mb-4">Upcoming Events</h3>
                    <div class="grid gap-4 md:grid-cols-2">
                        <div v-for="(event, index) in upcomingEventsList" :key="'upcoming-'+index"
                             class="flex items-center p-4 bg-gray-50 rounded-xl border border-gray-200 hover:shadow-md transition-shadow">
                            <div :class="['w-3 h-12 rounded-full mr-4 flex-shrink-0', event.type === 'labor' ? 'bg-pink-500' : 'bg-blue-500']"></div>
                            <div>
                                <p class="font-bold text-gray-800 text-lg">{{ event.title }}</p>
                                <p class="text-sm font-medium text-gray-500 mt-1 flex items-center gap-1">
                                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                                    {{ event.date.toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }) }}
                                </p>
                            </div>
                        </div>
                        <div v-if="upcomingEventsList.length === 0" class="col-span-2 p-8 text-center bg-gray-50 rounded-xl border border-gray-200 border-dashed">
                            <p class="text-gray-500 font-medium">No upcoming events scheduled.</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Appointment Tab -->
            <div v-if="activeTab === 'appointment'" class="bg-white rounded-lg shadow-sm p-8">
                <div class="mb-6">
                    <h2 class="text-2xl font-bold text-gray-800 mb-2">Create Online Appointment</h2>
                    <p class="text-gray-600">Schedule your next appointment with our clinic</p>
                </div>

                <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                    <p class="text-sm text-blue-800">
                        <span class="font-semibold">Note:</span> Online appointment is available only if you already have an existing clinic record.
                    </p>
                </div>

                <div class="space-y-6">
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Appointment Date</label>
                        <input v-model="appointmentForm.date" type="date"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Service Type</label>
                        <select v-model="appointmentForm.serviceType"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white">
                            <option value="">Select a service</option>
                            <option v-for="service in serviceTypes" :key="service" :value="service">{{ service }}</option>
                        </select>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Notes / Reason for Visit</label>
                        <textarea v-model="appointmentForm.notes" rows="4"
                            placeholder="Please describe your symptoms or reason for visit..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"></textarea>
                    </div>

                    <button @click="submitAppointment"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-4 rounded-lg transition-colors shadow-md">
                        Submit Appointment Request
                    </button>
                </div>
            </div>

            <!-- Records Tab — derived from real payment/service breakdown history -->
            <div v-if="activeTab === 'records'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Medical / Service Records</h2>
                <p class="text-gray-600 mb-6">Every service recorded against your account, based on your billing history.</p>

                <div v-if="serviceHistory.length === 0" class="text-center py-12 text-gray-400 italic">
                    No service records found yet.
                </div>
                <div v-else class="space-y-2">
                    <div v-for="(record, index) in serviceHistory" :key="index"
                        class="flex justify-between items-center p-4 bg-gray-50 rounded-lg border border-gray-200">
                        <div>
                            <p class="font-semibold text-gray-800">{{ record.name }}</p>
                            <p class="text-xs text-gray-500">
                                {{ record.date ? new Date(record.date).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) : 'Date not recorded' }}
                            </p>
                        </div>
                        <p class="font-semibold text-gray-700">{{ formatCurrency(record.amount) }}</p>
                    </div>
                </div>
            </div>

            <!-- Payments Tab — real SOA + payment installment history -->
            <div v-if="activeTab === 'payments'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Payment History</h2>

                <div v-if="soaDetails" class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
                    <div class="bg-orange-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Total Bill</p>
                        <p class="text-xl font-bold text-orange-500">{{ formatCurrency(soaDetails.totalAmount) }}</p>
                    </div>
                    <div class="bg-green-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Total Paid</p>
                        <p class="text-xl font-bold text-green-600">{{ formatCurrency(soaDetails.amountPaid) }}</p>
                    </div>
                    <div class="bg-red-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Balance</p>
                        <p class="text-xl font-bold text-red-500">{{ formatCurrency(soaDetails.balanceAmount) }}</p>
                    </div>
                </div>

                <div v-if="installments.length === 0" class="text-center py-12 text-gray-400 italic">
                    No payment history found yet.
                </div>
                <div v-else class="space-y-3">
                    <div v-for="installment in installments" :key="installment.installmentId"
                        class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                        <div class="flex justify-between items-center mb-2">
                            <div>
                                <p class="font-semibold text-gray-800">Payment #{{ installment.installmentNumber }}</p>
                                <p class="text-xs text-gray-500">
                                    {{ installment.paymentDate ? new Date(installment.paymentDate).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) : '' }}
                                    — {{ installment.paymentMethod }}
                                </p>
                            </div>
                            <p class="font-semibold text-green-600">{{ formatCurrency(installment.amountPaid) }}</p>
                        </div>

                        <!-- Itemized services/discounts for this payment, same detail as the admin SOA view -->
                        <div v-if="parseBreakdown(installment).length" class="border-t border-gray-200 mt-2 pt-2 space-y-1">
                            <div v-for="(item, i) in parseBreakdown(installment)" :key="i"
                                class="flex justify-between text-sm"
                                :class="item.isDiscount ? 'text-red-600' : 'text-gray-600'">
                                <span>
                                    {{ item.name }}
                                    <span v-if="item.isDiscount" class="text-xs italic">(Discount)</span>
                                </span>
                                <span>{{ item.isDiscount ? '- ' : '' }}{{ formatCurrency(Math.abs(item.amount)) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Services Tab — unique services derived from billing history -->
            <div v-if="activeTab === 'services'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Services Availed</h2>
                <div v-if="uniqueServiceNames.length === 0" class="text-center py-12 text-gray-400 italic">
                    No services recorded yet.
                </div>
                <div v-else class="flex flex-wrap gap-2">
                    <span v-for="name in uniqueServiceNames" :key="name"
                        class="px-4 py-2 bg-blue-50 text-blue-700 rounded-full text-sm font-medium border border-blue-200">
                        {{ name }}
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
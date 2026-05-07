<script setup>
import { ref, computed } from 'vue';

const activeTab = ref('calendar');
const patientName = ref('John Doe');
const lastLogin = ref('Today at 10:30 AM');

// Dashboard stats
const stats = ref({
    upcomingAppointments: 2,
    medicalRecords: 5,
    outstandingBalance: 180.00,
    servicesUsed: 6
});

// Appointment form
const appointmentForm = ref({
    date: '',
    serviceType: '',
    notes: ''
});

const serviceTypes = ref([
    'Prenatal Checkup',
    'Postnatal Care',
    'Family Planning',
    'Immunization',
    'General Checkup',
    'Normal Delivery',
    'Other'
]);

function submitAppointment() {
    if (!appointmentForm.value.date || !appointmentForm.value.serviceType) {
        alert('Please fill in all required fields (Date and Service Type)');
        return;
    }

    alert('Appointment request submitted successfully!');
    
    // Reset form
    appointmentForm.value = {
        date: '',
        serviceType: '',
        notes: ''
    };
}

function formatCurrency(amount) {
    return '₱' + amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function setActiveTab(tab) {
    activeTab.value = tab;
}

// Calendar Logic
const currentDate = ref(new Date());

const currentMonth = computed(() => currentDate.value.getMonth());
const currentYear = computed(() => currentDate.value.getFullYear());

const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

const daysInMonth = computed(() => {
    const year = currentYear.value;
    const month = currentMonth.value;
    const date = new Date(year, month, 1);
    const days = [];
    
    // Get day of week of the first day (0 = Sunday, 1 = Monday, etc.)
    const firstDayIndex = date.getDay();
    
    // Add empty slots for days before the 1st
    for (let i = 0; i < firstDayIndex; i++) {
        days.push({ empty: true, key: `empty-${i}` });
    }
    
    // Add the actual days of the month
    const numDays = new Date(year, month + 1, 0).getDate();
    for (let i = 1; i <= numDays; i++) {
        days.push({
            date: i,
            fullDate: new Date(year, month, i),
            empty: false,
            key: `day-${i}`
        });
    }
    
    return days;
});

function nextMonth() {
    currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1);
}

function prevMonth() {
    currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1);
}

// Mock Events
const mockEvents = ref([
    {
        date: new Date(new Date().getFullYear(), new Date().getMonth(), 15),
        title: 'Prenatal Checkup',
        type: 'checkup'
    },
    {
        date: new Date(new Date().getFullYear(), new Date().getMonth(), 28),
        title: 'Ultrasound',
        type: 'checkup'
    },
    {
        date: new Date(new Date().getFullYear(), new Date().getMonth() + 1, 10),
        title: 'Expected Day of Labor',
        type: 'labor'
    }
]);

function getEventsForDay(day) {
    if (day.empty) return [];
    return mockEvents.value.filter(e => {
        return e.date.getDate() === day.date && 
               e.date.getMonth() === currentMonth.value && 
               e.date.getFullYear() === currentYear.value;
    });
}

function isToday(day) {
    if (day.empty) return false;
    const today = new Date();
    return day.fullDate.getDate() === today.getDate() &&
           day.fullDate.getMonth() === today.getMonth() &&
           day.fullDate.getFullYear() === today.getFullYear();
}
</script>

<template>
    <div class="min-h-screen bg-gray-50">
        <!-- Header -->
        <div class="bg-white border-b border-gray-200 px-8 py-6">
            <div class="flex items-start justify-between">
                <div>
                    <h1 class="text-4xl font-bold text-gray-800">Welcome, {{ patientName }}</h1>
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
                <!-- Upcoming Appointments -->
                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Upcoming Appointments</p>
                            <p class="text-4xl font-bold text-gray-800">{{ stats.upcomingAppointments }}</p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- Medical Records -->
                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Medical Records</p>
                            <p class="text-4xl font-bold text-gray-800">{{ stats.medicalRecords }}</p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- Outstanding Balance -->
                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Outstanding Balance</p>
                            <p class="text-4xl font-bold text-red-600">{{ formatCurrency(stats.outstandingBalance) }}</p>
                        </div>
                        <div class="bg-red-100 p-3 rounded-lg">
                            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                            </svg>
                        </div>
                    </div>
                </div>

                <!-- Services Used -->
                <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
                    <div class="flex items-start justify-between">
                        <div>
                            <p class="text-sm text-gray-600 mb-2">Services Used</p>
                            <p class="text-4xl font-bold text-gray-800">{{ stats.servicesUsed }}</p>
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
                <button
                    @click="setActiveTab('calendar')"
                    :class="[
                        'px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'calendar' 
                            ? 'bg-blue-600 text-white' 
                            : 'text-gray-600 hover:bg-gray-100'
                    ]"
                >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    Calendar
                </button>
                <button
                    @click="setActiveTab('appointment')"
                    :class="[
                        'px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'appointment' 
                            ? 'bg-blue-600 text-white' 
                            : 'text-gray-600 hover:bg-gray-100'
                    ]"
                >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    Appointment
                </button>

                <button
                    @click="setActiveTab('records')"
                    :class="[
                        'px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'records' 
                            ? 'bg-blue-600 text-white' 
                            : 'text-gray-600 hover:bg-gray-100'
                    ]"
                >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                    </svg>
                    Records
                </button>

                <button
                    @click="setActiveTab('payments')"
                    :class="[
                        'px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'payments' 
                            ? 'bg-blue-600 text-white' 
                            : 'text-gray-600 hover:bg-gray-100'
                    ]"
                >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                    </svg>
                    Payments
                </button>

                <button
                    @click="setActiveTab('services')"
                    :class="[
                        'px-6 py-3 rounded-lg font-medium transition-colors flex items-center gap-2',
                        activeTab === 'services' 
                            ? 'bg-blue-600 text-white' 
                            : 'text-gray-600 hover:bg-gray-100'
                    ]"
                >
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
                            <span class="text-sm font-medium text-gray-600">Day of Labor</span>
                        </div>
                    </div>
                </div>

                <!-- Calendar Header -->
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

                <!-- Calendar Grid -->
                <div class="grid grid-cols-7 gap-px bg-gray-200 border border-gray-200 rounded-lg overflow-hidden">
                    <!-- Days of week -->
                    <div v-for="day in dayNames" :key="day" class="bg-gray-100 text-center py-3 font-semibold text-sm text-gray-700">
                        {{ day }}
                    </div>
                    
                    <!-- Days -->
                    <div 
                        v-for="day in daysInMonth" 
                        :key="day.key" 
                        class="bg-white min-h-[120px] p-2 transition-colors"
                        :class="{'bg-gray-50': day.empty}"
                    >
                        <div v-if="!day.empty" class="h-full flex flex-col">
                            <div class="flex justify-between items-start mb-2">
                                <span 
                                    class="text-sm font-medium w-8 h-8 flex items-center justify-center rounded-full"
                                    :class="isToday(day) ? 'bg-blue-600 text-white shadow-md' : 'text-gray-700'"
                                >
                                    {{ day.date }}
                                </span>
                            </div>
                            <div class="flex-1 overflow-y-auto space-y-1">
                                <div 
                                    v-for="(event, index) in getEventsForDay(day)" 
                                    :key="index"
                                    class="text-xs px-2 py-1.5 rounded truncate font-medium"
                                    :class="event.type === 'labor' ? 'bg-pink-100 text-pink-700 border border-pink-200' : 'bg-blue-100 text-blue-700 border border-blue-200'"
                                    :title="event.title"
                                >
                                    {{ event.title }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Upcoming List -->
                <div class="mt-8">
                    <h3 class="text-lg font-bold text-gray-800 mb-4">Upcoming Events</h3>
                    <div class="grid gap-4 md:grid-cols-2">
                        <div v-for="(event, index) in mockEvents.slice().sort((a,b) => a.date - b.date).filter(e => e.date >= new Date(new Date().setHours(0,0,0,0)))" :key="'upcoming-'+index" 
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
                        
                        <div v-if="mockEvents.filter(e => e.date >= new Date(new Date().setHours(0,0,0,0))).length === 0" class="col-span-2 p-8 text-center bg-gray-50 rounded-xl border border-gray-200 border-dashed">
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

                <!-- Note -->
                <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                    <p class="text-sm text-blue-800">
                        <span class="font-semibold">Note:</span> Online appointment is available only if you already have an existing clinic record.
                    </p>
                </div>

                <!-- Form -->
                <div class="space-y-6">
                    <!-- Appointment Date -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Appointment Date
                        </label>
                        <input
                            v-model="appointmentForm.date"
                            type="date"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                    </div>

                    <!-- Service Type -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Service Type
                        </label>
                        <select
                            v-model="appointmentForm.serviceType"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white"
                        >
                            <option value="">Select a service</option>
                            <option v-for="service in serviceTypes" :key="service" :value="service">
                                {{ service }}
                            </option>
                        </select>
                    </div>

                    <!-- Notes / Reason for Visit -->
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Notes / Reason for Visit
                        </label>
                        <textarea
                            v-model="appointmentForm.notes"
                            rows="4"
                            placeholder="Please describe your symptoms or reason for visit..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                        ></textarea>
                    </div>

                    <!-- Submit Button -->
                    <button
                        @click="submitAppointment"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-4 rounded-lg transition-colors shadow-md"
                    >
                        Submit Appointment Request
                    </button>
                </div>
            </div>

            <!-- Other Tabs (Placeholders) -->
            <div v-if="activeTab === 'records'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Medical Records</h2>
                <p class="text-gray-600">Your medical records will be displayed here.</p>
            </div>

            <div v-if="activeTab === 'payments'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Payment History</h2>
                <p class="text-gray-600">Your payment history and outstanding balance details will be shown here.</p>
            </div>

            <div v-if="activeTab === 'services'" class="bg-white rounded-lg shadow-sm p-8">
                <h2 class="text-2xl font-bold text-gray-800 mb-4">Services Used</h2>
                <p class="text-gray-600">A list of all services you've used will be displayed here.</p>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
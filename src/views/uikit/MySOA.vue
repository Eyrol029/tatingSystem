<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import { useUserDataStore } from '@/stores/userData';
import PromissoryLetter from './PromissoryLetter.vue';

const route = useRoute();
const router = useRouter();
const SOA_PATIENT_URL = 'http://localhost:8080/api/billing/soa/patient';
const INSTALLMENTS_URL = 'http://localhost:8080/api/billing/installments';
const SMS_URL = 'http://localhost:8080/api/sms/send';

const userStore = useUserDataStore();
if (!userStore.user) {
    try {
        userStore.init();
    } catch (e) {
        console.error('Failed to init userStore', e);
    }
}

function goBack() {
    router.back();
}

// If a patientId is present in the route (e.g. staff navigating from the
// Payment Dashboard's "View" button), use that. Otherwise fall back to the
// logged-in user's own patient ID (when a Patient views their own SOA page).
// Tries a couple of common param-name/casing variants defensively, since
// different routes in this project use different conventions.
const patientId = computed(() => {
    const fromRoute =
        route.params.patientId ??
        route.params.patientID ??
        route.params.id ??
        null;
    if (fromRoute != null && fromRoute !== '') return Number(fromRoute);

    try {
        return userStore.user?.patientID ?? null;
    } catch (e) {
        console.error('Failed to read patientID from userStore', e);
        return null;
    }
});

const loading = ref(true);
const error = ref('');
const soaDetails = ref(null);
const installments = ref([]);
const showPromissoryModal = ref(false);
const employeeList = ref([]);

function getEmployeeFullName(employee) {
    if (!employee) return '';
    const first = employee.fName || employee.firstName || employee.firstname || '';
    const last = employee.lName || employee.lastName || employee.lastname || '';
    return `${first} ${last}`.trim();
}

function formatCurrency(value) {
    return '₱' + Number(value || 0).toLocaleString('en-US', { minimumFractionDigits: 2 });
}

function formatDate(value) {
    if (!value) return '—';
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return '—';
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}

// Safely parses an installment's serviceBreakdown JSON for display —
// returns [] if missing/unparsable rather than throwing.
function parseBreakdown(installment) {
    if (!installment || !installment.serviceBreakdown) return [];
    try {
        const parsed = JSON.parse(installment.serviceBreakdown);
        return Array.isArray(parsed) ? parsed : [];
    } catch (e) {
        console.warn('Failed to parse serviceBreakdown for installment', installment, e);
        return [];
    }
}

async function loadSOA() {
    console.log('MySOA — route.params:', route.params);
    console.log('MySOA — resolved patientId:', patientId.value);

    if (!patientId.value) {
        error.value = 'No patient ID found for this Statement of Account. Please go back and try again.';
        loading.value = false;
        return;
    }

    loading.value = true;
    error.value = '';
    try {
        const res = await axios.get(`${SOA_PATIENT_URL}/${patientId.value}`);
        console.log('MySOA — SOA response:', res.data);
        soaDetails.value = res.data || null;

        const soaId = res.data?.soaId ?? res.data?.soaID ?? res.data?.id ?? null;
        if (soaId) {
            try {
                const instRes = await axios.get(`${INSTALLMENTS_URL}/soa/${soaId}`);
                const list = Array.isArray(instRes.data) ? instRes.data : [];
                installments.value = list.slice().reverse(); // newest first
            } catch (instErr) {
                console.error('Failed to load installments (SOA itself still loaded fine)', instErr);
                installments.value = [];
            }
        } else {
            installments.value = [];
        }
    } catch (e) {
        console.error('Failed to load Statement of Account', e);
        if (e?.response?.status === 404) {
            // No SOA exists yet for this patient — not a hard error.
            soaDetails.value = null;
        } else {
            error.value = 'Failed to load your Statement of Account. Please try again later.';
        }
    } finally {
        loading.value = false;
    }
}

function printSOA() {
    window.print();
}

async function loadStaffOptions() {
    try {
        const res = await axios.get('http://localhost:8080/api/employees');
        const raw = Array.isArray(res.data) ? res.data : [];
        employeeList.value = raw
            .filter(emp => getEmployeeFullName(emp))
            .map(emp => ({
                id: emp.employeeID ?? emp.id ?? null,
                name: getEmployeeFullName(emp),
                department: emp.department || '',
                position: emp.position || ''
            }));

    } catch (e) {
        console.error('Failed to load staff list for promissory letter', e);
        employeeList.value = [];
    }
}

const patientDisplayName = computed(() => {
    if (soaDetails.value?.patientName) {
        return soaDetails.value.patientName;
    }
    if (userStore.user?.firstname || userStore.user?.fName) {
        const first = userStore.user.firstname || userStore.user.fName || '';
        const last = userStore.user.lastname || userStore.user.lName || '';
        const full = `${first} ${last}`.trim();
        if (full) return full;
    }
    return 'Statement of Account';
});

const showSmsModal = ref(false);
const smsRecipient = ref('');
const smsMessage = ref('');
const sendingSms = ref(false);
const smsSuccessAlert = ref('');
const smsErrorAlert = ref('');

async function openSmsModal() {
    smsSuccessAlert.value = '';
    smsErrorAlert.value = '';

    if (patientId.value) {
        try {
            const patRes = await axios.get(`http://localhost:8080/api/patients/${patientId.value}`);
            if (patRes.data && patRes.data.contactNumber) {
                smsRecipient.value = patRes.data.contactNumber;
            }
        } catch (err) {
            console.warn('Could not auto-fetch patient contact number', err);
        }
    }
    if (!smsRecipient.value && userStore.user?.contactNumber) {
        smsRecipient.value = userStore.user.contactNumber;
    }

    const pName = patientDisplayName.value || 'Patient';
    const total = soaDetails.value ? formatCurrency(soaDetails.value.totalAmount) : '₱0.00';
    const paid = soaDetails.value ? formatCurrency(soaDetails.value.amountPaid) : '₱0.00';
    const balance = soaDetails.value ? formatCurrency(soaDetails.value.balanceAmount) : '₱0.00';
    const status = soaDetails.value?.paymentStatus || 'Pending';

    smsMessage.value = `Hello ${pName}, here is your Statement of Account summary from Tating Maternity Clinic:\nTotal Bill: ${total}\nTotal Paid: ${paid}\nBalance: ${balance}\nStatus: ${status}\n\nThank you!`;

    showSmsModal.value = true;
}

async function sendSMS() {
    if (!smsRecipient.value || !smsRecipient.value.trim()) {
        smsErrorAlert.value = 'Please enter a valid recipient phone number.';
        return;
    }
    if (!smsMessage.value || !smsMessage.value.trim()) {
        smsErrorAlert.value = 'Message cannot be empty.';
        return;
    }

    sendingSms.value = true;
    smsErrorAlert.value = '';
    smsSuccessAlert.value = '';

    try {
        const payload = {
            recipient: smsRecipient.value.trim(),
            message: smsMessage.value.trim(),
            patientId: patientId.value || null,
            soaId: soaDetails.value?.soaId ?? null
        };

        const res = await axios.post(SMS_URL, payload);

        if (res.data && res.data.success) {
            smsSuccessAlert.value = res.data.message || `SMS successfully sent to ${smsRecipient.value}!`;
            setTimeout(() => {
                showSmsModal.value = false;
                smsSuccessAlert.value = '';
            }, 2000);
        } else {
            smsErrorAlert.value = res.data?.message || 'Failed to send SMS via gateway.';
        }
    } catch (err) {
        console.error('Failed to send SMS', err);
        const backendError = err.response?.data?.message || err.message;
        smsErrorAlert.value = `Failed to send SMS: ${backendError}`;
    } finally {
        sendingSms.value = false;
    }
}

function openNativeSms() {
    if (!smsRecipient.value) {
        smsErrorAlert.value = 'Please enter a phone number first.';
        return;
    }
    const cleanNumber = smsRecipient.value.replace(/[^0-9+]/g, '');
    const url = `sms:${cleanNumber}?body=${encodeURIComponent(smsMessage.value)}`;
    window.open(url, '_blank');
}

onMounted(async () => {
    await loadStaffOptions();
    await loadSOA();
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">
        <div class="max-w-4xl mx-auto">

            <!-- Toolbar -->
            <div class="mb-4 no-print">
                <button @click="goBack"
                    class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg shadow transition">
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                    </svg>
                    Back
                </button>
            </div>

            <!-- Header -->
            <div class="flex justify-between items-center mb-6 no-print">
                <h1 class="text-2xl font-bold text-gray-800">{{ patientDisplayName }}</h1>
                <div class="flex items-center gap-3">
                    <button @click="openSmsModal"
                        class="bg-emerald-600 text-white px-4 py-2 rounded-lg hover:bg-emerald-700 transition flex items-center gap-2 shadow-sm font-medium">
                        💬 Send SMS
                    </button>
                    <button @click="showPromissoryModal = true"
                        class="bg-violet-600 text-white px-4 py-2 rounded-lg hover:bg-violet-700 transition flex items-center gap-2 shadow-sm font-medium">
                        📝 Promissory Letter
                    </button>
                    <button @click="printSOA"
                        class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition flex items-center gap-2 shadow-sm font-medium">
                        🖨️ Print
                    </button>
                </div>
            </div>

            <div v-if="loading" class="text-center py-20 text-gray-500">
                Loading your Statement of Account...
            </div>

            <div v-else-if="error" class="text-center py-20 text-red-500">
                {{ error }}
            </div>

            <div v-else-if="!soaDetails" class="bg-white rounded-lg shadow-sm border border-gray-200 p-12 text-center">
                <p class="text-gray-500">No Statement of Account found yet.</p>
            </div>

            <div v-else>
                <!-- Patient info -->
                <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                    <div class="flex justify-between items-start">
                        <div>
                            <p class="text-sm text-gray-500">Patient</p>
                            <p class="font-semibold text-gray-900 text-lg">{{ soaDetails.patientName || '—' }}</p>
                        </div>
                        <span
                            class="px-3 py-1 rounded-full text-xs font-semibold"
                            :class="{
                                'bg-green-100 text-green-700': soaDetails.paymentStatus === 'Paid',
                                'bg-yellow-100 text-yellow-700': soaDetails.paymentStatus === 'Partial',
                                'bg-red-100 text-red-700': soaDetails.paymentStatus === 'Pending'
                            }"
                        >
                            {{ soaDetails.paymentStatus || 'Unknown' }}
                        </span>
                    </div>
                </div>

                <!-- Totals -->
                <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
                    <div class="bg-orange-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Total Bill</p>
                        <p class="text-2xl font-bold text-orange-500">{{ formatCurrency(soaDetails.totalAmount) }}</p>
                    </div>
                    <div class="bg-green-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Total Paid</p>
                        <p class="text-2xl font-bold text-green-600">{{ formatCurrency(soaDetails.amountPaid) }}</p>
                    </div>
                    <div class="bg-red-50 rounded-lg p-4">
                        <p class="text-gray-500 text-sm mb-1">Balance</p>
                        <p class="text-2xl font-bold text-red-500">{{ formatCurrency(soaDetails.balanceAmount) }}</p>
                    </div>
                </div>

                <!-- Payment history -->
                <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
                    <div class="px-6 py-4 border-b border-gray-200">
                        <h3 class="text-lg font-semibold text-gray-800">Service Availed</h3>
                    </div>

                    <div v-if="installments.length === 0" class="text-center py-12 text-gray-400 italic">
                        No payments recorded yet.
                    </div>

                    <div v-else class="divide-y divide-gray-200">
                        <div v-for="installment in installments" :key="installment.installmentId ?? installment.installmentID ?? installment.id" class="p-6">
                            <div class="flex justify-between items-center mb-2">
                                <div>
                                    <p class="font-semibold text-gray-800">Payment #{{ installment.installmentNumber }}</p>
                                    <p class="text-xs text-gray-500">
                                        {{ formatDate(installment.paymentDate) }} — {{ installment.paymentMethod || '—' }}
                                    </p>
                                </div>
                                <p class="font-semibold text-green-600 text-lg">{{ formatCurrency(installment.amountPaid) }}</p>
                            </div>

                            <!-- Itemized breakdown, same detail as the admin SOA view -->
                            <div v-if="parseBreakdown(installment).length" class="border-t border-gray-100 mt-3 pt-3 space-y-1">
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
            </div>
        </div>

        <PromissoryLetter
            v-if="showPromissoryModal"
            :patient-id="patientId"
            :patient-name="patientDisplayName"
            :soa-details="soaDetails"
            :employee-list="employeeList"
            @close="showPromissoryModal = false"
            @saved="showPromissoryModal = false"
        />

        <!-- SMS Modal -->
        <div v-if="showSmsModal" class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50 no-print">
            <div class="bg-white rounded-xl shadow-2xl max-w-lg w-full p-6 border border-gray-200">
                <div class="flex justify-between items-center mb-4 pb-3 border-b border-gray-100">
                    <div class="flex items-center gap-2">
                        <div class="p-2 bg-emerald-100 text-emerald-600 rounded-lg">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M8 10h.01M12 10h.01M16 10h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                            </svg>
                        </div>
                        <h2 class="text-xl font-bold text-gray-800">Send SOA via SMS</h2>
                    </div>
                    <button @click="showSmsModal = false" class="text-gray-400 hover:text-gray-600 transition text-2xl leading-none">
                        &times;
                    </button>
                </div>

                <div v-if="smsSuccessAlert" class="mb-4 p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm rounded-lg flex items-center gap-2">
                    <svg class="w-5 h-5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                    </svg>
                    <span>{{ smsSuccessAlert }}</span>
                </div>

                <div v-if="smsErrorAlert" class="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg">
                    {{ smsErrorAlert }}
                </div>

                <div class="space-y-4">
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                            Recipient Phone Number
                        </label>
                        <input
                            v-model="smsRecipient"
                            type="tel"
                            placeholder="e.g. 09123456789"
                            class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-emerald-500 focus:outline-none"
                        />
                    </div>

                    <div>
                        <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">
                            SMS Message Preview
                        </label>
                        <textarea
                            v-model="smsMessage"
                            rows="6"
                            class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-emerald-500 focus:outline-none font-mono text-xs resize-none"
                        ></textarea>
                    </div>
                </div>

                <div class="flex justify-between items-center mt-6 pt-4 border-t border-gray-100 gap-2">
                    <div class="flex items-center gap-2">
                        <button
                            @click="showSmsModal = false"
                            type="button"
                            class="px-4 py-2 text-sm font-medium text-gray-600 hover:bg-gray-100 rounded-lg transition"
                        >
                            Cancel
                        </button>
                        <button
                            @click="sendSMS"
                            :disabled="sendingSms"
                            type="button"
                            class="px-5 py-2 text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 rounded-lg shadow transition flex items-center gap-2"
                        >
                            <svg v-if="sendingSms" class="animate-spin h-4 w-4 text-white" fill="none" viewBox="0 0 24 24">
                                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"></path>
                            </svg>
                            <span>{{ sendingSms ? 'Sending...' : 'Send SMS' }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
@media print {
    .no-print { display: none !important; }
    body, html { margin: 0 !important; padding: 0 !important; background: white !important; }
}
</style>
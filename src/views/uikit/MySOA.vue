<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import { useUserDataStore } from '@/stores/userData';

const route = useRoute();
const router = useRouter();
const SOA_PATIENT_URL = 'http://localhost:8080/api/billing/soa/patient';
const INSTALLMENTS_URL = 'http://localhost:8080/api/billing/installments';

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

onMounted(() => {
    loadSOA();
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
                <h1 class="text-2xl font-bold text-gray-800">My Statement of Account</h1>
                <button @click="printSOA"
                    class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition flex items-center gap-2">
                    🖨️ Print
                </button>
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
                        <h3 class="text-lg font-semibold text-gray-800">Payment History</h3>
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
    </div>
</template>

<style>
@media print {
    .no-print { display: none !important; }
    body, html { margin: 0 !important; padding: 0 !important; background: white !important; }
}
</style>
<script setup>
import { ref, computed } from 'vue';

const totalPaid = ref(35000.00);
const totalPending = ref(1000.00);

const transactions = ref([
    {
        id: 1,
        service: 'Family planning',
        date: 'December 20, 2024',
        amount: 5000.00,
        status: 'paid'
    },
    {
        id: 2,
        service: 'Ultra sound',
        date: 'December 5, 2026',
        amount: 1000.00,
        status: 'pending'
    },
    {
        id: 3,
        service: 'Prenatal Care',
        date: 'January 03, 2025',
        amount: 20000.00,
        status: 'paid'
    }
]);

const transactionCount = computed(() => transactions.value.length);

const hasPendingPayments = computed(() => {
    return transactions.value.some(t => t.status === 'pending');
});

const pendingAmount = computed(() => {
    return transactions.value
        .filter(t => t.status === 'pending')
        .reduce((sum, t) => sum + t.amount, 0);
});

function formatCurrency(amount) {
    return '₱' + amount.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function getStatusClass(status) {
    return status === 'paid' 
        ? 'bg-green-100 text-green-700' 
        : 'bg-red-100 text-red-700';
}

function getStatusText(status) {
    return status === 'paid' ? 'Paid' : 'Pending';
}
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">
        <div class="max-w-md mx-auto space-y-4">
            <!-- Total Paid -->
            <div class="bg-lime-300 rounded-lg p-5 shadow-sm">
                <div class="flex items-center gap-2 mb-2">
                    <svg class="w-5 h-5 text-green-700" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                    </svg>
                    <h2 class="text-xl font-bold text-gray-900">Total Paid</h2>
                </div>
                <p class="text-2xl font-bold text-gray-900">{{ formatCurrency(totalPaid) }}</p>
            </div>

            <!-- Total Pending -->
            <div class="bg-red-300 rounded-lg p-5 shadow-sm">
                <div class="flex items-center gap-2 mb-2">
                    <svg class="w-5 h-5 text-red-700" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                    </svg>
                    <h2 class="text-xl font-bold text-gray-900">Total Pending</h2>
                </div>
                <p class="text-2xl font-bold text-gray-900">{{ formatCurrency(totalPending) }}</p>
            </div>

            <!-- Payment History Header -->
            <div class="flex items-center justify-between pt-4">
                <h2 class="text-2xl font-bold text-gray-900">Payment History</h2>
                <span class="text-sm text-gray-500">{{ transactionCount }} Transaction{{ transactionCount !== 1 ? 's' : '' }}</span>
            </div>

            <!-- Transaction List -->
            <div class="space-y-3">
                <div
                    v-for="transaction in transactions"
                    :key="transaction.id"
                    class="bg-white rounded-lg border-2 border-gray-300 p-4 shadow-sm"
                >
                    <div class="flex items-start justify-between">
                        <!-- Left Side - Icon and Details -->
                        <div class="flex gap-3">
                            <div class="bg-blue-100 p-3 rounded-lg flex-shrink-0">
                                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                                </svg>
                            </div>

                            <div>
                                <h3 class="font-bold text-gray-900 text-base">{{ transaction.service }}</h3>
                                <p class="text-sm text-gray-500 mt-1">{{ transaction.date }}</p>
                                <p class="font-bold text-gray-900 mt-2">{{ formatCurrency(transaction.amount) }}</p>
                            </div>
                        </div>

                        <!-- Right Side - Status Badge -->
                        <span
                            :class="['px-3 py-1 rounded-md text-xs font-semibold flex items-center gap-1', getStatusClass(transaction.status)]"
                        >
                            <svg v-if="transaction.status === 'paid'" class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                            </svg>
                            <svg v-else class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                            </svg>
                            {{ getStatusText(transaction.status) }}
                        </span>
                    </div>
                </div>
            </div>

            <!-- Pending Payments Notice -->
            <div v-if="hasPendingPayments" class="bg-orange-50 border-2 border-orange-200 rounded-lg p-4 shadow-sm">
                <div class="flex items-start gap-3">
                    <div class="bg-red-400 rounded-full p-1 flex-shrink-0 mt-0.5">
                        <svg class="w-4 h-4 text-white" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                        </svg>
                    </div>
                    <div>
                        <h3 class="font-bold text-gray-900 mb-1">Pending Payments</h3>
                        <p class="text-sm text-gray-700">
                            You have <span class="font-semibold">{{ formatCurrency(pendingAmount) }}</span> in pending payments. Please settle your account at the billing office.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
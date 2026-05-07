<script setup>
import { ref,} from 'vue';

const activeTab = ref('appointment');
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
        <div class="px-8 py-4 bg-white border-b border-gray-200">
            <div class="flex space-x-1">
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
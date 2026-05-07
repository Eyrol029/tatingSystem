<script setup>
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const patients = ref([
    {
        id: 'P001',
        name: 'Maria Santos',
        age: 28,
        contact: '09123456789',
        gestationalWeek: 32,
        vitals: {
            systolic: 145,
            diastolic: 95,
            glucose: 150,
            fetalHeartRate: 140,
            fetalGrowthPercentile: 45
        },
        riskStatus: 'high-risk',
        lastVisit: '2024-12-15',
        medicalHistory: ['Gestational diabetes', 'Previous preeclampsia']
    },
    {
        id: 'P002',
        name: 'Ana Cruz',
        age: 25,
        contact: '09234567890',
        gestationalWeek: 28,
        vitals: {
            systolic: 120,
            diastolic: 80,
            glucose: 95,
            fetalHeartRate: 145,
            fetalGrowthPercentile: 55
        },
        riskStatus: 'normal',
        lastVisit: '2024-12-18',
        medicalHistory: []
    },
    {
        id: 'P003',
        name: 'Rosa Mendoza',
        age: 35,
        contact: '09345678901',
        gestationalWeek: 36,
        vitals: {
            systolic: 150,
            diastolic: 100,
            glucose: 145,
            fetalHeartRate: 138,
            fetalGrowthPercentile: 35
        },
        riskStatus: 'high-risk',
        lastVisit: '2024-12-16',
        medicalHistory: ['Hypertension', 'Advanced maternal age']
    },
    {
        id: 'P004',
        name: 'Elena Reyes',
        age: 22,
        contact: '09456789012',
        gestationalWeek: 24,
        vitals: {
            systolic: 115,
            diastolic: 75,
            glucose: 88,
            fetalHeartRate: 150,
            fetalGrowthPercentile: 60
        },
        riskStatus: 'normal',
        lastVisit: '2024-12-19',
        medicalHistory: []
    }
]);

const alertPatient = ref(null);
const selectedPatient = ref(null);
const filterStatus = ref('all');
const searchQuery = ref('');

const highRiskCount = computed(() => patients.value.filter((p) => p.riskStatus === 'high-risk').length);

const normalCount = computed(() => patients.value.filter((p) => p.riskStatus === 'normal').length);

const filteredPatients = computed(() => {
    // first apply status filter
    const statusFiltered = filterStatus.value === 'all' ? patients.value : patients.value.filter((p) => p.riskStatus === filterStatus.value);

    // then apply search query (by name, contact or id)
    const q = (searchQuery.value || '').trim().toLowerCase();
    if (!q) return statusFiltered;

    return statusFiltered.filter((p) => {
        const combined = `${p.name} ${p.contact} ${p.id}`.toLowerCase();
        return combined.includes(q);
    });
});

function handleSearch() {
    // trimming the query is enough; filtering is reactive
    searchQuery.value = (searchQuery.value || '').trim();
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function handleGenerateReferralClick(patient) {
    alertPatient.value = patient;
}

function handleGenerateReferral(patientId) {
    const patientObj = patients.value.find((p) => p.id === patientId);
    if (patientObj) {
        // store to localStorage as a robust fallback (works across navigation/reloads)
        try {
            localStorage.setItem('referral_patient', JSON.stringify(patientObj));
        } catch (e) {
            // ignore if localStorage is unavailable
        }

        // navigate to ReferralForm and pass patient via query as JSON (preferred)
        try {
            router.push({ path: '/uikit/ReferralForm', query: { patient: JSON.stringify(patientObj) } });
        } catch (e) {
            // Fallback to state if query fails for any reason
            router.push({ path: '/uikit/ReferralForm', state: { patient: patientObj } });
        }
    } else {
        alert(`Generating referral for patient ${patientId}`);
    }

    alertPatient.value = null;
    selectedPatient.value = null;
}

function handleViewDetails(patientId) {
    const patient = patients.value.find((p) => p.id === patientId);
    if (patient) {
        alertPatient.value = null;
        selectedPatient.value = patient;
    }
}

function handleViewPatientDetails(patient) {
    selectedPatient.value = patient;
}

function handleCloseAlert() {
    alertPatient.value = null;
}

function getRiskStatusClass(status) {
    if (status === 'high-risk') {
        return 'bg-red-100 text-red-800 px-4 py-2 rounded-full text-sm font-semibold';
    }
    return 'bg-green-100 text-green-800 px-4 py-2 rounded-full text-sm font-semibold';
}
</script>

<template>
    <div class="min-h-screen bg-gray-50">
        <!-- Header Section -->
        <div class="bg-white border-b border-gray-200 shadow-sm">
            <div class="max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-900">Patient Dashboard</h1>
                        <p class="text-gray-600 mt-2">Manage prenatal patients and monitor high-risk cases</p>
                    </div>
                    <div class="flex items-center gap-4">
                        <div class="bg-red-50 border border-red-200 rounded-lg px-4 py-3 flex items-center gap-2">
                            <span class="text-2xl font-bold text-red-600">{{ highRiskCount }}</span>
                            <span class="text-sm text-gray-600">High-Risk Patients</span>
                        </div>
                        <div class="bg-green-50 border border-green-200 rounded-lg px-4 py-3 flex items-center gap-2">
                            <span class="text-2xl font-bold text-green-600">{{ normalCount }}</span>
                            <span class="text-sm text-gray-600">Normal</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <!-- Filter Section -->
            <div class="mb-6 flex flex-wrap items-center gap-4">
                <button @click="filterStatus = 'all'" :class="['px-4 py-2 rounded-lg font-medium transition duration-200', filterStatus === 'all' ? 'bg-blue-600 text-white' : 'bg-white border border-gray-300 text-gray-700 hover:border-blue-300']">
                    All Patients
                </button>
                <button
                    @click="filterStatus = 'high-risk'"
                    :class="['px-4 py-2 rounded-lg font-medium transition duration-200', filterStatus === 'high-risk' ? 'bg-red-600 text-white' : 'bg-white border border-gray-300 text-gray-700 hover:border-red-300']"
                >
                    High-Risk Only
                </button>
                <button
                    @click="filterStatus = 'normal'"
                    :class="['px-4 py-2 rounded-lg font-medium transition duration-200', filterStatus === 'normal' ? 'bg-green-600 text-white' : 'bg-white border border-gray-300 text-gray-700 hover:border-green-300']"
                >
                    Normal Only
                </button>

                <div class="ml-auto flex items-center gap-2">
                    <input v-model="searchQuery" @keyup.enter="handleSearch" type="search" placeholder="Search name, contact or ID" class="px-4 py-2 border border-gray-300 rounded-lg w-64 focus:outline-none focus:ring-2 focus:ring-blue-400" />
                    <button @click="handleSearch" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2">
                        <svg class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden>
                            <circle cx="11" cy="11" r="7"></circle>
                            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                        </svg>
                        <span class="hidden sm:inline">Search</span>
                    </button>
                </div>
            </div>

            <!-- Patient Table -->
            <div class="bg-white rounded-lg shadow-md overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[1100px] table-auto">
                        <thead class="bg-gray-100 border-b border-gray-200">
                            <tr>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800 w-1/3">Patient Name</th>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800">Age</th>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800">Gestational Week</th>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800">BP / Glucose</th>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800">Status</th>
                                <th class="px-8 py-6 text-left text-lg font-semibold text-gray-800">Last Visit</th>
                                <th class="px-8 py-6 text-center text-lg font-semibold text-gray-800">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="patient in filteredPatients" :key="patient.id" class="border-b border-gray-200 hover:bg-gray-50 transition duration-150">
                                <td class="px-8 py-6">
                                    <div>
                                        <p class="font-semibold text-gray-900 text-lg">{{ patient.name }}</p>
                                        <p class="text-base text-gray-500">{{ patient.contact }}</p>
                                    </div>
                                </td>
                                <td class="px-8 py-6 text-gray-800">{{ patient.age }}</td>
                                <td class="px-8 py-6 text-gray-800">Week {{ patient.gestationalWeek }}</td>
                                <td class="px-8 py-6">
                                    <div class="text-base">
                                        <p :class="patient.vitals.systolic > 140 ? 'text-red-600 font-semibold' : 'text-gray-800'">{{ patient.vitals.systolic }}/{{ patient.vitals.diastolic }} mmHg</p>
                                        <p :class="patient.vitals.glucose > 140 ? 'text-red-600 font-semibold' : 'text-gray-800'">{{ patient.vitals.glucose }} mg/dL</p>
                                    </div>
                                </td>
                                <td class="px-8 py-6">
                                    <span :class="getRiskStatusClass(patient.riskStatus)">
                                        {{ patient.riskStatus === 'high-risk' ? 'High Risk' : 'Normal' }}
                                    </span>
                                </td>
                                <td class="px-8 py-6 text-gray-800">{{ formatDate(patient.lastVisit) }}</td>
                                <td class="px-8 py-6">
                                    <div class="flex justify-center gap-2">
                                        <button @click="handleViewPatientDetails(patient)" class="text-blue-600 hover:text-blue-800 font-medium text-base hover:underline">View</button>
                                        <button v-if="patient.riskStatus === 'high-risk'" @click="handleGenerateReferralClick(patient)" class="text-red-600 hover:text-red-800 font-medium text-base hover:underline">Refer</button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Empty State -->
                <div v-if="filteredPatients.length === 0" class="text-center py-12">
                    <p class="text-gray-500 text-lg">No patients found</p>
                </div>
            </div>

            <!-- Patient Details Modal -->
            <div v-if="selectedPatient" class="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4" @click.self="selectedPatient = null">
                <div class="bg-white rounded-lg shadow-2xl max-w-2xl w-full">
                    <div class="bg-blue-600 text-white p-6 rounded-t-lg flex items-center justify-between">
                        <h2 class="text-2xl font-bold">Patient Details</h2>
                        <button @click="selectedPatient = null" class="text-white hover:opacity-80 transition text-2xl">✕</button>
                    </div>
                    <div class="p-6 grid grid-cols-2 gap-6">
                        <div>
                            <p class="text-sm text-gray-600">Name</p>
                            <p class="text-lg font-semibold text-gray-900">{{ selectedPatient.name }}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Age</p>
                            <p class="text-lg font-semibold text-gray-900">{{ selectedPatient.age }} years</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Contact</p>
                            <p class="text-lg font-semibold text-gray-900">{{ selectedPatient.contact }}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Gestational Week</p>
                            <p class="text-lg font-semibold text-gray-900">{{ selectedPatient.gestationalWeek }}</p>
                        </div>
                        <div class="col-span-2">
                            <p class="text-sm text-gray-600 mb-2">Vitals</p>
                            <div class="grid grid-cols-2 gap-3">
                                <div class="bg-gray-50 p-3 rounded">
                                    <p class="text-xs text-gray-600">Blood Pressure</p>
                                    <p class="text-sm font-semibold">{{ selectedPatient.vitals.systolic }}/{{ selectedPatient.vitals.diastolic }} mmHg</p>
                                </div>
                                <div class="bg-gray-50 p-3 rounded">
                                    <p class="text-xs text-gray-600">Glucose</p>
                                    <p class="text-sm font-semibold">{{ selectedPatient.vitals.glucose }} mg/dL</p>
                                </div>
                                <div class="bg-gray-50 p-3 rounded">
                                    <p class="text-xs text-gray-600">Fetal Heart Rate</p>
                                    <p class="text-sm font-semibold">{{ selectedPatient.vitals.fetalHeartRate }} bpm</p>
                                </div>
                                <div class="bg-gray-50 p-3 rounded">
                                    <p class="text-xs text-gray-600">Fetal Growth</p>
                                    <p class="text-sm font-semibold">{{ selectedPatient.vitals.fetalGrowthPercentile }}%</p>
                                </div>
                            </div>
                        </div>
                        <div v-if="selectedPatient.medicalHistory.length > 0" class="col-span-2">
                            <p class="text-sm text-gray-600 mb-2">Medical History</p>
                            <ul class="space-y-1">
                                <li v-for="(history, idx) in selectedPatient.medicalHistory" :key="idx" class="text-sm text-gray-700">• {{ history }}</li>
                            </ul>
                        </div>
                        <div class="col-span-2 flex gap-3">
                            <button @click="selectedPatient = null" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 px-4 rounded-lg transition">Close</button>
                            <button v-if="selectedPatient.riskStatus === 'high-risk'" @click="handleGenerateReferral(selectedPatient.id)" class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg transition">
                                Generate Referral
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Alert Modal -->
            <div v-if="alertPatient" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4" @click.self="handleCloseAlert">
                <div class="bg-white rounded-lg shadow-2xl max-w-md w-full p-6">
                    <div class="text-center mb-4">
                        <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                            <span class="text-3xl text-red-600">⚠️</span>
                        </div>
                        <h3 class="text-xl font-bold text-gray-900 mb-2">High-Risk Patient Alert</h3>
                        <p class="text-gray-600">{{ alertPatient.name }} requires immediate attention</p>
                    </div>
                    <div class="flex gap-3">
                        <button @click="handleCloseAlert" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 px-4 rounded-lg transition">Close</button>
                        <button @click="handleViewDetails(alertPatient.id)" class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition">View Details</button>
                        <button @click="handleGenerateReferral(alertPatient.id)" class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg transition">Generate Referral</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

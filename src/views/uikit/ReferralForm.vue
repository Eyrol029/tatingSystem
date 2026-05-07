<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const patient = ref(null);

const referralForm = ref({
    hospitalName: '',
   
    notes: ''
});

const showSuccessMessage = ref(false);
const successMessage = ref('');
function setPatient(p) {
    patient.value = p;

    // Prefill some referral fields if patient data is available
    if (p) {
        referralForm.value.notes = `Auto-filled for patient ${p.name} (ID: ${p.id}).`;
    }
}

function handlePrintReferral() {
    window.print();
}

function handleSendNotification() {
    // Simulate sending notification
    showSuccessMessage.value = true;
    successMessage.value = 'Notification sent to the OB-GYN contact.';
    setTimeout(() => (showSuccessMessage.value = false), 3000);
}

function handleSaveReferral() {
    // Simulate saving the referral
    showSuccessMessage.value = true;
    successMessage.value = 'Referral saved to the patient record.';
    setTimeout(() => (showSuccessMessage.value = false), 3000);
}

onMounted(() => {
    const route = useRoute();

    // 1) Prefer JSON-encoded patient in query (added by Patient.vue)
    if (route && route.query && route.query.patient) {
        try {
            setPatient(JSON.parse(route.query.patient));
            return;
        } catch (e) {
            // ignore parse error
        }
    }

    // 2) Fallback: check browser history state (router.push({ state }))
    if (typeof window !== 'undefined' && window.history && window.history.state && window.history.state.patient) {
        setPatient(window.history.state.patient);
        return;
    }

    // 3) Finally check route params
    if (route && route.params && route.params.patient) {
        setPatient(route.params.patient);
        return;
    }

    // 4) localStorage fallback
    try {
        const stored = localStorage.getItem('referral_patient');
        if (stored) {
            setPatient(JSON.parse(stored));
            localStorage.removeItem('referral_patient');
            return;
        }
    } catch (e) {
        // ignore
    }
});
</script>

<template>
    <div class="min-h-screen bg-gray-50">
        <!-- Header -->
        <div class="bg-white border-b border-gray-200 shadow-sm">
            <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
                <router-link to="/" class="text-blue-600 hover:text-blue-800 flex items-center gap-2 mb-4">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M7.707 7.293a1 1 0 010 1.414L5.414 11l2.293 2.293a1 1 0 11-1.414 1.414l-3-3a1 1 0 010-1.414l3-3a1 1 0 011.414 0z" clip-rule="evenodd" />
                    </svg>
                    Back to Dashboard
                </router-link>
                <h1 class="text-3xl font-bold text-gray-900">Generate Referral</h1>
                <p class="text-gray-600 mt-2">Create and manage OB-GYN referral for high-risk patient</p>
            </div>
        </div>

        <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div v-if="patient" class="space-y-6">
                <!-- Patient Information Card -->
                <div class="bg-white rounded-lg shadow-md p-6 border-l-4 border-blue-600">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <svg class="w-5 h-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" />
                        </svg>
                        Patient Information
                    </h2>
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                        <div>
                            <p class="text-sm text-gray-600">Name</p>
                            <p class="font-semibold text-gray-900">{{ patient.name }}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Age</p>
                            <p class="font-semibold text-gray-900">{{ patient.age }} years</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Contact</p>
                            <p class="font-semibold text-gray-900">{{ patient.contact }}</p>
                        </div>
                        <div>
                            <p class="text-sm text-gray-600">Gestational Week</p>
                            <p class="font-semibold text-gray-900">{{ patient.gestationalWeek }}</p>
                        </div>
                    </div>
                </div>
                <!-- Prenatal Vitals Card -->
                <div class="bg-white rounded-lg shadow-md p-6 border-l-4 border-green-600">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <svg class="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clip-rule="evenodd" />
                        </svg>
                        Prenatal Vitals
                    </h2>
                    <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                        <div class="bg-gray-50 p-4 rounded-lg">
                            <p class="text-xs text-gray-600 uppercase">Blood Pressure</p>
                            <p :class="['text-lg font-semibold', patient.vitals.systolic > 140 ? 'text-red-600' : 'text-gray-900']">{{ patient.vitals.systolic }}/{{ patient.vitals.diastolic }} mmHg</p>
                        </div>
                        <div class="bg-gray-50 p-4 rounded-lg">
                            <p class="text-xs text-gray-600 uppercase">Glucose</p>
                            <p :class="['text-lg font-semibold', patient.vitals.glucose > 140 ? 'text-red-600' : 'text-gray-900']">{{ patient.vitals.glucose }} mg/dL</p>
                        </div>
                        <div class="bg-gray-50 p-4 rounded-lg">
                            <p class="text-xs text-gray-600 uppercase">Fetal Heart Rate</p>
                            <p class="text-lg font-semibold text-gray-900">{{ patient.vitals.fetalHeartRate }} bpm</p>
                        </div>
                        <div class="bg-gray-50 p-4 rounded-lg">
                            <p class="text-xs text-gray-600 uppercase">Fetal Growth</p>
                            <p :class="['text-lg font-semibold', patient.vitals.fetalGrowthPercentile < 30 ? 'text-red-600' : 'text-gray-900']">{{ patient.vitals.fetalGrowthPercentile }}%</p>
                        </div>
                    </div>
                </div>
                <!-- Risk Factors Card -->
                <div class="bg-white rounded-lg shadow-md p-6 border-l-4 border-red-600">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <svg class="w-5 h-5 text-red-600" fill="currentColor" viewBox="0 0 20 20">
                            <path
                                fill-rule="evenodd"
                                d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                                clip-rule="evenodd"
                            />
                        </svg>
                        Identified Risk Factors
                    </h2>
                    <div class="space-y-2" v-if="(patient.riskFactors && patient.riskFactors.length) || (patient.medicalHistory && patient.medicalHistory.length)">
                        <div v-for="(factor, idx) in patient.riskFactors || patient.medicalHistory || []" :key="idx" class="flex items-start gap-3">
                            <span class="text-red-600 font-bold flex-shrink-0">✓</span>
                            <span class="text-gray-700">{{ factor }}</span>
                        </div>
                    </div>
                    <div v-else class="text-sm text-gray-500">No identified risk factors.</div>
                </div>

                <!-- Referral Information Form -->
                <div class="bg-white rounded-lg shadow-md p-6 border-l-4 border-purple-600">
                    <h2 class="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <svg class="w-5 h-5 text-purple-600" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M2 5a2 2 0 012-2h12a2 2 0 012 2v10a2 2 0 01-2 2H4a2 2 0 01-2-2V5z" />
                        </svg>
                        Referral Details (Auto-filled)
                    </h2>
                    <form class="space-y-4">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Hospital Name</label>
                                <input v-model="referralForm.hospitalName" type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="Metropolitan Medical Center" />
                            </div>
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">OB-GYN Specialist</label>
                                <input v-model="referralForm.obGyneName" type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="Dr. James Mitchell" />
                            </div>
                            <div class="md:col-span-2">
                                <label class="block text-sm font-medium text-gray-700 mb-1">OB-GYN Contact</label>
                                <input v-model="referralForm.obGyneContact" type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="555-8901" />
                            </div>
                            <div class="md:col-span-2">
                                <label class="block text-sm font-medium text-gray-700 mb-1">Clinical Notes</label>
                                <textarea
                                    v-model="referralForm.notes"
                                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                    rows="4"
                                    placeholder="Additional clinical notes or observations..."
                                ></textarea>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Action Buttons -->
                <div class="bg-white rounded-lg shadow-md p-6">
                    <div class="flex flex-col sm:flex-row gap-3">
                        <button @click="handlePrintReferral" class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-4 rounded-lg transition duration-200 flex items-center justify-center gap-2">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M5 4a2 2 0 012-2h6a2 2 0 012 2v14l-5-2.5L5 18V4z" />
                            </svg>
                            Print Referral
                        </button>
                        <button @click="handleSendNotification" class="flex-1 bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-4 rounded-lg transition duration-200 flex items-center justify-center gap-2">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
                            </svg>
                            Send Notification
                        </button>
                        <button @click="handleSaveReferral" class="flex-1 bg-purple-600 hover:bg-purple-700 text-white font-semibold py-3 px-4 rounded-lg transition duration-200 flex items-center justify-center gap-2">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M5.5 13a3.5 3.5 0 01-.369-6.98 4 4 0 117.753-1.3A4.5 4.5 0 1113.5 13H11V9.413l1.293 1.293a1 1 0 001.414-1.414l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 001.414 1.414L9 9.414V13H5.5z" />
                            </svg>
                            Save to Record
                        </button>
                        <router-link to="/" class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-3 px-4 rounded-lg transition duration-200 flex items-center justify-center"> Cancel </router-link>
                    </div>
                </div>

                <!-- Success Message -->
                <div v-if="showSuccessMessage" class="bg-green-50 border border-green-200 rounded-lg p-4 flex items-center gap-3">
                    <svg class="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                    </svg>
                    <span class="text-green-700 font-medium">{{ successMessage }}</span>
                </div>
            </div>

            <!-- No Patient Found -->
            <div v-else class="bg-white rounded-lg shadow-md p-12 text-center">
                <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <h2 class="text-xl font-semibold text-gray-900 mb-2">Patient Not Found</h2>
                <p class="text-gray-600 mb-4">The patient ID could not be found.</p>
                <router-link to="/" class="text-blue-600 hover:text-blue-800 font-medium"> Return to Dashboard </router-link>
            </div>
        </div>
    </div>
</template>

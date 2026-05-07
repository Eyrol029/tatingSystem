<script setup>
import { ref } from 'vue';

const appointments = ref([
    {
        id: 1,
        type: 'Prenatal care',
        recordType: 'Medical Record',
        time: '10:00 AM',
        status: 'completed',
        reasonForVisit: 'Complaints of nausea/vomiting',
        findings: 'All lab results within normal range',
        assistBy: 'Rowena C. Roxas',
        vitalSigns: {
            bp: '118/78',
            weight: '65kg'
        },
        treatmentGiven: 'No treatment needed',
        expanded: false
    },
    {
        id: 2,
        type: 'Follow-up Consultation',
        recordType: 'Book an Appointment',
        time: '9:00 AM',
        status: 'pending',
        reasonForVisit: 'Follow-up for cold symptoms',
        findings: null,
        assistBy: null,
        vitalSigns: null,
        treatmentGiven: null,
        expanded: false
    },
    {
        id: 3,
        type: 'Postnatal Care',
        recordType: 'Medical Record',
        time: '2:00 PM',
        status: 'completed',
        reasonForVisit: 'Routine postpartum checkup',
        findings: 'Mother and baby both healthy',
        assistBy: 'Maria Santos',
        vitalSigns: {
            bp: '120/80',
            weight: '58kg'
        },
        treatmentGiven: 'Vitamins prescribed',
        expanded: false
    },
    {
        id: 4,
        type: 'Immunization',
        recordType: 'Book an Appointment',
        time: '11:30 AM',
        status: 'pending',
        reasonForVisit: 'Baby vaccination schedule',
        findings: null,
        assistBy: null,
        vitalSigns: null,
        treatmentGiven: null,
        expanded: false
    }
]);

function toggleExpand(id) {
    const appointment = appointments.value.find(a => a.id === id);
    if (appointment) {
        appointment.expanded = !appointment.expanded;
    }
}

function getStatusClass(status) {
    return status === 'completed' 
        ? 'bg-green-100 text-green-700' 
        : 'bg-blue-100 text-blue-700';
}

function getStatusText(status) {
    return status === 'completed' ? 'Completed' : 'Pending';
}
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">
        <div class="max-w-2xl mx-auto">
            <h1 class="text-3xl font-bold text-gray-900 mb-8">My Services & Appointments</h1>

            <div class="space-y-4">
                <div
                    v-for="appointment in appointments"
                    :key="appointment.id"
                    class="bg-white rounded-xl border-2 border-gray-200 shadow-sm overflow-hidden"
                >
                    <!-- Header -->
                    <div class="p-5">
                        <div class="flex items-start justify-between mb-3">
                            <div class="flex items-start gap-3">
                                <!-- Icon -->
                                <div class="mt-1">
                                    <svg class="w-8 h-8 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                    </svg>
                                </div>

                                <!-- Title and Record Type -->
                                <div>
                                    <h2 class="text-xl font-bold text-gray-900">{{ appointment.type }}</h2>
                                    <div class="flex items-center gap-2 mt-1">
                                        <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                        </svg>
                                        <span class="text-sm text-gray-600">{{ appointment.recordType }}</span>
                                    </div>
                                </div>
                            </div>

                            <!-- Time and Status -->
                            <div class="text-right flex flex-col items-end gap-2">
                                <span class="text-lg font-semibold text-gray-900">{{ appointment.time }}</span>
                                <span 
                                    :class="['px-3 py-1 rounded-full text-xs font-semibold', getStatusClass(appointment.status)]"
                                >
                                    ✓ {{ getStatusText(appointment.status) }}
                                </span>
                            </div>
                        </div>

                        <!-- Divider -->
                        <div class="border-t-2 border-blue-500 mt-4"></div>
                    </div>

                    <!-- Expandable Content -->
                    <div v-if="appointment.status === 'completed'">
                        <button
                            @click="toggleExpand(appointment.id)"
                            class="w-full px-5 py-3 text-left hover:bg-gray-50 transition-colors flex items-center justify-between"
                        >
                            <span class="text-sm font-medium text-gray-700">
                                {{ appointment.expanded ? 'Hide Details' : 'View Details' }}
                            </span>
                            <svg 
                                :class="['w-5 h-5 text-gray-500 transition-transform', appointment.expanded ? 'rotate-180' : '']"
                                fill="none" 
                                stroke="currentColor" 
                                viewBox="0 0 24 24"
                            >
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                            </svg>
                        </button>

                        <div 
                            v-if="appointment.expanded"
                            class="px-5 pb-5 space-y-4 bg-gray-50"
                        >
                            <!-- Reason for Visit -->
                            <div>
                                <p class="text-sm font-semibold text-gray-700">Reason for Visit:</p>
                                <p class="text-sm text-gray-600">{{ appointment.reasonForVisit }}</p>
                            </div>

                            <!-- Findings -->
                            <div v-if="appointment.findings">
                                <p class="text-sm font-semibold text-gray-700">Findings</p>
                                <p class="text-sm text-gray-600">{{ appointment.findings }}</p>
                            </div>

                            <!-- Assist by -->
                            <div v-if="appointment.assistBy">
                                <p class="text-sm text-gray-700">
                                    <span class="font-semibold">Assist by:</span> {{ appointment.assistBy }}
                                </p>
                            </div>

                            <!-- Vital Signs -->
                            <div v-if="appointment.vitalSigns">
                                <p class="text-sm font-semibold text-gray-700">Vital Signs</p>
                                <p class="text-sm text-gray-600">
                                    BP: {{ appointment.vitalSigns.bp }}, Weight: {{ appointment.vitalSigns.weight }}
                                </p>
                            </div>

                            <!-- Treatment Given -->
                            <div v-if="appointment.treatmentGiven">
                                <p class="text-sm font-semibold text-gray-700">Treatment Given</p>
                                <p class="text-sm text-gray-600">{{ appointment.treatmentGiven }}</p>
                            </div>
                        </div>
                    </div>

                    <!-- Pending Appointment Content -->
                    <div v-else class="px-5 pb-5 bg-gray-50">
                        <div>
                            <p class="text-sm font-semibold text-gray-700">Reason for Visit</p>
                            <p class="text-sm text-gray-600">{{ appointment.reasonForVisit }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Empty State -->
            <div v-if="appointments.length === 0" class="text-center py-12">
                <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p class="text-gray-500">No appointments found</p>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
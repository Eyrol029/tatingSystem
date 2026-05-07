<script setup>
import { ref, computed } from 'vue';

const showAddModal = ref(false);

const newAppointment = ref({
    dateTime: '',
    patientName: '',
    contact: '',
    email: '',
    serviceType: '',
    reason: '',
    diagnosis: ''
});

const appointments = ref([
    {
        id: 'APT001',
        date: '2025-12-15',
        time: '09:00',
        patientName: 'Gil B. Inigo',
        contact: '09625024023',
        email: 'gil@gmail',
        serviceType: 'General Consultation',
        reason: 'Annual Checkup',
        diagnosis: 'Healthy'
    },
    {
        id: 'APT002',
        date: '2025-12-15',
        time: '10:00',
        patientName: 'Maria Santos',
        contact: '09123456789',
        email: 'maria@gmail.com',
        serviceType: 'Prenatal Checkup',
        reason: 'Monthly Checkup',
        diagnosis: 'Normal'
    },
    {
        id: 'APT003',
        date: '2025-12-20',
        time: '14:00',
        patientName: 'Ana Cruz',
        contact: '09234567890',
        email: 'ana@gmail.com',
        serviceType: 'Postnatal Care',
        reason: 'Follow-up',
        diagnosis: 'Recovering Well'
    }
]);

const serviceTypes = ref([
    'General Consultation',
    'Prenatal Checkup',
    'Postnatal Care',
    'Family Planning',
    'Immunization',
    'Other'
]);

const totalAppointments = computed(() => appointments.value.length);

const todaysAppointments = computed(() => {
    const today = new Date().toISOString().split('T')[0];
    return appointments.value.filter(apt => apt.date === today).length;
});

const upcomingAppointments = computed(() => {
    const today = new Date().toISOString().split('T')[0];
    return appointments.value.filter(apt => apt.date > today).length;
});

function openAddModal() {
    showAddModal.value = true;
    newAppointment.value = {
        dateTime: '',
        patientName: '',
        contact: '',
        email: '',
        serviceType: '',
        reason: '',
        diagnosis: ''
    };
}

function closeAddModal() {
    showAddModal.value = false;
}

function addAppointment() {
    if (!newAppointment.value.dateTime || !newAppointment.value.patientName || !newAppointment.value.serviceType) {
        alert('Please fill in all required fields');
        return;
    }

    const [date, time] = newAppointment.value.dateTime.split('T');
    
    const appointment = {
        id: 'APT' + String(appointments.value.length + 1).padStart(3, '0'),
        date: date,
        time: time,
        patientName: newAppointment.value.patientName,
        contact: newAppointment.value.contact,
        email: newAppointment.value.email,
        serviceType: newAppointment.value.serviceType,
        reason: newAppointment.value.reason,
        diagnosis: newAppointment.value.diagnosis
    };

    appointments.value.push(appointment);
    closeAddModal();
}

function editAppointment(id) {
    alert(`Edit appointment ${id}`);
    // In a real app, open edit modal with appointment data
}

function deleteAppointment(id) {
    if (confirm('Are you sure you want to delete this appointment?')) {
        appointments.value = appointments.value.filter(apt => apt.id !== id);
    }
}
</script>

<template>
    <div class="min-h-screen bg-gray-100 p-6">
        <div class="max-w-full mx-auto px-4">
            <!-- Header -->
            <div class="flex items-center justify-between mb-6">
                <h1 class="text-3xl font-bold text-gray-900">Appointment Management</h1>
                <button
                    @click="openAddModal"
                    class="bg-purple-400 hover:bg-purple-500 text-gray-900 px-6 py-3 rounded-lg font-semibold transition-colors shadow-md flex items-center gap-2"
                >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                    Add Appointment
                </button>
            </div>

            <!-- Stats Cards -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <div class="bg-white rounded-lg shadow-sm p-5 border border-gray-200">
                    <p class="text-sm text-gray-600 mb-1">Total Appointments</p>
                    <p class="text-3xl font-bold text-gray-900">{{ totalAppointments }}</p>
                </div>

                <div class="bg-white rounded-lg shadow-sm p-5 border border-gray-200">
                    <p class="text-sm text-gray-600 mb-1">Today's Appointment</p>
                    <p class="text-3xl font-bold text-gray-900">{{ todaysAppointments }}</p>
                </div>

                <div class="bg-white rounded-lg shadow-sm p-5 border border-gray-200">
                    <p class="text-sm text-gray-600 mb-1">Upcoming</p>
                    <p class="text-3xl font-bold text-gray-900">{{ upcomingAppointments }}</p>
                </div>
            </div>

            <!-- Appointments Table -->
            <div class="bg-white rounded-lg shadow-sm overflow-hidden border border-gray-200">
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">ID</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Date & Time</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Patient Name</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Contact</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">ServiceType</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Reason</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Diagnosis</th>
                                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <tr
                                v-for="appointment in appointments"
                                :key="appointment.id"
                                class="hover:bg-gray-50 transition-colors"
                            >
                                <td class="px-6 py-4 text-sm text-gray-900">{{ appointment.id }}</td>
                                <td class="px-6 py-4 text-sm text-gray-900">
                                    <div>{{ appointment.date }}</div>
                                    <div>{{ appointment.time }}</div>
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-900">{{ appointment.patientName }}</td>
                                <td class="px-6 py-4 text-sm text-gray-900">
                                    <div>{{ appointment.email }}</div>
                                    <div>{{ appointment.contact }}</div>
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-900">{{ appointment.serviceType }}</td>
                                <td class="px-6 py-4 text-sm text-gray-900">{{ appointment.reason }}</td>
                                <td class="px-6 py-4 text-sm text-gray-900">{{ appointment.diagnosis }}</td>
                                <td class="px-6 py-4">
                                    <div class="flex items-center gap-2">
                                        <button
                                            @click="editAppointment(appointment.id)"
                                            class="text-gray-600 hover:text-gray-900 transition-colors"
                                            title="Edit"
                                        >
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                            </svg>
                                        </button>
                                        <button
                                            @click="deleteAppointment(appointment.id)"
                                            class="text-red-600 hover:text-red-700 transition-colors"
                                            title="Delete"
                                        >
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                            </svg>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div v-if="appointments.length === 0" class="text-center py-12">
                    <p class="text-gray-500">No appointments found</p>
                </div>
            </div>
        </div>

        <!-- Add Appointment Modal -->
        <div
            v-if="showAddModal"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
            @click.self="closeAddModal"
        >
            <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full p-6 max-h-[90vh] overflow-y-auto">
                <div class="flex items-center justify-between mb-6">
                    <h2 class="text-2xl font-bold text-gray-800">Add New Appointment</h2>
                    <button
                        @click="closeAddModal"
                        class="text-gray-400 hover:text-gray-600"
                    >
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>

                <div class="space-y-4">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Date & Time <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="newAppointment.dateTime"
                                type="datetime-local"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Patient Name <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="newAppointment.patientName"
                                type="text"
                                placeholder="Enter patient name"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Contact Number
                            </label>
                            <input
                                v-model="newAppointment.contact"
                                type="tel"
                                placeholder="e.g., 09123456789"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Email
                            </label>
                            <input
                                v-model="newAppointment.email"
                                type="email"
                                placeholder="email@example.com"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Service Type <span class="text-red-500">*</span>
                        </label>
                        <select
                            v-model="newAppointment.serviceType"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        >
                            <option value="">Select a service</option>
                            <option v-for="service in serviceTypes" :key="service" :value="service">
                                {{ service }}
                            </option>
                        </select>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Reason for Visit
                        </label>
                        <input
                            v-model="newAppointment.reason"
                            type="text"
                            placeholder="e.g., Annual Checkup"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Diagnosis
                        </label>
                        <input
                            v-model="newAppointment.diagnosis"
                            type="text"
                            placeholder="e.g., Healthy"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>
                </div>

                <div class="flex gap-3 mt-6">
                    <button
                        @click="closeAddModal"
                        class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-semibold transition-colors"
                    >
                        Cancel
                    </button>
                    <button
                        @click="addAppointment"
                        class="flex-1 px-4 py-2 bg-purple-400 text-gray-900 rounded-lg hover:bg-purple-500 font-semibold transition-colors"
                    >
                        Add Appointment
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
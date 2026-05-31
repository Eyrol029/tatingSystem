<script setup>
import { ref, computed, onMounted } from 'vue';
import { AppointmentService } from '@/service/AppointmentService'; // ← DIRI GI EDIT


const appointments = ref([]);
const loading = ref(false);
const error = ref('');
const showModal = ref(false);
const isEditing = ref(false);
const today = new Date().toISOString().split('T')[0];

const emptyForm = {
    appointmentID: null,
    patientID: null,
    patientServiceID: null,
    appointmentDate: '',
    appointmentTime: '',
    fName: '',
    lName: '',
    middleI: '',
    age: null,
    address: '',
    serviceType: '',
    gravida: null,
    term: null,
    preterm: null,
    noOfLivingChildren: null
};

const form = ref({ ...emptyForm });

const totalAppointments = computed(() => appointments.value.length);
const todaysAppointments = computed(() =>
    appointments.value.filter(a => a.appointmentDate === today).length
);
const upcomingAppointments = computed(() =>
    appointments.value.filter(a => a.appointmentDate > today).length
);

async function fetchAppointments() {
    loading.value = true;
    error.value = '';
    try {
        appointments.value = await AppointmentService.getAll();
    } catch (e) {
        error.value = 'Failed to load appointments.';
    } finally {
        loading.value = false;
    }
}

function openAddModal() {
    form.value = { ...emptyForm };
    isEditing.value = false;
    showModal.value = true;
}

function openEditModal(appointment) {
    form.value = { ...appointment };
    isEditing.value = true;
    showModal.value = true;
}

function closeModal() {
    showModal.value = false;
    form.value = { ...emptyForm };
}

async function saveAppointment() {
    try {
        if (isEditing.value) {
            await AppointmentService.update(form.value);
        } else {
            await AppointmentService.add(form.value);
        }
        await fetchAppointments();
        closeModal();
    } catch (e) {
        error.value = 'Failed to save appointment.';
    }
}

async function deleteAppointment(id) {
    if (!confirm('Delete this appointment?')) return;
    try {
        await AppointmentService.delete(id);
        await fetchAppointments();
    } catch (e) {
        error.value = 'Failed to delete appointment.';
    }
}

function formatDateTime(date, time) {
    if (!date && !time) return '—';
    return `${date ?? ''} ${time ?? ''}`.trim();
}

function fullName(a) { // ← DIRI GI EDIT
    return [a.lName + ',', a.fName, a.middleI ? a.middleI + '.' : ''].filter(Boolean).join(' ');
}

onMounted(fetchAppointments);
</script>

<template>
    <div class="p-6 bg-gray-100 min-h-screen">
        <!-- Header -->
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold text-gray-800">Appointment Management</h1>
            <button
                @click="openAddModal"
                class="flex items-center gap-2 px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 trans2ition"
            >
                <span class="text-lg font-bold">+</span> Add Appointment
            </button>
        </div>

        <!-- Error -->
        <div v-if="error" class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg">{{ error }}</div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-gray-500 text-sm mb-1">Total Appointments</p>
                <p class="text-3xl font-bold text-gray-800">{{ totalAppointments }}</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-gray-500 text-sm mb-1">Today's Appointment</p>
                <p class="text-3xl font-bold text-gray-800">{{ todaysAppointments }}</p>
            </div>
            <div class="bg-white rounded-lg shadow p-6">
                <p class="text-gray-500 text-sm mb-1">Upcoming</p>
                <p class="text-3xl font-bold text-gray-800">{{ upcomingAppointments }}</p>
            </div>
        </div>

        <!-- Table -->
        <div class="bg-white rounded-lg shadow overflow-x-auto">
            <div v-if="loading" class="p-8 text-center text-gray-500">Loading...</div>
            <table v-else class="w-full text-sm">
                <thead class="bg-gray-50 border-b border-gray-200">
                    <tr>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">ID</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Date & Time</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Patient Name</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Age</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Address</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Service Type</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Gravida</th>
                        <th class="text-left px-4 py-3 text-gray-600 font-semibold">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                        v-for="a in appointments"
                        :key="a.appointmentID"
                        class="border-b border-gray-100 hover:bg-gray-50 transition"
                    >
                        <td class="px-4 py-3 text-gray-700">APT{{ String(a.appointmentID).padStart(3, '0') }}</td>
                        <td class="px-4 py-3 text-gray-700">{{ formatDateTime(a.appointmentDate, a.appointmentTime) }}</td>
                        <td class="px-4 py-3 text-gray-800 font-medium">{{ fullName(a) }}</td>
                        <td class="px-4 py-3 text-gray-700">{{ a.age ?? '—' }}</td>
                        <td class="px-4 py-3 text-gray-700">{{ a.address || '—' }}</td>
                        <td class="px-4 py-3 text-gray-700">{{ a.serviceType || '—' }}</td>
                        <td class="px-4 py-3 text-gray-700">{{ a.gravida ?? '—' }}</td>
                        <td class="px-4 py-3">
                            <div class="flex gap-2">
                                <button @click="openEditModal(a)" class="text-blue-500 hover:text-blue-700 transition">
                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                    </svg>
                                </button>
                                <button @click="deleteAppointment(a.appointmentID)" class="text-red-500 hover:text-red-700 transition">
                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                    </svg>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <tr v-if="appointments.length === 0">
                        <td colspan="8" class="text-center py-10 text-gray-400">No appointments found.</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Modal -->
        <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
            <div class="bg-white rounded-2xl shadow-xl w-full max-w-2xl mx-4 p-6 max-h-[90vh] overflow-y-auto">
                <div class="flex justify-between items-center mb-5">
                    <h2 class="text-xl font-bold text-gray-800">{{ isEditing ? 'Edit Appointment' : 'Add Appointment' }}</h2>
                    <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                        </svg>
                    </button>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">First Name</label>
                        <input v-model="form.fName" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Middle Initial</label>
                        <input v-model="form.middleI" type="text" maxlength="2" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Last Name</label>
                        <input v-model="form.lName" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Age</label>
                        <input v-model="form.age" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Appointment Date</label>
                        <input v-model="form.appointmentDate" type="date" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Appointment Time</label>
                        <input v-model="form.appointmentTime" type="time" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div class="md:col-span-2">
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Address</label>
                        <input v-model="form.address" type="text" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div class="md:col-span-2">
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Service Type</label>
                        <select v-model="form.serviceType" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]">
                            <option value="">Select service</option>
                            <option>General Consultation</option>
                            <option>Prenatal Checkup</option>
                            <option>Postnatal Care</option>
                            <option>Family Planning</option>
                            <option>Immunization</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Gravida</label>
                        <input v-model="form.gravida" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Term</label>
                        <input v-model="form.term" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Preterm</label>
                        <input v-model="form.preterm" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">No. of Living Children</label>
                        <input v-model="form.noOfLivingChildren" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Patient ID</label>
                        <input v-model="form.patientID" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                    <div>
                        <label class="block text-xs font-semibold text-gray-600 mb-1">Patient Service ID</label>
                        <input v-model="form.patientServiceID" type="number" class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
                    </div>
                </div>

                <div class="flex justify-end gap-3 mt-6">
                    <button @click="closeModal" class="px-5 py-2 border border-gray-300 rounded-lg text-gray-600 hover:bg-gray-50 text-sm">Cancel</button>
                    <button @click="saveAppointment" class="px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 text-sm transition">
                        {{ isEditing ? 'Update' : 'Save' }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
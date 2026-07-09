<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { useUserDataStore } from '@/stores/userData';

const router = useRouter();
const userStore = useUserDataStore();

const BASE_URL = 'http://localhost:8080/api/patients';

// Pulls the real logged-in patient's ID from the auth store.
if (!userStore.user) {
    userStore.init();
}

const patientData = reactive({
    patientID: null,
    fName: '',
    lName: '',
    middleI: '',
    age: null,
    bDate: '',
    houseNo: '',
    street: '',
    barangay: '',
    municipality: '',
    province: '',
    contactNumber: '',
    religion: '',
    educationalAttainment: '',
    occupation: '',
    email: ''
});

const loading = ref(true);
const error = ref('');
const services = ref([]);

async function fetchPatient() {
    const id = userStore.user?.patientID;
    if (!id) {
        error.value = 'No logged-in patient found. Please log in again.';
        loading.value = false;
        return;
    }
    try {
        const res = await axios.get(`${BASE_URL}/${id}`);
        Object.assign(patientData, res.data);
    } catch (e) {
        error.value = 'Failed to load patient data.';
    } finally {
        loading.value = false;
    }
}

async function fetchServices() {
    const id = userStore.user?.patientID;
    if (!id) return;
    try {
        const res = await axios.get(`http://localhost:8080/api/patient-services/patient/${id}`);
        services.value = res.data.map(item => ({
            id: item.patientServiceID,
            service: item.serviceName,
            employee: item.employeeName ?? '---',
            ward: item.wardName ?? '---',
            dateAvailed: item.dateAvailed,
            remarks: item.remarks ?? '---'
        }));
    } catch (e) {
        console.error('Failed to load services data.', e);
    }
}

function printServices() {
    window.print();
}

function viewService(service) {
    const name = service.service?.toLowerCase();
    const patientId = userStore.user?.patientID;
    const serviceId = service.id;

    if (name === 'prenatal') {
        router.push(`/uikit/PrenatalAdmission/${patientId}/${serviceId}`);
    } else if (name === 'family planning') {
        router.push(`/uikit/FamilyPlanningAdmission/${patientId}/${serviceId}`);
    } else if (name === 'ultrasound') {
        router.push(`/uikit/UltrasoundAdmission/${patientId}/${serviceId}`);
    } else {
        alert(`No dedicated view page for "${service.service}" yet.`);
    }
}

onMounted(() => {
    fetchPatient();
    fetchServices();
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">

        <div v-if="loading" class="text-center py-20 text-gray-500">
            Loading your profile...
        </div>

        <div v-else-if="error" class="text-center py-20 text-red-500">
            {{ error }}
        </div>

        <div v-else class="max-w-screen mx-auto">
            <div class="flex justify-between items-center mb-6 no-print">
                <div class="flex items-center gap-4">
                    <button @click="router.push('/uikit/PatientDashboard')"
                        class="text-gray-600 hover:text-gray-800 font-semibold">
                        ←
                    </button>
                    <h1 class="text-2xl font-semibold text-gray-800">My Profile</h1>
                </div>
                <button @click="printServices"
                    class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition flex items-center gap-2">
                    🖨️ Print My Services
                </button>
            </div>

            <h2 class="text-3xl font-bold text-gray-900 mb-6">
                {{ patientData.fName }} {{ patientData.middleI }} {{ patientData.lName }}
            </h2>

            <!-- General Information — VIEW ONLY, no Edit button -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">General Information</h3>

                <div class="grid grid-cols-3 gap-6">
                    <div>
                        <p class="text-sm text-gray-500">Patient ID</p>
                        <p class="font-medium text-gray-900">{{ patientData.patientID }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Age</p>
                        <p class="font-medium text-gray-900">{{ patientData.age ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Birth Date</p>
                        <p class="font-medium text-gray-900">{{ patientData.bDate ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">House No</p>
                        <p class="font-medium text-gray-900">{{ patientData.houseNo ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Street</p>
                        <p class="font-medium text-gray-900">{{ patientData.street ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Barangay</p>
                        <p class="font-medium text-gray-900">{{ patientData.barangay ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Contact Number</p>
                        <p class="font-medium text-gray-900">{{ patientData.contactNumber ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Municipality</p>
                        <p class="font-medium text-gray-900">{{ patientData.municipality ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Province</p>
                        <p class="font-medium text-gray-900">{{ patientData.province ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Religion</p>
                        <p class="font-medium text-gray-900">{{ patientData.religion ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Education</p>
                        <p class="font-medium text-gray-900">{{ patientData.educationalAttainment ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Occupation</p>
                        <p class="font-medium text-gray-900">{{ patientData.occupation ?? '—' }}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Email</p>
                        <p class="font-medium text-gray-900">{{ patientData.email ?? '—' }}</p>
                    </div>
                </div>
            </div>

            <!-- Services Table — VIEW ONLY, no Action column -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden mb-6">
                <div class="px-6 py-4 border-b border-gray-200">
                    <h3 class="text-lg font-semibold text-gray-800">Services Availed</h3>
                </div>
                <table class="w-full">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="th">Service</th>
                            <th class="th">Employee</th>
                            <th class="th">Ward</th>
                            <th class="th">Date Availed</th>
                            <th class="th">Remarks</th>
                            <th class="th no-print">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="service in services" :key="service.id" class="border-b hover:bg-gray-50">
                            <td class="td">{{ service.service }}</td>
                            <td class="td">{{ service.employee }}</td>
                            <td class="td">{{ service.ward }}</td>
                            <td class="td">{{ service.dateAvailed }}</td>
                            <td class="td">{{ service.remarks }}</td>
                            <td class="td no-print">
                                <button @click="viewService(service)"
                                    class="bg-blue-600 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-700">
                                    View
                                </button>
                            </td>
                        </tr>
                        <tr v-if="!services.length">
                            <td colspan="6" class="text-center py-6 text-gray-500">No services recorded yet.</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>
.th {
    @apply px-6 py-3 text-sm font-medium text-gray-600 text-left;
}
.td {
    @apply px-6 py-4 text-sm text-gray-900;
}

/* Hide navigation/action controls when printing, so only the profile
   and services table show up on the printed page. */
@media print {
    .no-print {
        display: none !important;
    }
}
</style>
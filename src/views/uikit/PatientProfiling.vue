<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const route = useRoute();

const BASE_URL = 'http://localhost:8080/api/patients';

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
const services = ref([]); // ✅ changed from reactive to ref

async function fetchPatient() {
    try {
        const id = route.params.id;
        const res = await axios.get(`${BASE_URL}/${id}`);
        Object.assign(patientData, res.data);
    } catch (e) {
        error.value = 'Failed to load patient data.';
    } finally {
        loading.value = false;
    }
}

async function fetchServices() {
    try {
        const id = route.params.id;
        const res = await axios.get(`http://localhost:8080/api/patient-services/patient/${id}`);
        services.value = res.data.map(item => ({ // ✅ use .value
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

onMounted(() => {
    fetchPatient();
    fetchServices();
});

const showModal = ref(false);
const selectedType = ref('');
const loadingSubmit = ref(false);
const types = ['Prenatal', 'Family Planning', 'Other Services'];
const step = ref<'select' | 'form'>('select');

const form = reactive({
    dateAvailed: new Date().toISOString().split('T')[0],
    ward: '',
    employee: '',
    remarks: ''
});

async function selectType(type: string) {
    if (type === 'Family Planning') {
        try {
            await axios.post('http://localhost:8080/api/patient-services', {
                patientID: Number(route.params.id),
                serviceName: 'Family Planning',
                employeeName: '---',
                wardName: '---',
                dateAvailed: new Date().toISOString().split('T')[0],
                remarks: ''
            });
            await fetchServices();
        } catch (e) {
            console.error('Failed to save Family Planning service', e);
        }
        showModal.value = false;
        step.value = 'select';
        router.push(`/uikit/FamilyPlanningAdmission/${route.params.id}`); // ✅ has ID
        return;
    }
    if (type === 'Prenatal') {
        try {
            await axios.post('http://localhost:8080/api/patient-services', {
                patientID: Number(route.params.id),
                serviceName: 'Prenatal',
                employeeName: '---',
                wardName: '---',
                dateAvailed: new Date().toISOString().split('T')[0],
                remarks: ''
            });
            await fetchServices();
        } catch (e) {
            console.error('Failed to save Prenatal service', e);
        }
        showModal.value = false;
        step.value = 'select';
        router.push('/uikit/PrenatalAdmission'); // ✅ fixed route
        return;
    }

    selectedType.value = type;
    step.value = 'form';
    showModal.value = true;
}

function closeModal() {
    showModal.value = false;
    step.value = 'select';
    form.dateAvailed = new Date().toISOString().split('T')[0];
    form.ward = '';
    form.employee = '';
    form.remarks = '';
}

function viewService(service) {
    const name = service.service?.toLowerCase();
    const patientId = route.params.id;
    if (name === 'prenatal') {
        router.push(`/uikit/PrenatalAdmission/${patientId}`);
    } else if (name === 'family planning') {
        router.push(`/uikit/FamilyPlanningAdmission/${patientId}`); // ✅ pass patientID
    } else {
        alert(`No dedicated view page for "${service.service}" yet.`);
    }
}

async function handleSubmit() {
    loadingSubmit.value = true;
    try {
        await axios.post('http://localhost:8080/api/patient-services', {
            patientID: Number(route.params.id),
            serviceName: selectedType.value,
            employeeName: form.employee,
            wardName: form.ward,
            dateAvailed: form.dateAvailed,
            remarks: form.remarks
        });
        await fetchServices(); // ✅ refresh list
    } catch (e) {
        console.error('Failed to save service', e);
        alert('Failed to save service: ' + e.message);
    }
    loadingSubmit.value = false;
    closeModal();
}
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">

        <!-- Loading -->
        <div v-if="loading" class="text-center py-20 text-gray-500">
            Loading patient data...
        </div>

        <!-- Error -->
        <div v-else-if="error" class="text-center py-20 text-red-500">
            {{ error }}
        </div>

        <!-- Content -->
        <div v-else class="max-w-screen mx-auto">
            <div class="flex justify-between items-center mb-6">
                <div class="flex items-center gap-4">
                    <button
                        @click="router.push('/uikit/PatientsMain')"
                        class="text-gray-600 hover:text-gray-800 font-semibold">
                        ←
                    </button>
                    <h1 class="text-2xl font-semibold text-gray-800">Patient Profile</h1>
                </div>
                <button
                    @click="showModal = true"
                    class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">
                    Add New Service
                </button>
            </div>

            <!-- Patient Name -->
            <h2 class="text-3xl font-bold text-gray-900 mb-6">
                {{ patientData.fName }} {{ patientData.middleI }} {{ patientData.lName }}
            </h2>

            <!-- General Information -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg font-semibold text-gray-800">General Information</h3>
                    <button class="bg-blue-600 text-white px-4 py-1.5 rounded text-sm hover:bg-blue-700 transition">
                        Edit
                    </button>
                </div>

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

            <!-- Services Table -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden mb-6">
                <table class="w-full">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="th">Service</th>
                            <th class="th">Employee</th>
                            <th class="th">Ward</th>
                            <th class="th">Date Availed</th>
                            <th class="th">Remarks</th>
                            <th class="th">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="service in services" :key="service.id" class="border-b hover:bg-gray-50">
                            <td class="td">{{ service.service }}</td>
                            <td class="td">{{ service.employee }}</td>
                            <td class="td">{{ service.ward }}</td>
                            <td class="td">{{ service.dateAvailed }}</td>
                            <td class="td">{{ service.remarks }}</td>
                            <td class="td">
                                <button
                                    @click="viewService(service)"
                                    class="bg-blue-600 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-700">
                                    View
                                </button>
                            </td>
                        </tr>
                        <tr v-if="!services.length">
                            <td colspan="6" class="text-center py-6 text-gray-500">No services recorded.</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Service Modal -->
        <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
            <div class="bg-white rounded-xl shadow-2xl max-w-lg w-full overflow-hidden">
                <div class="p-6 border-b flex justify-between items-center bg-white">
                    <h3 class="text-xl font-bold text-gray-900">
                        {{ step === 'select' ? 'Select Service Type' : 'Add ' + selectedType }}
                    </h3>
                    <button @click="closeModal" class="text-gray-400 hover:text-gray-600">✕</button>
                </div>

                <div class="p-6 bg-gray-50">
                    <!-- Step 1: Selection -->
                    <div v-if="step === 'select'" class="space-y-4">
                        <button
                            v-for="type in types"
                            :key="type"
                            @click="selectType(type)"
                            class="w-full p-4 bg-white border rounded-lg text-left hover:border-blue-500 hover:bg-blue-50 transition flex justify-between items-center group"
                        >
                            <span class="font-semibold text-gray-700">{{ type }}</span>
                            <span class="text-blue-500 opacity-0 group-hover:opacity-100">➔</span>
                        </button>
                    </div>

                    <!-- Step 2: Form -->
                    <form v-else @submit.prevent="handleSubmit" class="space-y-4">
                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Date</label>
                                <input v-model="form.dateAvailed" type="date"
                                    class="w-full border rounded-lg px-3 py-2 bg-white" required />
                            </div>
                            <div>
                                <label class="block text-sm font-medium text-gray-700 mb-1">Ward</label>
                                <input v-model="form.ward" type="text" placeholder="e.g. Ward 1"
                                    class="w-full border rounded-lg px-3 py-2 bg-white" required />
                            </div>
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Attending Employee</label>
                            <input v-model="form.employee" type="text" placeholder="Name"
                                class="w-full border rounded-lg px-3 py-2 bg-white" required />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Remarks</label>
                            <textarea v-model="form.remarks"
                                class="w-full border rounded-lg px-3 py-2 bg-white" rows="3"></textarea>
                        </div>

                        <div class="flex gap-3 pt-4">
                            <button type="button" @click="step = 'select'"
                                class="flex-1 px-4 py-2 border rounded-lg hover:bg-gray-100">
                                Back
                            </button>
                            <button type="submit"
                                class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
                                :disabled="loadingSubmit">
                                {{ loadingSubmit ? 'Saving...' : 'Save Record' }}
                            </button>
                        </div>
                    </form>
                </div>
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
</style>
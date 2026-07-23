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

// A separate editable copy — so changes can be discarded on Cancel without
// mutating the displayed data until Save is actually confirmed.
const editForm = reactive({ ...patientData });

const loading = ref(true);
const error = ref('');
const services = ref([]);

// ── General Information edit state ──────────────────────────────────────────
const editingProfile = ref(false);
const savingProfile = ref(false);
const profileError = ref('');

function startEditProfile() {
    Object.assign(editForm, patientData);
    profileError.value = '';
    editingProfile.value = true;
}

function cancelEditProfile() {
    editingProfile.value = false;
    profileError.value = '';
}

async function saveProfile() {
    savingProfile.value = true;
    profileError.value = '';
    try {
        await axios.put(BASE_URL, { ...editForm, patientID: patientData.patientID });
        Object.assign(patientData, editForm);
        editingProfile.value = false;
    } catch (e) {
        console.error('Failed to update patient info', e);
        profileError.value = 'Failed to save changes. Please try again.';
    } finally {
        savingProfile.value = false;
    }
}

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

/** Fetch the list of clinical services so the modal is always up to date. */
async function fetchClinicalServices() {
    try {
        const res = await axios.get('http://localhost:8080/api/clinical-services');
        clinicalServices.value = res.data;
    } catch (e) {
        console.error('Failed to load clinical services.', e);
        // fallback: keep an empty list so the user sees nothing
    }
}

// ── Delete a mistaken service record ─────────────────────────────────────────
async function deleteService(serviceId) {
    if (!confirm('Are you sure you want to delete this service record? This cannot be undone.')) return;
    try {
        await axios.delete(`http://localhost:8080/api/patient-services/${serviceId}`);
        await fetchServices();
    } catch (e) {
        console.error('Failed to delete service record', e);
        alert('Failed to delete this service record: ' + (e.response?.data || e.message));
    }
}

onMounted(() => {
    fetchPatient();
    fetchServices();
    fetchClinicalServices();
});

const showModal = ref(false);
const selectedType = ref('');
const loadingSubmit = ref(false);
const clinicalServices = ref<Array<{ id: number; name: string; category: string }>>([]);
const step = ref<'select' | 'form'>('select');

const form = reactive({
    dateAvailed: new Date().toISOString().split('T')[0],
    ward: '',
    employee: '',
    remarks: ''
});

/**
 * Map a clinical‑service name to its dedicated route path, or null if it
 * does not have a dedicated form.
 */
function getDedicatedRoute(serviceName) {
    // Normalize: lowercase + strip all whitespace, so "Ultra Sound",
    // "UltraSound", and "ultrasound" all match the same way. Also handles
    // common typos like "Admision" (missing a letter) via startsWith.
    const normalized = serviceName.toLowerCase().replace(/\s+/g, '');

    if (normalized === 'prenatal') return 'PrenatalAdmission';
    if (normalized === 'familyplanning') return 'FamilyPlanningAdmission';
    if (normalized === 'ultrasound') return 'UltrasoundAdmission';
    if (normalized.startsWith('admi')) return 'Admission';  // covers "Admission" and "Admision" (typo)
    return null;
}

async function selectType(type: string) {
    // Check if this service has a dedicated form → redirect there
    const routeName = getDedicatedRoute(type);
    if (routeName) {
        try {
            const res = await axios.post('http://localhost:8080/api/patient-services', {
                patientID: Number(route.params.id),
                serviceName: type,
                employeeName: '---',
                wardName: '---',
                dateAvailed: new Date().toISOString().split('T')[0],
                remarks: ''
            });
            await fetchServices();
            const newServiceID = res.data.patientServiceID;
            showModal.value = false;
            step.value = 'select';
            router.push(`/uikit/${routeName}/${route.params.id}/${newServiceID}`);
        } catch (e) {
            console.error(`Failed to save ${type} service`, e);
        }
        return;
    }

    // Generic service → show the inline form
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
    const patientId = route.params.id;
    const serviceId = service.id;

    const routeName = getDedicatedRoute(service.service);
    if (routeName) {
        router.push(`/uikit/${routeName}/${patientId}/${serviceId}`);
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
        await fetchServices();
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

        <div v-if="loading" class="text-center py-20 text-gray-500">
            Loading patient data...
        </div>

        <div v-else-if="error" class="text-center py-20 text-red-500">
            {{ error }}
        </div>

        <div v-else class="max-w-screen mx-auto">
            <div class="flex justify-between items-center mb-6">
                <div class="flex items-center gap-4">
                    <button @click="router.push('/uikit/PatientsMain')"
                        class="text-gray-600 hover:text-gray-800 font-semibold">
                        ←
                    </button>
                    <h1 class="text-2xl font-semibold text-gray-800">Patient Profile</h1>
                </div>
                <button @click="showModal = true"
                    class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition">
                    Add New Service
                </button>
            </div>

            <h2 class="text-3xl font-bold text-gray-900 mb-6">
                {{ patientData.fName }} {{ patientData.middleI }} {{ patientData.lName }}
            </h2>

            <!-- General Information -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg font-semibold text-gray-800">General Information</h3>
                    <button
                        v-if="!editingProfile"
                        @click="startEditProfile"
                        class="bg-blue-600 text-white px-4 py-1.5 rounded text-sm hover:bg-blue-700 transition">
                        Edit
                    </button>
                    <div v-else class="flex gap-2">
                        <button
                            @click="cancelEditProfile"
                            :disabled="savingProfile"
                            class="bg-gray-200 text-gray-700 px-4 py-1.5 rounded text-sm hover:bg-gray-300 transition disabled:opacity-50">
                            Cancel
                        </button>
                        <button
                            @click="saveProfile"
                            :disabled="savingProfile"
                            class="bg-green-600 text-white px-4 py-1.5 rounded text-sm hover:bg-green-700 transition disabled:opacity-50">
                            {{ savingProfile ? 'Saving...' : 'Save Changes' }}
                        </button>
                    </div>
                </div>

                <p v-if="profileError" class="text-red-600 text-sm mb-3">{{ profileError }}</p>

                <!-- READ-ONLY VIEW -->
                <div v-if="!editingProfile" class="grid grid-cols-3 gap-6">
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

                <!-- EDIT FORM -->
                <div v-else class="grid grid-cols-3 gap-6">
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">First Name</label>
                        <input v-model="editForm.fName" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Middle Initial</label>
                        <input v-model="editForm.middleI" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Last Name</label>
                        <input v-model="editForm.lName" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Age</label>
                        <input v-model="editForm.age" type="number" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Birth Date</label>
                        <input v-model="editForm.bDate" type="date" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">House No</label>
                        <input v-model="editForm.houseNo" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Street</label>
                        <input v-model="editForm.street" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Barangay</label>
                        <input v-model="editForm.barangay" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Contact Number</label>
                        <input v-model="editForm.contactNumber" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Municipality</label>
                        <input v-model="editForm.municipality" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Province</label>
                        <input v-model="editForm.province" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Religion</label>
                        <input v-model="editForm.religion" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Education</label>
                        <input v-model="editForm.educationalAttainment" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Occupation</label>
                        <input v-model="editForm.occupation" type="text" class="w-full border rounded-lg px-3 py-2" />
                    </div>
                    <div>
                        <label class="text-sm text-gray-500 block mb-1">Email</label>
                        <input v-model="editForm.email" type="email" class="w-full border rounded-lg px-3 py-2" />
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
                                <div class="flex gap-2">
                                    <button @click="viewService(service)"
                                        class="bg-blue-600 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-700">
                                        View
                                    </button>
                                    <button @click="deleteService(service.id)"
                                        class="bg-red-50 text-red-700 px-3 py-1.5 rounded text-sm hover:bg-red-100 font-semibold">
                                        Delete
                                    </button>
                                </div>
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
                    <!-- Step 1: Selection — dynamic from clinical‑services API -->
                    <div v-if="step === 'select'" class="space-y-4">
                        <button
                            v-for="svc in clinicalServices"
                            :key="svc.id"
                            @click="selectType(svc.name)"
                            class="w-full p-4 bg-white border rounded-lg text-left hover:border-blue-500 hover:bg-blue-50 transition flex justify-between items-center group"
                        >
                            <span class="font-semibold text-gray-700">{{ svc.name }}</span>
                            <span class="text-gray-400 text-xs mr-2">{{ svc.category }}</span>
                            <span class="text-blue-500 opacity-0 group-hover:opacity-100">➔</span>
                        </button>
                        <!-- Show a friendly message when the list is empty -->
                        <p v-if="!clinicalServices.length" class="text-gray-400 text-center py-6">
                            No clinical services available. Please add one in Service Management first.
                        </p>
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
                            <textarea v-model="form.remarks" class="w-full border rounded-lg px-3 py-2 bg-white"
                                rows="3"></textarea>
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
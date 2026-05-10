<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/patients';

const searchQuery = ref('');
const showAddPatientModal = ref(false); // ✅ fixed syntax error (removed extra quote)
const patients = ref([]);

const formData = ref({
    fName: '',
    lName: '',
    middleI: '',
    age: '',
    bDate: '',
    houseNo: '',
    street: '',
    barangay: '',
    contactNumber: '',
    province: '',
    municipality: '',
    religion: '',
    educationalAttainment: '',
    occupation: '',
    email: ''
});

async function fetchPatients() {
    try {
        const res = await axios.get(`${BASE_URL}`);
        patients.value = res.data;
    } catch (e) {
        alert('Failed to fetch patients: ' + e.message);
    }
}

onMounted(() => fetchPatients());

const filteredPatients = computed(() => {
    if (!searchQuery.value) return patients.value;
    const query = searchQuery.value.toLowerCase();
    return patients.value.filter(patient =>
        (patient.patientID?.toString() || '').includes(query) ||
        (patient.fName || '').toLowerCase().includes(query) ||
        (patient.lName || '').toLowerCase().includes(query) ||
        (patient.contactNumber || '').includes(query)
    );
});

function addPatient() {
    showAddPatientModal.value = true;
}

function closeModal() {
    showAddPatientModal.value = false;
    resetForm();
}

function resetForm() {
    formData.value = {
        fName: '',
        lName: '',
        middleI: '',
        age: '',
        bDate: '',
        houseNo: '',
        street: '',
        barangay: '',
        contactNumber: '',
        province: '',
        municipality: '',
        religion: '',
        educationalAttainment: '',
        occupation: '',
        email: ''
    };
}

async function handleAddPatient() {
    if (!formData.value.fName || !formData.value.lName || !formData.value.contactNumber) {
        alert('Please fill in required fields: First Name, Last Name, Contact Number');
        return;
    }

    try {
        await axios.post(`${BASE_URL}`, {
            fName: formData.value.fName,
            lName: formData.value.lName,
            middleI: formData.value.middleI,
            age: formData.value.age ? parseInt(formData.value.age) : null,
            bDate: formData.value.bDate || null,
            houseNo: formData.value.houseNo,
            street: formData.value.street,
            barangay: formData.value.barangay,
            contactNumber: formData.value.contactNumber,
            province: formData.value.province,
            municipality: formData.value.municipality,
            religion: formData.value.religion,
            educationalAttainment: formData.value.educationalAttainment,
            occupation: formData.value.occupation,
            email: formData.value.email
        });

        await fetchPatients();
        closeModal();
        alert('Patient added successfully!');
    } catch (e) {
        alert('Failed to add patient: ' + (e.response?.data?.message || e.message));
    }
}

async function deletePatient(patient) {
    if (confirm(`Are you sure you want to delete ${patient.fName} ${patient.lName}?`)) {
        try {
            await axios.delete(`${BASE_URL}/${patient.patientID}`);
            await fetchPatients();
            alert('Patient deleted successfully!');
        } catch (e) {
            alert('Failed to delete patient: ' + (e.response?.data?.message || e.message));
        }
    }
}
</script>

<template>
    <div class="min-h-screen bg-white p-6">
        <!-- Page Header -->
        <div class="mb-6">
            <h1 class="text-2xl font-bold text-gray-800">Patient Management</h1>
            <p class="text-gray-600 text-sm">Manage patient records</p>
        </div>

        <!-- Search and Action Buttons -->
        <div class="flex items-center gap-4 mb-6">
            <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by Name, ID, or Contact Number"
                class="flex-1 px-4 py-2 bg-gray-100 border-0 rounded-full focus:ring-2 focus:ring-purple-500 focus:bg-white"
            />
            <button
                @click="$router.push('/uikit/Admission')"
                class="px-6 py-2 bg-purple-700 text-white rounded-full hover:bg-purple-800 transition-colors"
            >
                Admission
            </button>
            <button
                @click="$router.push('/uikit/Patient')"
                class="px-6 py-2 bg-purple-700 text-white rounded-full hover:bg-purple-800 transition-colors"
            >
                View list of Prenatal
            </button>
            <button
                @click="addPatient"
                class="px-6 py-2 bg-purple-700 text-white rounded-full hover:bg-purple-800 transition-colors"
            >
                Add Patient
            </button>
        </div>

        <!-- Patient Table -->
        <div class="bg-white rounded-lg border border-gray-200 overflow-hidden">
            <table class="w-full">
                <thead class="bg-gray-50">
                    <tr>
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">ID</th>
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Patient Name</th>
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Contact No.</th>
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Email</th>
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="patient in filteredPatients" :key="patient.patientID"
                        class="border-t border-gray-200 hover:bg-gray-50">
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.patientID }}</td>
                        <td class="px-6 py-4 text-sm text-gray-900">
                            {{ patient.fName }} {{ patient.middleI }} {{ patient.lName }}
                        </td>
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.contactNumber }}</td>
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.email ?? '—' }}</td>
                        <td class="px-6 py-4">
                            <div class="flex gap-2">
                                <button
                                    @click="$router.push(`/uikit/PatientProfiling/${patient.patientID}`)"
                                    class="px-4 py-1 bg-purple-200 text-gray-800 rounded hover:bg-purple-300 text-sm">
                                    View
                                    </button>
                                <button
                                    @click="deletePatient(patient)"
                                    class="px-4 py-1 bg-red-200 text-gray-800 rounded hover:bg-red-300 text-sm">
                                    Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div v-if="filteredPatients.length === 0" class="text-center py-12">
                <p class="text-gray-500">
                    {{ searchQuery ? `No patients found matching "${searchQuery}"` : 'No patients found' }}
                </p>
            </div>
        </div>

        <!-- Add Patient Modal -->
        <div v-if="showAddPatientModal"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
            @click.self="closeModal">
            <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">

                <div class="border-b border-gray-200 p-6">
                    <h3 class="text-xl font-bold text-gray-800 text-center">General Information</h3>
                </div>

                <div class="p-6">
                    <form @submit.prevent="handleAddPatient" class="space-y-4">

                        <!-- Name -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">First Name *</label>
                                <input v-model="formData.fName" type="text" required
                                    placeholder="First Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Middle Initial</label>
                                <input v-model="formData.middleI" type="text"
                                    placeholder="M.I."
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Last Name *</label>
                                <input v-model="formData.lName" type="text" required
                                    placeholder="Last Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                        </div>

                        <!-- Age, Birth Date, Email -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Age</label>
                                <input v-model="formData.age" type="number"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Birth Date</label>
                                <input v-model="formData.bDate" type="date"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Email</label>
                                <input v-model="formData.email" type="email"
                                    placeholder="email@example.com"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                        </div>

                        <!-- Address -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">House No.</label>
                                <input v-model="formData.houseNo" type="text"
                                    placeholder="Zone Number"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Street</label>
                                <input v-model="formData.street" type="text"
                                    placeholder="Zone"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Barangay</label>
                                <input v-model="formData.barangay" type="text"
                                    placeholder="Barangay Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                        </div>

                        <!-- Contact, Province, Municipality -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Contact Number *</label>
                                <input v-model="formData.contactNumber" type="tel" required
                                    placeholder="xxxx-xxx-xxxx"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Province</label>
                                <input v-model="formData.province" type="text"
                                    placeholder="Province Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Municipality</label>
                                <input v-model="formData.municipality" type="text"
                                    placeholder="Municipality Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                        </div>

                        <!-- Religion, Education, Occupation -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Religion</label>
                                <input v-model="formData.religion" type="text"
                                    placeholder="Patient's Religion"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Educational Attainment</label>
                                <input v-model="formData.educationalAttainment" type="text"
                                    placeholder="Highest Educ Attained"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Occupation</label>
                                <input v-model="formData.occupation" type="text"
                                    placeholder="Current Occupation"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" />
                            </div>
                        </div>

                        <!-- Buttons -->
                        <div class="flex justify-between items-center pt-4 border-t">
                            <button type="button" @click="closeModal"
                                class="text-gray-600 hover:text-gray-800 font-semibold">
                                ← Back
                            </button>
                            <button type="submit"
                                class="px-8 py-2 bg-purple-300 text-gray-800 rounded-lg hover:bg-purple-400 transition-colors font-semibold">
                                Add
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, computed } from 'vue';

const searchQuery = ref('');
const showAddPatientModal = ref(false);

const formData = ref({
    fullName: '',
    age: '',
    birthDate: '',
    houseNo: '',
    street: '',
    barangay: '',
    contactNumber: '',
    province: '',
    municipality: '',
    religion: '',
    educationalAttainment: '',
    occupation: ''
});

const patients = ref([
    {
        id: 'P001',
        name: 'Althea Mae Santos',
        contactNo: '0917 123 4567',
        email: 'sampleEmail.com'
    }
]);

const filteredPatients = computed(() => {
    if (!searchQuery.value) return patients.value;
    
    const query = searchQuery.value.toLowerCase();
    return patients.value.filter(patient =>
        patient.id.toLowerCase().includes(query) ||
        patient.name.toLowerCase().includes(query) ||
        patient.contactNo.includes(query)
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
        fullName: '',
        age: '',
        birthDate: '',
        houseNo: '',
        street: '',
        barangay: '',
        contactNumber: '',
        province: '',
        municipality: '',
        religion: '',
        educationalAttainment: '',
        occupation: ''
    };
}

function handleAddPatient() {
    if (!formData.value.fullName || !formData.value.contactNumber) {
        alert('Please fill in required fields');
        return;
    }

    const newPatient = {
        id: `P${String(patients.value.length + 1).padStart(3, '0')}`,
        name: formData.value.fullName,
        contactNo: formData.value.contactNumber,
        email: `${formData.value.fullName.toLowerCase().replace(/\s+/g, '.')}@email.com`
    };

    patients.value.push(newPatient);
    closeModal();
    alert('Patient added successfully!');
}

function deletePatient(patient) {
    if (confirm(`Are you sure you want to delete ${patient.name}?`)) {
        patients.value = patients.value.filter(p => p.id !== patient.id);
        alert('Patient deleted successfully!');
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
                    <tr v-for="patient in filteredPatients" :key="patient.id" class="border-t border-gray-200 hover:bg-gray-50">
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.id }}</td>
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.name }}</td>
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.contactNo }}</td>
                        <td class="px-6 py-4 text-sm text-gray-900">{{ patient.email }}</td>
                        <td class="px-6 py-4">
                            <div class="flex gap-2">
                                <button
                                @click="$router.push('/uikit/PatientProfiling')"
                                    class="px-4 py-1 bg-purple-200 text-gray-800 rounded hover:bg-purple-300 text-sm">View</button><button
                                    @click="deletePatient(patient)"
                                    class="px-4 py-1 bg-red-200 text-gray-800 rounded hover:bg-red-300 text-sm"
                                >
                                    Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <!-- No Results Message -->
            <div v-if="filteredPatients.length === 0" class="text-center py-12">
                <p class="text-gray-500">No patients found matching "{{ searchQuery }}"</p>
            </div>
        </div>

        <!-- Add Patient Modal -->
        <div v-if="showAddPatientModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4" @click.self="closeModal">
            <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
                <!-- Modal Header -->
                <div class="border-b border-gray-200 p-6">
                    <h3 class="text-xl font-bold text-gray-800 text-center">General Information</h3>
                </div>

                <!-- Modal Body -->
                <div class="p-6">
                    <form @submit.prevent="handleAddPatient" class="space-y-4">
                        <!-- Patient's Full Name, Age, Birth Date -->
                        <div class="grid grid-cols-3 gap-4">
                            <div class="col-span-2">
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Patient's Full Name</label>
                                <input
                                    v-model="formData.fullName"
                                    type="text"
                                    placeholder="First Name, Middle I, Last Name"
                                    required
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Age</label>
                                <input
                                    v-model="formData.age"
                                    type="number"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Birth Date</label>
                                <input
                                    v-model="formData.birthDate"
                                    type="date"
                                    placeholder="yy-mm-dd"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <!-- HouseNo, Street, Barangay -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">HouseNo</label>
                                <input
                                    v-model="formData.houseNo"
                                    type="text"
                                    placeholder="zone Number"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Street</label>
                                <input
                                    v-model="formData.street"
                                    type="text"
                                    placeholder="Zone"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Barangay</label>
                                <input
                                    v-model="formData.barangay"
                                    type="text"
                                    placeholder="Barangay Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <!-- Contact Number, Province, Municipality -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Contact Number</label>
                                <input
                                    v-model="formData.contactNumber"
                                    type="tel"
                                    placeholder="xxxx-xxx-xxxx"
                                    required
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Province</label>
                                <input
                                    v-model="formData.province"
                                    type="text"
                                    placeholder="Province Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Municipality</label>
                                <input
                                    v-model="formData.municipality"
                                    type="text"
                                    placeholder="Municipality Name"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <!-- Religion, Educational Attainment, Occupation -->
                        <div class="grid grid-cols-3 gap-4">
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Religion</label>
                                <input
                                    v-model="formData.religion"
                                    type="text"
                                    placeholder="Patient's Religion"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Educational Attainment</label>
                                <input
                                    v-model="formData.educationalAttainment"
                                    type="text"
                                    placeholder="Highest Educ Attain"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold text-gray-700 mb-2">Occupation</label>
                                <input
                                    v-model="formData.occupation"
                                    type="text"
                                    placeholder="Current Occupation"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <!-- Buttons -->
                        <div class="flex justify-between items-center pt-4 border-t">
                            <button
                                type="button"
                                @click="closeModal"
                                class="text-gray-600 hover:text-gray-800 font-semibold"
                            >
                                ← Back
                            </button>
                            <button
                                type="submit"
                                class="px-8 py-2 bg-purple-300 text-gray-800 rounded-lg hover:bg-purple-400 transition-colors font-semibold"
                            >
                                Add
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>
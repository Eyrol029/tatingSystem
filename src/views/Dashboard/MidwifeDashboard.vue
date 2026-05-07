<script setup>
import { computed, ref } from 'vue';

const view = ref('list');
const selectedEmployee = ref(null);
const searchQuery = ref('');

const employees = ref([
    {
        id: 'E001',
        name: 'Dr. Carmen Reyes',
        position: 'Obstetrician',
        department: 'Maternity',
        contact: '09123456789',
        email: 'carmen.reyes@clinic.com',
        dateHired: '2020-01-15',
        salary: 50000,
        status: 'Active'
    },
    {
        id: 'E002',
        name: 'Nurse Elena Santos',
        position: 'Registered Nurse',
        department: 'Maternity',
        contact: '09234567890',
        email: 'elena.santos@clinic.com',
        dateHired: '2021-03-20',
        salary: 25000,
        status: 'Active'
    },
    {
        id: 'E003',
        name: 'Maria Cruz',
        position: 'Midwife',
        department: 'Family Planning',
        contact: '09345678901',
        email: 'maria.cruz@clinic.com',
        dateHired: '2019-07-10',
        salary: 22000,
        status: 'Active'
    }
]);

const formData = ref({
    name: '',
    position: '',
    department: 'Maternity',
    contact: '',
    email: '',
    salary: '',
    status: 'Active'
});

const filteredEmployees = computed(() => {
    const query = searchQuery.value.toLowerCase();
    return employees.value.filter((e) => e.name.toLowerCase().includes(query) || e.id.toLowerCase().includes(query) || e.position.toLowerCase().includes(query));
});

function handleAddEmployee() {
    const newEmployee = {
        id: `E${String(employees.value.length + 1).padStart(3, '0')}`,
        name: formData.value.name,
        position: formData.value.position,
        department: formData.value.department,
        contact: formData.value.contact,
        email: formData.value.email,
        dateHired: new Date().toISOString().split('T')[0],
        salary: parseFloat(formData.value.salary),
        status: formData.value.status
    };
    employees.value.push(newEmployee);
    resetForm();
    view.value = 'list';
}

function handleEditEmployee() {
    if (!selectedEmployee.value) return;
    const index = employees.value.findIndex((e) => e.id === selectedEmployee.value.id);
    if (index !== -1) {
        employees.value[index] = {
            ...selectedEmployee.value,
            name: formData.value.name,
            position: formData.value.position,
            department: formData.value.department,
            contact: formData.value.contact,
            email: formData.value.email,
            salary: parseFloat(formData.value.salary),
            status: formData.value.status
        };
    }
    resetForm();
    view.value = 'list';
}

function handleDeleteEmployee(employeeId) {
    if (confirm('Are you sure you want to delete this employee record?')) {
        employees.value = employees.value.filter((e) => e.id !== employeeId);
    }
}

function resetForm() {
    formData.value = {
        name: '',
        position: '',
        department: 'Maternity',
        contact: '',
        email: '',
        salary: '',
        status: 'Active'
    };
    selectedEmployee.value = null;
}

function openEditForm(employee) {
    selectedEmployee.value = employee;
    formData.value = {
        name: employee.name,
        position: employee.position,
        department: employee.department,
        contact: employee.contact,
        email: employee.email,
        salary: employee.salary.toString(),
        status: employee.status
    };
    view.value = 'edit';
}

function viewEmployeeDetail(employee) {
    selectedEmployee.value = employee;
    view.value = 'detail';
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}
</script>

<template>
    <div class="space-y-6 p-6 bg-white min-h-screen">
        <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-2xl font-bold text-gray-800 mb-6">
                    {{ view === 'add' ? 'Add New Employee' : 'Edit Employee Details' }}
                </h3>
                <form @submit.prevent="view === 'add' ? handleAddEmployee() : handleEditEmployee()" class="space-y-4">
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Full Name *</label>
                        <input v-model="formData.name" type="text" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Position *</label>
                            <input v-model="formData.position" type="text" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Department *</label>
                            <select v-model="formData.department" class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                                <option>Maternity</option>
                                <option>Family Planning</option>
                                <option>Administration</option>
                                <option>Finance</option>
                            </select>
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Contact Number *</label>
                        <input v-model="formData.contact" type="tel" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Email Address *</label>
                        <input v-model="formData.email" type="email" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Monthly Salary (₱) *</label>
                            <input v-model="formData.salary" type="number" step="0.01" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Status *</label>
                            <select v-model="formData.status" class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                                <option>Active</option>
                                <option>Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="flex gap-3 pt-4">
                        <button type="submit" class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">
                            {{ view === 'add' ? 'Add Employee' : 'Save Changes' }}
                        </button>
                        <button
                            type="button"
                            @click="
                                resetForm();
                                view = 'list';
                            "
                            class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <div v-else-if="view === 'detail' && selectedEmployee" class="max-w-3xl">
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center justify-between mb-6">
                    <h3 class="text-2xl font-bold text-gray-800">Employee Details</h3>
                    <button @click="view = 'list'" class="text-gray-500 hover:text-gray-700 text-2xl">✕</button>
                </div>
                <div class="space-y-4">
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Employee ID</p>
                            <p class="text-gray-900 mt-1">{{ selectedEmployee.id }}</p>
                        </div>
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Date Hired</p>
                            <p class="text-gray-900 mt-1">{{ formatDate(selectedEmployee.dateHired) }}</p>
                        </div>
                    </div>
                    <div>
                        <p class="text-sm font-semibold text-gray-500">Full Name</p>
                        <p class="text-gray-900 mt-1">{{ selectedEmployee.name }}</p>
                    </div>
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Position</p>
                            <p class="text-gray-900 mt-1">{{ selectedEmployee.position }}</p>
                        </div>
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Department</p>
                            <p class="text-gray-900 mt-1">{{ selectedEmployee.department }}</p>
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Contact Number</p>
                            <p class="text-gray-900 mt-1">{{ selectedEmployee.contact }}</p>
                        </div>
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Email Address</p>
                            <p class="text-gray-900 mt-1">{{ selectedEmployee.email }}</p>
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Monthly Salary</p>
                            <p class="text-gray-900 mt-1">₱{{ selectedEmployee.salary.toLocaleString() }}</p>
                        </div>
                        <div>
                            <p class="text-sm font-semibold text-gray-500">Status</p>
                            <span :class="['inline-block px-2 py-1 rounded text-xs mt-1', selectedEmployee.status === 'Active' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700']">
                                {{ selectedEmployee.status }}
                            </span>
                        </div>
                    </div>
                </div>
                <div class="flex gap-3 mt-6 pt-6 border-t">
                    <button @click="openEditForm(selectedEmployee)" class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">Edit Details</button>
                    <button @click="view = 'list'" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300">Back to List</button>
                </div>
            </div>
        </div>

        <template v-else>
            <div class="flex items-center justify-between">
                <div>
                    <h3 class="text-2xl font-bold text-gray-800">Employee Management</h3>
                    <p class="text-gray-600 text-sm mt-1">Manage employee records and information</p>
                </div>
                <button @click="view = 'add'" class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 flex items-center gap-2">
                    <span class="text-xl">+</span>
                    Add Employee
                </button>
            </div>

            <div class="bg-white rounded-lg shadow p-4">
                <input v-model="searchQuery" type="text" placeholder="Search by name, ID, or position..." class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
            </div>

            <div class="bg-white rounded-lg shadow overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gray-50 border-b">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">ID</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Name</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Position</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Department</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Contact</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
                                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <tr v-for="employee in filteredEmployees" :key="employee.id" class="hover:bg-gray-50">
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ employee.id }}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-gray-900">{{ employee.name }}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.position }}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.department }}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.contact }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span :class="['px-2 py-1 rounded text-xs', employee.status === 'Active' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700']">
                                        {{ employee.status }}
                                    </span>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm">
                                    <div class="flex gap-2">
                                        <button @click="viewEmployeeDetail(employee)" class="px-3 py-1 bg-blue-500 text-white rounded text-sm hover:bg-blue-600">View</button>
                                        <button @click="openEditForm(employee)" class="px-3 py-1 bg-teal-600 text-white rounded text-sm hover:bg-teal-700">Edit</button>
                                        <button @click="handleDeleteEmployee(employee.id)" class="px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600">Delete</button>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div v-if="filteredEmployees.length === 0" class="bg-white rounded-lg shadow p-12 text-center">
                <p class="text-gray-600">No employees found</p>
            </div>
        </template>
    </div>
</template>

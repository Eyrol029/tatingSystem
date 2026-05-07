<script setup>
import { ref, computed } from 'vue';

const showModal = ref(false);
const searchQuery = ref('');
const sortField = ref('date');
const sortOrder = ref('desc');

const newLog = ref({
    name: '',
    date: '',
    purpose: '',
    address: '',
    contactNo: ''
});

// Sample clinic logs data
const logs = ref([
    {
        id: 'L001',
        name: 'Maria Santos',
        date: '2024-12-28',
        purpose: 'Prenatal Checkup',
        address: '123 Main St, Davao City',
        contactNo: '09123456789'
    },
    {
        id: 'L002',
        name: 'Ana Cruz',
        date: '2024-12-27',
        purpose: 'Postnatal Consultation',
        address: '456 Oak Ave, Davao City',
        contactNo: '09234567890'
    },
    {
        id: 'L003',
        name: 'Rosa Mendoza',
        date: '2024-12-26',
        purpose: 'Family Planning',
        address: '789 Pine Rd, Davao City',
        contactNo: '09345678901'
    },
    {
        id: 'L004',
        name: 'Elena Reyes',
        date: '2024-12-25',
        purpose: 'Immunization',
        address: '321 Elm St, Davao City',
        contactNo: '09456789012'
    },
    {
        id: 'L005',
        name: 'Carmen Garcia',
        date: '2024-12-24',
        purpose: 'General Checkup',
        address: '654 Maple Dr, Davao City',
        contactNo: '09567890123'
    }
]);

const filteredLogs = computed(() => {
    return logs.value.filter((log) => {
        const searchLower = searchQuery.value.toLowerCase();
        return (
            log.name.toLowerCase().includes(searchLower) ||
            log.purpose.toLowerCase().includes(searchLower) ||
            log.address.toLowerCase().includes(searchLower) ||
            log.contactNo.includes(searchQuery.value)
        );
    });
});

const sortedLogs = computed(() => {
    const copy = [...filteredLogs.value];
    copy.sort((a, b) => {
        let aVal = a[sortField.value];
        let bVal = b[sortField.value];

        if (typeof aVal === 'string') {
            return sortOrder.value === 'asc' 
                ? aVal.localeCompare(bVal) 
                : bVal.localeCompare(aVal);
        }

        return sortOrder.value === 'asc' ? aVal - bVal : bVal - aVal;
    });

    return copy;
});

function openModal() {
    showModal.value = true;
    newLog.value = {
        name: '',
        date: new Date().toISOString().split('T')[0],
        purpose: '',
        address: '',
        contactNo: ''
    };
}

function closeModal() {
    showModal.value = false;
}

function addLog() {
    if (!newLog.value.name || !newLog.value.date || !newLog.value.purpose) {
        alert('Please fill in all required fields (Name, Date, Purpose)');
        return;
    }

    const log = {
        id: 'L' + String(logs.value.length + 1).padStart(3, '0'),
        ...newLog.value
    };

    logs.value.unshift(log);
    closeModal();
}

function handleSort(field) {
    if (sortField.value === field) {
        sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
    } else {
        sortField.value = field;
        sortOrder.value = 'asc';
    }
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

function deleteLog(id) {
    if (confirm('Are you sure you want to delete this log?')) {
        logs.value = logs.value.filter(log => log.id !== id);
    }
}
</script>

<template>
    <div class="min-h-screen bg-gray-50 p-6">
        <div class="max-w-full mx-auto px-4">
            <!-- Header -->
            <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
                <div class="flex items-center justify-between">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-800">Clinic Logs</h1>
                        <p class="text-gray-600 mt-1">Clinic system logs</p>
                    </div>
                    <button
                        @click="openModal"
                        class="bg-purple-600 hover:bg-purple-700 text-white px-6 py-3 rounded-lg font-semibold transition-colors shadow-md"
                    >
                        Add Logs
                    </button>
                </div>
            </div>

            <!-- Search Bar -->
            <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
                <input
                    v-model="searchQuery"
                    type="text"
                    placeholder="Search by name, purpose, address, or contact number..."
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                />
            </div>

            <!-- Stats -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <div class="bg-white p-4 rounded-lg shadow-sm">
                    <p class="text-sm text-gray-600 mb-1">Total Logs</p>
                    <p class="text-3xl font-bold text-gray-800">{{ logs.length }}</p>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-sm">
                    <p class="text-sm text-gray-600 mb-1">Today's Visits</p>
                    <p class="text-3xl font-bold text-purple-600">
                        {{ logs.filter(l => l.date === new Date().toISOString().split('T')[0]).length }}
                    </p>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-sm">
                    <p class="text-sm text-gray-600 mb-1">This Week</p>
                    <p class="text-3xl font-bold text-blue-600">{{ logs.length }}</p>
                </div>
            </div>

            <!-- Table -->
            <div class="bg-white rounded-lg shadow-sm overflow-hidden">
                <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-gray-50 border-b border-gray-200">
                            <tr>
                                <th class="px-6 py-4 text-left">
                                    <button
                                        @click="handleSort('name')"
                                        class="flex items-center gap-2 font-semibold text-sm text-gray-700 hover:text-purple-600"
                                    >
                                        Name
                                        <span v-if="sortField === 'name'">
                                            {{ sortOrder === 'asc' ? '↑' : '↓' }}
                                        </span>
                                    </button>
                                </th>
                                <th class="px-6 py-4 text-left">
                                    <button
                                        @click="handleSort('date')"
                                        class="flex items-center gap-2 font-semibold text-sm text-gray-700 hover:text-purple-600"
                                    >
                                        Date
                                        <span v-if="sortField === 'date'">
                                            {{ sortOrder === 'asc' ? '↑' : '↓' }}
                                        </span>
                                    </button>
                                </th>
                                <th class="px-6 py-4 text-left">
                                    <button
                                        @click="handleSort('purpose')"
                                        class="flex items-center gap-2 font-semibold text-sm text-gray-700 hover:text-purple-600"
                                    >
                                        Purpose
                                        <span v-if="sortField === 'purpose'">
                                            {{ sortOrder === 'asc' ? '↑' : '↓' }}
                                        </span>
                                    </button>
                                </th>
                                <th class="px-6 py-4 text-left font-semibold text-sm text-gray-700">
                                    Address
                                </th>
                                <th class="px-6 py-4 text-left font-semibold text-sm text-gray-700">
                                    Contact No.
                                </th>
                                <th class="px-6 py-4 text-center font-semibold text-sm text-gray-700">
                                    Action
                                </th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200">
                            <tr
                                v-for="log in sortedLogs"
                                :key="log.id"
                                class="hover:bg-gray-50 transition-colors"
                            >
                                <td class="px-6 py-4">
                                    <p class="font-medium text-gray-800">{{ log.name }}</p>
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-600">
                                    {{ formatDate(log.date) }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-600">
                                    {{ log.purpose }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-600">
                                    {{ log.address }}
                                </td>
                                <td class="px-6 py-4 text-sm text-gray-600">
                                    {{ log.contactNo }}
                                </td>
                                <td class="px-6 py-4 text-center">
                                    <button
                                        @click="deleteLog(log.id)"
                                        class="text-red-600 hover:text-red-700 text-sm font-medium"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div v-if="sortedLogs.length === 0" class="text-center py-12">
                    <p class="text-gray-500">No logs found matching your search.</p>
                </div>
            </div>
        </div>

        <!-- Add Log Modal -->
        <div
            v-if="showModal"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
            @click.self="closeModal"
        >
            <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
                <div class="flex items-center justify-between mb-6">
                    <h2 class="text-2xl font-bold text-gray-800">Add New Log</h2>
                    <button
                        @click="closeModal"
                        class="text-gray-400 hover:text-gray-600"
                    >
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>

                <div class="space-y-4">
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Name <span class="text-red-500">*</span>
                        </label>
                        <input
                            v-model="newLog.name"
                            type="text"
                            placeholder="Enter patient name"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Date <span class="text-red-500">*</span>
                        </label>
                        <input
                            v-model="newLog.date"
                            type="date"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Purpose <span class="text-red-500">*</span>
                        </label>
                        <input
                            v-model="newLog.purpose"
                            type="text"
                            placeholder="e.g., Prenatal Checkup"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Address
                        </label>
                        <input
                            v-model="newLog.address"
                            type="text"
                            placeholder="Enter address"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Contact No.
                        </label>
                        <input
                            v-model="newLog.contactNo"
                            type="tel"
                            placeholder="e.g., 09123456789"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        />
                    </div>
                </div>

                <div class="flex gap-3 mt-6">
                    <button
                        @click="closeModal"
                        class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-semibold transition-colors"
                    >
                        Cancel
                    </button>
                    <button
                        @click="addLog"
                        class="flex-1 px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 font-semibold transition-colors"
                    >
                        Add Log
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
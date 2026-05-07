<script setup>
import { ref } from 'vue';

const dateFilter = ref('Today');
const customStartDate = ref('');
const customEndDate = ref('');

// Stats data
const stats = {
    totalPatients: 504,
    totalDeliveries: 301,
    availableWards: 3,
    totalAppointments: 102
};

// Recent activities
const recentActivities = [
    { text: 'New patient Maria Santos registered', time: '5 minutes ago' },
    { text: 'Prenatal checkup record updated', time: '1 hour ago' },
    { text: 'Monthly prenatal report generated', time: '2 hours ago' },
    { text: 'Ward 2 occupancy updated', time: '3 hours ago' },
    { text: 'Payment received paid by GH Prep', time: '4 hours ago' }
];

// Pending requests
const pendingRequests = [
    { type: 'Admission Request', name: 'Ana Cruz', subtitle: 'Family Planning Consultation', status: 'Pending' },
    { type: 'Supply Request', name: 'Medical Supplies', subtitle: 'Maternity Department', status: 'Pending' },
    { type: 'Appointment Request', name: 'Rosa Mendoza', subtitle: 'Maternity Checkup', status: 'Pending' }
];

// Financial data
const financial = {
    totalRevenue: 125450.0,
    totalExpenses: 48230.0,
    netIncome: 77220.0
};

function generateReport() {
    alert('Generating report...');
}
</script>

<template>
    <div class="p-6 bg-gray-100 min-h-screen">
        <!-- Header -->
        <div class="flex justify-between items-start mb-6">
            <div>
                <h1 class="text-2xl font-bold text-gray-800">Welcome to the Dashboard</h1>
                <p class="text-gray-600 text-sm mt-1">Overview of clinic operations and key metrics</p>
            </div>

            <!-- Filter Section -->
            <div class="bg-white rounded-lg shadow p-4 w-90">
                <div class="flex items-center gap-2 mb-3">
                    <span class="text-gray-700 font-semibold">📅 Filter By Date</span>
                </div>
                <div class="flex gap-2 mb-3">
                    <button @click="dateFilter = 'Today'" :class="['px-4 py-2 rounded text-sm', dateFilter === 'Today' ? 'bg-[#8e4f70] text-white' : 'bg-gray-100 text-gray-600 hover:bg-[#8e4f70] hover:text-white']">Today</button>
                    <button @click="dateFilter = 'Last 7 days'" :class="['px-4 py-2 rounded text-sm', dateFilter === 'Last 7 days' ? 'bg-[#8e4f70] text-white' : 'bg-gray-100 text-gray-600 hover:bg-[#8e4f70] hover:text-white']">Last 7 days</button>
                    <button @click="dateFilter = 'This Month'" :class="['px-4 py-2 rounded text-sm', dateFilter === 'This Month' ? 'bg-[#8e4f70] text-white' : 'bg-gray-100 text-gray-600 hover:bg-[#8e4f70] hover:text-white']">This Month</button>
                </div>
                <div>
                    <label class="block text-xs text-gray-600 mb-2">Custom</label>
                    <div class="flex gap-2">
                        <input v-model="customStartDate" type="date" class="flex-1 px-3 py-2 border rounded text-sm" placeholder="Start Date" />
                        <input v-model="customEndDate" type="date" class="flex-1 px-3 py-2 border rounded text-sm" placeholder="End Date" />
                    </div>
                </div>
            </div>
        </div>
        <!-- Generate Report Button -->
        <div class="flex justify-end mb-6">
            <button @click="generateReport" class="px-6 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-500">Generate Report</button>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
            <!-- Total Patients -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-3 mb-2">
                    <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <span class="text-blue-600 text-xl">👤</span>
                    </div>
                    <span class="text-gray-600 text-sm">Total Patients</span>
                </div>
                <div class="text-3xl font-bold text-gray-800">{{ stats.totalPatients }}</div>
            </div>

            <!-- Total Deliveries -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-3 mb-2">
                    <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <span class="text-blue-600 text-xl">👶</span>
                    </div>
                    <span class="text-gray-600 text-sm">Total Deliveries</span>
                </div>
                <div class="text-3xl font-bold text-gray-800">{{ stats.totalDeliveries }}</div>
            </div>

            <!-- Available Wards -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-3 mb-2">
                    <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <span class="text-blue-600 text-xl">🏥</span>
                    </div>
                    <span class="text-gray-600 text-sm">Available Wards</span>
                </div>
                <div class="text-3xl font-bold text-gray-800">{{ stats.availableWards }}</div>
            </div>

            <!-- Total Appointments -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-3 mb-2">
                    <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <span class="text-blue-600 text-xl">📅</span>
                    </div>
                    <span class="text-gray-600 text-sm">Total Appointment</span>
                </div>
                <div class="text-3xl font-bold text-gray-800">{{ stats.totalAppointments }}</div>
            </div>
        </div>

        <!-- Recent Activity & Pending Requests -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
            <!-- Recent Activity -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-2 mb-4">
                    <span class="text-xl">📊</span>
                    <h3 class="font-bold text-gray-800">Recent Activity</h3>
                </div>
                <div class="space-y-4">
                    <div v-for="(activity, index) in recentActivities" :key="index" class="flex items-start gap-3">
                        <div class="w-2 h-2 bg-green-500 rounded-full mt-2"></div>
                        <div class="flex-1">
                            <p class="text-gray-800 text-sm">{{ activity.text }}</p>
                            <p class="text-gray-500 text-xs">{{ activity.time }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Pending Requests -->
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center gap-2 mb-4">
                    <span class="text-xl">📈</span>
                    <h3 class="font-bold text-gray-800">Pending Requests</h3>
                </div>
                <div class="space-y-4">
                    <div v-for="(request, index) in pendingRequests" :key="index" class="bg-purple-50 rounded-lg p-4">
                        <div class="flex justify-between items-start mb-2">
                            <div>
                                <p class="text-xs text-gray-600">{{ request.type }}</p>
                                <p class="font-bold text-gray-800">{{ request.name }}</p>
                            </div>
                            <span class="px-3 py-1 bg-yellow-400 text-yellow-900 text-xs rounded-full">{{ request.status }}</span>
                        </div>
                        <p class="text-xs text-gray-600">{{ request.subtitle }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Financial Overview -->
        <div class="bg-white rounded-lg shadow p-6">
            <div class="flex justify-between items-center mb-6">
                <div class="flex items-center gap-2">
                    <span class="text-xl">📊</span>
                    <h3 class="font-bold text-gray-800">Financial Overview - This Month</h3>
                </div>
                <button @click="$router.push('/uikit/PaymentDashboard')" class="text-orange-500 text-sm font-semibold flex items-center gap-2">View Details →</button>
            </div>
            <div class="grid grid-cols-3 gap-8">
                <div>
                    <p class="text-gray-600 text-sm mb-2">Total Revenue</p>
                    <p class="text-2xl font-bold text-orange-500">₱{{ financial.totalRevenue.toLocaleString() }}</p>
                </div>
                <div>
                    <p class="text-gray-600 text-sm mb-2">Total Expenses</p>
                    <p class="text-2xl font-bold text-red-500">₱{{ financial.totalExpenses.toLocaleString() }}</p>
                </div>
                <div>
                    <p class="text-gray-600 text-sm mb-2">Net Income</p>
                    <p class="text-2xl font-bold text-green-600">₱{{ financial.netIncome.toLocaleString() }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue';

// --- State for the Patient List ---
const searchQuery = ref('');
const statusFilter = ref('all');
const serviceFilter = ref('all');
const sortField = ref('name');
const sortOrder = ref('asc');

// --- State for the Detail View / Modal ---
const showDetails = ref(false);
const selectedPatient = ref(null);
const reminderType = ref('email');
const reminderMessage = ref('');

// Sample patient data
const patients = ref([
    { id: 'P001', name: 'Maria Santos', email: 'maria.santos@email.com', phone: '09123456789', service: 'Prenatal Checkup', totalBill: 5000, amountPaid: 5000, paymentStatus: 'paid', reminders: [] },
    { id: 'P002', name: 'Ana Cruz', email: 'ana.cruz@email.com', phone: '09234567890', service: 'Normal Delivery', totalBill: 25000, amountPaid: 15000, paymentStatus: 'partial', reminders: [] },
    { id: 'P003', name: 'Rosa Mendoza', email: 'rosa.mendoza@email.com', phone: '09345678901', service: 'Family Planning', totalBill: 2000, amountPaid: 0, paymentStatus: 'pending', reminders: [] },
    { id: 'P004', name: 'Elena Reyes', email: 'elena.reyes@email.com', phone: '09456789012', service: 'Postnatal Care', totalBill: 3500, amountPaid: 1500, paymentStatus: 'partial', reminders: [] },
    { id: 'P005', name: 'Carmen Garcia', email: 'carmen.garcia@email.com', phone: '09567890123', service: 'Prenatal Checkup', totalBill: 5000, amountPaid: 5000, paymentStatus: 'paid', reminders: [] }
]);

// --- List Logic ---
const services = computed(() => ['all', ...new Set(patients.value.map((p) => p.service))]);

const filteredPatients = computed(() => {
    return patients.value.filter((p) => {
        const matchesSearch = p.name.toLowerCase().includes(searchQuery.value.toLowerCase()) || p.email.toLowerCase().includes(searchQuery.value.toLowerCase());
        const matchesStatus = statusFilter.value === 'all' || p.paymentStatus === statusFilter.value;
        const matchesService = serviceFilter.value === 'all' || p.service === serviceFilter.value;
        return matchesSearch && matchesStatus && matchesService;
    });
});

const sortedPatients = computed(() => {
    const copy = [...filteredPatients.value];
    copy.sort((a, b) => {
        let aVal, bVal;
        if (sortField.value === 'outstanding') {
            aVal = a.totalBill - a.amountPaid;
            bVal = b.totalBill - b.amountPaid;
        } else {
            aVal = a[sortField.value];
            bVal = b[sortField.value];
        }
        if (typeof aVal === 'string') return sortOrder.value === 'asc' ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
        return sortOrder.value === 'asc' ? aVal - bVal : bVal - aVal;
    });
    return copy;
});

// --- Detail View Logic ---
function openDetails(patient) {
    selectedPatient.value = patient;
    showDetails.value = true;
    reminderMessage.value = `Reminder: Outstanding balance of ${formatCurrency(patient.totalBill - patient.amountPaid)} due for ${patient.service}.`;
}

function handleClose() {
    showDetails.value = false;
    selectedPatient.value = null;
}

function handleSendReminder() {
    alert(`Reminder sent via ${reminderType.value} to ${selectedPatient.value.name}`);
}

// --- Helpers ---
function formatCurrency(amount) { return '₱' + (amount || 0).toLocaleString(); }
function getPaymentPercentage(p) { return p.totalBill === 0 ? 0 : Math.round((p.amountPaid / p.totalBill) * 100); }
function getStatusBg(status) {
    if (status === 'paid') return 'bg-green-100 text-green-700';
    if (status === 'partial') return 'bg-yellow-100 text-yellow-700';
    return 'bg-red-100 text-red-700';
}
function handleSort(field) {
    if (sortField.value === field) sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
    else { sortField.value = field; sortOrder.value = 'asc'; }
}
</script>

<template>
    <div class="space-y-6 p-6 bg-gray-50 min-h-screen">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <button @click="$router.push('/uikit/viewListOfExpense')" class="bg-white border-2 border-purple-500 text-purple-700 px-6 py-4 rounded-lg font-semibold hover:bg-purple-50 flex items-center justify-center gap-2">View Expenses</button>
            <button @click="$router.push('/uikit/viewListOfRevenue')" class="bg-white border-2 border-green-500 text-green-700 px-6 py-4 rounded-lg font-semibold hover:bg-green-50 flex items-center justify-center gap-2">View Revenue</button>
            <button @click="$router.push('/uikit/viewListOfSOA')" class="bg-white border-2 border-blue-500 text-blue-700 px-6 py-4 rounded-lg font-semibold hover:bg-blue-50 flex items-center justify-center gap-2">View SOA</button>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="bg-white p-4 rounded-lg border shadow-sm"><p class="text-xs text-gray-500 uppercase font-bold">Total Patients</p><p class="text-2xl font-bold">{{ sortedPatients.length }}</p></div>
            <div class="bg-green-50 p-4 rounded-lg border border-green-100 shadow-sm"><p class="text-xs text-green-600 uppercase font-bold">Paid</p><p class="text-2xl font-bold text-green-700">{{ patients.filter(p => p.paymentStatus === 'paid').length }}</p></div>
            <div class="bg-yellow-50 p-4 rounded-lg border border-yellow-100 shadow-sm"><p class="text-xs text-yellow-600 uppercase font-bold">Partial</p><p class="text-2xl font-bold text-yellow-700">{{ patients.filter(p => p.paymentStatus === 'partial').length }}</p></div>
            <div class="bg-red-50 p-4 rounded-lg border border-red-100 shadow-sm"><p class="text-xs text-red-600 uppercase font-bold">Pending</p><p class="text-2xl font-bold text-red-700">{{ patients.filter(p => p.paymentStatus === 'pending').length }}</p></div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 bg-white p-4 rounded-lg shadow-sm border">
            <input v-model="searchQuery" type="text" placeholder="Search patients..." class="md:col-span-2 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none" />
            <select v-model="statusFilter" class="px-4 py-2 border rounded-lg">
                <option value="all">All Statuses</option>
                <option value="paid">Paid</option>
                <option value="partial">Partial</option>
                <option value="pending">Pending</option>
            </select>
            <select v-model="serviceFilter" class="px-4 py-2 border rounded-lg">
                <option v-for="s in services" :key="s" :value="s">{{ s === 'all' ? 'All Services' : s }}</option>
            </select>
        </div>

        <div class="bg-white border rounded-xl overflow-hidden shadow-sm">
            <div class="overflow-x-auto">
                <table class="w-full text-left border-collapse">
                    <thead class="bg-gray-50 border-b">
                        <tr>
                            <th @click="handleSort('name')" class="px-6 py-4 cursor-pointer hover:text-blue-600 font-bold text-xs uppercase tracking-wider">Patient</th>
                            <th @click="handleSort('totalBill')" class="px-6 py-4 cursor-pointer text-right hover:text-blue-600 font-bold text-xs uppercase tracking-wider">Total Bill</th>
                            <th @click="handleSort('amountPaid')" class="px-6 py-4 cursor-pointer text-right hover:text-blue-600 font-bold text-xs uppercase tracking-wider">Paid</th>
                            <th @click="handleSort('outstanding')" class="px-6 py-4 cursor-pointer text-right hover:text-blue-600 font-bold text-xs uppercase tracking-wider">Outstanding</th>
                            <th class="px-6 py-4 text-center font-bold text-xs uppercase tracking-wider">Status</th>
                            <th class="px-6 py-4 text-center font-bold text-xs uppercase tracking-wider">Action</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-100">
                        <tr v-for="p in sortedPatients" :key="p.id" class="hover:bg-blue-50/30 transition-colors">
                            <td class="px-6 py-4">
                                <p class="font-bold text-gray-800">{{ p.name }}</p>
                                <p class="text-[11px] text-gray-500 font-medium">{{ p.service }}</p>
                            </td>
                            <td class="px-6 py-4 text-right font-bold text-gray-700">{{ formatCurrency(p.totalBill) }}</td>
                            <td class="px-6 py-4 text-right font-bold text-green-600">{{ formatCurrency(p.amountPaid) }}</td>
                            <td class="px-6 py-4 text-right font-bold text-red-600">{{ formatCurrency(p.totalBill - p.amountPaid) }}</td>
                            <td class="px-6 py-4 text-center">
                                <span :class="['px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-tighter', getStatusBg(p.paymentStatus)]">
                                    {{ p.paymentStatus }}
                                </span>
                            </td>
                            <td class="px-6 py-4 text-center">
                                <button @click="openDetails(p)" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-xs font-black shadow-md transition-all active:scale-95">VIEW</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div v-if="showDetails && selectedPatient" class="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50" @click.self="handleClose">
            <div class="bg-white rounded-2xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto border-t-8 border-blue-600">
                <div class="p-6 border-b flex justify-between items-center">
                    <div>
                        <h2 class="text-2xl font-black text-gray-800">{{ selectedPatient.name }}</h2>
                        <p class="text-blue-600 font-bold text-sm uppercase tracking-widest">{{ selectedPatient.service }}</p>
                    </div>
                    <button @click="handleClose" class="text-3xl text-gray-400 hover:text-gray-600 transition-transform hover:rotate-90">&times;</button>
                </div>

                <div class="p-6 space-y-6">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div class="bg-gray-50 p-4 rounded-xl border border-gray-200">
                            <p class="text-[10px] text-gray-500 font-black uppercase mb-1">Total Bill</p>
                            <p class="text-xl font-black text-gray-800">{{ formatCurrency(selectedPatient.totalBill) }}</p>
                        </div>
                        <div class="bg-green-50 p-4 rounded-xl border border-green-100">
                            <p class="text-[10px] text-green-600 font-black uppercase mb-1">Amount Paid</p>
                            <p class="text-xl font-black text-green-700">{{ formatCurrency(selectedPatient.amountPaid) }}</p>
                        </div>
                        <div class="bg-red-50 p-4 rounded-xl border border-red-100">
                            <p class="text-[10px] text-red-600 font-black uppercase mb-1">Outstanding</p>
                            <p class="text-xl font-black text-red-700">{{ formatCurrency(selectedPatient.totalBill - selectedPatient.amountPaid) }}</p>
                        </div>
                    </div>

                    <div class="bg-blue-50 p-6 rounded-2xl border border-blue-100 shadow-inner">
                        <h3 class="text-sm font-black text-blue-900 mb-4 flex items-center gap-2 uppercase tracking-widest"><span>📤</span> Send Reminder</h3>
                        <div class="space-y-4">
                            <div class="grid grid-cols-2 gap-4">
                                <label class="flex items-center gap-3 p-3 border-2 rounded-xl bg-white cursor-pointer transition-all" :class="reminderType === 'email' ? 'border-blue-500 scale-[1.02]' : 'border-gray-100'">
                                    <input type="radio" v-model="reminderType" value="email" class="w-4 h-4 accent-blue-600">
                                    <span class="text-xs font-black uppercase">Email</span>
                                </label>
                                <label class="flex items-center gap-3 p-3 border-2 rounded-xl bg-white cursor-pointer transition-all" :class="reminderType === 'sms' ? 'border-blue-500 scale-[1.02]' : 'border-gray-100'">
                                    <input type="radio" v-model="reminderType" value="sms" class="w-4 h-4 accent-blue-600">
                                    <span class="text-xs font-black uppercase">SMS</span>
                                </label>
                            </div>
                            <textarea v-model="reminderMessage" rows="3" class="w-full p-4 border rounded-xl focus:ring-4 focus:ring-blue-100 outline-none text-sm font-medium border-gray-200"></textarea>
                            <button @click="handleSendReminder" class="w-full bg-blue-600 text-white font-black py-4 rounded-xl hover:bg-blue-700 shadow-xl shadow-blue-200 transition-all active:scale-95 uppercase tracking-widest">Send Now</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
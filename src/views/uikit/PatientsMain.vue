<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

const BASE_URL = 'http://localhost:8080/api/patients';
const REPORT_URL = 'http://localhost:8080/api/reports';
const CLINICAL_SERVICES_URL = 'http://localhost:8080/api/clinical-services';

const searchQuery = ref('');
const showAddPatientModal = ref(false); // ✅ fixed syntax error (removed extra quote)
const patients = ref([]);
const patientServices = ref([]);
const clinicalServices = ref([]);
const selectedServiceType = ref('All');

// ── Report Generation State ─────────────────────────────────────────────────
const showReportModal = ref(false);
const reportLoading = ref(false);
const reportData = ref(null);
const reportStartDate = ref('');
const reportEndDate = ref('');
const reportServiceFilter = ref('');  
const reportCategoryFilter = ref('');

function openReportModal() {
    // Default to current month range
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    reportStartDate.value = `${year}-${month}-01`;
    const lastDay = new Date(year, now.getMonth() + 1, 0).getDate();
    reportEndDate.value = `${year}-${month}-${String(lastDay).padStart(2, '0')}`;
    reportServiceFilter.value = '';
    reportCategoryFilter.value = '';
    reportData.value = null;
    showReportModal.value = true;
}

function closeReportModal() {
    showReportModal.value = false;
    reportData.value = null;
}

async function generateReport() {
    if (!reportStartDate.value || !reportEndDate.value) {
        alert('Please select both start and end dates.');
        return;
    }
    reportLoading.value = true;
    try {
        let url = `${REPORT_URL}/philhealth?start=${reportStartDate.value}&end=${reportEndDate.value}`;
        if (reportServiceFilter.value) url += `&service=${encodeURIComponent(reportServiceFilter.value)}`;
        if (reportCategoryFilter.value) url += `&category=${encodeURIComponent(reportCategoryFilter.value)}`;
        const res = await axios.get(url);
        reportData.value = res.data;
    } catch (e) {
        console.error('Failed to generate report:', e);
        alert('Failed to generate report: ' + (e.response?.data?.message || e.message));
    } finally {
        reportLoading.value = false;
    }
}

function downloadCsv() {
    if (!reportStartDate.value || !reportEndDate.value) return;
    let url = `${REPORT_URL}/philhealth/csv?start=${reportStartDate.value}&end=${reportEndDate.value}`;
    if (reportServiceFilter.value) url += `&service=${encodeURIComponent(reportServiceFilter.value)}`;
    if (reportCategoryFilter.value) url += `&category=${encodeURIComponent(reportCategoryFilter.value)}`;
    window.open(url, '_blank');
}

function printReport() {
    const printWindow = window.open('', '_blank');
    if (!printWindow || !reportData.value) return;
    const records = reportData.value.patientRecords || [];
    const summary = reportData.value.summary || {};
    const breakdown = reportData.value.serviceBreakdown || [];
    const serviceStatsRows = reportServiceStats.value.map(service =>
        `<div class="stat-card"><div class="label">${service.name}</div><div class="value">${service.count}</div></div>`
    ).join('');
    let rows = records.map(r =>
        `<tr>
            <td>${r.caseNumber || '—'}</td>
            <td>${r.patientName || '—'}</td>
            <td>${r.age ?? '—'}</td>
            <td>${r.serviceName || '—'}</td>
            <td>${r.serviceCategory || '—'}</td>
            <td>${r.riskStatus || 'Normal'}</td>
            <td>${r.hasPhilHealth ? 'Yes' : 'No'}</td>
            <td>${r.dateAvailed || '—'}</td>
        </tr>`
    ).join('');
    let breakdownRows = breakdown.map(b =>
        `<tr>
            <td>${b.serviceName || '—'}</td>
            <td>${b.caseNumber || '—'}</td>
            <td>${b.category || '—'}</td>
            <td>${b.count ?? 0}</td>
            <td>${b.philHealthCount ?? 0}</td>
            <td>${(b.percentage ?? 0).toFixed(1)}%</td>
        </tr>`
    ).join('');
    printWindow.document.write(`
        <html><head><title>PhilHealth Report</title>
        <style>
            body { font-family: Arial, sans-serif; padding: 20px; color: #333; }
            h1 { text-align: center; font-size: 18px; margin-bottom: 4px; }
            h2 { font-size: 14px; margin-top: 24px; border-bottom: 2px solid #7c3aed; padding-bottom: 4px; }
            .subtitle { text-align: center; font-size: 12px; color: #666; margin-bottom: 20px; }
            table { width: 100%; border-collapse: collapse; margin-top: 8px; font-size: 11px; }
            th, td { border: 1px solid #ccc; padding: 6px 8px; text-align: left; }
            th { background: #7c3aed; color: white; }
            .stats { display: flex; flex-wrap: wrap; gap: 12px; margin-top: 8px; }
            .stat-card { border: 1px solid #ddd; padding: 8px 14px; border-radius: 6px; min-width: 140px; }
            .stat-card .label { font-size: 10px; color: #888; }
            .stat-card .value { font-size: 16px; font-weight: bold; }
            @media print { body { padding: 0; } }
        </style></head><body>
        <h1>PhilHealth & Clinical Service Report</h1>
        <p class="subtitle">${reportStartDate.value} to ${reportEndDate.value}</p>
        <h2>Summary Statistics</h2>
        <div class="stats">
            <div class="stat-card"><div class="label">Total Patients</div><div class="value">${summary.totalUniquePatients ?? 0}</div></div>
            <div class="stat-card"><div class="label">Services Availed</div><div class="value">${summary.totalServicesAvailed ?? 0}</div></div>
            ${serviceStatsRows}
            <div class="stat-card"><div class="label">PhilHealth Covered</div><div class="value">${summary.totalPhilHealthCovered ?? 0}</div></div>
        </div>
        <h2>Service Breakdown (with Case Numbers)</h2>
        <table>
            <thead><tr><th>Service Availed</th><th>Case Number</th><th>Service Category</th><th>No. of Patients</th><th>PhilHealth Covered</th><th>PhilHealth Coverage Rate</th></tr></thead>
            <tbody>${breakdownRows || '<tr><td colspan="6" style="text-align:center">No data</td></tr>'}</tbody>
        </table>
        <h2>Patient Masterlist</h2>
        <table>
            <thead><tr><th>Case Number</th><th>Patient Name</th><th>Age</th><th>Service Availed</th><th>Service Category</th><th>Patient Type</th><th>PhilHealth Status</th><th>Date Availed</th></tr></thead>
            <tbody>${rows || '<tr><td colspan="8" style="text-align:center">No records</td></tr>'}</tbody>
        </table>
        </body></html>
    `);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
}

// Watch to route to Prenatal list when selectedServiceType is 'Prenatal'
watch(selectedServiceType, (newVal) => {
    if (newVal === 'Prenatal') {
        router.push('/uikit/Patient');
    }
});

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

async function fetchPatientServices() {
    try {
        const res = await axios.get('http://localhost:8080/api/patient-services');
        patientServices.value = res.data;
    } catch (e) {
        console.error('Failed to fetch patient services:', e);
    }
}

async function fetchClinicalServices() {
    try {
        const res = await axios.get(CLINICAL_SERVICES_URL);
        clinicalServices.value = Array.isArray(res.data) ? res.data : [];
    } catch (e) {
        console.error('Failed to fetch clinical services:', e);
    }
}

const reportServiceOptions = computed(() => {
    const catalogNames = clinicalServices.value
        .map(service => service.name?.trim())
        .filter(Boolean);
    const patientServiceNames = patientServices.value
        .map(service => service.serviceName?.trim())
        .filter(Boolean);
    return [...new Set([...catalogNames, ...patientServiceNames])]
        .sort((a, b) => a.localeCompare(b));
});

const reportServiceStats = computed(() => {
    const records = reportData.value?.patientRecords || [];
    return reportServiceOptions.value.map((serviceName, index) => ({
        name: serviceName,
        count: records.filter(record =>
            String(record.serviceName || '').trim().toLowerCase() === serviceName.toLowerCase()
        ).length,
        classes: [
            ['border-pink-200', 'bg-pink-50', 'text-pink-600', 'text-pink-800'],
            ['border-teal-200', 'bg-teal-50', 'text-teal-600', 'text-teal-800'],
            ['border-blue-200', 'bg-blue-50', 'text-blue-600', 'text-blue-800'],
            ['border-orange-200', 'bg-orange-50', 'text-orange-600', 'text-orange-800']
        ][index % 4]
    }));
});

onMounted(async () => {
    await fetchPatients();
    await fetchPatientServices();
    await fetchClinicalServices();
});

const filteredPatients = computed(() => {
    let result = patients.value;

    // Filter by Service Availed first
    if (selectedServiceType.value !== 'All') {
        const patientIdsWithService = patientServices.value
            .filter(ps => ps.serviceName === selectedServiceType.value)
            .map(ps => ps.patientID);
        
        result = result.filter(patient => patientIdsWithService.includes(patient.patientID));
    }

    if (!searchQuery.value) return result;
    const query = searchQuery.value.toLowerCase();
    return result.filter(patient =>
        (patient.patientID?.toString() || '').includes(query) ||
        (patient.fName || '').toLowerCase().includes(query) ||
        (patient.lName || '').toLowerCase().includes(query) ||
        (patient.contactNumber || '').includes(query)
    );
});

function getAvailedServicesForPatient(patientID) {
    const list = patientServices.value
        .filter(ps => ps.patientID === patientID)
        .map(ps => ps.serviceName);
    return [...new Set(list)];
}

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
        <div class="flex flex-wrap items-center gap-4 mb-6">
            <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by Name, ID, or Contact Number"
                class="flex-grow min-w-[200px] px-4 py-2 bg-gray-100 border-0 rounded-full focus:ring-2 focus:ring-purple-500 focus:bg-white"
            />
            
            <div class="flex items-center gap-2">
                <span class="text-sm font-semibold text-gray-700 whitespace-nowrap">Service Availed:</span>
                <select
                    v-model="selectedServiceType"
                    class="px-4 py-2 bg-gray-100 border border-gray-200 rounded-full focus:ring-2 focus:ring-purple-500 focus:bg-white font-medium text-gray-700 focus:outline-none"
                >
                    <option value="All">All Patients</option>
                    <option value="Prenatal">Prenatal</option>
                    <option value="Family Planning">Family Planning</option>
                    <option value="Ultrasound Service">Ultrasound Service</option>
                    <option value="Other Services">Other Services</option>
                </select>
            </div>

            <button
                @click="$router.push('/uikit/Admission')"
                class="px-6 py-2 bg-purple-700 text-white rounded-full hover:bg-purple-800 transition-colors"
            >
                Lying-In Admission
            </button>
            <button
                @click="addPatient"
                class="px-6 py-2 bg-purple-700 text-white rounded-full hover:bg-purple-800 transition-colors"
            >
                Add Patient
            </button>
            <button
                @click="openReportModal"
                class="px-6 py-2 bg-green-600 text-white rounded-full hover:bg-green-700 transition-colors flex items-center gap-2"
            >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
                Generate Report
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
                        <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Services Availed</th>
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
                        <td class="px-6 py-4 text-sm text-gray-900">
                            <div class="flex flex-wrap gap-1">
                                <span 
                                    v-for="srv in getAvailedServicesForPatient(patient.patientID)" 
                                    :key="srv"
                                    class="px-2 py-0.5 rounded text-xs font-bold bg-purple-100 text-purple-800"
                                >
                                    {{ srv }}
                                </span>
                                <span v-if="!getAvailedServicesForPatient(patient.patientID).length" class="text-gray-400">—</span>
                            </div>
                        </td>
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

        <!-- ═══════════════════════════════════════════════════════════════════ -->
        <!-- Generate Report Modal                                             -->
        <!-- ═══════════════════════════════════════════════════════════════════ -->
        <div v-if="showReportModal"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
            @click.self="closeReportModal">
            <div class="bg-white rounded-xl shadow-2xl w-full max-w-5xl max-h-[92vh] overflow-y-auto">

                <!-- Modal Header -->
                <div class="border-b border-gray-200 p-6 flex items-center justify-between">
                    <h3 class="text-xl font-bold text-gray-800">📊 PhilHealth &amp; Clinical Service Report</h3>
                    <button @click="closeReportModal" class="text-gray-400 hover:text-gray-600 text-2xl">&times;</button>
                </div>

                <div class="p-6 space-y-6">

                    <!-- Filters -->
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Start Date *</label>
                            <input v-model="reportStartDate" type="date" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">End Date *</label>
                            <input v-model="reportEndDate" type="date" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-1">Service</label>
                            <select v-model="reportServiceFilter" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500">
                                <option value="">All Services</option>
                                <option v-for="serviceName in reportServiceOptions" :key="serviceName" :value="serviceName">
                                    {{ serviceName }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Action buttons -->
                    <div class="flex flex-wrap gap-3">
                        <button @click="generateReport" :disabled="reportLoading"
                            class="px-6 py-2 bg-purple-700 text-white rounded-lg hover:bg-purple-800 transition-colors disabled:opacity-50 font-semibold">
                            {{ reportLoading ? 'Generating…' : '🔍 Generate Report' }}
                        </button>
                        <button @click="downloadCsv" :disabled="!reportData"
                            class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50 font-semibold">
                            📥 Download CSV
                        </button>
                        <button @click="printReport" :disabled="!reportData"
                            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50 font-semibold">
                            🖨️ Print Report
                        </button>
                    </div>

                    <!-- Report Results -->
                    <div v-if="reportData">

                        <!-- Summary Cards -->
                        <div class="grid grid-cols-2 md:grid-cols-5 gap-3 mb-6">
                            <div class="border border-purple-200 bg-purple-50 rounded-lg p-3 text-center">
                                <p class="text-xs text-purple-600 font-semibold">Total Patients</p>
                                <p class="text-2xl font-bold text-purple-800">{{ reportData.summary?.totalUniquePatients ?? 0 }}</p>
                            </div>
                            <div class="border border-blue-200 bg-blue-50 rounded-lg p-3 text-center">
                                <p class="text-xs text-blue-600 font-semibold">Services Availed</p>
                                <p class="text-2xl font-bold text-blue-800">{{ reportData.summary?.totalServicesAvailed ?? 0 }}</p>
                            </div>
                            <div v-for="service in reportServiceStats" :key="service.name"
                                class="rounded-lg p-3 text-center" :class="service.classes.slice(0, 2)">
                                <p class="text-xs font-semibold" :class="service.classes[2]">{{ service.name }}</p>
                                <p class="text-2xl font-bold" :class="service.classes[3]">{{ service.count }}</p>
                            </div>
                        </div>

                        <!-- Service Breakdown Table -->
                        <h4 class="text-md font-bold text-gray-800 mb-2">Service Breakdown (with Case Numbers)</h4>
                        <div class="overflow-x-auto mb-6">
                            <table class="w-full text-sm">
                                <thead class="bg-purple-600 text-white">
                                    <tr>
                                        <th class="px-4 py-2 text-left">Service Availed</th>
                                        <th class="px-4 py-2 text-left">Case Number</th>
                                        <th class="px-4 py-2 text-left">Service Category</th>
                                        <th class="px-4 py-2 text-center">No. of Records</th>
                                        <th class="px-4 py-2 text-center">PhilHealth Covered</th>
                                        <th class="px-4 py-2 text-center">PhilHealth Coverage Rate</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(b, idx) in reportData.serviceBreakdown" :key="idx" class="border-b hover:bg-gray-50">
                                        <td class="px-4 py-2">{{ b.serviceName }}</td>
                                        <td class="px-4 py-2 font-mono text-purple-700">{{ b.caseNumber || '—' }}</td>
                                        <td class="px-4 py-2">{{ b.category || '—' }}</td>
                                        <td class="px-4 py-2 text-center font-bold">{{ b.count }}</td>
                                        <td class="px-4 py-2 text-center">{{ b.philHealthCount }}</td>
                                        <td class="px-4 py-2 text-center">{{ (b.percentage ?? 0).toFixed(1) }}%</td>
                                    </tr>
                                    <tr v-if="!reportData.serviceBreakdown?.length">
                                        <td colspan="6" class="px-4 py-4 text-center text-gray-400">No service breakdown available</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- Patient Masterlist Table -->
                        <h4 class="text-md font-bold text-gray-800 mb-2">Patient Masterlist</h4>
                        <div class="overflow-x-auto">
                            <table class="w-full text-sm">
                                <thead class="bg-purple-600 text-white">
                                    <tr>
                                        <th class="px-3 py-2 text-left">Case Number</th>
                                        <th class="px-3 py-2 text-left">Patient Name</th>
                                        <th class="px-3 py-2 text-center">Age</th>
                                        <th class="px-3 py-2 text-left">Service Availed</th>
                                        <th class="px-3 py-2 text-left">Service Category</th>
                                        <th class="px-3 py-2 text-center">Patient Type</th>
                                        <th class="px-3 py-2 text-center">PhilHealth Status</th>
                                        <th class="px-3 py-2 text-left">Date Availed</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(r, idx) in reportData.patientRecords" :key="idx" class="border-b hover:bg-gray-50">
                                        <td class="px-3 py-2 font-mono text-purple-700 font-bold">{{ r.caseNumber || '—' }}</td>
                                        <td class="px-3 py-2">{{ r.patientName }}</td>
                                        <td class="px-3 py-2 text-center">{{ r.age ?? '—' }}</td>
                                        <td class="px-3 py-2">{{ r.serviceName }}</td>
                                        <td class="px-3 py-2">{{ r.serviceCategory || '—' }}</td>
                                        <td class="px-3 py-2 text-center">
                                            <span :class="r.riskStatus === 'High Risk' ? 'text-red-600 font-bold' : 'text-green-600 font-bold'">{{ r.riskStatus || 'Normal' }}</span>
                                        </td>
                                        <td class="px-3 py-2 text-center">
                                            <span :class="r.hasPhilHealth ? 'text-green-600 font-bold' : 'text-gray-400'">{{ r.hasPhilHealth ? '✓ Yes' : 'No' }}</span>
                                        </td>
                                        <td class="px-3 py-2">{{ r.dateAvailed || '—' }}</td>
                                    </tr>
                                    <tr v-if="!reportData.patientRecords?.length">
                                        <td colspan="8" class="px-4 py-4 text-center text-gray-400">No patient records found for this period</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- No report yet message -->
                    <div v-else-if="!reportLoading" class="text-center py-8 text-gray-400">
                        <p class="text-lg">Select a date range and click <strong>Generate Report</strong></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
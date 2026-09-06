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
    patientCode: '',
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
const patientHighRisk = ref(false);
const highRiskReasons = ref([]);

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
            caseNumber: item.caseNumber ?? '---',
            remarks: item.remarks ?? '---',
            isHighRisk: patientHighRisk.value,
            riskReasons: [...highRiskReasons.value]
        }));

        await Promise.all(services.value
            .filter(service => service.service?.toLowerCase().includes('prenatal'))
            .map(async service => {
                try {
                    const recordsRes = await axios.get(`http://localhost:8080/api/prenatal/records/service/${service.id}`);
                    const records = Array.isArray(recordsRes.data) ? recordsRes.data : [];
                    const latest = records.reduce((current, record) =>
                        !current || record.prenatalrecordID > current.prenatalrecordID ? record : current, null);
                    if (!latest?.prenatalrecordID) return;

                    const riskRes = await axios.get(`http://localhost:8080/api/prenatal/high-risk-assessment/${latest.prenatalrecordID}`);
                    if (riskRes.data?.highRisk) {
                        patientHighRisk.value = true;
                        highRiskReasons.value.push(...(riskRes.data.reasons || []));
                    }
                } catch (riskError) {
                    console.error('Failed to load prenatal risk status.', riskError);
                }
            }));

        services.value = services.value.map(service => ({
            ...service,
            isHighRisk: patientHighRisk.value,
            riskReasons: [...new Set(highRiskReasons.value)]
        }));
    } catch (e) {
        console.error('Failed to load services data.', e);
    }
}

async function fetchPatientRiskStatus() {
    const id = userStore.user?.patientID;
    if (!id) return;

    try {
        const [referralsRes, admissionsRes] = await Promise.all([
            axios.get(`http://localhost:8080/api/referrals?patientId=${id}`),
            axios.get('http://localhost:8080/api/admissions')
        ]);
        const referrals = Array.isArray(referralsRes.data) ? referralsRes.data : [];
        const admissions = Array.isArray(admissionsRes.data) ? admissionsRes.data : [];
        const patientAdmissions = admissions.filter(admission => Number(admission.patientID) === Number(id));

        if (referrals.length || patientAdmissions.some(admission => admission.isHighRisk === true)) {
            patientHighRisk.value = true;
            highRiskReasons.value.push(
                ...referrals.flatMap(referral => String(referral.riskFactors || '').split(',')),
                ...patientAdmissions.filter(admission => admission.isHighRisk === true).map(() => 'High risk identified during Admission')
            );
            highRiskReasons.value = [...new Set(highRiskReasons.value.map(reason => reason.trim()).filter(Boolean))];
        }
    } catch (riskError) {
        console.error('Failed to load patient risk status.', riskError);
    }
}

function printServices() {
    window.print();
}

function viewService(service) {
    const name = service.service?.toLowerCase() || '';
    const patientId = userStore.user?.patientID;
    const serviceId = service.id;

    if (name === 'prenatal') {
        router.push(`/uikit/PrenatalAdmission/${patientId}/${serviceId}`);
    } else if (name === 'family planning') {
        router.push(`/uikit/FamilyPlanningAdmission/${patientId}/${serviceId}`);
    } else if (name === 'ultrasound') {
        router.push(`/uikit/UltrasoundAdmission/${patientId}/${serviceId}`);
    } else if (name.includes('laboratory') || name.includes('lab')) {
        router.push(`/uikit/Laboratoryform/${patientId}/${serviceId}`);
    } else if (name.includes('admis') || name.includes('lying-in') || name.includes('admission')) {
        router.push(`/uikit/Admission/${patientId}/${serviceId}`);
    } else {
        alert(`No dedicated view page for "${service.service}" yet.`);
    }
}

onMounted(() => {
    fetchPatient();
    fetchPatientRiskStatus().then(fetchServices);
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
                    <h1 class="text-2xl font-semibold text-gray-800">My Profile & Records</h1>
                    <span v-if="patientHighRisk"
                        :title="highRiskReasons.join(', ')"
                        class="inline-flex items-center gap-1 rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-700 border border-red-200">
                        <span class="h-2 w-2 rounded-full bg-red-600"></span>
                        HIGH RISK PATIENT
                    </span>
                </div>
                <button @click="printServices"
                    class="bg-gray-800 text-white px-4 py-2 rounded-lg hover:bg-gray-900 transition flex items-center gap-2">
                    Print Records
                </button>
            </div>

            <!-- General Information (Read Only) -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">Personal Information</h3>
                <div class="grid grid-cols-3 gap-6">
                    <div>
                        <p class="text-sm text-gray-500">Full Name</p>
                        <p class="font-medium text-gray-900">
                            {{ patientData.fName }} {{ patientData.middleI }} {{ patientData.lName }}
                        </p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500">Patient ID</p>
                        <p class="font-bold text-purple-700">{{ patientData.patientCode || patientData.patientID }}</p>
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
                            <th class="th">Case Number</th>
                            <th class="th no-print">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="service in services" :key="service.id" class="border-b hover:bg-gray-50">
                            <td class="td font-medium text-gray-900">
                                <div class="flex items-center gap-2">
                                    <span>{{ service.service }}</span>
                                    <span v-if="service.isHighRisk"
                                        :title="service.riskReasons.join(', ')"
                                        class="inline-flex items-center gap-1 rounded-full bg-red-100 px-2 py-0.5 text-xs font-bold text-red-700 border border-red-200">
                                        <span class="h-2 w-2 rounded-full bg-red-600"></span>
                                        HIGH RISK
                                    </span>
                                </div>
                            </td>
                            <td class="td">{{ service.employee }}</td>
                            <td class="td">{{ service.ward }}</td>
                            <td class="td">{{ service.dateAvailed }}</td>
                            <td class="td font-semibold text-gray-700">
                                <span v-if="service.caseNumber && service.caseNumber !== '---'"
                                      class="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold bg-teal-50 text-teal-700 border border-teal-200">
                                    {{ service.caseNumber }}
                                </span>
                                <span v-else class="text-gray-400">---</span>
                            </td>
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
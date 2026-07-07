<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// ── API endpoints ────────────────────────────────────────────────────────────
// NOTE: /api/patients and /api/wards are ASSUMED paths based on patterns used
// elsewhere in this project. If your actual endpoints differ, update these.
const PATIENTS_URL    = 'http://localhost:8080/api/patients';
const WARDS_URL       = 'http://localhost:8080/api/wards';
const EMPLOYEES_URL   = 'http://localhost:8080/api/employees';
const ADMISSIONS_URL  = 'http://localhost:8080/api/admissions';
const SOA_URL         = 'http://localhost:8080/api/billing/soa';

const currentStep = ref('arrival');
const saving = ref(false);
const saveError = ref('');

// Backend-linked record IDs — populated as the workflow progresses
const admissionId = ref(null);
const patientId = ref(null);
const soaId = ref(null);

// Real lists pulled from the backend for the Ward/Staff assignment step
const wardsList = ref([]);
const employeesList = ref([]);

const patientData = ref({
    firstName: '',
    lastName: '',
    age: '',
    gestationalAge: '',
    readyForDelivery: null,
    isHighRisk: null,
    wardID: null,
    attendingStaffID: null,
    hasPhilHealth: null,
    paymentComplete: false,
    paymentMethod: 'Cash'
});

// Itemized billing — now editable. Starts with sensible defaults, but staff
// can add/remove fee lines before creating the real Statement of Account.
const billingItems = ref([
    { name: 'Admission Fee', amount: 1500 },
    { name: 'Delivery Fee', amount: 8000 },
    { name: 'Postpartum Care (2 days)', amount: 3000 },
    { name: 'Newborn Care', amount: 2500 },
    { name: 'Medications & Supplies', amount: 2000 }
]);
const newFeeName = ref('');
const newFeeAmount = ref('');
const billingTotal = computed(() =>
    billingItems.value.reduce((sum, item) => sum + item.amount, 0)
);
const philHealthCoverage = 6500;

function addBillingItem() {
    const name = newFeeName.value.trim();
    const amount = Number(newFeeAmount.value);
    if (!name || !amount || amount <= 0) return;

    billingItems.value.push({ name, amount });
    newFeeName.value = '';
    newFeeAmount.value = '';
}

function removeBillingItem(index) {
    billingItems.value.splice(index, 1);
}

const steps = {
    arrival: { title: 'Patient Arrival', color: 'bg-blue-500' },
    assessment: { title: 'Initial Assessment', color: 'bg-purple-500' },
    notReady: { title: 'Not Ready - Discharge Home', color: 'bg-yellow-500' },
    admission: { title: 'Admission & Assignment', color: 'bg-green-500' },
    monitoring: { title: 'Patient Monitoring', color: 'bg-pink-500' },
    referral: { title: 'Referral to St. Mary Hospital', color: 'bg-red-500' },
    delivery: { title: 'Delivery Process', color: 'bg-indigo-500' },
    postpartum: { title: 'Postpartum & Newborn Care', color: 'bg-rose-500' },
    billing: { title: 'Statement of Account', color: 'bg-teal-500' },
    payment: { title: 'Payment Processing', color: 'bg-emerald-500' },
    discharge: { title: 'Patient Discharge', color: 'bg-green-600' }
};

const currentStepInfo = computed(() => steps[currentStep.value]);

const progressWidth = computed(() => {
    const progressMap = {
        arrival: '10%', assessment: '20%', notReady: '30%', admission: '35%',
        monitoring: '45%', referral: '50%', delivery: '60%', postpartum: '70%',
        billing: '80%', payment: '90%', discharge: '100%'
    };
    return progressMap[currentStep.value];
});

const amountDue = computed(() => {
    return patientData.value.hasPhilHealth ? billingTotal.value - philHealthCoverage : billingTotal.value;
});

const selectedWardName = computed(() => {
    const ward = wardsList.value.find(w => w.id === patientData.value.wardID);
    return ward ? ward.name : '';
});

const selectedStaffName = computed(() => {
    const staff = employeesList.value.find(e => e.employeeID === patientData.value.attendingStaffID);
    return staff ? `${staff.fName || ''} ${staff.lName || ''}`.trim() : '';
});

function handleInputChange(field, value) {
    patientData.value[field] = value;
}

// ── Backend loaders ───────────────────────────────────────────────────────────
async function fetchWards() {
    try {
        const res = await axios.get(WARDS_URL);
        wardsList.value = res.data;
    } catch (error) {
        console.error('Failed to load wards list', error);
    }
}

async function fetchEmployees() {
    try {
        const res = await axios.get(EMPLOYEES_URL);
        employeesList.value = res.data;
    } catch (error) {
        console.error('Failed to load employees list', error);
    }
}

// Saves/updates the Admission record so the workflow can be resumed later.
async function saveAdmission(extraFields = {}) {
    saving.value = true;
    saveError.value = '';
    try {
        const payload = {
            admissionID: admissionId.value,
            patientID: patientId.value,
            patientName: `${patientData.value.firstName} ${patientData.value.lastName}`.trim(),
            age: patientData.value.age ? Number(patientData.value.age) : null,
            gestationalAge: patientData.value.gestationalAge,
            readyForDelivery: patientData.value.readyForDelivery,
            isHighRisk: patientData.value.isHighRisk,
            wardID: patientData.value.wardID,
            wardName: selectedWardName.value,
            attendingStaffID: patientData.value.attendingStaffID,
            attendingStaffName: selectedStaffName.value,
            hasPhilHealth: patientData.value.hasPhilHealth,
            paymentComplete: patientData.value.paymentComplete,
            soaID: soaId.value,
            currentStep: currentStep.value,
            ...extraFields
        };

        if (admissionId.value) {
            const res = await axios.put(ADMISSIONS_URL, payload);
            admissionId.value = res.data.admissionID;
        } else {
            payload.admissionDate = new Date().toISOString();
            const res = await axios.post(ADMISSIONS_URL, payload);
            admissionId.value = res.data.admissionID;
        }
    } catch (error) {
        console.error('Failed to save admission record', error);
        saveError.value = 'Failed to save this step to the server, but you can continue — try again later.';
    } finally {
        saving.value = false;
    }
}

function navigateToStep(step) {
    currentStep.value = step;
    saveAdmission({ currentStep: step });
}

// ── Step 1: Arrival — creates the real Patient record, then the Admission ────
async function proceedFromArrival() {
    if (!patientData.value.firstName) return;
    saving.value = true;
    saveError.value = '';

    try {
        const patientRes = await axios.post(PATIENTS_URL, {
            fName: patientData.value.firstName,
            lName: patientData.value.lastName
        });
        patientId.value = patientRes.data.patientID ?? patientRes.data.id;

        currentStep.value = 'assessment';
        await saveAdmission({ currentStep: 'assessment' });
    } catch (error) {
        console.error('Failed to create patient/admission', error);
        saveError.value = 'Failed to create the patient record. Please check the patient endpoint and try again.';
    } finally {
        saving.value = false;
    }
}

// ── Billing step — creates the real Statement of Account ────────────────────
// Just navigates to the Billing step — the itemized fees are entered/edited
// there, and the real Statement of Account isn't created until the PhilHealth
// question is answered (see finalizeBillingAndProceed below).
function proceedToBilling() {
    navigateToStep('billing');
}

// Creates the real Statement of Account using whatever fees were entered on
// the Billing step, then proceeds to Payment.
async function finalizeBillingAndProceed(hasPhilHealth) {
    handleInputChange('hasPhilHealth', hasPhilHealth);

    saving.value = true;
    saveError.value = '';
    try {
        const description = billingItems.value.map(i => `${i.name}: ₱${i.amount.toLocaleString()}`).join(', ');

        const soaRes = await axios.post(SOA_URL, {
            patientID: patientId.value,
            totalAmount: billingTotal.value,
            amountPaid: 0.0,
            balanceAmount: billingTotal.value,
            description: `Lying-In Admission Charges — ${description}`,
            invoiceReceiptNumber: 'ADM-' + Math.floor(Math.random() * 100000),
            dueDate: new Date().toISOString()
        });
        soaId.value = soaRes.data.soaID;

        currentStep.value = 'payment';
        await saveAdmission({ currentStep: 'payment', soaID: soaId.value, hasPhilHealth });
    } catch (error) {
        console.error('Failed to create Statement of Account', error);
        saveError.value = 'Failed to create the billing record. Please try again.';
    } finally {
        saving.value = false;
    }
}

// ── Payment step — records the real payment against the SOA ─────────────────
async function confirmPayment() {
    if (!soaId.value) {
        saveError.value = 'No Statement of Account found for this patient — cannot record payment.';
        return;
    }

    saving.value = true;
    saveError.value = '';
    try {
        const serviceBreakdown = [...billingItems.value.map(i => ({
            name: i.name, amount: i.amount, isDiscount: false
        }))];

        if (patientData.value.hasPhilHealth) {
            serviceBreakdown.push({
                name: 'PhilHealth Coverage',
                amount: -philHealthCoverage,
                isDiscount: true
            });
        }

        await axios.post(`${SOA_URL}/${soaId.value}/payments`, {
            amount: amountDue.value,
            totalAmount: billingTotal.value,
            paymentDate: new Date().toISOString(),
            paymentMethod: patientData.value.paymentMethod,
            notes: `Lying-In admission full payment${patientData.value.hasPhilHealth ? ' | PhilHealth applied' : ''}`,
            discountName: patientData.value.hasPhilHealth ? 'PhilHealth' : null,
            discountAmount: patientData.value.hasPhilHealth ? philHealthCoverage : null,
            serviceBreakdown: JSON.stringify(serviceBreakdown)
        });

        patientData.value.paymentComplete = true;
        currentStep.value = 'discharge';
        await saveAdmission({ currentStep: 'discharge', paymentComplete: true });
    } catch (error) {
        console.error('Failed to record payment', error);
        saveError.value = 'Failed to record the payment. Please try again.';
    } finally {
        saving.value = false;
    }
}

function resetProcess() {
    patientData.value = {
        firstName: '', lastName: '', age: '', gestationalAge: '',
        readyForDelivery: null, isHighRisk: null,
        wardID: null, attendingStaffID: null,
        hasPhilHealth: null, paymentComplete: false, paymentMethod: 'Cash'
    };
    currentStep.value = 'arrival';
    admissionId.value = null;
    patientId.value = null;
    soaId.value = null;
    saveError.value = '';
}

onMounted(() => {
    fetchWards();
    fetchEmployees();
});
</script>

<template>
    <div class="min-h-screen bg-gradient-to-br from-blue-50 to-pink-50 p-6">
        <div class="max-w-4xl mx-auto">
            <div class="bg-white rounded-2xl shadow-xl p-8">
                <!-- Header -->
                <div class="text-center mb-8">
                    <h1 class="text-3xl font-bold text-gray-800 mb-2">Lying-In Admission Process</h1>
                    <p class="text-gray-600">Complete patient care workflow management</p>
                </div>

                <!-- Save error banner -->
                <div v-if="saveError" class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg text-sm flex justify-between">
                    {{ saveError }}
                    <button @click="saveError = ''" class="ml-3 font-bold text-red-400">✕</button>
                </div>

                <!-- Current Step Indicator -->
                <div class="flex items-center justify-center mb-8">
                    <div :class="[currentStepInfo.color, 'p-4 rounded-full']">
                        <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-xl font-bold text-gray-800">{{ currentStepInfo.title }}</h2>
                        <p v-if="patientData.firstName" class="text-sm text-gray-600">
                            Patient: {{ patientData.firstName }} {{ patientData.lastName }}
                            <span v-if="admissionId" class="text-gray-400">— Admission #{{ admissionId }}</span>
                        </p>
                        <p v-if="saving" class="text-xs text-purple-500 animate-pulse">Saving…</p>
                    </div>
                </div>

                <!-- Progress Bar -->
                <div class="mb-8">
                    <div class="flex justify-between text-xs text-gray-500 mb-2">
                        <span>Arrival</span><span>Assessment</span><span>Care</span><span>Billing</span><span>Discharge</span>
                    </div>
                    <div class="w-full bg-gray-200 rounded-full h-2">
                        <div :class="[currentStepInfo.color, 'h-2 rounded-full transition-all duration-500']"
                            :style="{ width: progressWidth }"></div>
                    </div>
                </div>

                <!-- Step Content -->
                <div class="bg-gray-50 rounded-xl p-6">

                    <!-- Arrival Step -->
                    <div v-if="currentStep === 'arrival'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Arrival with Labor Symptoms</h3>
                        <div class="space-y-3">
                            <div class="grid grid-cols-2 gap-3">
                                <input v-model="patientData.firstName" type="text" placeholder="First Name"
                                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                                <input v-model="patientData.lastName" type="text" placeholder="Last Name"
                                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                            </div>
                            <input v-model="patientData.age" type="number" placeholder="Age"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                            <input v-model="patientData.gestationalAge" type="text" placeholder="Gestational Age (weeks)"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                            <button @click="proceedFromArrival" :disabled="!patientData.firstName"
                                class="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                {{ saving ? 'Saving…' : 'Proceed to Assessment' }}
                            </button>
                        </div>
                    </div>

                    <!-- Assessment Step -->
                    <div v-if="currentStep === 'assessment'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Initial Assessment</h3>
                        <p class="text-gray-600">Evaluate if patient is ready for childbirth</p>
                        <div class="space-y-3">
                            <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                <p class="font-medium text-gray-700 mb-2">Patient: {{ patientData.firstName }} {{ patientData.lastName }}</p>
                                <p class="text-gray-600">Age: {{ patientData.age }} | GA: {{ patientData.gestationalAge }} weeks</p>
                            </div>
                            <div class="grid grid-cols-2 gap-3">
                                <button @click="handleInputChange('readyForDelivery', false); navigateToStep('notReady')"
                                    class="bg-yellow-500 text-white py-3 rounded-lg hover:bg-yellow-600 transition-colors font-semibold">
                                    Not Ready for Delivery
                                </button>
                                <button @click="handleInputChange('readyForDelivery', true); navigateToStep('admission')"
                                    class="bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition-colors font-semibold">
                                    Ready for Delivery
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Not Ready Step -->
                    <div v-if="currentStep === 'notReady'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Not Ready - Discharge Home</h3>
                        <div class="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                            <p class="text-yellow-800 font-medium mb-2">Patient advised to go home</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p>✓ Monitor contractions (frequency and intensity)</p>
                                <p>✓ Track fetal movements</p>
                                <p>✓ Watch for water breaking</p>
                                <p>✓ Return if contractions are regular (every 5 minutes)</p>
                                <p>✓ Seek immediate care if bleeding or severe pain occurs</p>
                            </div>
                        </div>
                        <button @click="resetProcess"
                            class="w-full bg-gray-500 text-white py-3 rounded-lg hover:bg-gray-600 transition-colors font-semibold">
                            Complete & Return to Start
                        </button>
                    </div>

                    <!-- Admission Step -->
                    <div v-if="currentStep === 'admission'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Admission & Assignment</h3>
                        <div class="space-y-3">
                            <select v-model="patientData.wardID"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500">
                                <option :value="null" disabled>-- Select Ward --</option>
                                <option v-for="ward in wardsList" :key="ward.id" :value="ward.id">
                                    {{ ward.name }}
                                </option>
                            </select>
                            <p v-if="wardsList.length === 0" class="text-xs text-gray-400">
                                No wards found — check the wards endpoint.
                            </p>

                            <select v-model="patientData.attendingStaffID"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500">
                                <option :value="null" disabled>-- Select Attending Staff --</option>
                                <option v-for="staff in employeesList" :key="staff.employeeID" :value="staff.employeeID">
                                    {{ staff.fName }} {{ staff.lName }}
                                </option>
                            </select>
                            <p v-if="employeesList.length === 0" class="text-xs text-gray-400">
                                No employees found — check the employees endpoint.
                            </p>

                            <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
                                <p class="text-green-800 font-medium">Patient Admitted Successfully</p>
                                <p class="text-sm text-gray-600 mt-1">Ward: {{ selectedWardName || 'Not assigned' }}</p>
                                <p class="text-sm text-gray-600">Staff: {{ selectedStaffName || 'Not assigned' }}</p>
                            </div>
                            <button @click="navigateToStep('monitoring')"
                                :disabled="!patientData.wardID || !patientData.attendingStaffID"
                                class="w-full bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                {{ saving ? 'Saving…' : 'Begin Monitoring' }}
                            </button>
                        </div>
                    </div>

                    <!-- Monitoring Step -->
                    <div v-if="currentStep === 'monitoring'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Monitoring</h3>
                        <div class="p-4 bg-pink-50 border border-pink-200 rounded-lg">
                            <p class="text-pink-800 font-medium mb-2">Closely monitoring patient</p>
                            <div class="space-y-1 text-sm text-gray-700">
                                <p>• Vital signs monitoring</p>
                                <p>• Contraction frequency and intensity</p>
                                <p>• Fetal heart rate monitoring</p>
                                <p>• Cervical dilation progress</p>
                            </div>
                        </div>
                        <div class="space-y-3">
                            <p class="font-medium text-gray-700">Risk Assessment:</p>
                            <div class="grid grid-cols-2 gap-3">
                                <button @click="handleInputChange('isHighRisk', true); navigateToStep('referral')"
                                    class="bg-red-500 text-white py-3 rounded-lg hover:bg-red-600 transition-colors font-semibold">
                                    High Risk Identified
                                </button>
                                <button @click="handleInputChange('isHighRisk', false); navigateToStep('delivery')"
                                    class="bg-indigo-500 text-white py-3 rounded-lg hover:bg-indigo-600 transition-colors font-semibold">
                                    Continue to Delivery
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Referral Step -->
                    <div v-if="currentStep === 'referral'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Immediate Referral Required</h3>
                        <div class="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p class="text-red-800 font-bold mb-2">⚠️ High-Risk Patient Identified</p>
                            <p class="text-red-700 mb-3">Patient requires immediate referral to St. Mary Hospital</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p class="font-medium">Referral Process:</p>
                                <p>✓ Contact St. Mary Hospital emergency department</p>
                                <p>✓ Prepare patient transfer documents</p>
                                <p>✓ Arrange ambulance transport</p>
                                <p>✓ Provide complete medical records</p>
                                <p>✓ Brief receiving hospital staff on patient condition</p>
                            </div>
                        </div>
                        <button @click="resetProcess"
                            class="w-full bg-red-500 text-white py-3 rounded-lg hover:bg-red-600 transition-colors font-semibold">
                            Complete Referral & Return to Start
                        </button>
                    </div>

                    <!-- Delivery Step -->
                    <div v-if="currentStep === 'delivery'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Delivery Process</h3>
                        <div class="p-4 bg-indigo-50 border border-indigo-200 rounded-lg">
                            <p class="text-indigo-800 font-medium mb-2">Midwife proceeding with delivery</p>
                            <p class="text-sm text-gray-700 mb-2">Birth aide called for assistance</p>
                            <div class="space-y-1 text-sm text-gray-700">
                                <p>• Preparing delivery room</p>
                                <p>• Monitoring maternal and fetal status</p>
                                <p>• Assisting with delivery positions</p>
                                <p>• Ready for newborn reception</p>
                            </div>
                        </div>
                        <button @click="navigateToStep('postpartum')"
                            class="w-full bg-indigo-500 text-white py-3 rounded-lg hover:bg-indigo-600 transition-colors font-semibold">
                            Delivery Complete - Proceed to Postpartum Care
                        </button>
                    </div>

                    <!-- Postpartum Step -->
                    <div v-if="currentStep === 'postpartum'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Postpartum & Newborn Care</h3>
                        <div class="space-y-3">
                            <div class="p-4 bg-rose-50 border border-rose-200 rounded-lg">
                                <p class="text-rose-800 font-medium mb-2">Mother's Care:</p>
                                <div class="space-y-1 text-sm text-gray-700">
                                    <p>✓ Postpartum monitoring</p>
                                    <p>✓ Vital signs assessment</p>
                                    <p>✓ Bleeding and uterine contraction check</p>
                                    <p>✓ Breastfeeding initiation support</p>
                                    <p>✓ Pain management</p>
                                </div>
                            </div>
                            <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                                <p class="text-blue-800 font-medium mb-2">Newborn Care:</p>
                                <div class="space-y-1 text-sm text-gray-700">
                                    <p>✓ APGAR score assessment</p>
                                    <p>✓ Newborn screening</p>
                                    <p>✓ Vitamin K administration</p>
                                    <p>✓ Eye prophylaxis</p>
                                    <p>✓ Initial breastfeeding</p>
                                </div>
                            </div>
                            <button @click="proceedToBilling"
                                class="w-full bg-rose-500 text-white py-3 rounded-lg hover:bg-rose-600 transition-colors font-semibold disabled:opacity-50">
                                {{ saving ? 'Creating billing record…' : 'Proceed to Billing' }}
                            </button>
                        </div>
                    </div>

                    <!-- Billing Step -->
                    <div v-if="currentStep === 'billing'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Statement of Account (SOA)</h3>
                        <p v-if="soaId" class="text-xs text-gray-400">SOA #{{ soaId }} created</p>

                        <!-- Add Fee form -->
                        <div class="p-4 bg-white border border-gray-200 rounded-lg space-y-3">
                            <p class="font-medium text-gray-700">Add a Fee</p>
                            <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                                <input
                                    v-model="newFeeName"
                                    type="text"
                                    placeholder="Fee name (e.g. Ultrasound)"
                                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 md:col-span-2"
                                />
                                <input
                                    v-model="newFeeAmount"
                                    type="number"
                                    placeholder="Amount (₱)"
                                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
                                />
                            </div>
                            <button
                                type="button"
                                @click="addBillingItem"
                                class="bg-teal-600 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-teal-700"
                            >
                                + Add Fee
                            </button>
                        </div>

                        <!-- Itemized breakdown -->
                        <div class="p-4 bg-teal-50 border border-teal-200 rounded-lg">
                            <p class="text-teal-800 font-medium mb-3">Statement of Account</p>
                            <div class="space-y-2 text-sm">
                                <div v-if="billingItems.length === 0" class="text-gray-400 italic">
                                    No fees added yet.
                                </div>
                                <div v-for="(item, index) in billingItems" :key="index"
                                    class="flex justify-between items-center">
                                    <span>{{ item.name }}:</span>
                                    <span class="flex items-center gap-3">
                                        ₱{{ item.amount.toLocaleString() }}
                                        <button @click="removeBillingItem(index)" class="text-red-500 hover:text-red-700 text-xs">✕</button>
                                    </span>
                                </div>
                                <hr class="my-2 border-teal-300" />
                                <div class="flex justify-between font-bold text-base">
                                    <span>Total:</span><span>₱{{ billingTotal.toLocaleString() }}</span>
                                </div>
                            </div>
                        </div>

                        <div class="space-y-3">
                            <p class="font-medium text-gray-700">PhilHealth Status:</p>
                            <div class="grid grid-cols-2 gap-3">
                                <button @click="finalizeBillingAndProceed(false)"
                                    :disabled="billingItems.length === 0"
                                    class="bg-gray-500 text-white py-3 rounded-lg hover:bg-gray-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                    {{ saving ? 'Saving…' : 'No PhilHealth' }}
                                </button>
                                <button @click="finalizeBillingAndProceed(true)"
                                    :disabled="billingItems.length === 0"
                                    class="bg-emerald-500 text-white py-3 rounded-lg hover:bg-emerald-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                    {{ saving ? 'Saving…' : 'Has PhilHealth' }}
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Payment Step -->
                    <div v-if="currentStep === 'payment'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Payment Processing</h3>
                        <div v-if="patientData.hasPhilHealth" class="p-4 bg-emerald-50 border border-emerald-200 rounded-lg">
                            <p class="text-emerald-800 font-medium mb-2">PhilHealth Coverage Applied</p>
                            <div class="space-y-2 text-sm">
                                <div class="flex justify-between"><span>Total Bill:</span><span>₱{{ billingTotal.toLocaleString() }}</span></div>
                                <div class="flex justify-between text-emerald-700"><span>PhilHealth Coverage:</span><span>-₱{{ philHealthCoverage.toLocaleString() }}</span></div>
                                <hr class="my-2 border-emerald-300" />
                                <div class="flex justify-between font-bold text-base"><span>Amount Due:</span><span>₱{{ amountDue.toLocaleString() }}</span></div>
                            </div>
                        </div>
                        <div v-else class="p-4 bg-gray-50 border border-gray-200 rounded-lg">
                            <p class="text-gray-800 font-medium mb-2">Assisting Patient with Payment</p>
                            <div class="space-y-2 text-sm">
                                <div class="flex justify-between font-bold text-base"><span>Amount Due:</span><span>₱{{ amountDue.toLocaleString() }}</span></div>
                                <p class="text-gray-600 mt-2">• Payment plan options available</p>
                                <p class="text-gray-600">• Social service assistance available</p>
                            </div>
                        </div>
                        <div class="space-y-3">
                            <select v-model="patientData.paymentMethod"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500">
                                <option>Cash</option>
                                <option>Debit/Credit Card</option>
                                <option>GCash</option>
                                <option>Bank Transfer</option>
                                <option>Installment Plan</option>
                            </select>
                            <button @click="confirmPayment"
                                class="w-full bg-emerald-500 text-white py-3 rounded-lg hover:bg-emerald-600 transition-colors font-semibold disabled:opacity-50">
                                {{ saving ? 'Recording payment…' : 'Confirm Full Payment' }}
                            </button>
                        </div>
                    </div>

                    <!-- Discharge Step -->
                    <div v-if="currentStep === 'discharge'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Discharge</h3>
                        <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
                            <p class="text-green-800 font-bold mb-2">✓ Payment Complete - Ready for Discharge</p>
                            <p class="text-green-700 font-medium mb-3">Discharge Instructions:</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p class="font-medium">For Mother:</p>
                                <p>• Rest adequately and avoid strenuous activities</p>
                                <p>• Monitor for excessive bleeding or fever</p>
                                <p>• Continue breastfeeding on demand</p>
                                <p>• Take prescribed medications as directed</p>
                                <p>• Return for postpartum checkup in 1 week</p>
                                <p class="font-medium mt-3">For Newborn:</p>
                                <p>• Exclusive breastfeeding for 6 months</p>
                                <p>• Follow vaccination schedule</p>
                                <p>• Watch for jaundice, difficulty breathing, or poor feeding</p>
                                <p>• Newborn screening results in 2 weeks</p>
                                <p>• Return for checkup as scheduled</p>
                            </div>
                        </div>
                        <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                            <p class="text-blue-800 font-medium">Emergency Contact:</p>
                            <p class="text-sm text-gray-700">Call clinic hotline: (123) 456-7890</p>
                            <p class="text-sm text-gray-700">Available 24/7 for concerns</p>
                        </div>
                        <button @click="resetProcess"
                            class="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 transition-colors font-semibold">
                            Complete Discharge & Return to Start
                        </button>
                    </div>
                </div>

                <!-- Footer -->
                <div class="mt-6 text-center text-sm text-gray-500">
                    <p>Maternal & Child Health Care System</p>
                </div>
            </div>
        </div>
    </div>
</template>
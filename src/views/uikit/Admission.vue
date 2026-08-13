<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

// ── API endpoints ────────────────────────────────────────────────────────────
const PATIENTS_URL    = 'http://localhost:8080/api/patients';
const WARDS_URL       = 'http://localhost:8080/api/wards';
const EMPLOYEES_URL   = 'http://localhost:8080/api/employees';
const ADMISSIONS_URL  = 'http://localhost:8080/api/admissions';
const SOA_URL         = 'http://localhost:8080/api/billing/soa';
const CALENDAR_URL    = 'http://localhost:8080/api/calendar';
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services';
const POSTPARTUM_URL  = 'http://localhost:8080/api/postpartum';

// ── Route params — tries multiple common param names since different routes
// in this project use different conventions (patientID, id, clientId).
const patientID = route.params.patientID || route.params.id || route.params.clientId;
const serviceId = route.params.serviceId;

function goBack() {
    router.back();
}

const stepOrder = ['arrival', 'assessment', 'admission', 'monitoring', 'delivery', 'postpartum', 'billing', 'payment', 'discharge', 'discharged'];

const currentStepIndex = computed(() => stepOrder.indexOf(currentStep.value));
const canGoPrev = computed(() => currentStepIndex.value > 0);
const canGoNext = computed(() => currentStepIndex.value >= 0 && currentStepIndex.value < stepOrder.length - 1);

function goNextStep() {
    if (canGoNext.value) {
        const nextStep = stepOrder[currentStepIndex.value + 1];
        navigateToStep(nextStep);
    }
}

function goPrevStep() {
    if (canGoPrev.value) {
        const prevStep = stepOrder[currentStepIndex.value - 1];
        navigateToStep(prevStep);
    }
}

const currentStep = ref('arrival');
const saving = ref(false);
const loadingExisting = ref(true);
const saveError = ref('');
const followUpDate = ref(todayLocalDateString());

// Backend-linked record IDs — populated as the workflow progresses
const admissionId = ref(null);
const patientId = ref(patientID ? Number(patientID) : null);
const soaId = ref(null);

// Real lists pulled from the backend for the Ward/Staff assignment step
const wardsList = ref([]);
const employeesList = ref([]);

const patientData = ref({
    firstName: '',
    lastName: '',
    age: '',
    gestationalAge: '',
    symptomsComplaints: '',
    feelsLaborPain: null,
    cervicalDilation: '',
    preLaborNotes: '',
    readyForDelivery: null,
    isHighRisk: null,
    wardID: null,
    attendingStaffID: null,
    hasPhilHealth: null,
    paymentComplete: false,
    paymentMethod: 'Cash',
    dischargeDate: null,
    monitoringNotes: '', // New field for monitoring notes
    admissionNotes: '', // New field for admission step notes
    isMotherBabyStable: null,
    hasComplications: null,
    healthEducationGiven: true,
    breastfeedingSupportGiven: true
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

// Delivery notes
const deliveryNotes = ref('');

// Postpartum - Mother's Care items (editable list)
const motherCareItems = ref([
    'Postpartum monitoring',
    'Vital signs assessment',
    'Bleeding and uterine contraction check',
    'Breastfeeding initiation support',
    'Pain management'
]);
const newMotherCareItem = ref('');

function addMotherCareItem() {
    const item = newMotherCareItem.value.trim();
    if (!item) return;
    motherCareItems.value.push(item);
    newMotherCareItem.value = '';
}

function removeMotherCareItem(index) {
    motherCareItems.value.splice(index, 1);
}

// Postpartum - Newborn Care items (editable list)
const newbornCareItems = ref([
    'APGAR score assessment',
    'Newborn screening',
    'Vitamin K administration',
    'Eye prophylaxis',
    'Initial breastfeeding'
]);
const newNewbornCareItem = ref('');

function addNewbornCareItem() {
    const item = newNewbornCareItem.value.trim();
    if (!item) return;
    newbornCareItems.value.push(item);
    newNewbornCareItem.value = '';
}

function removeNewbornCareItem(index) {
    newbornCareItems.value.splice(index, 1);
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
    discharge: { title: 'Patient Discharge', color: 'bg-green-600' },
    discharged: { title: 'Discharged & Completed', color: 'bg-gray-700' }
};

const currentStepInfo = computed(() => steps[currentStep.value] || { title: 'Admission Process', color: 'bg-blue-500' });

const progressWidth = computed(() => {
    const progressMap = {
        arrival: '10%', assessment: '20%', notReady: '30%', admission: '35%',
        monitoring: '45%', referral: '50%', delivery: '60%', postpartum: '70%',
        billing: '80%', payment: '90%', discharge: '95%', discharged: '100%'
    };
    return progressMap[currentStep.value] || '100%';
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

// ── Postpartum record validation ──────────────────────────────────────────────
const postpartumRecord = ref(null);

async function fetchPostpartumRecord() {
    if (!serviceId) return;
    try {
        const res = await axios.get(POSTPARTUM_URL, { params: { serviceID: serviceId } });
        const records = Array.isArray(res.data) ? res.data : [];
        if (records.length > 0) {
            postpartumRecord.value = records[records.length - 1];
        }
    } catch (error) {
        console.error('Failed to load postpartum record', error);
    }
}

const isPostpartumVisitsComplete = computed(() => {
    if (!postpartumRecord.value) return false;
    const visits = postpartumRecord.value.visits || [];
    const v3 = visits.find(v => v.visitNumber === 3) || visits[2];
    const v4 = visits.find(v => v.visitNumber === 4) || visits[3];
    return Boolean(v3?.dateOfVisit && v4?.dateOfVisit);
});

// Loads the existing Patient record for this route's patientID, so the
// form shows the real name instead of asking staff to re-type it.
async function fetchExistingPatient() {
    if (!patientId.value) {
        console.warn('No patientId resolved from route — cannot fetch patient info.');
        return;
    }
    try {
        const res = await axios.get(`${PATIENTS_URL}/${patientId.value}`);
        patientData.value.firstName = res.data.fName || '';
        patientData.value.lastName = res.data.lName || '';
        patientData.value.age = res.data.age ?? '';
    } catch (error) {
        console.error('Failed to load existing patient record', error);
    }
}

// Picks the record with the highest numeric ID field from an array, instead
// of trusting array order (the backend does not guarantee ordering).
function pickLatestRecord(records) {
    if (!Array.isArray(records) || records.length === 0) return null;
    const sample = records[0];
    const idKey = Object.keys(sample).find(
        k => /id$/i.test(k) && typeof sample[k] === 'number'
    );
    if (!idKey) return records[records.length - 1];
    return records.reduce((latest, current) =>
        (!latest || current[idKey] > latest[idKey]) ? current : latest, null);
}

// If an Admission record already exists for THIS specific serviceId, resume
// the workflow from wherever it left off instead of creating a duplicate.
//
// STRICT match: only ever match by serviceID — never fall back to patientID.
// The patientID fallback was removed because it caused the wrong admission
// record to load when a patient had multiple Admission-type service entries.
async function loadExistingAdmission() {
    if (!serviceId) { loadingExisting.value = false; return; }
    try {
        const res = await axios.get(ADMISSIONS_URL);
        const all = Array.isArray(res.data) ? res.data : [];
        const matches = all.filter(a => String(a.serviceID) === String(serviceId));

        if (!matches.length) { loadingExisting.value = false; return; }

        const latest = pickLatestRecord(matches);

        admissionId.value = latest.admissionID;
        soaId.value = latest.soaID || null;
        patientData.value.age = latest.age ?? patientData.value.age;
        patientData.value.gestationalAge = latest.gestationalAge || '';
        patientData.value.symptomsComplaints = latest.symptomsComplaints || '';
        patientData.value.feelsLaborPain = latest.feelsLaborPain ?? null;
        patientData.value.cervicalDilation = latest.cervicalDilation ?? '';
        patientData.value.preLaborNotes = latest.preLaborNotes || '';
        patientData.value.readyForDelivery = latest.readyForDelivery ?? null;
        patientData.value.isHighRisk = latest.isHighRisk ?? null;
        patientData.value.wardID = latest.wardID ?? null;
        patientData.value.attendingStaffID = latest.attendingStaffID ?? null;
        patientData.value.hasPhilHealth = latest.hasPhilHealth ?? null;
        patientData.value.paymentComplete = !!latest.paymentComplete;
        patientData.value.dischargeDate = latest.dischargeDate || null;
        patientData.value.isMotherBabyStable = latest.isMotherBabyStable ?? null;
        patientData.value.hasComplications = latest.hasComplications ?? null;
        if (latest.currentStep && steps[latest.currentStep]) {
            currentStep.value = latest.currentStep;
        }

        if (latest.soaID) {
            try {
                const soaRes = await axios.get(`${SOA_URL}/${latest.soaID}`);
                const description = soaRes.data.description || '';
                if (description.includes('Lying-In Admission Charges — ')) {
                    const cleanDesc = description.replace('Lying-In Admission Charges — ', '');
                    const parts = cleanDesc.split(', ');
                    const parsedItems = [];
                    for (const p of parts) {
                        const colonIndex = p.lastIndexOf(':');
                        if (colonIndex !== -1) {
                            const name = p.substring(0, colonIndex).trim();
                            const amtStr = p.substring(colonIndex + 1).replace(/[^\d]/g, '');
                            const amount = Number(amtStr);
                            if (name && !isNaN(amount)) {
                                parsedItems.push({ name, amount });
                            }
                        }
                    }
                    if (parsedItems.length > 0) {
                        billingItems.value = parsedItems;
                    }
                }
            } catch (soaErr) {
                console.error('Failed to load Statement of Account description', soaErr);
            }
        }
    } catch (error) {
        console.error('Failed to load existing admission record (this is OK if none exists yet):', error);
    } finally {
        loadingExisting.value = false;
    }
}

// Saves/updates the Admission record so the workflow can be resumed later.
// Because admissionId.value is now reliably populated by loadExistingAdmission()
// above (once it succeeds), this correctly PUTs (updates) on every subsequent
// save instead of creating a new row each time.
async function saveAdmission(extraFields = {}) {
    saving.value = true;
    saveError.value = '';
    try {
        const payload = {
            admissionID: admissionId.value,
            serviceID: serviceId ? Number(serviceId) : null,
            patientID: patientId.value,
            patientName: `${patientData.value.firstName} ${patientData.value.lastName}`.trim(),
            age: patientData.value.age ? Number(patientData.value.age) : null,
            gestationalAge: patientData.value.gestationalAge,
            symptomsComplaints: patientData.value.symptomsComplaints,
            feelsLaborPain: patientData.value.feelsLaborPain,
            cervicalDilation: patientData.value.cervicalDilation,
            preLaborNotes: patientData.value.preLaborNotes,
            readyForDelivery: patientData.value.readyForDelivery,
            isHighRisk: patientData.value.isHighRisk,
            wardID: patientData.value.wardID,
            wardName: selectedWardName.value,
            attendingStaffID: patientData.value.attendingStaffID,
            attendingStaffName: selectedStaffName.value,
            hasPhilHealth: patientData.value.hasPhilHealth,
            paymentComplete: patientData.value.paymentComplete,
            isMotherBabyStable: patientData.value.isMotherBabyStable,
            hasComplications: patientData.value.hasComplications,
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

// Sync the assigned attending staff name back onto PatientService so it
// displays correctly in the "Services Availed" tables, same pattern as the
// Ultrasound/Prenatal/Family Planning forms.
async function syncEmployeeNameToPatientService() {
    if (!selectedStaffName.value || !serviceId) return;
    try {
        const currentRes = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceId}`);
        const currentService = currentRes.data;
        await axios.put(PATIENT_SERVICE_BASE, {
            ...currentService,
            employeeName: selectedStaffName.value
        });
    } catch (syncErr) {
        console.error('Failed to sync employee name to PatientService', syncErr);
    }
}

function navigateToStep(step) {
    currentStep.value = step;
    saveAdmission({ currentStep: step });
}

function todayLocalDateString() {
    const now = new Date();
    const yyyy = now.getFullYear();
    const mm = String(now.getMonth() + 1).padStart(2, '0');
    const dd = String(now.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
}

async function scheduleFollowUp() {
    const patientName = `${patientData.value.firstName || ''} ${patientData.value.lastName || ''}`.trim();
    const chosenDate = followUpDate.value || todayLocalDateString();

    if (!chosenDate) {
        saveError.value = 'Please select a follow-up date.';
        return;
    }

    saving.value = true;
    saveError.value = '';

    try {
        await axios.post(`${CALENDAR_URL}/manual`, {
            title: 'Follow-up Visit',
            eventDate: chosenDate,
            eventType: 'follow-up-visit',
            patientId: patientId.value || patientID || null,
            patientName: patientName || 'Patient',
            description: `Follow-up consultation for ${patientName || 'patient'} after not ready for delivery.`
        });

        currentStep.value = 'discharged';
        await saveAdmission({
            currentStep: 'discharged',
            dischargeDate: new Date().toISOString(),
            followUpDate: chosenDate
        });
    } catch (error) {
        console.error('Failed to save follow-up date to calendar', error);
        saveError.value = 'Failed to save the follow-up date to the calendar.';
    } finally {
        saving.value = false;
    }
}

function handleHighRiskReferral() {
    const patientName = `${patientData.value.firstName || ''} ${patientData.value.lastName || ''}`.trim();
    const referralPayload = {
        id: patientId.value || patientID || null,
        name: patientName || 'Patient',
        age: patientData.value.age || null,
        contact: '',
        gestationalWeek: patientData.value.gestationalAge || '',
        riskFactors: ['High risk identified']
    };

    try {
        localStorage.setItem('referral_patient', JSON.stringify(referralPayload));
    } catch (e) {
        console.warn('Could not persist referral patient context', e);
    }

    router.push({
        path: '/uikit/ClinicalReferralform',
        query: {
            patientId: referralPayload.id || '',
            patient: JSON.stringify(referralPayload)
        }
    });
}

function handleProceedToPostpartum() {
    // Update step to postpartum before leaving so the record is saved
    navigateToStep('postpartum');
    router.push({
        path: '/uikit/PostpartumCareform',
        query: {
            patientId: patientId.value || patientID || '',
            serviceId: serviceId || '',
            patientName: `${patientData.value.firstName || ''} ${patientData.value.lastName || ''}`.trim(),
            admissionPatientId: patientId.value || patientID || '',
            admissionServiceId: serviceId || '',
            returnToBilling: 'true'
        }
    });
}

function viewPostpartumForm() {
    router.push({
        path: '/uikit/PostpartumCareform',
        query: {
            patientId: patientId.value || patientID || '',
            serviceId: serviceId || '',
            patientName: `${patientData.value.firstName || ''} ${patientData.value.lastName || ''}`.trim(),
            admissionPatientId: patientId.value || patientID || '',
            admissionServiceId: serviceId || ''
        }
    });
}

// ── Step 1: Arrival — uses the EXISTING patient from the route; no longer
// creates a duplicate Patient record. Just confirms the name/age shown and
// moves on, saving the Admission tied to this serviceId.
async function proceedFromArrival() {
    if (!patientData.value.firstName) return;
    saving.value = true;
    saveError.value = '';

    try {
        if (!patientId.value) {
            // Fallback: no patientID in the route at all (shouldn't normally
            // happen since this form is always opened via a PatientService).
            const patientRes = await axios.post(PATIENTS_URL, {
                fName: patientData.value.firstName,
                lName: patientData.value.lastName
            });
            patientId.value = patientRes.data.patientID ?? patientRes.data.id;
        }

        currentStep.value = 'assessment';
        await saveAdmission({ currentStep: 'assessment' });
    } catch (error) {
        console.error('Failed to save admission', error);
        saveError.value = 'Failed to save the admission record. Please check the endpoint and try again.';
    } finally {
        saving.value = false;
    }
}

// ── Billing step — creates the real Statement of Account ────────────────────
function proceedToBilling() {
    if (!isPostpartumVisitsComplete.value) {
        saveError.value = 'Cannot proceed to Billing: 3rd and 4th Postpartum Care visits must be completed first in the Postpartum Care Form.';
        return;
    }
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

        await syncEmployeeNameToPatientService();
    } catch (error) {
        console.error('Failed to record payment', error);
        saveError.value = 'Failed to record the payment. Please try again.';
    } finally {
        saving.value = false;
    }
}

function resetProcess() {
    const idToUse = patientId.value || patientID;
    if (idToUse) {
        router.push(`/uikit/PatientProfiling/${idToUse}`);
    } else {
        router.push('/uikit/PatientsMain');
    }
}

async function completeDischarge() {
    saving.value = true;
    saveError.value = '';
    try {
        currentStep.value = 'discharged';
        await saveAdmission({
            currentStep: 'discharged',
            dischargeDate: new Date().toISOString()
        });
        const idToUse = patientId.value || patientID;
        if (idToUse) {
            router.push(`/uikit/PatientProfiling/${idToUse}`);
        } else {
            router.push('/uikit/PatientsMain');
        }
    } catch (error) {
        console.error('Failed to complete discharge', error);
        saveError.value = 'Failed to complete discharge. Please try again.';
    } finally {
        saving.value = false;
    }
}

onMounted(async () => {
    await fetchWards();
    await fetchEmployees();
    await fetchExistingPatient();
    await loadExistingAdmission();
    await fetchPostpartumRecord();

    // If navigated back from the PostpartumCareform after saving, auto-advance to billing ONLY if 3rd and 4th visits are done.
    if (route.query.fromPostpartum === 'true' && currentStep.value === 'postpartum') {
        if (isPostpartumVisitsComplete.value) {
            navigateToStep('billing');
        }
    }
});
</script>

<template>
    <div class="min-h-screen bg-gradient-to-br from-blue-50 to-pink-50 p-6">
        <div class="max-w-4xl mx-auto">
            <!-- Toolbar -->
            <div class="mb-4 flex justify-between">
                <button @click="goBack"
                    class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg shadow transition"
                >
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                    </svg>
                    Back
                </button>
                <button @click="goNextStep"
                    :disabled="!canGoNext"
                    class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-green-600 hover:bg-green-700 rounded-lg shadow transition"
                >
                    Next
                </button>
            </div>

            <div class="bg-white rounded-2xl shadow-xl p-8">
                <!-- Header -->
                <div class="text-center mb-8">
                    <h1 class="text-3xl font-bold text-gray-800 mb-2">Lying-In Admission Process</h1>
                    <p class="text-gray-600">Complete patient care workflow management</p>
                </div>

                <div v-if="loadingExisting" class="text-center py-10 text-gray-500">
                    Loading admission record…
                </div>

                <div v-else>
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

                    <!-- Assessment Step (Lying-In Admission Process Diagram) -->
                    <div v-if="currentStep === 'assessment'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800 flex items-center gap-2">
                            <span>🩺</span> Initial Assessment & Examination
                        </h3>
                        <p class="text-xs text-gray-500">Follow the Lying-In Admission clinical decision workflow</p>
                        
                        <div class="p-4 bg-gray-50 rounded-lg border border-gray-200 text-sm space-y-1">
                            <p class="font-semibold text-gray-800">Patient: {{ patientData.firstName }} {{ patientData.lastName }}</p>
                            <p class="text-gray-600">Age: {{ patientData.age }} | Gestational Age: {{ patientData.gestationalAge }} weeks</p>
                        </div>

                        <!-- Step 1: Symptoms / Complaints -->
                        <div class="p-4 bg-white border border-purple-200 rounded-lg shadow-sm space-y-3">
                            <div>
                                <label class="block font-medium text-purple-900 text-sm mb-1">
                                    1. Patient Symptoms / Complaints:
                                </label>
                                <textarea
                                    v-model="patientData.symptomsComplaints"
                                    placeholder="Describe symptoms (e.g. hypogastric pain, labor pains, bloody show, fluid leakage...)"
                                    class="w-full p-2.5 border border-purple-200 rounded-md text-sm focus:ring-2 focus:ring-purple-400 outline-none"
                                    rows="2"
                                ></textarea>
                            </div>

                            <!-- Decision 1: Labor Pain? -->
                            <div class="pt-2 border-t border-purple-100">
                                <label class="block font-medium text-gray-800 text-sm mb-2">
                                    2. Midwife Assessment: Does patient feel labor pain?
                                </label>
                                <div class="grid grid-cols-2 gap-3">
                                    <button 
                                        @click="patientData.feelsLaborPain = false; handleInputChange('readyForDelivery', false); navigateToStep('notReady')"
                                        class="py-2.5 px-4 rounded-lg font-semibold text-sm border transition flex flex-col items-center justify-center gap-0.5"
                                        :class="patientData.feelsLaborPain === false ? 'bg-amber-600 text-white border-amber-600' : 'border-amber-300 bg-amber-50 text-amber-900 hover:bg-amber-100'"
                                    >
                                        <span>❌ No Labor Pain</span>
                                        <span class="text-[11px] opacity-90">(Advise to go home)</span>
                                    </button>
                                    <button 
                                        @click="patientData.feelsLaborPain = true"
                                        class="py-2.5 px-4 rounded-lg font-semibold text-sm border transition flex flex-col items-center justify-center gap-0.5"
                                        :class="patientData.feelsLaborPain === true ? 'bg-purple-600 text-white border-purple-600 ring-2 ring-purple-400' : 'border-purple-300 bg-purple-50 text-purple-900 hover:bg-purple-100'"
                                    >
                                        <span>✅ Yes, Feels Labor Pain</span>
                                        <span class="text-[11px] opacity-90">(Proceed to Pre-Labor Assessment)</span>
                                    </button>
                                </div>
                            </div>

                            <!-- Step 2: Pre-Labor Assessment & Internal Examination (shown if Labor Pain = Yes) -->
                            <div v-if="patientData.feelsLaborPain === true" class="pt-3 border-t border-purple-100 space-y-3">
                                <div class="p-3.5 bg-purple-50 border border-purple-200 rounded-md">
                                    <p class="font-bold text-purple-900 text-xs uppercase tracking-wider mb-2">Pre-Labor Assessment & Internal Examination</p>
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                                        <div>
                                            <label class="block text-xs font-semibold text-gray-700 mb-1">Cervical Dilation (cm):</label>
                                            <input 
                                                v-model="patientData.cervicalDilation"
                                                type="number" step="0.5" min="0" max="10"
                                                placeholder="e.g. 4"
                                                class="w-full p-2 border border-purple-300 rounded text-sm focus:ring-2 focus:ring-purple-500 outline-none"
                                            />
                                        </div>
                                        <div>
                                            <label class="block text-xs font-semibold text-gray-700 mb-1">Exam Notes:</label>
                                            <input 
                                                v-model="patientData.preLaborNotes"
                                                type="text"
                                                placeholder="Effacement %, station, membranes status"
                                                class="w-full p-2 border border-purple-300 rounded text-sm focus:ring-2 focus:ring-purple-500 outline-none"
                                            />
                                        </div>
                                    </div>
                                </div>

                                <!-- Decision 2: Cervical Dilation >= 4 cm? -->
                                <div v-if="patientData.cervicalDilation !== ''" class="pt-1">
                                    <p class="font-semibold text-xs text-gray-800 mb-2">
                                        Clinical Decision: Cervical Dilation is {{ Number(patientData.cervicalDilation) >= 4 ? '≥ 4 cm (Active Labor Phase)' : '< 4 cm (Early Labor Phase)' }}
                                    </p>
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                                        <button 
                                            @click="handleInputChange('readyForDelivery', false); navigateToStep('notReady')"
                                            class="py-3 px-4 rounded-lg font-bold text-sm bg-amber-500 text-white hover:bg-amber-600 transition flex items-center justify-center gap-1.5 shadow"
                                        >
                                            <span>🏠 Dilation &lt; 4 cm</span>
                                            <span class="text-xs font-normal">(Advised to Go Home)</span>
                                        </button>
                                        <button 
                                            @click="handleInputChange('readyForDelivery', true); navigateToStep('admission')"
                                            class="py-3 px-4 rounded-lg font-bold text-sm bg-green-600 text-white hover:bg-green-700 transition flex items-center justify-center gap-1.5 shadow"
                                        >
                                            <span>🏥 Dilation ≥ 4 cm</span>
                                            <span class="text-xs font-normal">(Assign Ward & Admit)</span>
                                        </button>
                                    </div>
                                </div>
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
                        <div class="space-y-3">
                            <label class="block text-sm font-medium text-gray-700">
                                Follow-up Date
                                <input v-model="followUpDate" type="date"
                                    class="mt-1 w-full px-4 py-2 border border-yellow-300 rounded-lg focus:ring-2 focus:ring-yellow-500 focus:border-transparent" />
                            </label>
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                                <button @click="scheduleFollowUp"
                                    :disabled="saving"
                                    class="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                    {{ saving ? 'Saving…' : 'Schedule Follow-up' }}
                                </button>
                                <button @click="resetProcess"
                                    class="w-full bg-gray-500 text-white py-3 rounded-lg hover:bg-gray-600 transition-colors font-semibold">
                                    Complete & Return
                                </button>
                            </div>
                        </div>

                            <!-- Ready for Delivery — patient has returned and is now ready -->
                            <div class="mt-2 p-4 bg-green-50 border-2 border-green-400 rounded-xl">
                                <p class="text-green-800 font-semibold mb-1">🤱 Patient has returned and is now ready for delivery?</p>
                                <p class="text-sm text-green-700 mb-3">Click below to skip the scheduling and proceed directly to the full admission &amp; delivery workflow.</p>
                                <button
                                    @click="handleInputChange('readyForDelivery', true); navigateToStep('admission')"
                                    :disabled="saving"
                                    class="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 active:bg-green-800 transition-colors font-bold text-base shadow-md disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                                >
                                    <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                    Patient is Now Ready for Delivery — Proceed to Admission
                                </button>
                            </div>
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
                                <p class="text-green-800 font-medium">Admission Notes</p>
                                <textarea v-model="patientData.admissionNotes" placeholder="Enter any notes about admission..." class="w-full p-2 border border-green-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" rows="3"></textarea>
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
                            <p class="text-pink-800 font-medium mb-2">Monitoring Notes</p>
                            <textarea v-model="patientData.monitoringNotes" placeholder="Enter observations, vitals, and notes here..." class="w-full p-2 border border-pink-300 rounded-md focus:outline-none focus:ring-2 focus:ring-pink-500" rows="4"></textarea>
                        </div>
                        <div class="space-y-3">
                            <p class="font-medium text-gray-700">Risk Assessment:</p>
                            <div class="grid grid-cols-2 gap-3">
                                <button @click="handleInputChange('isHighRisk', true); handleHighRiskReferral()"
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
                            Complete Referral & Return
                        </button>
                    </div>

                    <!-- Delivery Step -->
                    <div v-if="currentStep === 'delivery'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Delivery Process</h3>
                        <div class="p-4 bg-indigo-50 border border-indigo-200 rounded-lg">
                            <p class="text-indigo-800 font-medium mb-2">Delivery Notes</p>
                            <p class="text-xs text-gray-500 mb-2">Describe the delivery process, procedures done, and any observations.</p>
                            <textarea
                                v-model="deliveryNotes"
                                placeholder="e.g. Normal spontaneous delivery, no complications, placenta delivered intact..."
                                class="w-full p-3 border border-indigo-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
                                rows="5"
                            ></textarea>
                        </div>
                        <button @click="handleProceedToPostpartum()"
                            class="w-full bg-indigo-500 text-white py-3 rounded-lg hover:bg-indigo-600 transition-colors font-semibold">
                            Delivery Complete - Proceed to Postpartum Care
                        </button>
                    </div>

                    <!-- Postpartum Step -->
                    <div v-if="currentStep === 'postpartum'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Postpartum & Newborn Care</h3>
                        
                        <!-- Postpartum Visits Requirement Card -->
                        <div class="p-4 rounded-lg border text-sm flex flex-col md:flex-row items-start md:items-center justify-between gap-3"
                            :class="isPostpartumVisitsComplete ? 'bg-emerald-50 border-emerald-200 text-emerald-800' : 'bg-amber-50 border-amber-200 text-amber-900'">
                            <div>
                                <p class="font-bold flex items-center gap-1.5">
                                    <span>{{ isPostpartumVisitsComplete ? '✅' : '⚠️' }}</span>
                                    <span>{{ isPostpartumVisitsComplete ? '3rd & 4th Postpartum Visits Completed' : '3rd & 4th Postpartum Visits Pending' }}</span>
                                </p>
                                <p class="text-xs opacity-90 mt-0.5">
                                    {{ isPostpartumVisitsComplete 
                                        ? 'All required postpartum visits are recorded. You may proceed to Billing & Payment.' 
                                        : 'Payment and Billing cannot be accessed until the 3rd and 4th postpartum visits are recorded in the Postpartum Care Form.' }}
                                </p>
                            </div>
                            <button @click="handleProceedToPostpartum()"
                                class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 font-semibold text-xs transition shadow-sm whitespace-nowrap">
                                📋 {{ postpartumRecord ? 'Update' : 'Open' }} Postpartum Form (Visits)
                            </button>
                        </div>

                            <!-- Postnatal Health Education & Observations (Diagram 2) -->
                            <div class="p-4 bg-teal-50 border border-teal-200 rounded-lg space-y-3">
                                <p class="text-teal-900 font-semibold text-sm">Postnatal Observation & Health Education (Midwife)</p>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-2 text-xs">
                                    <label class="flex items-center gap-2 cursor-pointer bg-white p-2 border border-teal-200 rounded">
                                        <input type="checkbox" v-model="patientData.healthEducationGiven" class="rounded text-teal-600 focus:ring-teal-500" />
                                        <span>Gives Health Education</span>
                                    </label>
                                    <label class="flex items-center gap-2 cursor-pointer bg-white p-2 border border-teal-200 rounded">
                                        <input type="checkbox" v-model="patientData.breastfeedingSupportGiven" class="rounded text-teal-600 focus:ring-teal-500" />
                                        <span>Breastfeeding Support</span>
                                    </label>
                                </div>

                                <!-- Decision: Is Mother & Newborn in stable condition? -->
                                <div class="pt-2 border-t border-teal-200">
                                    <p class="font-medium text-xs text-teal-900 mb-2">Midwife Assessment: Are both mother and newborn in stable condition?</p>
                                    <div class="grid grid-cols-2 gap-2">
                                        <button 
                                            @click="patientData.isMotherBabyStable = true"
                                            class="py-2 px-3 rounded text-xs font-semibold border transition"
                                            :class="patientData.isMotherBabyStable === true ? 'bg-emerald-600 text-white border-emerald-600 shadow' : 'bg-white text-emerald-800 border-emerald-300 hover:bg-emerald-100'"
                                        >
                                            ✅ Yes, Stable Condition
                                        </button>
                                        <button 
                                            @click="patientData.isMotherBabyStable = false"
                                            class="py-2 px-3 rounded text-xs font-semibold border transition"
                                            :class="patientData.isMotherBabyStable === false ? 'bg-red-600 text-white border-red-600 shadow' : 'bg-white text-red-800 border-red-300 hover:bg-red-100'"
                                        >
                                            ⚠️ No, Unstable / Complications
                                        </button>
                                    </div>

                                    <!-- If Unstable: Check for Heavy Complications -->
                                    <div v-if="patientData.isMotherBabyStable === false" class="mt-3 p-3 bg-red-50 border border-red-200 rounded-md space-y-2">
                                        <p class="text-xs font-bold text-red-900">Does the mother or baby experience heavy complications?</p>
                                        <div class="grid grid-cols-2 gap-2">
                                            <button 
                                                @click="patientData.hasComplications = false"
                                                class="py-2 px-3 rounded text-xs font-semibold border bg-white text-red-800 border-red-300 hover:bg-red-100"
                                            >
                                                Continue Observation
                                            </button>
                                            <button 
                                                @click="patientData.hasComplications = true; handleHighRiskReferral()"
                                                class="py-2 px-3 rounded text-xs font-bold border bg-red-600 text-white border-red-600 hover:bg-red-700 shadow"
                                            >
                                                🚨 Refer to Hospital
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <button @click="proceedToBilling"
                                :disabled="saving || !isPostpartumVisitsComplete"
                                class="w-full bg-rose-500 text-white py-3 rounded-lg hover:bg-rose-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed">
                                {{ saving ? 'Creating billing record…' : 'Proceed to Billing' }}
                            </button>
                            <p v-if="!isPostpartumVisitsComplete" class="text-xs text-amber-700 text-center font-medium">
                                🔒 Complete 3rd and 4th visits in Postpartum Care Form to unlock Proceed to Billing
                            </p>
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
                        <button @click="completeDischarge"
                            class="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 transition-colors font-semibold">
                            Complete Discharge & Return
                        </button>
                    </div>

                    <!-- Discharged Summary Step -->
                    <div v-if="currentStep === 'discharged'" class="space-y-6">
                        <div class="bg-white border border-gray-200 rounded-xl p-6 shadow-sm space-y-4">
                            <h3 class="text-xl font-bold text-gray-800 border-b pb-2">Patient Discharge Summary</h3>
                            
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">Patient Name</p>
                                    <p class="text-sm font-semibold text-gray-800">{{ patientData.firstName }} {{ patientData.lastName }}</p>
                                </div>
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">Age / Gestational Age</p>
                                    <p class="text-sm font-semibold text-gray-800">{{ patientData.age }} years old / {{ patientData.gestationalAge }} weeks</p>
                                </div>
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">Assigned Ward</p>
                                    <p class="text-sm font-semibold text-gray-800">{{ selectedWardName || 'Not assigned' }}</p>
                                </div>
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">Attending Staff</p>
                                    <p class="text-sm font-semibold text-gray-800">{{ selectedStaffName || 'Not assigned' }}</p>
                                </div>
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">PhilHealth Status</p>
                                    <p class="text-sm font-semibold text-gray-800">{{ patientData.hasPhilHealth ? 'Applied Coverage' : 'No PhilHealth' }}</p>
                                </div>
                                <div>
                                    <p class="text-xs text-gray-500 uppercase font-semibold">Payment Status</p>
                                    <p class="text-sm font-semibold text-emerald-600">✓ Fully Paid</p>
                                </div>
                            </div>
                            
                            <div class="border-t pt-4">
                                <p class="text-xs text-gray-500 uppercase font-semibold mb-2">Statement of Account (Paid)</p>
                                <div class="bg-gray-50 p-4 rounded-lg space-y-2 text-sm">
                                    <div v-for="(item, index) in billingItems" :key="index" class="flex justify-between text-gray-700">
                                        <span>{{ item.name }}:</span>
                                        <span>₱{{ item.amount.toLocaleString() }}</span>
                                    </div>
                                    <hr class="my-2 border-gray-200" />
                                    <div class="flex justify-between font-bold text-gray-800">
                                        <span>Total Amount:</span>
                                        <span>₱{{ billingTotal.toLocaleString() }}</span>
                                    </div>
                                    <div v-if="patientData.hasPhilHealth" class="flex justify-between text-emerald-700 text-xs">
                                        <span>PhilHealth Discount:</span>
                                        <span>-₱{{ philHealthCoverage.toLocaleString() }}</span>
                                    </div>
                                    <div class="flex justify-between font-bold text-emerald-600 border-t pt-2">
                                        <span>Total Paid:</span>
                                        <span>₱{{ amountDue.toLocaleString() }}</span>
                                    </div>
                                </div>
                            </div>

                            <div v-if="patientData.dischargeDate" class="border-t pt-4">
                                <p class="text-xs text-gray-500 uppercase font-semibold">Discharge Date & Time</p>
                                <p class="text-sm font-medium text-gray-800">{{ new Date(patientData.dischargeDate).toLocaleString() }}</p>
                            </div>

                            <div class="border-t pt-4 flex flex-col md:flex-row items-start md:items-center justify-between gap-3 bg-indigo-50/60 p-4 rounded-xl border border-indigo-100">
                                <div>
                                    <p class="text-xs text-indigo-900 uppercase font-bold tracking-wider">Postpartum Clinical Care Record</p>
                                    <p class="text-xs text-gray-600 mt-0.5">View recorded 1st to 4th visit dates, examination results, and care notes</p>
                                </div>
                                <button @click="viewPostpartumForm"
                                    class="px-4 py-2.5 bg-indigo-600 text-white text-xs font-bold rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition shadow flex items-center gap-1.5 whitespace-nowrap">
                                    📋 View Postpartum Care Form
                                </button>
                            </div>
                        </div>

                        <button @click="resetProcess"
                            class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors font-semibold">
                            Return to Patient Profile
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
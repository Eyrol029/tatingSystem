<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useUserDataStore, UserRole } from '@/stores/userData'

const route = useRoute()
const router = useRouter()

// Read-only mode for Patient accounts
const userStore = useUserDataStore()
if (!userStore.user) {
  userStore.init()
}
const isReadOnly = computed(() => userStore.userRole === UserRole.PATIENT)

function goBack() {
  router.back()
}

function printForm() {
  window.print()
}

// API Endpoints
const LAB_REQUEST_URL = 'http://localhost:8080/api/laboratory-requests'
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services'
const PATIENTS_URL = 'http://localhost:8080/api/patients'

// Header / Clinic Information
const clinicInfo = ref({
  name: "TATING'S BIRTHING HOME",
  address: "P26, POBLACION, MANTICAO, MIS ORI",
  contact: "09673953780 / 09991605864",
  physician: "DR. LOVELLA G. HUGO, M.D., FPOGS",
  specialization: "Obstetrician-Gynecologist",
  licenseNo: "0092775"
})

// Form State
const formData = ref({
  date: todayLocalDateString(),
  patientName: '',
  address: '',
  age: '',
  sex: '',
  birthDate: '',
  diagnosis: '',
  selectedTests: []
})

// UI Feedback States
const isSubmitting = ref(false)
const statusMessage = ref('')
const isError = ref(false)

// Available Laboratory Tests
const labTests = [
  'Pregnancy Test',
  'Complete Blood Count (CBC)',
  'Urinalysis (UA)',
  'Blood Typing (BT)',
  'Hepatitis B Screening (HbsAg)',
  'HIV Screening',
  'Syphilis screening (RPR or EIA)',
  'Oral Glucose Tolerance Test (OGTT)',
  'Pelvic Ultrasound or Transvaginal UTZ'
]

function todayLocalDateString() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

async function saveAndReturnToProfile() {
  if (isReadOnly.value) return

  const patientId = route.params.patientID || route.params.id

  if (!formData.value.patientName.trim()) {
    statusMessage.value = 'Please enter the patient name.'
    isError.value = true
    return
  }

  if (formData.value.selectedTests.length === 0) {
    statusMessage.value = 'Please select at least one laboratory test.'
    isError.value = true
    return
  }

  isSubmitting.value = true
  statusMessage.value = ''
  isError.value = false

  const payload = {
    requestDate: formData.value.date,
    patientName: formData.value.patientName.trim(),
    address: formData.value.address.trim(),
    age: formData.value.age ? Number(formData.value.age) : null,
    sex: formData.value.sex,
    birthDate: formData.value.birthDate || null,
    diagnosis: formData.value.diagnosis.trim(),
    selectedTests: formData.value.selectedTests
  }

  try {
    await axios.post(LAB_REQUEST_URL, payload)
    statusMessage.value = '✅ Laboratory Request saved successfully!'
    isError.value = false

    if (patientId) {
      router.push(`/uikit/PatientProfiling/${patientId}`)
    } else {
      router.push('/uikit/PatientsMain')
    }
  } catch (error) {
    console.error('Failed to save laboratory request:', error)
    statusMessage.value = '❌ ' + (error.response?.data || 'Unable to save laboratory request to database.')
    isError.value = true
  } finally {
    isSubmitting.value = false
  }
}

function handleProceedToPayment() {
  const patientId = Number(route.params.patientID || route.params.id || 0)
  if (!patientId) {
    statusMessage.value = 'Patient ID is missing. Please reopen this form from the patient record.'
    isError.value = true
    return
  }

  router.push({
    path: '/uikit/viewListOfSOA',
    query: { patientId }
  })
}

function resetForm() {
  if (isReadOnly.value) return
  const patientName = formData.value.patientName || ''
  const address = formData.value.address || ''
  const age = formData.value.age || ''
  const sex = formData.value.sex || ''
  const birthDate = formData.value.birthDate || ''

  formData.value = {
    date: todayLocalDateString(),
    patientName,
    address,
    age,
    sex,
    birthDate,
    diagnosis: '',
    selectedTests: []
  }
  statusMessage.value = ''
  isError.value = false
}

async function loadPatientFromRoute() {
  const patientId = route.params.patientID || route.params.id;
  if (!patientId) return;

  try {
    const response = await axios.get(`${PATIENTS_URL}/${patientId}`);
    const patient = response.data || {};

    const fullName = [patient.fName, patient.middleI, patient.lName]
      .filter(Boolean)
      .join(' ')
      .trim();

    const addressParts = [patient.houseNo, patient.street, patient.barangay, patient.municipality, patient.province]
      .filter(value => value !== null && value !== undefined && value !== '')
      .map(value => String(value).trim());

    if (fullName) {
      formData.value.patientName = fullName;
    }

    if (addressParts.length) {
      formData.value.address = addressParts.join(', ');
    }

    if (patient.age !== undefined && patient.age !== null && patient.age !== '') {
      formData.value.age = String(patient.age);
    }

    if (patient.bDate) {
      formData.value.birthDate = patient.bDate;
    }

    await loadExistingLaboratoryRequest(fullName);
  } catch (error) {
    console.error('Failed to load patient details for laboratory form:', error);
  }
}

async function loadExistingLaboratoryRequest(patientName) {
  const name = (patientName || formData.value.patientName || '').trim();
  if (!name) return;

  try {
    const response = await axios.get(LAB_REQUEST_URL, {
      params: { patientName: name }
    });

    const requests = Array.isArray(response.data) ? response.data : [];
    if (!requests.length) return;

    const latest = [...requests].sort((a, b) => {
      const aTime = a.id || a.requestDate ? new Date(a.requestDate || 0).getTime() : 0;
      const bTime = b.id || b.requestDate ? new Date(b.requestDate || 0).getTime() : 0;
      return bTime - aTime;
    })[0];

    if (!latest) return;

    formData.value.date = latest.requestDate || formData.value.date;
    formData.value.patientName = latest.patientName || formData.value.patientName;
    formData.value.address = latest.address || formData.value.address;
    formData.value.age = latest.age !== null && latest.age !== undefined ? String(latest.age) : formData.value.age;
    formData.value.sex = latest.sex || formData.value.sex;
    formData.value.birthDate = latest.birthDate || formData.value.birthDate;
    formData.value.diagnosis = latest.diagnosis || formData.value.diagnosis;
    formData.value.selectedTests = Array.isArray(latest.selectedTests) ? [...latest.selectedTests] : [];
  } catch (error) {
    console.error('Failed to load previous laboratory request for this patient:', error);
  }
}

onMounted(() => {
  loadPatientFromRoute();
});

// Auto-compute Age when Birth Date changes
function onBirthDateChange() {
  if (!formData.value.birthDate) return
  const birth = new Date(formData.value.birthDate)
  const today = new Date()
  let age = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--
  }
  formData.value.age = age >= 0 ? String(age) : ''
}

async function saveLaboratoryRequest() {
  await saveAndReturnToProfile()
}
</script>

<template>
  <!-- Toolbar -->
  <div class="no-print max-w-screen mx-auto px-6 pt-6 flex items-center gap-3">
    <button @click="goBack"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
      </svg>
      Back
    </button>
    <button @click="printForm"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round"
          d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z" />
      </svg>
      Print / Save as PDF
    </button>

    <span v-if="isReadOnly" class="ml-2 text-xs font-semibold text-indigo-700 bg-indigo-100 px-3 py-1.5 rounded-full">
      👁️ View Only
    </span>
  </div>

  <!-- Printable Landscape Form Card -->
  <div id="printable-area"
    class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <div class="flex items-center justify-center gap-3 mb-2">
        <img src="/static/TATING (2).png" alt="Tating Logo"
          style="height: 3.5rem; width: auto; object-fit: contain;" />
        <div class="text-left">
          <p class="text-xs text-gray-500 uppercase tracking-widest leading-tight">{{ clinicInfo.name }}</p>
          <h1 class="text-xl font-bold tracking-wide uppercase leading-tight">LABORATORY REQUEST FORM</h1>
          <p class="text-xs text-gray-400 leading-tight">{{ clinicInfo.address }} | CONTACT NO.: {{ clinicInfo.contact }}</p>
        </div>
      </div>
    </div>

    <!-- Status feedback messages -->
    <div v-if="statusMessage" class="no-print mb-4">
      <div :class="isError ? 'text-red-700 bg-red-100 border-red-200' : 'text-green-700 bg-green-100 border-green-200'"
        class="text-sm border rounded p-3 text-center font-medium">
        {{ statusMessage }}
      </div>
    </div>

    <!-- READ-ONLY WRAPPER -->
    <fieldset :disabled="isReadOnly" style="border:none;padding:0;margin:0;min-width:0;">

      <!-- Patient Information Section -->
      <div class="border border-gray-200 rounded-lg p-4 mb-6">
        <h3 class="section-title mb-3">PATIENT INFORMATION</h3>
        <div class="grid grid-cols-12 gap-4">
          
          <!-- Date -->
          <div class="col-span-12 sm:col-span-3 md:col-span-2">
            <label class="field-label">Date</label>
            <input type="date" v-model="formData.date" class="input" />
          </div>

          <!-- Name -->
          <div class="col-span-12 sm:col-span-9 md:col-span-5">
            <label class="field-label">Name <span class="text-red-500">*</span></label>
            <input type="text" v-model="formData.patientName" placeholder="Full Patient Name" class="input" />
          </div>

          <!-- B-Date -->
          <div class="col-span-12 sm:col-span-4 md:col-span-2">
            <label class="field-label">B-Date</label>
            <input type="date" v-model="formData.birthDate" @change="onBirthDateChange" class="input" />
          </div>

          <!-- Age -->
          <div class="col-span-6 sm:col-span-4 md:col-span-1">
            <label class="field-label">Age</label>
            <input type="number" v-model="formData.age" placeholder="Age" class="input text-center" />
          </div>

          <!-- Sex -->
          <div class="col-span-6 sm:col-span-4 md:col-span-2">
            <label class="field-label">Sex</label>
            <select v-model="formData.sex" class="input bg-white">
              <option value="" disabled>Select</option>
              <option value="Female">Female</option>
              <option value="Male">Male</option>
            </select>
          </div>

          <!-- Address -->
          <div class="col-span-12 md:col-span-6">
            <label class="field-label">Address</label>
            <input type="text" v-model="formData.address" placeholder="Complete Address" class="input" />
          </div>

          <!-- Diagnosis -->
          <div class="col-span-12 md:col-span-6">
            <label class="field-label">Diagnosis</label>
            <input type="text" v-model="formData.diagnosis" placeholder="Clinical Diagnosis / Notes" class="input" />
          </div>

        </div>
      </div>

      <!-- Requested Examinations Section (Grid format for landscape) -->
      <div class="border border-gray-200 rounded-lg p-5 mb-6">
        <h3 class="text-center font-bold text-sm tracking-widest mb-4 uppercase">
          REQUESTED EXAMINATIONS <span class="text-red-500">*</span>
        </h3>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
          <label
            v-for="(test, index) in labTests"
            :key="index"
            class="flex items-center gap-3 p-3 rounded-lg border transition cursor-pointer"
            :class="formData.selectedTests.includes(test)
              ? 'border-indigo-500 bg-indigo-50/50 shadow-sm'
              : 'border-gray-200 bg-white hover:border-indigo-300 hover:bg-gray-50'"
          >
            <input
              type="checkbox"
              :value="test"
              v-model="formData.selectedTests"
              :disabled="isReadOnly"
              class="w-4 h-4 text-indigo-600 rounded border-gray-300 focus:ring-indigo-500"
            />
            <span class="text-xs font-semibold text-gray-800">{{ test }}</span>
          </label>
        </div>
      </div>

      <!-- Signatures / Physician Information -->
      <div class="grid grid-cols-2 gap-16 mt-8">
        <div class="text-center flex flex-col justify-end min-h-[4.5rem]">
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold uppercase">Patient's Signature / Date</p>
          </div>
        </div>
        <div class="text-center flex flex-col justify-end min-h-[4.5rem]">
          <p class="font-bold text-sm text-gray-900 uppercase">{{ clinicInfo.physician }}</p>
          <p class="text-xs text-gray-600">{{ clinicInfo.specialization }}</p>
          <p class="text-xs text-gray-500 mb-1">License no.: {{ clinicInfo.licenseNo }}</p>
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold uppercase">Attending Physician / Obstetrician-Gynecologist</p>
          </div>
        </div>
      </div>

    </fieldset>
    <!-- END Read-only wrapper -->

    <!-- Action Buttons Footer -->
    <div v-if="!isReadOnly" class="no-print mt-8 flex flex-wrap justify-end items-center gap-3">
      <button
        @click="resetForm"
        type="button"
        class="px-5 py-2.5 bg-gray-100 text-gray-700 hover:bg-gray-200 rounded-lg text-sm font-semibold transition"
      >
        Clear
      </button>

      <button
        @click="saveLaboratoryRequest"
        :disabled="isSubmitting"
        type="button"
        class="px-6 py-2.5 bg-indigo-600 text-white rounded-lg text-sm font-bold hover:bg-indigo-700 transition shadow disabled:opacity-50 flex items-center gap-2"
      >
        {{ isSubmitting ? 'Saving...' : 'SAVE & RETURN' }}
      </button>

      <button
        @click="handleProceedToPayment"
        type="button"
        class="px-6 py-2.5 bg-amber-600 text-white rounded-lg text-sm font-bold hover:bg-amber-700 transition shadow flex items-center gap-2"
      >
        PROCEED TO PAYMENT →
      </button>
    </div>

  </div>
</template>

<style scoped>
.input { width:100%; padding:6px 8px; border:1px solid #d1d5db; border-radius:6px; outline:none; font-size:13px; }
.input-line { display:block; width:100%; border:none; border-bottom:1px solid #94a3b8; outline:none; padding:2px; background:transparent; font-size:13px; }
.field-label { display:block; font-size:11px; font-weight:600; color:#6b7280; margin-bottom:2px; }
.section-title { font-weight:700; font-size:0.95rem; color:#1e293b; margin-bottom:8px; }

fieldset:disabled input,
fieldset:disabled select,
fieldset:disabled textarea {
  opacity: 1;
  color: inherit;
  cursor: default;
}
</style>

<style>
@page { size: landscape; margin: 12mm 12mm; }
@media print {
  * { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; }
  .no-print { display: none !important; }
  input[type="text"], input[type="date"], input[type="number"] { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; }
  select { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; -webkit-appearance:none; appearance:none; }
}
</style>
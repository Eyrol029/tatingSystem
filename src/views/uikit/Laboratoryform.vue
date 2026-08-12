<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

// API Endpoint for Laboratory Requests
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

function handlePrint() {
  window.print()
}

async function saveAndReturnToProfile() {
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
    statusMessage.value = 'Laboratory Request saved successfully!'
    isError.value = false

    if (patientId) {
      router.push(`/uikit/PatientProfiling/${patientId}`)
    } else {
      router.push('/uikit/PatientsMain')
    }
  } catch (error) {
    console.error('Failed to save laboratory request:', error)
    statusMessage.value = error.response?.data || 'Unable to save laboratory request to database.'
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

// BACKEND INTEGRATION: Save Laboratory Request to Spring Boot API
async function saveLaboratoryRequest() {
  await saveAndReturnToProfile()
}
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen flex justify-center items-start">
    
    <!-- CARD CONTAINER -->
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-2xl print-container">
      
      <!-- HEADER / CLINIC DETAILS -->
      <div class="text-center mb-6 pb-4 border-b border-gray-300">
        <h1 class="text-xl font-bold tracking-wide text-gray-800 uppercase">{{ clinicInfo.name }}</h1>
        <p class="text-xs text-gray-600 mt-1">{{ clinicInfo.address }}</p>
        <p class="text-xs text-gray-600">CONTACT NO.: {{ clinicInfo.contact }}</p>
        <h2 class="text-sm font-semibold text-gray-700 mt-3 tracking-wider uppercase">Laboratory Request Form</h2>
      </div>

      <!-- PATIENT DETAILS FORM -->
      <div class="space-y-4">
        
        <!-- Date & Name -->
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div class="sm:col-span-1">
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Date</label>
            <input
              v-model="formData.date"
              type="date"
              class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
          <div class="sm:col-span-2">
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">
              Name <span class="text-red-500">*</span>
            </label>
            <input
              v-model="formData.patientName"
              type="text"
              placeholder="Full Patient Name"
              class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
        </div>

        <!-- Address -->
        <div>
          <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Address</label>
          <input
            v-model="formData.address"
            type="text"
            placeholder="Complete Address"
            class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
          />
        </div>

        <!-- B-Date, Age, Sex -->
        <div class="grid grid-cols-3 gap-4">
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">B-Date</label>
            <input
              v-model="formData.birthDate"
              @change="onBirthDateChange"
              type="date"
              class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Age</label>
            <input
              v-model="formData.age"
              type="number"
              placeholder="Age"
              class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>
          <div>
            <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Sex</label>
            <select
              v-model="formData.sex"
              class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none bg-white"
            >
              <option value="" disabled>Select</option>
              <option value="Female">Female</option>
              <option value="Male">Male</option>
            </select>
          </div>
        </div>

        <!-- Diagnosis -->
        <div>
          <label class="block text-xs font-bold text-gray-700 uppercase mb-1">Diagnosis</label>
          <input
            v-model="formData.diagnosis"
            type="text"
            placeholder="Clinical Diagnosis / Notes"
            class="w-full border rounded px-3 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
          />
        </div>

      </div>

      <!-- LABORATORY TESTS CHECKBOX LIST -->
      <div class="mt-6 border-t pt-4">
        <h3 class="text-xs font-bold text-gray-700 uppercase mb-3">
          Requested Examinations <span class="text-red-500">*</span>:
        </h3>
        
        <div class="grid grid-cols-1 gap-2.5">
          <label
            v-for="(test, index) in labTests"
            :key="index"
            class="flex items-center gap-3 p-2 rounded hover:bg-gray-50 cursor-pointer border border-transparent hover:border-gray-200 transition-colors"
          >
            <input
              type="checkbox"
              :value="test"
              v-model="formData.selectedTests"
              class="w-4 h-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500"
            />
            <span class="text-sm font-medium text-gray-800">{{ test }}</span>
          </label>
        </div>
      </div>

      <!-- FOOTER / PHYSICIAN SIGNATURE -->
      <div class="mt-10 border-t pt-4 text-center">
        <p class="font-bold text-sm text-gray-900 uppercase">{{ clinicInfo.physician }}</p>
        <p class="text-xs text-gray-600">{{ clinicInfo.specialization }}</p>
        <p class="text-xs text-gray-500 mt-0.5">License no.: {{ clinicInfo.licenseNo }}</p>
      </div>

      <!-- STATUS MESSAGE (Hidden during Print) -->
      <div v-if="statusMessage" class="mt-4 no-print">
        <p :class="isError ? 'text-red-600 bg-red-50 border-red-200' : 'text-green-600 bg-green-50 border-green-200'" class="text-xs p-3 rounded border text-center font-medium">
          {{ statusMessage }}
        </p>
      </div>

      <!-- ACTION BUTTONS (Hidden during Print) -->
      <div class="mt-6 flex flex-wrap justify-end gap-3 no-print">
        <button
          @click="resetForm"
          type="button"
          class="px-4 py-2 bg-gray-200 text-gray-700 rounded text-sm hover:bg-gray-300 font-medium transition"
        >
          Clear
        </button>

        <button
          @click="saveLaboratoryRequest"
          :disabled="isSubmitting"
          type="button"
          class="px-5 py-2 bg-green-600 text-white rounded text-sm hover:bg-green-700 font-medium shadow transition flex items-center gap-2 disabled:opacity-50"
        >
          {{ isSubmitting ? 'Saving...' : 'Save & Return' }}
        </button>

        <button
          @click="handleProceedToPayment"
          type="button"
          class="px-5 py-2 bg-amber-600 text-white rounded text-sm hover:bg-amber-700 font-medium shadow transition flex items-center gap-2"
        >
          → PROCEED TO PAYMENT
        </button>

        <button
          @click="handlePrint"
          type="button"
          class="px-5 py-2 bg-purple-600 text-white rounded text-sm hover:bg-purple-700 font-medium shadow transition flex items-center gap-2"
        >
          🖨️ Print Request Slip
        </button>
      </div>

    </div>

  </div>
</template>

<style scoped>
/* Print stylesheet overrides */
@media print {
  .no-print {
    display: none !important;
  }
  body {
    background-color: white !important;
  }
  .print-container {
    box-shadow: none !important;
    border: none !important;
    padding: 0 !important;
    width: 100% !important;
    max-width: 100% !important;
  }
  input, select {
    border: none !important;
    border-bottom: 1px solid #000 !important;
    border-radius: 0 !important;
    padding-left: 0 !important;
  }
}
</style>
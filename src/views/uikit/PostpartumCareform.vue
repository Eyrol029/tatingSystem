<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useUserDataStore, UserRole } from '@/stores/userData'

const route = useRoute()
const router = useRouter()

// ── API Base Endpoints ───────────────────────────────────────────────────────
const BASE = 'http://localhost:8080/api/postpartum'
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services'
const EMPLOYEES_BASE = 'http://localhost:8080/api/employees'

// ── Route Params ─────────────────────────────────────────────────────────────
const patientID = route.params.patientID || route.params.clientId || route.params.id
  || route.query.patientId || route.query.patientID
const serviceId = route.params.serviceId || route.query.serviceId
const routePatientName = (route.query.patientName || '').toString().trim()

function applyPatientNameToForm(patientName) {
  const name = (patientName || '').trim()
  if (!name) return false

  const parts = name.split(/\s+/)
  form.value.firstName = parts[0] || ''
  form.value.middleName = parts.length > 2 ? parts.slice(1, -1).join(' ') : ''
  form.value.lastName = parts.length > 1 ? parts[parts.length - 1] : ''
  form.value.patientConformeName = name
  return true
}

// If this form was opened from the Admission delivery step, we hold a reference
// to the admission's patientID + serviceId so we can navigate back to billing.
const admissionPatientId = route.query.admissionPatientId || null
const admissionServiceId = route.query.admissionServiceId || null
const returnToBilling = route.query.returnToBilling === 'true'

// ── Check if 3rd & 4th visits are completed ─────────────────────────────────
const isVisitsCompleted = computed(() => {
  const v3 = form.value.visits[2]
  const v4 = form.value.visits[3]
  return Boolean(v3?.dateOfVisit && v4?.dateOfVisit)
})

// ── Read-Only State for Patients ──────────────────────────────────────────────
const userStore = useUserDataStore()
if (!userStore.user) {
  userStore.init()
}
const isReadOnly = computed(() => userStore.userRole === UserRole.PATIENT)

function goBack() { router.back() }
function printForm() { window.print() }

const submitStatus = ref({ loading: false, error: '', success: '' })
const existingRecordId = ref(null)
const employeesList = ref([])

// ── Form State (12 Sections matching Backend Entity) ─────────────────────────
const form = ref({
  panNumber: '',
  healthFacilityName: "Tating's Birthing Home and Family Planning Clinic",
  lastName: '',
  firstName: '',
  middleName: '',
  pin: '',
  houseNumberStreet: '',
  subdivisionBarangay: '',
  cityMunicipality: '',
  province: '',
  zipCode: '',
  gravida: '',
  paraTerm: '',
  paraPreterm: '',
  paraAbortion: '',
  paraLiving: '',
  dateOfDelivery: '',
  briefHistory: '',

  // 4 Antenatal / Postpartum Visits
  visits: Array.from({ length: 4 }, (_, i) => ({
    visitNumber: i + 1,
    dateOfVisit: '',
    bp: '',
    temperature: '',
    weight: '',
    tetanusImmunization: false,
    mms: false, mmsQty: '',
    iron: false, ironQty: '',
    folicAcid: false, folicAcidQty: '',
    calcium: false, calciumQty: '',
    counselingMentalHealth: false,
    counselingFamilyPlanning: false,
    counselingNutrition: false,
    lactationManagement: false
  })),

  referred: false,
  referredReason: '',
  attendingStaffID: null,
  attendingSignatureName: '',
  patientConformeName: '',
  dateSigned: todayLocalDateString(),

  // PART III: NEWBORN CARE
  // 1. Immediate Newborn Care (EINC)
  immediateDrying: false,
  skinToSkinContact: false,
  properCordClamping: false,
  earlyBreastfeedingInitiation: false,

  // 2. Routine Preventive Care
  eyeProphylaxis: false,
  vitaminKAdministered: false,
  hepatitisBVaccine: false,
  bcgVaccine: false,
  cordCareApplied: false,

  // 3. Physical Exam & Measurements
  birthWeight: '',
  birthLength: '',
  headCircumference: '',
  chestCircumference: '',
  apgarScore1Min: '',
  apgarScore5Min: '',
  gestationalAgeWeeks: '',

  // 4. Screening Tests
  newbornScreening: '',
  newbornHearingScreening: '',

  // 5. Newborn Care Notes
  newbornCareNotes: ''
})

function todayLocalDateString() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

function parseDateForForm(value) {
  if (!value) return ''
  const s = String(value).trim()
  const m = s.match(/^(\d{4})-(\d{2})-(\d{2})/)
  return m ? `${m[1]}-${m[2]}-${m[3]}` : ''
}

// ── Fetch Attending Staff / Employees ────────────────────────────────────────
async function fetchEmployees() {
  try {
    const res = await axios.get(EMPLOYEES_BASE)
    const raw = res.data
    employeesList.value = Array.isArray(raw)
      ? raw
      : (Array.isArray(raw?.data) ? raw.data
        : (Array.isArray(raw?.employees) ? raw.employees : []))
  } catch (error) {
    console.error('Failed to load employees list', error)
  }
}

const onAttendingStaffChange = () => {
  const staffID = form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null
  const staff = employeesList.value.find(e => Number(e.employeeID) === staffID)
  form.value.attendingSignatureName = staff
    ? `${staff.fName || ''} ${staff.lName || ''}`.trim()
    : ''
}

// ── Fetch Existing Record or Prefill Patient Details ─────────────────────────
async function loadExistingRecord() {
  if (!serviceId) return
  try {
    const res = await axios.get(BASE, { params: { serviceID: serviceId } })
    const records = Array.isArray(res.data) ? res.data : []

    if (records.length > 0) {
      // Pick the latest saved record for this service
      const latest = records[records.length - 1]
      existingRecordId.value = latest.postpartumRecordID

      form.value.panNumber = latest.panNumber || ''
      form.value.healthFacilityName = latest.healthFacilityName || form.value.healthFacilityName
      form.value.lastName = latest.lastName || ''
      form.value.firstName = latest.firstName || ''
      form.value.middleName = latest.middleName || ''
      form.value.pin = latest.pin || ''
      form.value.houseNumberStreet = latest.houseNumberStreet || ''
      form.value.subdivisionBarangay = latest.subdivisionBarangay || ''
      form.value.cityMunicipality = latest.cityMunicipality || ''
      form.value.province = latest.province || ''
      form.value.zipCode = latest.zipCode || ''
      form.value.gravida = latest.gravida != null ? String(latest.gravida) : ''
      form.value.paraTerm = latest.paraTerm != null ? String(latest.paraTerm) : ''
      form.value.paraPreterm = latest.paraPreterm != null ? String(latest.paraPreterm) : ''
      form.value.paraAbortion = latest.paraAbortion != null ? String(latest.paraAbortion) : ''
      form.value.paraLiving = latest.paraLiving != null ? String(latest.paraLiving) : ''
      form.value.dateOfDelivery = parseDateForForm(latest.dateOfDelivery)
      form.value.briefHistory = latest.briefHistory || ''
      form.value.referred = !!latest.referred
      form.value.referredReason = latest.referredReason || ''
      form.value.patientConformeName = latest.patientConformeName || ''
      form.value.dateSigned = parseDateForForm(latest.dateSigned) || form.value.dateSigned

      // Newborn Care fields
      form.value.immediateDrying = !!latest.immediateDrying
      form.value.skinToSkinContact = !!latest.skinToSkinContact
      form.value.properCordClamping = !!latest.properCordClamping
      form.value.earlyBreastfeedingInitiation = !!latest.earlyBreastfeedingInitiation

      form.value.eyeProphylaxis = !!latest.eyeProphylaxis
      form.value.vitaminKAdministered = !!latest.vitaminKAdministered
      form.value.hepatitisBVaccine = !!latest.hepatitisBVaccine
      form.value.bcgVaccine = !!latest.bcgVaccine
      form.value.cordCareApplied = !!latest.cordCareApplied

      form.value.birthWeight = latest.birthWeight || ''
      form.value.birthLength = latest.birthLength || ''
      form.value.headCircumference = latest.headCircumference || ''
      form.value.chestCircumference = latest.chestCircumference || ''
      form.value.apgarScore1Min = latest.apgarScore1Min || ''
      form.value.apgarScore5Min = latest.apgarScore5Min || ''
      form.value.gestationalAgeWeeks = latest.gestationalAgeWeeks || ''

      form.value.newbornScreening = latest.newbornScreening || ''
      form.value.newbornHearingScreening = latest.newbornHearingScreening || ''
      form.value.newbornCareNotes = latest.newbornCareNotes || ''

      if (latest.attendingStaffID != null) {
        form.value.attendingStaffID = Number(latest.attendingStaffID)
        onAttendingStaffChange()
      } else {
        form.value.attendingSignatureName = latest.attendingSignatureName || ''
      }

      if (Array.isArray(latest.visits) && latest.visits.length) {
        latest.visits.forEach((v, i) => {
          if (i < form.value.visits.length) {
            form.value.visits[i] = {
              visitNumber: i + 1,
              dateOfVisit: parseDateForForm(v.dateOfVisit),
              bp: v.bp || '',
              temperature: v.temperature || '',
              weight: v.weight || '',
              tetanusImmunization: !!v.tetanusImmunization,
              mms: !!v.mms, mmsQty: v.mmsQty != null ? String(v.mmsQty) : '',
              iron: !!v.iron, ironQty: v.ironQty != null ? String(v.ironQty) : '',
              folicAcid: !!v.folicAcid, folicAcidQty: v.folicAcidQty != null ? String(v.folicAcidQty) : '',
              calcium: !!v.calcium, calciumQty: v.calciumQty != null ? String(v.calciumQty) : '',
              counselingMentalHealth: !!v.counselingMentalHealth,
              counselingFamilyPlanning: !!v.counselingFamilyPlanning,
              counselingNutrition: !!v.counselingNutrition,
              lactationManagement: !!v.lactationManagement
            }
          }
        })
      }
      submitStatus.value.success = `Loaded Postpartum Care record #${latest.postpartumRecordID}`
    } else {
      // Prefill patient name from PatientService if this is a new form
      fetchPatientServiceDetails()
    }
  } catch (e) {
    console.error('Failed to load existing postpartum record', e)
  }
}

async function fetchPatientServiceDetails() {
  if (!serviceId) {
    if (routePatientName) {
      applyPatientNameToForm(routePatientName)
    }
    return
  }

  try {
    const res = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceId}`)
    const ps = res.data
    if (ps?.patientName) {
      applyPatientNameToForm(ps.patientName)
      return
    }

    if (routePatientName) {
      applyPatientNameToForm(routePatientName)
    }
  } catch (err) {
    console.error('Failed to fetch patient service info for prefill', err)
    if (routePatientName) {
      applyPatientNameToForm(routePatientName)
    }
  }
}

// ── Sync Attending Physician/Midwife to PatientService ──────────────────────
async function syncEmployeeNameToPatientService() {
  if (!form.value.attendingSignatureName || !serviceId) return
  try {
    const currentRes = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceId}`)
    const currentService = currentRes.data
    await axios.put(PATIENT_SERVICE_BASE, {
      ...currentService,
      employeeName: form.value.attendingSignatureName
    })
  } catch (syncErr) {
    console.error('Failed to sync employee name to PatientService', syncErr)
  }
}

// ── Submit / Save Form ───────────────────────────────────────────────────────
async function submitForm() {
  if (isReadOnly.value) return

  submitStatus.value.loading = true
  submitStatus.value.error = ''
  submitStatus.value.success = ''

  if (!serviceId) {
    submitStatus.value.error = 'Service ID missing. Please go back and select a patient service.'
    submitStatus.value.loading = false
    return
  }

  const payload = {
    serviceID: Number(serviceId),
    panNumber: form.value.panNumber || null,
    healthFacilityName: form.value.healthFacilityName || null,
    lastName: form.value.lastName || null,
    firstName: form.value.firstName || null,
    middleName: form.value.middleName || null,
    pin: form.value.pin || null,
    houseNumberStreet: form.value.houseNumberStreet || null,
    subdivisionBarangay: form.value.subdivisionBarangay || null,
    cityMunicipality: form.value.cityMunicipality || null,
    province: form.value.province || null,
    zipCode: form.value.zipCode || null,
    gravida: form.value.gravida !== '' ? Number(form.value.gravida) : null,
    paraTerm: form.value.paraTerm !== '' ? Number(form.value.paraTerm) : null,
    paraPreterm: form.value.paraPreterm !== '' ? Number(form.value.paraPreterm) : null,
    paraAbortion: form.value.paraAbortion !== '' ? Number(form.value.paraAbortion) : null,
    paraLiving: form.value.paraLiving !== '' ? Number(form.value.paraLiving) : null,
    dateOfDelivery: form.value.dateOfDelivery || null,
    briefHistory: form.value.briefHistory || null,
    referred: form.value.referred,
    referredReason: form.value.referredReason || null,
    attendingStaffID: form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null,
    attendingSignatureName: form.value.attendingSignatureName || null,
    patientConformeName: form.value.patientConformeName || null,
    dateSigned: form.value.dateSigned || null,

    // PART III: NEWBORN CARE
    immediateDrying: form.value.immediateDrying,
    skinToSkinContact: form.value.skinToSkinContact,
    properCordClamping: form.value.properCordClamping,
    earlyBreastfeedingInitiation: form.value.earlyBreastfeedingInitiation,

    eyeProphylaxis: form.value.eyeProphylaxis,
    vitaminKAdministered: form.value.vitaminKAdministered,
    hepatitisBVaccine: form.value.hepatitisBVaccine,
    bcgVaccine: form.value.bcgVaccine,
    cordCareApplied: form.value.cordCareApplied,

    birthWeight: form.value.birthWeight || null,
    birthLength: form.value.birthLength || null,
    headCircumference: form.value.headCircumference || null,
    chestCircumference: form.value.chestCircumference || null,
    apgarScore1Min: form.value.apgarScore1Min || null,
    apgarScore5Min: form.value.apgarScore5Min || null,
    gestationalAgeWeeks: form.value.gestationalAgeWeeks || null,

    newbornScreening: form.value.newbornScreening || null,
    newbornHearingScreening: form.value.newbornHearingScreening || null,
    newbornCareNotes: form.value.newbornCareNotes || null,

    visits: form.value.visits.map((v, i) => ({
      visitNumber: i + 1,
      dateOfVisit: v.dateOfVisit || null,
      bp: v.bp || null,
      temperature: v.temperature || null,
      weight: v.weight || null,
      tetanusImmunization: v.tetanusImmunization,
      mms: v.mms, mmsQty: v.mmsQty !== '' ? Number(v.mmsQty) : null,
      iron: v.iron, ironQty: v.ironQty !== '' ? Number(v.ironQty) : null,
      folicAcid: v.folicAcid, folicAcidQty: v.folicAcidQty !== '' ? Number(v.folicAcidQty) : null,
      calcium: v.calcium, calciumQty: v.calciumQty !== '' ? Number(v.calciumQty) : null,
      counselingMentalHealth: v.counselingMentalHealth,
      counselingFamilyPlanning: v.counselingFamilyPlanning,
      counselingNutrition: v.counselingNutrition,
      lactationManagement: v.lactationManagement
    }))
  }

  try {
    if (existingRecordId.value) {
      await axios.put(BASE, { ...payload, postpartumRecordID: existingRecordId.value })
      submitStatus.value.success = '✅ Postpartum Care record updated successfully!'
    } else {
      const res = await axios.post(BASE, payload)
      existingRecordId.value = res.data.postpartumRecordID
      submitStatus.value.success = '✅ Postpartum Care record saved successfully!'
    }

    await syncEmployeeNameToPatientService()

    // If this form was opened from the Admission delivery flow, redirect back
    // to the Admission page if 3rd and 4th visits are completed.
    if (returnToBilling && admissionServiceId) {
      if (isVisitsCompleted.value) {
        submitStatus.value.success = '✅ Postpartum Care record saved! 3rd & 4th visits completed — proceeding to Billing...'
        setTimeout(() => {
          router.push({
            path: `/uikit/Admission/${admissionPatientId}/${admissionServiceId}`,
            query: { fromPostpartum: 'true' }
          })
        }, 800)
        return
      } else {
        submitStatus.value.success = '✅ Postpartum Care record saved successfully! (Note: 3rd and 4th visits must be completed before proceeding to Billing/Payment).'
      }
    }

  } catch (error) {
    const msg = error?.response?.data?.message || error?.response?.data || error?.message || 'Unknown error'
    submitStatus.value.error = '❌ Save failed: ' + msg
    console.error('Postpartum Care save error:', error)
  } finally {
    submitStatus.value.loading = false
  }
}

onMounted(async () => {
  if (routePatientName) {
    applyPatientNameToForm(routePatientName)
  }
  await fetchEmployees()
  await loadExistingRecord()
})
</script>

<template>
  <!-- Action Toolbar -->
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

  <!-- Printable Container -->
  <div id="printable-area"
    class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <div class="flex items-center justify-center gap-3 mb-2">
        <img src="/static/TATING (2).png" alt="Tating Logo"
          style="height: 3.5rem; width: auto; object-fit: contain;" />
        <div class="text-left">
          <p class="text-xs text-gray-500 leading-tight">Tating's Birthing Home and Family Planning Clinic</p>
          <p class="text-xs text-gray-400 leading-tight">Purok 26, Poblacion, Manticao, Misamis Oriental</p>
          <h1 class="text-lg font-bold tracking-wide uppercase leading-tight mt-1">Postpartum Care Record</h1>
        </div>
      </div>
    </div>

    <!-- Status Messages -->
    <div class="no-print mb-4">
      <div v-if="submitStatus.error"
        class="text-sm text-red-700 bg-red-100 border border-red-200 rounded p-3 mb-2">
        {{ submitStatus.error }}
      </div>
      <div v-if="submitStatus.success"
        class="text-sm text-green-700 bg-green-100 border border-green-200 rounded p-3 mb-2">
        {{ submitStatus.success }}
      </div>
    </div>

    <!-- READ-ONLY WRAPPER -->
    <fieldset :disabled="isReadOnly" style="border:none;padding:0;margin:0;min-width:0;">

      <!-- PART I: PATIENT INFORMATION -->
      <div class="section-header">PART I. PATIENT (MOTHER) INFORMATION</div>

      <div class="grid grid-cols-2 gap-4 mb-4">
        <div>
          <label class="field-label">PhilHealth Accreditation Number (PAN)</label>
          <input type="text" v-model="form.panNumber" class="input" placeholder="e.g. 00-000000000-0" />
        </div>
        <div>
          <label class="field-label">PhilHealth Identification Number (PIN)</label>
          <input type="text" v-model="form.pin" class="input" placeholder="e.g. 00-000000000-0" />
        </div>
      </div>

      <div class="mb-4">
        <label class="field-label">1. Health Facility Name</label>
        <input type="text" v-model="form.healthFacilityName" class="input" />
      </div>

      <div class="mb-1 font-medium text-sm">2. Name of Patient:</div>
      <div class="grid grid-cols-3 gap-4 mb-4">
        <div>
          <label class="field-label">Last Name</label>
          <input type="text" v-model="form.lastName" class="input" />
        </div>
        <div>
          <label class="field-label">First Name</label>
          <input type="text" v-model="form.firstName" class="input" />
        </div>
        <div>
          <label class="field-label">Middle Name</label>
          <input type="text" v-model="form.middleName" class="input" />
        </div>
      </div>

      <div class="mb-1 font-medium text-sm">4. Address:</div>
      <div class="grid grid-cols-5 gap-3 mb-4">
        <div>
          <label class="field-label">House No./Street</label>
          <input type="text" v-model="form.houseNumberStreet" class="input" />
        </div>
        <div>
          <label class="field-label">Subdivision/Barangay</label>
          <input type="text" v-model="form.subdivisionBarangay" class="input" />
        </div>
        <div>
          <label class="field-label">City/Municipality</label>
          <input type="text" v-model="form.cityMunicipality" class="input" />
        </div>
        <div>
          <label class="field-label">Province</label>
          <input type="text" v-model="form.province" class="input" />
        </div>
        <div>
          <label class="field-label">ZIP Code</label>
          <input type="text" v-model="form.zipCode" class="input" />
        </div>
      </div>

      <div class="grid grid-cols-6 gap-3 mb-4 items-end">
        <div>
          <label class="field-label">5. Gravida (G)</label>
          <input type="number" min="0" v-model="form.gravida" class="input-line w-full text-center" />
        </div>
        <div>
          <label class="field-label">Para — Term</label>
          <input type="number" min="0" v-model="form.paraTerm" class="input-line w-full text-center" />
        </div>
        <div>
          <label class="field-label">Para — Preterm</label>
          <input type="number" min="0" v-model="form.paraPreterm" class="input-line w-full text-center" />
        </div>
        <div>
          <label class="field-label">Para — Abortion</label>
          <input type="number" min="0" v-model="form.paraAbortion" class="input-line w-full text-center" />
        </div>
        <div>
          <label class="field-label">Para — Living</label>
          <input type="number" min="0" v-model="form.paraLiving" class="input-line w-full text-center" />
        </div>
        <div>
          <label class="field-label">Date of Delivery (DOD)</label>
          <input type="date" v-model="form.dateOfDelivery" class="input" />
        </div>
      </div>

      <div class="mb-6">
        <label class="field-label">6. Brief History of Present Illness / OB History</label>
        <textarea v-model="form.briefHistory" rows="3"
          class="w-full border border-gray-300 rounded p-2 text-sm"
          placeholder="Enter brief history or OB history..."></textarea>
      </div>

      <!-- PART II: PHYSICAL EXAMINATION -->
      <div class="section-header">PART II. PHYSICAL EXAMINATION</div>
      <p class="text-xs text-gray-500 mb-2 font-semibold">7. General Survey (Pertinent Findings Per System)</p>

      <div class="overflow-x-auto mb-6">
        <table class="w-full border-collapse text-xs visit-table">
          <thead>
            <tr class="bg-gray-100">
              <th class="td w-32 text-left">Antenatal Care No.</th>
              <th class="td" v-for="(v, i) in form.visits" :key="'h'+i">{{ i + 1 }}{{ ['st','nd','rd','th'][i] }}</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="td font-medium">Date of visit (mm/dd/yy)</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'d'+i">
                <input type="date" v-model="v.dateOfVisit" class="input-table" />
              </td>
            </tr>
            <tr>
              <td class="td font-medium">BP</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'bp'+i">
                <input type="text" v-model="v.bp" placeholder="e.g. 120/80" class="input-table" />
              </td>
            </tr>
            <tr>
              <td class="td font-medium">Temperature</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'t'+i">
                <input type="text" v-model="v.temperature" class="input-table" />
              </td>
            </tr>
            <tr>
              <td class="td font-medium">Weight</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'w'+i">
                <input type="text" v-model="v.weight" class="input-table" />
              </td>
            </tr>

            <tr class="bg-gray-50">
              <td class="td font-bold" colspan="5">8. Services Provided</td>
            </tr>

            <tr>
              <td class="td">Tetanus Immunization</td>
              <td class="td text-center" v-for="(v, i) in form.visits" :key="'tet'+i">
                <input type="checkbox" v-model="v.tetanusImmunization" class="w-4 h-4" />
              </td>
            </tr>
            <tr>
              <td class="td">MMS</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'mms'+i">
                <label class="flex items-center gap-1">
                  <input type="checkbox" v-model="v.mms" class="w-4 h-4" />
                  <span class="text-[10px]">Qty:</span>
                  <input type="text" v-model="v.mmsQty" class="input-table flex-1" />
                </label>
              </td>
            </tr>
            <tr>
              <td class="td">Iron</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'iron'+i">
                <label class="flex items-center gap-1">
                  <input type="checkbox" v-model="v.iron" class="w-4 h-4" />
                  <span class="text-[10px]">Qty:</span>
                  <input type="text" v-model="v.ironQty" class="input-table flex-1" />
                </label>
              </td>
            </tr>
            <tr>
              <td class="td">Folic Acid</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'fa'+i">
                <label class="flex items-center gap-1">
                  <input type="checkbox" v-model="v.folicAcid" class="w-4 h-4" />
                  <span class="text-[10px]">Qty:</span>
                  <input type="text" v-model="v.folicAcidQty" class="input-table flex-1" />
                </label>
              </td>
            </tr>
            <tr>
              <td class="td">Calcium</td>
              <td class="td" v-for="(v, i) in form.visits" :key="'ca'+i">
                <label class="flex items-center gap-1">
                  <input type="checkbox" v-model="v.calcium" class="w-4 h-4" />
                  <span class="text-[10px]">Qty:</span>
                  <input type="text" v-model="v.calciumQty" class="input-table flex-1" />
                </label>
              </td>
            </tr>
            <tr>
              <td class="td">Counseling: Mental Health</td>
              <td class="td text-center" v-for="(v, i) in form.visits" :key="'mh'+i">
                <input type="checkbox" v-model="v.counselingMentalHealth" class="w-4 h-4" />
              </td>
            </tr>
            <tr>
              <td class="td">Family Planning</td>
              <td class="td text-center" v-for="(v, i) in form.visits" :key="'fp'+i">
                <input type="checkbox" v-model="v.counselingFamilyPlanning" class="w-4 h-4" />
              </td>
            </tr>
            <tr>
              <td class="td">Nutrition Counseling</td>
              <td class="td text-center" v-for="(v, i) in form.visits" :key="'nut'+i">
                <input type="checkbox" v-model="v.counselingNutrition" class="w-4 h-4" />
              </td>
            </tr>
            <tr>
              <td class="td">Lactation Management</td>
              <td class="td text-center" v-for="(v, i) in form.visits" :key="'lac'+i">
                <input type="checkbox" v-model="v.lactationManagement" class="w-4 h-4" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- PART III: NEWBORN CARE -->
      <div class="section-header">PART III. NEWBORN CARE</div>

      <!-- 1. Immediate Newborn Care (EINC) -->
      <div class="mb-4 bg-purple-50/60 border border-purple-200 rounded-lg p-4">
        <h4 class="font-bold text-sm text-purple-900 mb-3 flex items-center gap-2">
          <span>👶</span> 1. Immediate Newborn Care (EINC Checklist)
        </h4>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-purple-100 hover:bg-purple-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.immediateDrying" class="w-4 h-4 text-purple-600 rounded" />
            <span>Immediate and thorough drying</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-purple-100 hover:bg-purple-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.skinToSkinContact" class="w-4 h-4 text-purple-600 rounded" />
            <span>Skin-to-skin contact (Uninterrupted)</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-purple-100 hover:bg-purple-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.properCordClamping" class="w-4 h-4 text-purple-600 rounded" />
            <span>Proper cord clamping and cutting (Delayed 1-3 mins)</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-purple-100 hover:bg-purple-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.earlyBreastfeedingInitiation" class="w-4 h-4 text-purple-600 rounded" />
            <span>Early breastfeeding initiation (Non-separation)</span>
          </label>
        </div>
      </div>

      <!-- 2. Routine Preventive Care -->
      <div class="mb-4 bg-teal-50/60 border border-teal-200 rounded-lg p-4">
        <h4 class="font-bold text-sm text-teal-900 mb-3 flex items-center gap-2">
          <span>💉</span> 2. Routine Preventive Care
        </h4>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-teal-100 hover:bg-teal-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.eyeProphylaxis" class="w-4 h-4 text-teal-600 rounded" />
            <span>Eye Prophylaxis: Erythromycin / Tetracycline</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-teal-100 hover:bg-teal-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.vitaminKAdministered" class="w-4 h-4 text-teal-600 rounded" />
            <span>Vitamin K: Administered (1 mg IM)</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-teal-100 hover:bg-teal-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.hepatitisBVaccine" class="w-4 h-4 text-teal-600 rounded" />
            <span>Hepatitis B Vaccine: Birth dose (IM)</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-teal-100 hover:bg-teal-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.bcgVaccine" class="w-4 h-4 text-teal-600 rounded" />
            <span>BCG Vaccine: Birth dose (ID)</span>
          </label>
          <label class="flex items-center gap-2 bg-white p-2.5 rounded border border-teal-100 hover:bg-teal-50 cursor-pointer transition text-xs font-medium text-gray-800">
            <input type="checkbox" v-model="form.cordCareApplied" class="w-4 h-4 text-teal-600 rounded" />
            <span>Cord Care: Clean, dry cord care applied</span>
          </label>
        </div>
      </div>

      <!-- 3. Physical Exam & Measurements -->
      <div class="mb-4 bg-blue-50/60 border border-blue-200 rounded-lg p-4">
        <h4 class="font-bold text-sm text-blue-900 mb-3 flex items-center gap-2">
          <span>📏</span> 3. Physical Exam & Measurements
        </h4>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div>
            <label class="field-label">Birth Weight</label>
            <input type="text" v-model="form.birthWeight" class="input bg-white" placeholder="e.g. 3,200 g / 3.2 kg" />
          </div>
          <div>
            <label class="field-label">Birth Length (cm)</label>
            <input type="text" v-model="form.birthLength" class="input bg-white" placeholder="e.g. 50 cm" />
          </div>
          <div>
            <label class="field-label">Head Circumference (cm)</label>
            <input type="text" v-model="form.headCircumference" class="input bg-white" placeholder="e.g. 34 cm" />
          </div>
          <div>
            <label class="field-label">Chest Circumference (cm)</label>
            <input type="text" v-model="form.chestCircumference" class="input bg-white" placeholder="e.g. 33 cm" />
          </div>
          <div>
            <label class="field-label">APGAR Score (1 min)</label>
            <input type="text" v-model="form.apgarScore1Min" class="input bg-white" placeholder="e.g. 8/10" />
          </div>
          <div>
            <label class="field-label">APGAR Score (5 mins)</label>
            <input type="text" v-model="form.apgarScore5Min" class="input bg-white" placeholder="e.g. 9/10" />
          </div>
          <div>
            <label class="field-label">Gestational Age (weeks)</label>
            <input type="text" v-model="form.gestationalAgeWeeks" class="input bg-white" placeholder="e.g. 38 weeks" />
          </div>
        </div>
      </div>

      <!-- 4. Screening Tests -->
      <div class="mb-4 bg-amber-50/60 border border-amber-200 rounded-lg p-4">
        <h4 class="font-bold text-sm text-amber-900 mb-3 flex items-center gap-2">
          <span>🔬</span> 4. Screening Tests
        </h4>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="field-label font-semibold text-gray-800">Newborn Screening (NBS) — Filter Paper Blood Spot</label>
            <input type="text" v-model="form.newbornScreening" class="input bg-white" placeholder="e.g. Done / Blood spot collected / Referred" />
          </div>
          <div>
            <label class="field-label font-semibold text-gray-800">Newborn Hearing Screening Test (NHST)</label>
            <input type="text" v-model="form.newbornHearingScreening" class="input bg-white" placeholder="e.g. Done (Pass) / Referred" />
          </div>
        </div>
      </div>

      <!-- 5. Newborn Care Notes -->
      <div class="mb-6 bg-gray-50 border border-gray-200 rounded-lg p-4">
        <label class="field-label text-gray-800 font-bold text-sm mb-2 flex items-center gap-2">
          <span>📝</span> 5. Newborn Care Notes / Clinical Observations
        </label>
        <textarea v-model="form.newbornCareNotes" rows="3"
          class="w-full border border-gray-300 rounded p-2 text-sm bg-white focus:ring-2 focus:ring-purple-500 focus:outline-none"
          placeholder="Enter newborn care notes, clinical observations, feeding status, instructions given to parents..."></textarea>
      </div>

      <!-- Section 9: Referred -->
      <div class="border border-gray-200 rounded p-4 mb-6">
        <label class="flex items-center gap-2 font-medium mb-2">
          <input type="checkbox" v-model="form.referred" class="w-4 h-4" />
          9. Referred
        </label>
        <div v-if="form.referred">
          <label class="field-label">Reason</label>
          <input type="text" v-model="form.referredReason" class="input" />
        </div>
      </div>

      <!-- Section 10: Certification -->
      <div class="border-l-4 border-indigo-500 bg-indigo-50 p-4 mb-6 rounded-r">
        <p class="font-bold mb-2">10. Certification of Attending Physician/Midwife:</p>
        <p class="text-sm text-gray-700 mb-4">
          I certify that the above information given in this form are true and correct.
        </p>
        <label class="field-label">Attending Physician/Midwife</label>
        <select v-model="form.attendingStaffID" @change="onAttendingStaffChange" class="input">
          <option :value="null">-- Select Attending Staff --</option>
          <option v-for="emp in employeesList" :key="emp.employeeID" :value="Number(emp.employeeID)">
            {{ emp.fName }} {{ emp.lName }}
          </option>
        </select>
      </div>

      <!-- Signatures -->
      <div class="grid grid-cols-2 gap-16 mt-10 mb-6">
        <div class="text-center flex flex-col justify-end min-h-[4rem]">
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold">{{ form.attendingSignatureName || 'Signature' }}</p>
            <p class="text-[10px] text-gray-500">Signature / Printed Name</p>
          </div>
        </div>
        <div class="text-center flex flex-col justify-end min-h-[4rem]">
          <input type="text" v-model="form.patientConformeName" placeholder="Patient's printed name"
            class="input-line text-center w-full mb-1" />
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold">11. Patient's Conforme — Signature / Printed Name</p>
          </div>
        </div>
      </div>

      <div class="mb-6">
        <label class="field-label">12. Date Signed</label>
        <input type="date" v-model="form.dateSigned" class="input w-64" />
      </div>

    </fieldset>

    <!-- Save Button -->
    <div v-if="!isReadOnly" class="no-print mt-6">
      <button @click="submitForm" :disabled="submitStatus.loading"
        class="w-full bg-indigo-600 text-white py-3 rounded-lg font-bold hover:bg-indigo-700 transition shadow disabled:opacity-50 disabled:cursor-not-allowed">
        {{ submitStatus.loading ? 'Saving...' : (existingRecordId ? 'UPDATE POSTPARTUM CARE RECORD' : 'SAVE POSTPARTUM CARE RECORD') }}
      </button>
    </div>

  </div>
</template>

<style scoped>
.input { width:100%; padding:6px 8px; border:1px solid #d1d5db; border-radius:6px; outline:none; font-size:13px; }
.input-line { display:block; width:100%; border:none; border-bottom:1px solid #94a3b8; outline:none; padding:2px; background:transparent; font-size:13px; }
.input-table { width:100%; padding:2px; border:none; text-align:center; font-size:11px; background:transparent; }
.field-label { display:block; font-size:11px; font-weight:600; color:#6b7280; margin-bottom:2px; }
.section-header { background:#8e4f70; color:white; font-weight:700; font-size:0.8rem; letter-spacing:0.05em; text-transform:uppercase; padding:8px 12px; border-radius:4px; margin-bottom:12px; margin-top:16px; }
.td { border:1px solid #e5e7eb; padding:4px 6px; vertical-align:middle; }
.visit-table th { padding:6px; }

fieldset:disabled input,
fieldset:disabled select,
fieldset:disabled textarea {
  opacity: 1;
  color: inherit;
  cursor: default;
}
</style>

<style>
@page { size: A4 portrait; margin: 12mm 12mm; }
@media print {
  * { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; }
  .no-print { display: none !important; }
  input[type="text"], input[type="date"], input[type="number"] { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; }
  select { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; -webkit-appearance:none; appearance:none; }
  textarea { border:1px solid #555 !important; background:transparent !important; resize:none !important; }
  table { border-collapse:collapse !important; }
  th, td { border:1px solid #333 !important; }
}
</style>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useUserDataStore, UserRole } from '@/stores/userData'

const route = useRoute()
const router = useRouter()
const BASE = 'http://localhost:8080/api/ultrasound'
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services'

// ── Route params — matches the same convention as PrenatalAdmission /
// FamilyPlanningAdmission: patient ID + the specific patient-service record ID.
const patientID = route.params.patientID || route.params.clientId
const serviceId = route.params.serviceId

// ── Read-only mode for Patient accounts ──────────────────────────────────────
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
const form = ref({
  ultrasoundDate: new Date().toISOString().split('T')[0],
  gravida: '',
  term: '',
  preterm: '',
  abortion: '',
  livingChildren: '',
  attendingStaffID: null,
  attendingMidwife: '',
  remarksNotes: ''
})

// ── Load the employees/staff list for the Attending Staff dropdown ──────────
// Defensive: normalizes a few common response shapes (raw array, {data:[...]},
// {employees:[...]}) and surfaces a visible error instead of only logging it,
// so an empty dropdown is easy to diagnose from the UI.
async function fetchEmployees() {
  try {
    const res = await axios.get('http://localhost:8080/api/employees')
    const raw = res.data
    employeesList.value = Array.isArray(raw)
      ? raw
      : (Array.isArray(raw?.data) ? raw.data
        : (Array.isArray(raw?.employees) ? raw.employees : []))

    if (!employeesList.value.length) {
      console.warn('Employees list loaded but is empty. Raw response:', raw)
    }
  } catch (error) {
    console.error('Failed to load employees list', error)
    submitStatus.value.error = '⚠️ Could not load staff list: ' + (error?.response?.status || error.message)
  }
}

const onAttendingStaffChange = () => {
  const staffID = form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null
  const staff = employeesList.value.find(
    e => Number(e.employeeID) === staffID
  )

  form.value.attendingMidwife = staff
    ? `${staff.fName || ''} ${staff.lName || ''}`.trim()
    : ''
}

// ── Ultrasound finding tags — same picker as the Prenatal form.
// Since UltrasoundRecord only has a single `remarks` TEXT column (no
// dedicated tags table), the selected tags are folded into that field
// alongside any free-text notes.
const HIGH_RISK_US_TAGS = ['Placenta Previa', 'Oligohydramnios', 'Fetal Anomaly', 'IUGR']
const ALL_US_TAGS = ['Normal', 'Placenta Previa', 'Oligohydramnios', 'Polyhydramnios', 'Fetal Anomaly', 'IUGR', 'Breech Presentation', 'Multiple Gestation']
const ultrasoundTags = ref([])

function toggleUltrasoundTag(tag) {
  if (isReadOnly.value) return
  const idx = ultrasoundTags.value.indexOf(tag)
  if (idx === -1) { ultrasoundTags.value.push(tag) }
  else { ultrasoundTags.value.splice(idx, 1) }
}

const ultrasoundHighRisk = computed(() =>
  ultrasoundTags.value.some(t => HIGH_RISK_US_TAGS.includes(t))
)

// Combines tags + free notes into the single `remarks` field the backend expects
function buildRemarks() {
  const tagsPart = ultrasoundTags.value.length ? ultrasoundTags.value.join(', ') : ''
  const notesPart = form.value.remarksNotes ? form.value.remarksNotes.trim() : ''
  if (tagsPart && notesPart) return `${tagsPart} | ${notesPart}`
  return tagsPart || notesPart || ''
}

// Splits a saved `remarks` string back into tags + free notes for editing
function parseRemarks(remarksString) {
  if (!remarksString) return
  const [tagsPart, ...rest] = remarksString.split('|')
  const notesPart = rest.join('|').trim()

  const possibleTags = tagsPart.split(',').map(t => t.trim()).filter(Boolean)
  const matchedTags = possibleTags.filter(t => ALL_US_TAGS.includes(t))

  if (matchedTags.length) {
    ultrasoundTags.value = matchedTags
    form.value.remarksNotes = notesPart
  } else {
    // Nothing matched a known tag — treat the whole string as free notes
    ultrasoundTags.value = []
    form.value.remarksNotes = remarksString
  }
}

// ── Load existing record for this service, if any ───────────────────────────
async function loadExistingUltrasound() {
  if (!serviceId) return
  try {
    const res = await axios.get(BASE)
    const records = Array.isArray(res.data) ? res.data : []
    const matches = records.filter(r => String(r.serviceID) === String(serviceId))
    if (!matches.length) return

    const latest = matches.reduce((latest, current) =>
      (!latest || current.ultrasoundRecordID > latest.ultrasoundRecordID) ? current : latest, null)

    existingRecordId.value = latest.ultrasoundRecordID
    form.value.ultrasoundDate = latest.ultrasoundDate || new Date().toISOString().split('T')[0]
    form.value.gravida        = latest.gravida != null ? String(latest.gravida) : ''
    form.value.term           = latest.term != null ? String(latest.term) : ''
    form.value.preterm        = latest.preterm != null ? String(latest.preterm) : ''
    form.value.abortion       = latest.abortion != null ? String(latest.abortion) : ''
    form.value.livingChildren = latest.living != null ? String(latest.living) : ''
    parseRemarks(latest.remarks)

    // Restore attending staff selection, matching against the loaded employee list.
    // Coerce to Number on both sides — the API may return attendingStaffID (and/or
    // employeeID) as a string, which would otherwise fail the select's v-model match.
    if (latest.attendingStaffID != null && latest.attendingStaffID !== '') {
      const staffID = Number(latest.attendingStaffID)
      form.value.attendingStaffID = staffID
      const staff = employeesList.value.find(e => Number(e.employeeID) === staffID)
      form.value.attendingMidwife = staff ? `${staff.fName || ''} ${staff.lName || ''}`.trim() : (latest.attendingMidwife || '')
    } else {
      form.value.attendingStaffID = null
      form.value.attendingMidwife = latest.attendingMidwife || ''
    }

    submitStatus.value.success = `Viewing Ultrasound record #${latest.ultrasoundRecordID}`
  } catch (e) {
    console.error('Failed to load existing ultrasound record', e)
  }
}

// ── Sync the assigned employee name back onto PatientService so it displays
// correctly in the "Services Availed" tables (patient profile + patient's own
// "My Services" view). PUT /api/patient-services has no /{id} path — it
// requires the FULL PatientService object in the body — so we fetch the
// current record first, merge in the new employeeName, then PUT it back.
async function syncEmployeeNameToPatientService() {
  if (!form.value.attendingMidwife || !serviceId) return
  try {
    const currentRes = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceId}`)
    const currentService = currentRes.data
    await axios.put(PATIENT_SERVICE_BASE, {
      ...currentService,
      employeeName: form.value.attendingMidwife
    })
  } catch (syncErr) {
    console.error('Failed to sync employee name to PatientService', syncErr)
  }
}

// ── Save (create or update) ──────────────────────────────────────────────────
async function submitForm() {
  if (isReadOnly.value) return

  submitStatus.value.loading = true
  submitStatus.value.error   = ''
  submitStatus.value.success = ''

  if (!serviceId) {
    submitStatus.value.error = 'Service ID not found. Please go back and try again.'
    submitStatus.value.loading = false
    return
  }

  const payload = {
    serviceID: Number(serviceId),
    ultrasoundDate: form.value.ultrasoundDate || null,
    gravida:  form.value.gravida  !== '' ? Number(form.value.gravida)  : null,
    term:     form.value.term     !== '' ? Number(form.value.term)     : null,
    preterm:  form.value.preterm  !== '' ? Number(form.value.preterm)  : null,
    abortion: form.value.abortion !== '' ? Number(form.value.abortion) : null,
    living:   form.value.livingChildren !== '' ? Number(form.value.livingChildren) : null,
    remarks:  buildRemarks() || null,
    attendingStaffID: form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null,
    attendingMidwife: form.value.attendingMidwife || null
  }

  try {
    if (existingRecordId.value) {
      await axios.put(BASE, { ...payload, ultrasoundRecordID: existingRecordId.value })
      submitStatus.value.success = '✅ Ultrasound record updated successfully!'
    } else {
      const res = await axios.post(BASE, payload)
      existingRecordId.value = res.data.ultrasoundRecordID
      submitStatus.value.success = '✅ Ultrasound record saved successfully!'
    }

    await syncEmployeeNameToPatientService()

  } catch (error) {
    const msg = error?.response?.data?.message || error?.response?.data || error?.message || 'Unknown error'
    submitStatus.value.error = '❌ Save failed: ' + msg
    console.error('Ultrasound save error:', error)
  } finally {
    submitStatus.value.loading = false
  }
}

onMounted(async () => {
  await fetchEmployees()
  await loadExistingUltrasound()
})
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

  <!-- Printable form -->
  <div id="printable-area"
    class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <div class="flex items-center justify-center gap-3 mb-2">
        <img src="/static/TATING (2).png" alt="Tating Logo"
          style="height: 3.5rem; width: auto; object-fit: contain;" />
        <div class="text-left">
          <p class="text-xs text-gray-500 uppercase tracking-widest leading-tight">Tating's Birthing Home</p>
          <h1 class="text-xl font-bold tracking-wide uppercase leading-tight">Ultrasound Record Form</h1>
          <p class="text-xs text-gray-400 leading-tight">Maternal Health Record</p>
        </div>
      </div>
    </div>

    <!-- HIGH-RISK BANNER -->
    <div v-if="ultrasoundHighRisk" class="no-print mb-4 rounded-lg border-2 border-red-500 bg-red-50 p-4">
      <div class="flex items-center gap-2">
        <span class="text-2xl">🚨</span>
        <span class="font-bold text-red-700 text-lg">HIGH-RISK FINDING(S) SELECTED</span>
      </div>
      <p class="text-sm text-red-700 mt-1">
        {{ ultrasoundTags.filter(t => HIGH_RISK_US_TAGS.includes(t)).join(', ') }}
      </p>
    </div>

    <!-- Status messages -->
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

    <!-- READ-ONLY WRAPPER — locks every field for Patient accounts -->
    <fieldset :disabled="isReadOnly" style="border:none;padding:0;margin:0;min-width:0;">

      <!-- Basic Info -->
      <div class="grid grid-cols-3 gap-4 mb-6">
        <div>
          <label class="field-label">Date of Ultrasound</label>
          <input type="date" v-model="form.ultrasoundDate" class="input" />
        </div>
      </div>

      <!-- Obstetric Score -->
      <div class="border border-gray-200 rounded p-4 mb-6">
        <h3 class="section-title mb-3">Obstetric Score</h3>
        <div class="grid grid-cols-5 gap-3">
          <div>
            <label class="field-label">Gravida</label>
            <input type="number" min="0" v-model="form.gravida" class="input-line w-full text-center" />
          </div>
          <div>
            <label class="field-label">Term</label>
            <input type="number" min="0" v-model="form.term" class="input-line w-full text-center" />
          </div>
          <div>
            <label class="field-label">Preterm</label>
            <input type="number" min="0" v-model="form.preterm" class="input-line w-full text-center" />
          </div>
          <div>
            <label class="field-label">Abortion</label>
            <input type="number" min="0" v-model="form.abortion" class="input-line w-full text-center" />
          </div>
          <div>
            <label class="field-label">Living Children</label>
            <input type="number" min="0" v-model="form.livingChildren" class="input-line w-full text-center" />
          </div>
        </div>
      </div>

      <!-- Ultrasound Results -->
      <div class="border rounded p-4 mb-6"
        :class="ultrasoundHighRisk ? 'border-red-400 bg-red-50' : 'border-gray-200'">
        <h3 class="text-center font-bold text-sm tracking-widest mb-3">ULTRASOUND RESULTS</h3>
        <label class="block font-medium mb-1">Findings:</label>
        <div class="flex gap-2 mb-3 flex-wrap">
          <button type="button"
            v-for="tag in ALL_US_TAGS"
            :key="tag"
            @click="toggleUltrasoundTag(tag)"
            :disabled="isReadOnly"
            class="text-xs px-2 py-1 rounded border transition disabled:opacity-50 disabled:cursor-not-allowed"
            :class="ultrasoundTags.includes(tag)
              ? (HIGH_RISK_US_TAGS.includes(tag) ? 'bg-red-500 text-white border-red-500' : 'bg-indigo-500 text-white border-indigo-500')
              : 'bg-white text-gray-600 border-gray-300 hover:border-indigo-400'">
            {{ tag }}
          </button>
        </div>
        <label class="block font-medium mb-1">Additional Notes:</label>
        <textarea v-model="form.remarksNotes" rows="3"
          placeholder="Additional findings or notes..."
          class="w-full border border-gray-300 rounded p-2 text-sm"></textarea>
      </div>

      <!-- Signatures -->
      <div class="grid grid-cols-2 gap-16 mt-10">
        <div class="text-center flex flex-col justify-end min-h-[4rem]">
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold">Patient's Signature / Date</p>
          </div>
        </div>
        <div class="text-center flex flex-col justify-end min-h-[4rem]">
          <select
            v-model="form.attendingStaffID"
            @change="onAttendingStaffChange"
            class="input-line text-center w-full mb-1">
            <option :value="null">-- Select Attending Staff --</option>
            <option
              v-for="emp in employeesList"
              :key="emp.employeeID"
              :value="Number(emp.employeeID)">
              {{ emp.fName }} {{ emp.lName }}
            </option>
          </select>
          <div class="border-t border-gray-800 pt-1">
            <p class="text-xs font-semibold">Attending Midwife / Physician</p>
          </div>
        </div>
      </div>

    </fieldset>
    <!-- END read-only wrapper -->

    <!-- Save button — hidden entirely for Patient accounts -->
    <div v-if="!isReadOnly" class="no-print mt-6">
      <button @click="submitForm" :disabled="submitStatus.loading"
        class="w-full bg-indigo-600 text-white py-3 rounded-lg font-bold hover:bg-indigo-700 transition shadow disabled:opacity-50 disabled:cursor-not-allowed">
        {{ submitStatus.loading ? 'Saving...' : (existingRecordId ? 'UPDATE ULTRASOUND RECORD' : 'SAVE ULTRASOUND RECORD') }}
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
@page { size: A4 portrait; margin: 12mm 12mm; }
@media print {
  * { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; }
  .no-print { display: none !important; }
  input[type="text"], input[type="date"], input[type="number"] { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; }
  select { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; -webkit-appearance:none; appearance:none; }
  textarea { border:1px solid #555 !important; background:transparent !important; resize:none !important; }
  .bg-red-50 { background-color:#fef2f2 !important; }
}
</style>
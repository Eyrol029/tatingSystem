<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const BASE = 'http://localhost:8080/api/appointment'

const appointments = ref([])
const loading      = ref(false)
const error        = ref('')
const successMsg   = ref('')
const showModal    = ref(false)
const isEditing    = ref(false)
const saveLoading  = ref(false)
const today        = new Date().toISOString().split('T')[0]

const emptyForm = {
  appointmentID:      null,
  patientID:          null,
  patientServiceID:   null,
  appointmentDate:    '',
  appointmentTime:    '',
  fName:              '',
  lName:              '',
  middleI:            '',
  age:                null,
  address:            '',
  serviceType:        '',
  gravida:            null,
  term:               null,
  preterm:            null,
  noOfLivingChildren: null
}

const form = ref({ ...emptyForm })

// ─── Stats ────────────────────────────────────────────────────────────────────
const totalAppointments = computed(() => appointments.value.length)

const todaysAppointments = computed(() =>
  appointments.value.filter(a => {
    const d = a.appointmentDate
    // backend returns LocalDate as array [y,m,d] or string
    const dateStr = Array.isArray(d)
      ? `${d[0]}-${String(d[1]).padStart(2,'0')}-${String(d[2]).padStart(2,'0')}`
      : d
    return dateStr === today
  }).length
)

const upcomingAppointments = computed(() =>
  appointments.value.filter(a => {
    const d = a.appointmentDate
    const dateStr = Array.isArray(d)
      ? `${d[0]}-${String(d[1]).padStart(2,'0')}-${String(d[2]).padStart(2,'0')}`
      : d
    return dateStr > today
  }).length
)

// ─── Fetch ────────────────────────────────────────────────────────────────────
async function fetchAppointments() {
  loading.value = true
  error.value   = ''
  try {
    const res = await axios.get(BASE)
    appointments.value = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    error.value = 'Failed to load appointments. Is the server running?'
    console.error(e)
  } finally {
    loading.value = false
  }
}

// ─── Modal helpers ────────────────────────────────────────────────────────────
function openAddModal() {
  form.value   = { ...emptyForm }
  isEditing.value = false
  error.value  = ''
  showModal.value = true
}

function openEditModal(appointment) {
  // Normalize date/time from backend (may be array or string)
  form.value = {
    ...appointment,
    appointmentDate: normalizeDate(appointment.appointmentDate),
    appointmentTime: normalizeTime(appointment.appointmentTime)
  }
  isEditing.value = true
  error.value     = ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  form.value = { ...emptyForm }
}

// ─── Save ─────────────────────────────────────────────────────────────────────
async function saveAppointment() {
  saveLoading.value = true
  error.value       = ''
  try {
    const payload = buildPayload()
    if (isEditing.value) {
      const res = await axios.put(BASE, payload)
      const idx = appointments.value.findIndex(a => a.appointmentID === payload.appointmentID)
      if (idx !== -1) appointments.value[idx] = res.data
      successMsg.value = '✅ Appointment updated successfully!'
    } else {
      const res = await axios.post(BASE, payload)
      appointments.value.push(res.data)
      successMsg.value = '✅ Appointment added successfully!'
    }
    closeModal()
    // auto-clear success after 3s
    setTimeout(() => { successMsg.value = '' }, 3000)
  } catch (e) {
    error.value = '❌ Failed to save: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    saveLoading.value = false
  }
}

// ─── Delete ───────────────────────────────────────────────────────────────────
async function deleteAppointment(id) {
  if (!confirm('Delete this appointment?')) return
  error.value = ''
  try {
    await axios.delete(`${BASE}/${id}`)
    appointments.value = appointments.value.filter(a => a.appointmentID !== id)
    successMsg.value = '✅ Appointment deleted.'
    setTimeout(() => { successMsg.value = '' }, 3000)
  } catch (e) {
    error.value = '❌ Failed to delete: ' + (e?.response?.data?.message ?? e.message)
  }
}

// ─── Payload builder ──────────────────────────────────────────────────────────
// Spring expects LocalDate as "yyyy-MM-dd" and LocalTime as "HH:mm:ss"
function buildPayload() {
  return {
    appointmentID:      form.value.appointmentID ?? undefined,
    patientID:          form.value.patientID     ? Number(form.value.patientID)   : null,
    patientServiceID:   form.value.patientServiceID ? Number(form.value.patientServiceID) : null,
    appointmentDate:    form.value.appointmentDate || null,
    appointmentTime:    form.value.appointmentTime
                          ? form.value.appointmentTime + (form.value.appointmentTime.length === 5 ? ':00' : '')
                          : null,
    fName:              form.value.fName    || null,
    lName:              form.value.lName    || null,
    middleI:            form.value.middleI  || null,
    age:                form.value.age      ? Number(form.value.age)      : null,
    address:            form.value.address  || null,
    serviceType:        form.value.serviceType || null,
    gravida:            form.value.gravida  ? Number(form.value.gravida)  : null,
    term:               form.value.term     ? Number(form.value.term)     : null,
    preterm:            form.value.preterm  ? Number(form.value.preterm)  : null,
    noOfLivingChildren: form.value.noOfLivingChildren
                          ? Number(form.value.noOfLivingChildren) : null
  }
}

// ─── Date / time normalizers ──────────────────────────────────────────────────
// Backend may return LocalDate as [2025,6,15] array or "2025-06-15" string
function normalizeDate(d) {
  if (!d) return ''
  if (Array.isArray(d))
    return `${d[0]}-${String(d[1]).padStart(2,'0')}-${String(d[2]).padStart(2,'0')}`
  return String(d).substring(0, 10)
}

// Backend may return LocalTime as [9,30,0] array or "09:30:00" string
function normalizeTime(t) {
  if (!t) return ''
  if (Array.isArray(t))
    return `${String(t[0]).padStart(2,'0')}:${String(t[1]).padStart(2,'0')}`
  return String(t).substring(0, 5)
}

// ─── Display helpers ─────────────────────────────────────────────────────────
function formatDateTime(date, time) {
  if (!date && !time) return '—'
  const d = normalizeDate(date)
  const t = normalizeTime(time)
  return [d, t].filter(Boolean).join(' ')
}

function fullName(a) {
  return [a.lName + ',', a.fName, a.middleI ? a.middleI + '.' : '']
    .filter(Boolean).join(' ')
}

onMounted(fetchAppointments)
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Appointment Management</h1>
      <div class="flex gap-2">
        <button @click="fetchAppointments" :disabled="loading"
          class="flex items-center gap-1 px-4 py-2 bg-white border border-gray-300 text-gray-600 rounded-lg hover:bg-gray-50 text-sm transition">
          <span :class="loading ? 'animate-spin inline-block' : ''">↻</span> Refresh
        </button>
        <button @click="openAddModal"
          class="flex items-center gap-2 px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 transition">
          <span class="text-lg font-bold">+</span> Add Appointment
        </button>
      </div>
    </div>

    <!-- Alerts -->
    <div v-if="error"
      class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg flex items-center justify-between text-sm">
      {{ error }}
      <button @click="error = ''" class="ml-3 font-bold text-red-400 hover:text-red-600">✕</button>
    </div>
    <div v-if="successMsg"
      class="mb-4 p-3 bg-green-100 text-green-700 rounded-lg flex items-center justify-between text-sm">
      {{ successMsg }}
      <button @click="successMsg = ''" class="ml-3 font-bold text-green-400 hover:text-green-600">✕</button>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-gray-500 text-sm mb-1">Total Appointments</p>
        <p class="text-3xl font-bold text-gray-800">{{ totalAppointments }}</p>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-gray-500 text-sm mb-1">Today's Appointments</p>
        <p class="text-3xl font-bold text-[#8e4f70]">{{ todaysAppointments }}</p>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-gray-500 text-sm mb-1">Upcoming</p>
        <p class="text-3xl font-bold text-indigo-600">{{ upcomingAppointments }}</p>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow overflow-x-auto">
      <div v-if="loading" class="p-8 text-center text-gray-500">Loading appointments...</div>
      <table v-else class="w-full text-sm">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">ID</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Date & Time</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Patient Name</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Age</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Address</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Service Type</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Gravida</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="a in appointments" :key="a.appointmentID"
            class="border-b border-gray-100 hover:bg-gray-50 transition">
            <td class="px-4 py-3 text-gray-600 font-mono text-xs">
              APT{{ String(a.appointmentID).padStart(3, '0') }}
            </td>
            <td class="px-4 py-3 text-gray-700">
              {{ formatDateTime(a.appointmentDate, a.appointmentTime) }}
            </td>
            <td class="px-4 py-3 text-gray-800 font-medium">{{ fullName(a) }}</td>
            <td class="px-4 py-3 text-gray-700">{{ a.age ?? '—' }}</td>
            <td class="px-4 py-3 text-gray-700">{{ a.address || '—' }}</td>
            <td class="px-4 py-3 text-gray-700">
              <span v-if="a.serviceType"
                class="px-2 py-1 bg-purple-100 text-purple-700 rounded text-xs font-medium">
                {{ a.serviceType }}
              </span>
              <span v-else class="text-gray-400">—</span>
            </td>
            <td class="px-4 py-3 text-gray-700">{{ a.gravida ?? '—' }}</td>
            <td class="px-4 py-3">
              <div class="flex gap-2">
                <button @click="openEditModal(a)"
                  class="text-blue-500 hover:text-blue-700 transition" title="Edit">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                  </svg>
                </button>
                <button @click="deleteAppointment(a.appointmentID)"
                  class="text-red-500 hover:text-red-700 transition" title="Delete">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="appointments.length === 0">
            <td colspan="8" class="text-center py-10 text-gray-400">No appointments found.</td>
          </tr>
        </tbody>
      </table>

      <!-- Row count -->
      <div v-if="!loading && appointments.length > 0"
        class="px-4 py-2 bg-gray-50 border-t text-xs text-gray-400">
        {{ appointments.length }} appointment(s) total
      </div>
    </div>

    <!-- ── MODAL ── -->
    <div v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-2xl shadow-xl w-full max-w-2xl mx-4 p-6 max-h-[90vh] overflow-y-auto">

        <!-- Modal header -->
        <div class="flex justify-between items-center mb-5">
          <h2 class="text-xl font-bold text-gray-800">
            {{ isEditing ? 'Edit Appointment' : 'Add Appointment' }}
          </h2>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- Error inside modal -->
        <div v-if="error" class="mb-4 p-3 bg-red-100 text-red-700 rounded text-sm">{{ error }}</div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

          <!-- Name fields -->
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">First Name</label>
            <input v-model="form.fName" type="text"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Middle Initial</label>
            <input v-model="form.middleI" type="text" maxlength="2"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Last Name</label>
            <input v-model="form.lName" type="text"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Age</label>
            <input v-model="form.age" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>

          <!-- Date & Time -->
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Appointment Date</label>
            <input v-model="form.appointmentDate" type="date"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Appointment Time</label>
            <input v-model="form.appointmentTime" type="time"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>

          <!-- Address -->
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Address</label>
            <input v-model="form.address" type="text"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>

          <!-- Service Type -->
          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Service Type</label>
            <select v-model="form.serviceType"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]">
              <option value="">— Select service —</option>
              <option>General Consultation</option>
              <option>Prenatal Checkup</option>
              <option>Postnatal Care</option>
              <option>Family Planning</option>
              <option>Immunization</option>
            </select>
          </div>

          <!-- Obstetric info -->
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Gravida</label>
            <input v-model="form.gravida" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Term</label>
            <input v-model="form.term" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Preterm</label>
            <input v-model="form.preterm" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">No. of Living Children</label>
            <input v-model="form.noOfLivingChildren" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>

          <!-- Optional IDs -->
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Patient ID</label>
            <input v-model="form.patientID" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Patient Service ID</label>
            <input v-model="form.patientServiceID" type="number" min="0"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>
        </div>

        <!-- Modal footer -->
        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeModal"
            class="px-5 py-2 border border-gray-300 rounded-lg text-gray-600 hover:bg-gray-50 text-sm">
            Cancel
          </button>
          <button @click="saveAppointment" :disabled="saveLoading"
            class="px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 text-sm transition disabled:opacity-50 disabled:cursor-not-allowed">
            {{ saveLoading ? 'Saving...' : (isEditing ? 'Update' : 'Save') }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
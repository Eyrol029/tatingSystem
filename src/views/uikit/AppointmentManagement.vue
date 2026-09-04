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

// NEW: filter for the checklist — All / Pending / Done
const statusFilter = ref('All')

const emptyForm = {
  appointmentID:   null,
  appointmentDate: '',
  appointmentTime: '',
  fName:           '',
  lName:           '',
  middleI:         '',
  age:             null,
  address:         '',
  serviceType:     '',
  completed:       false // NEW
}

const form = ref({ ...emptyForm })

// ─── Stats ────────────────────────────────────────────────────────────────────
const totalAppointments = computed(() => appointments.value.length)

const todaysAppointments = computed(() =>
  appointments.value.filter(a => {
    const d = a.appointmentDate
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

// NEW: count of completed appointments
const completedCount = computed(() =>
  appointments.value.filter(a => a.completed).length
)

// NEW: appointments filtered by the checklist toggle (All / Pending / Done)
const filteredAppointments = computed(() => {
  if (statusFilter.value === 'Pending') return appointments.value.filter(a => !a.completed)
  if (statusFilter.value === 'Done')    return appointments.value.filter(a => a.completed)
  return appointments.value
})

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
  form.value = { ...emptyForm }
  isEditing.value = false
  error.value = ''
  showModal.value = true
}

function openEditModal(appointment) {
  form.value = {
    ...appointment,
    appointmentDate: normalizeDate(appointment.appointmentDate),
    appointmentTime: normalizeTime(appointment.appointmentTime),
    completed: !!appointment.completed
  }
  isEditing.value = true
  error.value = ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  form.value = { ...emptyForm }
}

// ─── Save ─────────────────────────────────────────────────────────────────────
async function saveAppointment() {
  saveLoading.value = true
  error.value = ''

  try {
    const payload = buildPayload()

    if (isEditing.value) {
      const res = await axios.put(BASE, payload)
      const idx = appointments.value.findIndex(
        a => a.appointmentID === payload.appointmentID
      )
      if (idx !== -1) appointments.value[idx] = res.data
      successMsg.value = '✅ Appointment updated successfully!'
    } else {
      const res = await axios.post(BASE, payload)
      appointments.value.push(res.data)
      successMsg.value = '✅ Appointment added successfully!'
    }

    closeModal()
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
    appointments.value = appointments.value.filter(
      a => a.appointmentID !== id
    )
    successMsg.value = '✅ Appointment deleted.'
    setTimeout(() => { successMsg.value = '' }, 3000)
  } catch (e) {
    error.value = '❌ Failed to delete: ' + (e?.response?.data?.message ?? e.message)
  }
}

// ─── Toggle Done (checklist) ──────────────────────────────────────────────────
// NEW: flips the "completed" flag for one appointment. Updates the UI right
// away (optimistic), then saves the whole record via the existing PUT
// endpoint. If the save fails, the checkbox reverts back.
async function toggleCompleted(appointment) {
  const previous = appointment.completed
  appointment.completed = !previous // optimistic update

  try {
    const payload = {
      appointmentID:   appointment.appointmentID,
      appointmentDate: normalizeDate(appointment.appointmentDate),
      appointmentTime: normalizeTime(appointment.appointmentTime)
        ? normalizeTime(appointment.appointmentTime) + ':00'
        : null,
      fName:       appointment.fName || null,
      lName:       appointment.lName || null,
      middleI:     appointment.middleI || null,
      age:         appointment.age ? Number(appointment.age) : null,
      address:     appointment.address || null,
      serviceType: appointment.serviceType || null,
      completed:   appointment.completed
    }

    const res = await axios.put(BASE, payload)
    const idx = appointments.value.findIndex(
      a => a.appointmentID === appointment.appointmentID
    )
    if (idx !== -1) appointments.value[idx] = res.data
  } catch (e) {
    appointment.completed = previous // revert on failure
    error.value = '❌ Failed to update status: ' + (e?.response?.data?.message ?? e.message)
  }
}

// ─── Payload builder ──────────────────────────────────────────────────────────
function buildPayload() {
  return {
    appointmentID:   form.value.appointmentID ?? undefined,
    appointmentDate: form.value.appointmentDate || null,
    appointmentTime: form.value.appointmentTime
      ? form.value.appointmentTime +
        (form.value.appointmentTime.length === 5 ? ':00' : '')
      : null,
    fName:       form.value.fName || null,
    lName:       form.value.lName || null,
    middleI:     form.value.middleI || null,
    age:         form.value.age ? Number(form.value.age) : null,
    address:     form.value.address || null,
    serviceType: form.value.serviceType || null,
    completed:   !!form.value.completed // NEW
  }
}

// ─── Date / time normalizers ──────────────────────────────────────────────────
function normalizeDate(d) {
  if (!d) return ''
  if (Array.isArray(d))
    return `${d[0]}-${String(d[1]).padStart(2,'0')}-${String(d[2]).padStart(2,'0')}`
  return String(d).substring(0, 10)
}

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
    .filter(Boolean)
    .join(' ')
}

onMounted(fetchAppointments)
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Appointment Management</h1>

      <div class="flex gap-2">

        <button
          @click="openAddModal"
          class="flex items-center gap-2 px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 transition"
        >
          <span class="text-lg font-bold">+</span>
          Add Appointment
        </button>
      </div>
    </div>

    <!-- Alerts -->
    <div
      v-if="error"
      class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg flex items-center justify-between text-sm"
    >
      {{ error }}
      <button @click="error = ''" class="ml-3 font-bold text-red-400 hover:text-red-600">✕</button>
    </div>

    <div
      v-if="successMsg"
      class="mb-4 p-3 bg-green-100 text-green-700 rounded-lg flex items-center justify-between text-sm"
    >
      {{ successMsg }}
      <button @click="successMsg = ''" class="ml-3 font-bold text-green-400 hover:text-green-600">✕</button>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
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

      <!-- NEW: Completed stat card -->
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-gray-500 text-sm mb-1">Completed</p>
        <p class="text-3xl font-bold text-green-600">{{ completedCount }}</p>
      </div>
    </div>

    <!-- NEW: Checklist filter tabs -->
    <div class="flex gap-2 mb-4">
      <button v-for="f in ['All', 'Pending', 'Done']" :key="f"
        @click="statusFilter = f"
        :class="['px-4 py-1.5 rounded-full text-xs font-medium transition',
          statusFilter === f
            ? 'bg-[#8e4f70] text-white'
            : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50']">
        {{ f }}
      </button>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow overflow-x-auto">

      <div v-if="loading" class="p-8 text-center text-gray-500">
        Loading appointments...
      </div>

      <table v-else class="w-full text-sm">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <!-- NEW: Done checkbox column -->
            <th class="text-center px-4 py-3 text-gray-600 font-semibold w-12">Done</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">ID</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Date & Time</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Patient Name</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Age</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Address</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Service Type</th>
            <th class="text-left px-4 py-3 text-gray-600 font-semibold">Actions</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="a in filteredAppointments"
            :key="a.appointmentID"
            class="border-b border-gray-100 hover:bg-gray-50 transition"
            :class="{ 'bg-gray-50/60': a.completed }"
          >
            <!-- NEW: checklist checkbox -->
            <td class="px-4 py-3 text-center">
              <input
                type="checkbox"
                :checked="!!a.completed"
                @change="toggleCompleted(a)"
                class="w-4 h-4 accent-[#8e4f70] cursor-pointer"
                title="Mark as done"
              />
            </td>

            <td class="px-4 py-3 text-gray-600 font-mono text-xs" :class="{ 'line-through opacity-50': a.completed }">
              APT{{ String(a.appointmentID).padStart(3, '0') }}
            </td>

            <td class="px-4 py-3 text-gray-700" :class="{ 'line-through opacity-50': a.completed }">
              {{ formatDateTime(a.appointmentDate, a.appointmentTime) }}
            </td>

            <td class="px-4 py-3 text-gray-800 font-medium" :class="{ 'line-through opacity-50': a.completed }">
              {{ fullName(a) }}
            </td>

            <td class="px-4 py-3 text-gray-700" :class="{ 'opacity-50': a.completed }">
              {{ a.age ?? '—' }}
            </td>

            <td class="px-4 py-3 text-gray-700" :class="{ 'opacity-50': a.completed }">
              {{ a.address || '—' }}
            </td>

            <td class="px-4 py-3 text-gray-700">
              <span
                v-if="a.serviceType"
                class="px-2 py-1 bg-purple-100 text-purple-700 rounded text-xs font-medium"
                :class="{ 'opacity-50': a.completed }"
              >
                {{ a.serviceType }}
              </span>

              <span v-else class="text-gray-400">—</span>

              <!-- NEW: Done badge -->
              <span v-if="a.completed"
                class="ml-2 px-2 py-1 bg-green-100 text-green-700 rounded text-xs font-medium">
                ✓ Done
              </span>
            </td>

            <td class="px-4 py-3">
              <div class="flex gap-2">

                <button
                  @click="openEditModal(a)"
                  class="text-blue-500 hover:text-blue-700 transition"
                  title="Edit"
                >
                  Edit
                </button>

                <button
                  @click="deleteAppointment(a.appointmentID)"
                  class="text-red-500 hover:text-red-700 transition"
                  title="Delete"
                >
                  Delete
                </button>

              </div>
            </td>
          </tr>

          <tr v-if="filteredAppointments.length === 0">
            <td colspan="8" class="text-center py-10 text-gray-400">
              {{ statusFilter === 'All' ? 'No appointments found.' : `No ${statusFilter.toLowerCase()} appointments.` }}
            </td>
          </tr>
        </tbody>
      </table>

      <div
        v-if="!loading && filteredAppointments.length > 0"
        class="px-4 py-2 bg-gray-50 border-t text-xs text-gray-400"
      >
        {{ filteredAppointments.length }} appointment(s) shown · {{ completedCount }} completed total
      </div>
    </div>

    <!-- Modal -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    >

      <div
        class="bg-white rounded-2xl shadow-xl w-full max-w-2xl mx-4 p-6 max-h-[90vh] overflow-y-auto"
      >

        <div class="flex justify-between items-center mb-5">
          <h2 class="text-xl font-bold text-gray-800">
            {{ isEditing ? 'Edit Appointment' : 'Add Appointment' }}
          </h2>

          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            ✕
          </button>
        </div>

        <div v-if="error" class="mb-4 p-3 bg-red-100 text-red-700 rounded text-sm">
          {{ error }}
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

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

          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Address</label>
            <input v-model="form.address" type="text"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]" />
          </div>

          <div class="md:col-span-2">
            <label class="block text-xs font-semibold text-gray-600 mb-1">Service Type</label>
            <select v-model="form.serviceType"
              class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#8e4f70]"
            >
              <option value="">— Select service —</option>
              <option>General Consultation</option>
              <option>Prenatal Checkup</option>
              <option>Postnatal Care</option>
              <option>Family Planning</option>
              <option>Immunization</option>
            </select>
          </div>

          <!-- NEW: Mark as done checkbox inside the modal too -->
          <div class="md:col-span-2 flex items-center gap-2 pt-1">
            <input
              id="completedCheckbox"
              type="checkbox"
              v-model="form.completed"
              class="w-4 h-4 accent-[#8e4f70] cursor-pointer"
            />
            <label for="completedCheckbox" class="text-sm text-gray-700 cursor-pointer">
              Mark this appointment as done
            </label>
          </div>

        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button
            @click="closeModal"
            class="px-5 py-2 border border-gray-300 rounded-lg text-gray-600 hover:bg-gray-50 text-sm"
          >
            Cancel
          </button>

          <button
            @click="saveAppointment"
            :disabled="saveLoading"
            class="px-5 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-700 text-sm transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ saveLoading ? 'Saving...' : (isEditing ? 'Update' : 'Save') }}
          </button>
        </div>

      </div>
    </div>

  </div>
</template>
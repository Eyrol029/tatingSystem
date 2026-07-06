<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const BASE_URL = 'http://localhost:8080/api/revenue'
const SOA_PATIENT_URL = 'http://localhost:8080/api/billing/soa/patient'

const revenues = ref([])
const showModal = ref(false)

// List of existing patients — lets revenue be traced to a real billing record
// instead of a free-typed name.
const patientsList = ref([])
const selectedPatientId = ref('')
const patientSearchQuery = ref('')
const loadingPatientTotals = ref(false)

// Filters the patient list by name or ID as the user types, so they don't
// have to scroll through a long dropdown to find someone.
const filteredPatientsList = computed(() => {
  const q = patientSearchQuery.value.trim().toLowerCase()
  if (!q) return patientsList.value
  return patientsList.value.filter((patient) => {
    const fullName = `${patient.fName || ''} ${patient.lName || ''}`.toLowerCase()
    return (
      fullName.includes(q) ||
      String(patient.patientID || '').includes(q)
    )
  })
})

const form = ref({
  dealer: '',
  description: '',
  amount: '',
  patientID: null
})

async function fetchRevenues() {
  try {
    const response = await axios.get(BASE_URL)
    revenues.value = response.data
  } catch (error) {
    console.error('Failed to load revenue records', error)
    alert('Failed to load revenue records: ' + (error.response?.data || error.message))
  }
}

// Fetch the existing Patient list (adjust the endpoint path if yours differs)
async function fetchPatients() {
  try {
    const res = await axios.get('http://localhost:8080/api/patients')
    patientsList.value = res.data
  } catch (error) {
    console.error('Failed to fetch patients list', error)
  }
}

// When a patient is selected, pull their billing totals (amountPaid) from the
// SOA/billing system and use it to auto-fill the revenue entry — this way the
// clinic's recorded revenue traces back to what the patient actually paid.
async function onPatientSelect() {
  if (!selectedPatientId.value) {
    form.value.patientID = null
    return
  }

  loadingPatientTotals.value = true
  try {
    const res = await axios.get(`${SOA_PATIENT_URL}/${selectedPatientId.value}`)
    const details = res.data

    form.value.patientID = selectedPatientId.value
    form.value.dealer = details.patientName || form.value.dealer
    form.value.amount = details.amountPaid != null ? String(details.amountPaid) : form.value.amount
    form.value.description = form.value.description
      || `Total amount paid by ${details.patientName || 'patient'} to date`
  } catch (error) {
    console.error('Failed to fetch patient billing totals', error)
    alert('Could not load this patient\'s billing totals. You can still enter the amount manually.')
  } finally {
    loadingPatientTotals.value = false
  }
}

function openModal() {
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  resetForm()
}

function resetForm() {
  form.value = {
    dealer: '',
    description: '',
    amount: '',
    patientID: null
  }
  selectedPatientId.value = ''
  patientSearchQuery.value = ''
}

async function saveRevenue() {
  if (!form.value.dealer || !form.value.amount) return

  try {
    await axios.post(BASE_URL, {
      dealer: form.value.dealer,
      description: form.value.description,
      amount: Number(form.value.amount),
      revenueDate: new Date().toISOString().slice(0, 10), // YYYY-MM-DD
      patientID: form.value.patientID
    })

    await fetchRevenues()
    closeModal()
  } catch (error) {
    console.error('Failed to save revenue', error)
    alert('Failed to save revenue: ' + (error.response?.data || error.message))
  }
}

async function deleteRevenue(id) {
  if (!confirm('Are you sure you want to delete this record?')) return

  try {
    await axios.delete(`${BASE_URL}/${id}`)
    await fetchRevenues()
  } catch (error) {
    console.error('Failed to delete revenue', error)
    alert('Failed to delete revenue: ' + (error.response?.data || error.message))
  }
}

onMounted(() => {
  fetchRevenues()
  fetchPatients()
})
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold">Revenue Records</h2>
      <button
        @click="openModal"
        class="bg-purple-700 text-white px-4 py-2 rounded-md"
      >
        + Add Revenue
      </button>
    </div>

    <!-- TABLE -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="w-full text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 text-left">Revenue ID / Invoice No.</th>
            <th class="p-3 text-left">Paid by</th>
            <th class="p-3 text-left">Patient ID</th>
            <th class="p-3 text-left">Description</th>
            <th class="p-3 text-left">Amount</th>
            <th class="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="rev in revenues" :key="rev.id" class="border-t">
            <td class="p-3 font-mono text-xs text-gray-500">REV-{{ String(rev.id).padStart(5, '0') }}</td>
            <td class="p-3">{{ rev.dealer }}</td>
            <td class="p-3 text-gray-500">{{ rev.patientID ?? '—' }}</td>
            <td class="p-3">{{ rev.description }}</td>
            <td class="p-3 font-semibold">₱{{ rev.amount }}</td>
            <td class="p-3">
              <button
                @click="deleteRevenue(rev.id)"
                class="text-red-600 hover:underline"
              >
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="revenues.length === 0" class="text-center py-8 text-gray-400 text-sm">
        No revenue records yet.
      </div>
    </div>

    <!-- MODAL -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    >
      <div class="bg-white w-full max-w-lg rounded-lg p-6 shadow-lg">
        <h3 class="text-lg font-semibold mb-4">Add Revenue</h3>

        <div class="space-y-4">
          <div>
            <label class="text-sm font-medium">Link to Patient (optional)</label>

            <!-- Search box to quickly narrow down the patient list -->
            <div class="relative mb-2">
              <input
                v-model="patientSearchQuery"
                type="text"
                placeholder="Search patient by name or ID…"
                class="w-full border rounded px-3 py-2 text-sm"
              />
              <button
                v-if="patientSearchQuery"
                type="button"
                @click="patientSearchQuery = ''"
                class="absolute right-2 top-1/2 -translate-y-1/2 text-sm text-gray-400 hover:text-gray-600"
              >×</button>
            </div>

            <select
              v-model="selectedPatientId"
              @change="onPatientSelect"
              class="w-full border rounded px-3 py-2"
            >
              <option value="">-- Manual entry (no patient) --</option>
              <option
                v-for="patient in filteredPatientsList"
                :key="patient.patientID"
                :value="patient.patientID"
              >
                {{ patient.fName }} {{ patient.lName }} (ID: {{ patient.patientID }})
              </option>
            </select>
            <p v-if="patientSearchQuery && filteredPatientsList.length === 0" class="text-xs text-gray-400 mt-1">
              No patients match "{{ patientSearchQuery }}".
            </p>
            <p v-if="loadingPatientTotals" class="text-xs text-purple-500 mt-1">
              Loading patient's total amount paid…
            </p>
            <p v-if="selectedPatientId && !loadingPatientTotals" class="text-xs text-gray-400 mt-1">
              Amount is locked to this patient's total paid on their Statement of Account.
              The auto-generated Revenue ID also serves as the invoice/receipt number.
            </p>
          </div>

          <div>
            <label class="text-sm font-medium">Paid by</label>
            <input v-model="form.dealer" class="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label class="text-sm font-medium">Description</label>
            <textarea v-model="form.description" class="w-full border rounded px-3 py-2"></textarea>
          </div>

          <div>
            <label class="text-sm font-medium">
              Amount (₱) {{ selectedPatientId ? '— auto-filled from billing' : '' }}
            </label>
            <input
              type="number"
              v-model="form.amount"
              :readonly="!!selectedPatientId"
              :class="[
                'w-full border rounded px-3 py-2',
                selectedPatientId ? 'bg-gray-100 text-gray-600 cursor-not-allowed' : ''
              ]"
            />
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeModal" class="px-6 py-2 bg-gray-200 rounded-md">
            Cancel
          </button>
          <button @click="saveRevenue" class="px-6 py-2 bg-purple-700 text-white rounded-md">
            Save
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
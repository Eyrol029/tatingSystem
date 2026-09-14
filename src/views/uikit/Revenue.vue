<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { recordReport } from '@/service/reportHistory'
import { useConfirmDelete } from '@/composables/useConfirmDelete'

const { confirmDelete } = useConfirmDelete()

const BASE_URL = 'http://localhost:8080/api/revenue'
const SOA_PATIENT_URL = 'http://localhost:8080/api/billing/soa/patient'

const revenues = ref([])
const showModal = ref(false)
const filterFrom = ref('')
const filterTo = ref('')

function clearDateFilter() {
  filterFrom.value = ''
  filterTo.value = ''
}

// Filters the Revenue Records table by date range.
const filteredRevenues = computed(() => {
  return revenues.value.filter((rev) => {
    const dateStr = rev.revenueDate ? String(rev.revenueDate).slice(0, 10) : ''
    return (!filterFrom.value || dateStr >= filterFrom.value) &&
      (!filterTo.value || dateStr <= filterTo.value)
  })
})

const filteredTotal = computed(() => {
  return filteredRevenues.value.reduce(
    (sum, rev) => sum + Number(rev.amount || 0),
    0
  )
})

// List of existing patients — lets revenue be traced to a real billing record
// instead of a free-typed name.
const patientsList = ref([])
const patientServicesList = ref([])
const selectedPatientId = ref('')
const patientSearchQuery = ref('')
const loadingPatientTotals = ref(false)

const patientCaseNumbers = computed(() => {
  const caseNumbers = {}
  patientServicesList.value.forEach((service) => {
    const patientId = service.patientID
    const caseNumber = String(service.caseNumber || '').trim()
    if (patientId != null && caseNumber && caseNumber !== '---' && !caseNumbers[patientId]) {
      caseNumbers[patientId] = caseNumber
    }
  })
  return caseNumbers
})

function getPatientCaseNumber(patientId) {
  return patientCaseNumbers.value[patientId] || '—'
}

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

// Returns today's date as YYYY-MM-DD using LOCAL time, not UTC — avoids the
// off-by-one-day bug that .toISOString() causes for PH (UTC+8) users.
function todayLocalDateString() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

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

async function fetchPatientServices() {
  try {
    const res = await axios.get('http://localhost:8080/api/patient-services')
    patientServicesList.value = res.data
  } catch (error) {
    console.error('Failed to fetch patient case numbers', error)
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
      revenueDate: todayLocalDateString(), // YYYY-MM-DD, local time
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
  if (!await confirmDelete()) return

  try {
    await axios.delete(`${BASE_URL}/${id}`)
    await fetchRevenues()
  } catch (error) {
    console.error('Failed to delete revenue', error)
    alert('Failed to delete revenue: ' + (error.response?.data || error.message))
  }
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, character => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;'
  })[character])
}

function printReport() {
  if (!filteredRevenues.value.length) {
    alert('No revenue records found for the selected filter.')
    return
  }

  const range = filterFrom.value || filterTo.value
    ? `${filterFrom.value || 'Beginning'} to ${filterTo.value || 'Present'}`
    : 'All dates'

  const formattedTotal = Number(filteredTotal.value || 0).toLocaleString('en-PH', { minimumFractionDigits: 2 })
  const reportDate = new Date().toLocaleString('en-PH', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })

  const rows = filteredRevenues.value.map((rev, index) => {
    const revId = `REV-${String(rev.id).padStart(5, '0')}`
    const dateStr = rev.revenueDate
      ? new Date(rev.revenueDate).toLocaleDateString('en-PH', { year: 'numeric', month: 'short', day: 'numeric' })
      : '—'
    const caseNumber = getPatientCaseNumber(rev.patientID)
    const amountStr = `₱${Number(rev.amount || 0).toLocaleString('en-PH', { minimumFractionDigits: 2 })}`

    return `<tr>
      <td style="text-align: center;">${index + 1}</td>
      <td style="font-family: monospace; font-weight: 600;">${escapeHtml(revId)}</td>
      <td>${escapeHtml(dateStr)}</td>
      <td><strong>${escapeHtml(rev.dealer || '—')}</strong></td>
      <td>${escapeHtml(caseNumber)}</td>
      <td>${escapeHtml(rev.description || '—')}</td>
      <td style="text-align: right; font-weight: 600;">${escapeHtml(amountStr)}</td>
    </tr>`
  }).join('')

  const printWindow = window.open('', '_blank', 'width=1000,height=850')
  if (!printWindow) {
    alert('Please allow pop-ups to print the report.')
    return
  }

  const html = `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Revenue Report - Tating Maternity Clinic</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      color: #1f2937;
      margin: 28px;
      line-height: 1.4;
    }
    .header {
      border-bottom: 2px solid #6b21a8;
      padding-bottom: 14px;
      margin-bottom: 16px;
      display: flex;
      justify-content: space-between;
      align-items: flex-end;
    }
    .clinic-name {
      font-size: 22px;
      font-weight: bold;
      color: #6b21a8;
      margin: 0;
    }
    .report-title {
      font-size: 13px;
      color: #4b5563;
      margin-top: 4px;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .meta {
      font-size: 12px;
      color: #6b7280;
      text-align: right;
    }
    .summary-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #faf5ff;
      border: 1px solid #e9d5ff;
      border-radius: 6px;
      padding: 10px 16px;
      margin-bottom: 16px;
      font-size: 12px;
      color: #581c87;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 12px;
    }
    th {
      background: #6b21a8;
      color: #ffffff;
      text-align: left;
      padding: 10px 8px;
      font-weight: 600;
    }
    td {
      padding: 8px;
      border-bottom: 1px solid #e5e7eb;
    }
    tr:nth-child(even) {
      background: #faf5ff;
    }
    .total-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
    .total-box {
      background: #f3e8ff;
      border: 1px solid #d8b4fe;
      border-radius: 6px;
      padding: 12px 24px;
      text-align: right;
      min-width: 220px;
    }
    .total-label {
      font-size: 11px;
      text-transform: uppercase;
      font-weight: bold;
      color: #6b21a8;
      letter-spacing: 0.05em;
    }
    .total-amount {
      font-size: 20px;
      font-weight: bold;
      color: #581c87;
      margin-top: 2px;
    }
    .footer {
      margin-top: 36px;
      border-top: 1px solid #e5e7eb;
      padding-top: 12px;
      font-size: 11px;
      color: #9ca3af;
      display: flex;
      justify-content: space-between;
    }
    @media print {
      body { margin: 12mm; }
      @page { size: auto; margin: 10mm; }
    }
  </style>
</head>
<body>
  <div class="header">
    <div>
      <div class="clinic-name">Tating Maternity Clinic</div>
      <div class="report-title">Revenue & Income Report</div>
    </div>
    <div class="meta">
      <div><strong>Date Generated:</strong> ${reportDate}</div>
    </div>
  </div>

  <div class="summary-bar">
    <div><strong>Period:</strong> ${escapeHtml(range)}</div>
    <div><strong>Total Transactions:</strong> ${filteredRevenues.value.length} record(s)</div>
  </div>

  <table>
    <thead>
      <tr>
        <th style="width: 35px; text-align: center;">#</th>
        <th>Revenue ID / Invoice</th>
        <th>Date Paid</th>
        <th>Paid By</th>
        <th>Patient Case No.</th>
        <th>Description</th>
        <th style="text-align: right;">Amount</th>
      </tr>
    </thead>
    <tbody>
      ${rows}
    </tbody>
  </table>

  <div class="total-container">
    <div class="total-box">
      <div class="total-label">Total Revenue</div>
      <div class="total-amount">₱${formattedTotal}</div>
    </div>
  </div>

  <div class="footer">
    <div>Generated from Tating Maternity Clinic Financial Management System</div>
    <div>Official Report</div>
  </div>

  <script>
    window.onload = () => {
      window.focus();
      window.print();
      window.onafterprint = () => window.close();
    };
  <\/script>
</body>
</html>`

  printWindow.document.open()
  printWindow.document.write(html)
  printWindow.document.close()

  recordReport({
    name: 'Revenue Report',
    type: 'Revenue',
    period: range,
    details: `${filteredRevenues.value.length} record(s), total ₱${formattedTotal}`
  })
}

onMounted(() => {
  fetchRevenues()
  fetchPatients()
  fetchPatientServices()
})
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-3 mb-6">
      <h2 class="text-xl font-semibold text-gray-800">Revenue Records</h2>
      <div class="flex items-center gap-2">
        <button
          @click="printReport"
          class="border border-purple-300 text-purple-700 bg-white hover:bg-purple-50 px-4 py-2 rounded-md text-sm font-medium shadow-sm transition"
        >
          Print Report
        </button>
        <button
          @click="openModal"
          class="bg-purple-700 hover:bg-purple-800 text-white px-4 py-2 rounded-md text-sm font-medium shadow-sm transition"
        >
          + Add Revenue
        </button>
      </div>
    </div>

    <!-- DATE FILTERS -->
    <div class="bg-white shadow-sm rounded-lg p-4 mb-4 flex flex-col md:flex-row md:items-end gap-3">
      <div>
        <label class="block text-xs font-semibold text-gray-500 mb-1">From Date</label>
        <input v-model="filterFrom" type="date" class="border rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-purple-500" />
      </div>
      <div>
        <label class="block text-xs font-semibold text-gray-500 mb-1">To Date</label>
        <input v-model="filterTo" type="date" class="border rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-purple-500" />
      </div>
      <button
        @click="clearDateFilter"
        :disabled="!filterFrom && !filterTo"
        class="px-4 py-2 text-sm border rounded text-gray-600 hover:bg-gray-50 disabled:opacity-40"
      >
        Clear Filter
      </button>
      <div class="md:ml-auto text-sm font-semibold text-purple-900 bg-purple-50 px-3 py-2 rounded border border-purple-200">
        Filtered Total: ₱{{ filteredTotal.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
      </div>
    </div>

    <!-- TABLE -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="w-full text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 text-left">Revenue ID / Invoice No.</th>
            <th class="p-3 text-left">Paid by</th>
            <th class="p-3 text-left">Patient Case No.</th>
            <th class="p-3 text-left">Date Paid</th>
            <th class="p-3 text-left">Description</th>
            <th class="p-3 text-left">Amount</th>
            <th class="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="rev in filteredRevenues" :key="rev.id" class="border-t">
            <td class="p-3 font-mono text-xs text-gray-500 font-semibold">REV-{{ String(rev.id).padStart(5, '0') }}</td>
            <td class="p-3 font-medium">{{ rev.dealer }}</td>
            <td class="p-3 text-gray-600">{{ getPatientCaseNumber(rev.patientID) }}</td>
            <td class="p-3 text-gray-600">
              {{ rev.revenueDate ? new Date(rev.revenueDate).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' }) : '—' }}
            </td>
            <td class="p-3">{{ rev.description }}</td>
            <td class="p-3 font-semibold text-purple-950">₱{{ Number(rev.amount || 0).toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}</td>
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
      <div v-else-if="filteredRevenues.length === 0" class="text-center py-8 text-gray-400 text-sm">
        No revenue records match the selected filters.
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
                {{ patient.fName }} {{ patient.lName }} (Case: {{ getPatientCaseNumber(patient.patientID) }})
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
<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const BASE  = 'http://localhost:8080/api/clinic-logs'

const logs        = ref([])
const loading     = ref(false)
const saveLoading = ref(false)
const errorMsg    = ref('')
const successMsg  = ref('')
const showModal   = ref(false)
const searchQuery = ref('')
const sortField   = ref('date')
const sortOrder   = ref('desc')
const today       = new Date().toISOString().split('T')[0]

// ─── Date filter ──────────────────────────────────────────────────────────────
const filterFrom = ref('')
const filterTo   = ref('')

function clearDateFilter() {
  filterFrom.value = ''
  filterTo.value   = ''
}

// ─── Download format picker ───────────────────────────────────────────────────
const showDownloadMenu = ref(false)

const emptyLog = { name: '', date: today, purpose: '', address: '', contactNo: '' }
const newLog   = ref({ ...emptyLog })

// ─── Fetch ────────────────────────────────────────────────────────────────────
async function fetchLogs() {
  loading.value  = true
  errorMsg.value = ''
  try {
    const res  = await axios.get(BASE)
    logs.value = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    errorMsg.value = 'Failed to load logs. Is the server running?'
  } finally {
    loading.value = false
  }
}

// ─── Add ──────────────────────────────────────────────────────────────────────
async function addLog() {
  if (!newLog.value.name || !newLog.value.date || !newLog.value.purpose) {
    errorMsg.value = 'Please fill in Name, Date, and Purpose.'
    return
  }
  saveLoading.value = true
  errorMsg.value    = ''
  try {
    const res = await axios.post(BASE, {
      name:      newLog.value.name,
      date:      newLog.value.date,
      purpose:   newLog.value.purpose,
      address:   newLog.value.address   || null,
      contactNo: newLog.value.contactNo || null
    })
    logs.value.unshift(res.data)
    showSuccess('✅ Log added successfully!')
    closeModal()
  } catch (e) {
    errorMsg.value = '❌ Failed to add log: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    saveLoading.value = false
  }
}

// ─── Delete ───────────────────────────────────────────────────────────────────
async function deleteLog(id) {
  if (!confirm('Are you sure you want to delete this log?')) return
  errorMsg.value = ''
  try {
    await axios.delete(`${BASE}/${id}`)
    logs.value = logs.value.filter(l => l.logID !== id)
    showSuccess('✅ Log deleted.')
  } catch (e) {
    errorMsg.value = '❌ Failed to delete: ' + (e?.response?.data?.message ?? e.message)
  }
}

// ─── Modal ────────────────────────────────────────────────────────────────────
function openModal()  { newLog.value = { ...emptyLog, date: today }; errorMsg.value = ''; showModal.value = true }
function closeModal() { showModal.value = false; newLog.value = { ...emptyLog } }

// ─── Date helpers ─────────────────────────────────────────────────────────────
function normalizeDate(d) {
  if (!d) return ''
  if (Array.isArray(d))
    return `${d[0]}-${String(d[1]).padStart(2,'0')}-${String(d[2]).padStart(2,'0')}`
  return String(d).substring(0, 10)
}
function formatDate(d) {
  const str = normalizeDate(d)
  if (!str) return '—'
  return new Date(str + 'T00:00:00').toLocaleDateString('en-US', { year:'numeric', month:'long', day:'numeric' })
}

// ─── Stats (based on full logs list, not filtered) ───────────────────────────
const totalLogs   = computed(() => logs.value.length)
const todaysLogs  = computed(() => logs.value.filter(l => normalizeDate(l.date) === today).length)
const thisWeekLogs = computed(() => {
  const now = new Date(); const mon = new Date(now)
  mon.setDate(now.getDate() - now.getDay() + 1); mon.setHours(0,0,0,0)
  return logs.value.filter(l => { const d = new Date(normalizeDate(l.date)+'T00:00:00'); return d >= mon && d <= now }).length
})

// ─── Filter + Sort ────────────────────────────────────────────────────────────
const filteredLogs = computed(() => {
  let list = logs.value

  // Date range filter
  if (filterFrom.value) {
    list = list.filter(l => normalizeDate(l.date) >= filterFrom.value)
  }
  if (filterTo.value) {
    list = list.filter(l => normalizeDate(l.date) <= filterTo.value)
  }

  // Search filter
  const q = searchQuery.value.toLowerCase()
  if (q) {
    list = list.filter(l =>
      (l.name      ?? '').toLowerCase().includes(q) ||
      (l.purpose   ?? '').toLowerCase().includes(q) ||
      (l.address   ?? '').toLowerCase().includes(q) ||
      (l.contactNo ?? '').includes(searchQuery.value)
    )
  }
  return list
})

const sortedLogs = computed(() => {
  const copy = [...filteredLogs.value]
  copy.sort((a, b) => {
    const aVal = sortField.value === 'date' ? normalizeDate(a.date) : (a[sortField.value] ?? '')
    const bVal = sortField.value === 'date' ? normalizeDate(b.date) : (b[sortField.value] ?? '')
    const cmp  = String(aVal).localeCompare(String(bVal))
    return sortOrder.value === 'asc' ? cmp : -cmp
  })
  return copy
})

function handleSort(field) {
  if (sortField.value === field) { sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc' }
  else { sortField.value = field; sortOrder.value = 'asc' }
}

// ─── Download CSV ─────────────────────────────────────────────────────────────
function downloadCSV() {
  showDownloadMenu.value = false
  const rows = sortedLogs.value
  if (!rows.length) { alert('No logs to export.'); return }

  const headers = ['#', 'Name', 'Date', 'Purpose', 'Address', 'Contact No.']
  const escape  = v => `"${String(v ?? '').replace(/"/g, '""')}"`
  const lines   = [
    headers.join(','),
    ...rows.map((l, i) => [
      i + 1,
      escape(l.name),
      escape(normalizeDate(l.date)),
      escape(l.purpose),
      escape(l.address),
      escape(l.contactNo)
    ].join(','))
  ]

  const blob = new Blob([lines.join('\n')], { type: 'text/csv;charset=utf-8;' })
  const url  = URL.createObjectURL(blob)
  const a    = document.createElement('a')
  a.href     = url
  a.download = `clinic-logs-${today}.csv`
  a.click()
  URL.revokeObjectURL(url)
  showSuccess('✅ CSV downloaded!')
}

// ─── Download PDF (via print) ─────────────────────────────────────────────────
function downloadPDF() {
  showDownloadMenu.value = false
  const rows = sortedLogs.value
  if (!rows.length) { alert('No logs to export.'); return }

  const rangeLabel = filterFrom.value || filterTo.value
    ? `Period: ${filterFrom.value || '—'} to ${filterTo.value || '—'}`
    : `Generated: ${formatDate(today)}`

  const tableRows = rows.map((l, i) => `
    <tr>
      <td>${i + 1}</td>
      <td>${l.name ?? ''}</td>
      <td>${normalizeDate(l.date)}</td>
      <td>${l.purpose ?? ''}</td>
      <td>${l.address ?? ''}</td>
      <td>${l.contactNo ?? ''}</td>
    </tr>`).join('')

  const html = `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Clinic Logs</title>
  <style>
    body { font-family: Arial, sans-serif; font-size: 12px; padding: 20px; color: #1f2937; }
    h1   { font-size: 20px; margin-bottom: 4px; color: #6b21a8; }
    p.sub{ color: #6b7280; margin-bottom: 16px; font-size: 11px; }
    table{ width: 100%; border-collapse: collapse; }
    th   { background: #f3f4f6; text-align: left; padding: 8px 10px; font-size: 11px;
           text-transform: uppercase; color: #374151; border-bottom: 2px solid #e5e7eb; }
    td   { padding: 7px 10px; border-bottom: 1px solid #e5e7eb; vertical-align: top; }
    tr:nth-child(even) td { background: #faf5ff; }
    .footer { margin-top: 20px; font-size: 10px; color: #9ca3af; text-align: right; }
  </style>
</head>
<body>
  <h1>🏥 Clinic Logs</h1>
  <p class="sub">${rangeLabel} &nbsp;|&nbsp; Total: ${rows.length} record(s)</p>
  <table>
    <thead>
      <tr>
        <th>#</th><th>Name</th><th>Date</th><th>Purpose</th><th>Address</th><th>Contact No.</th>
      </tr>
    </thead>
    <tbody>${tableRows}</tbody>
  </table>
  <div class="footer">Printed on ${new Date().toLocaleString()}</div>
  <script>window.onload = () => { window.print(); window.onafterprint = () => window.close(); }<\/script>
</body>
</html>`

  const win = window.open('', '_blank')
  win.document.write(html)
  win.document.close()
  showSuccess('✅ PDF print dialog opened!')
}

function showSuccess(msg) {
  successMsg.value = msg
  setTimeout(() => { successMsg.value = '' }, 3000)
}

// Close download menu when clicking outside
function handleOutsideClick(e) {
  if (!e.target.closest('#download-menu-wrapper')) showDownloadMenu.value = false
}

onMounted(() => { fetchLogs(); document.addEventListener('click', handleOutsideClick) })
</script>

<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-full mx-auto px-4">

      <!-- Header -->
      <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-800">Clinic Logs</h1>
            <p class="text-gray-500 mt-1">Clinic visitor and patient logs</p>
          </div>
          <div class="flex gap-2 items-center">
            <!-- Refresh -->
            <button @click="fetchLogs" :disabled="loading"
              class="px-4 py-2 border border-gray-300 text-gray-600 rounded-lg hover:bg-gray-50 text-sm flex items-center gap-1 transition">
              <span :class="loading ? 'animate-spin inline-block' : ''">↻</span> Refresh
            </button>

            <!-- Download dropdown -->
            <div id="download-menu-wrapper" class="relative">
              <button @click.stop="showDownloadMenu = !showDownloadMenu"
                class="px-4 py-2 border border-purple-300 text-purple-700 rounded-lg hover:bg-purple-50 text-sm flex items-center gap-1 transition font-semibold">
                ⬇ Download
                <span class="text-xs">▾</span>
              </button>
              <div v-if="showDownloadMenu"
                class="absolute right-0 mt-1 w-44 bg-white border border-gray-200 rounded-lg shadow-lg z-20 overflow-hidden">
                <button @click="downloadCSV"
                  class="w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-purple-50 hover:text-purple-700 flex items-center gap-2">
                  📄 Download CSV
                </button>
                <button @click="downloadPDF"
                  class="w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-purple-50 hover:text-purple-700 flex items-center gap-2 border-t border-gray-100">
                  🖨️ Save as PDF
                </button>
              </div>
            </div>

            <!-- Add Log -->
            <button @click="openModal"
              class="bg-purple-600 hover:bg-purple-700 text-white px-6 py-2 rounded-lg font-semibold transition shadow-md">
              + Add Log
            </button>
          </div>
        </div>
      </div>

      <!-- Alerts -->
      <div v-if="errorMsg"
        class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg flex items-center justify-between text-sm">
        {{ errorMsg }}
        <button @click="errorMsg = ''" class="ml-3 font-bold text-red-400 hover:text-red-600">✕</button>
      </div>
      <div v-if="successMsg"
        class="mb-4 p-3 bg-green-100 text-green-700 rounded-lg flex items-center justify-between text-sm">
        {{ successMsg }}
        <button @click="successMsg = ''" class="ml-3 font-bold text-green-400 hover:text-green-600">✕</button>
      </div>

      <!-- Search + Date Filter bar -->
      <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
        <div class="flex flex-col md:flex-row gap-3 items-start md:items-end">

          <!-- Search -->
          <div class="flex-1">
            <label class="block text-xs font-semibold text-gray-500 mb-1">Search</label>
            <input v-model="searchQuery" type="text"
              placeholder="Search by name, purpose, address, or contact..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm" />
          </div>

          <!-- From date -->
          <div>
            <label class="block text-xs font-semibold text-gray-500 mb-1">From Date</label>
            <input v-model="filterFrom" type="date"
              class="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm" />
          </div>

          <!-- To date -->
          <div>
            <label class="block text-xs font-semibold text-gray-500 mb-1">To Date</label>
            <input v-model="filterTo" type="date"
              class="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent text-sm" />
          </div>

          <!-- Clear filter -->
          <button @click="clearDateFilter"
            :disabled="!filterFrom && !filterTo"
            class="px-4 py-2 text-sm border border-gray-300 text-gray-600 rounded-lg hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition whitespace-nowrap">
            Clear Filter
          </button>
        </div>

        <!-- Active filter badge -->
        <div v-if="filterFrom || filterTo" class="mt-2 flex items-center gap-2">
          <span class="text-xs bg-purple-100 text-purple-700 px-2 py-1 rounded font-medium">
            🗓 Filtered:
            {{ filterFrom ? formatDate(filterFrom) : 'Start' }}
            →
            {{ filterTo ? formatDate(filterTo) : 'End' }}
            &nbsp;({{ sortedLogs.length }} result{{ sortedLogs.length !== 1 ? 's' : '' }})
          </span>
        </div>
      </div>

      <!-- Stats -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <p class="text-sm text-gray-500 mb-1">Total Logs</p>
          <p class="text-3xl font-bold text-gray-800">{{ totalLogs }}</p>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <p class="text-sm text-gray-500 mb-1">Today's Visits</p>
          <p class="text-3xl font-bold text-purple-600">{{ todaysLogs }}</p>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <p class="text-sm text-gray-500 mb-1">This Week</p>
          <p class="text-3xl font-bold text-blue-600">{{ thisWeekLogs }}</p>
        </div>
      </div>

      <!-- Table -->
      <div class="bg-white rounded-lg shadow-sm overflow-hidden">
        <div v-if="loading" class="p-10 text-center text-gray-500">Loading logs...</div>

        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">#</th>
                <th class="px-6 py-4 text-left">
                  <button @click="handleSort('name')"
                    class="flex items-center gap-1 font-semibold text-sm text-gray-700 hover:text-purple-600">
                    Name <span v-if="sortField==='name'">{{ sortOrder==='asc'?'↑':'↓' }}</span>
                  </button>
                </th>
                <th class="px-6 py-4 text-left">
                  <button @click="handleSort('date')"
                    class="flex items-center gap-1 font-semibold text-sm text-gray-700 hover:text-purple-600">
                    Date <span v-if="sortField==='date'">{{ sortOrder==='asc'?'↑':'↓' }}</span>
                  </button>
                </th>
                <th class="px-6 py-4 text-left">
                  <button @click="handleSort('purpose')"
                    class="flex items-center gap-1 font-semibold text-sm text-gray-700 hover:text-purple-600">
                    Purpose <span v-if="sortField==='purpose'">{{ sortOrder==='asc'?'↑':'↓' }}</span>
                  </button>
                </th>
                <th class="px-6 py-4 text-left font-semibold text-sm text-gray-700">Address</th>
                <th class="px-6 py-4 text-left font-semibold text-sm text-gray-700">Contact No.</th>
                <th class="px-6 py-4 text-center font-semibold text-sm text-gray-700">Action</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="(log, index) in sortedLogs" :key="log.logID"
                class="hover:bg-gray-50 transition-colors">
                <td class="px-4 py-3 text-xs text-gray-400">{{ index + 1 }}</td>
                <td class="px-6 py-4 font-medium text-gray-800">{{ log.name }}</td>
                <td class="px-6 py-4 text-sm text-gray-600">{{ formatDate(log.date) }}</td>
                <td class="px-6 py-4 text-sm">
                  <span class="px-2 py-1 bg-purple-100 text-purple-700 rounded text-xs font-medium">
                    {{ log.purpose }}
                  </span>
                </td>
                <td class="px-6 py-4 text-sm text-gray-600">{{ log.address || '—' }}</td>
                <td class="px-6 py-4 text-sm text-gray-600">{{ log.contactNo || '—' }}</td>
                <td class="px-6 py-4 text-center">
                  <button @click="deleteLog(log.logID)"
                    class="text-red-500 hover:text-red-700 text-sm font-medium transition">
                    Delete
                  </button>
                </td>
              </tr>
              <tr v-if="sortedLogs.length === 0">
                <td colspan="7" class="text-center py-12 text-gray-400">
                  {{ searchQuery || filterFrom || filterTo
                    ? 'No logs match your filters.'
                    : 'No logs yet. Click + Add Log to start.' }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="!loading && logs.length > 0"
          class="px-6 py-2 bg-gray-50 border-t text-xs text-gray-400">
          Showing {{ sortedLogs.length }} of {{ logs.length }} log(s)
        </div>
      </div>
    </div>

    <!-- ── ADD LOG MODAL ── -->
    <div v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click.self="closeModal">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-2xl font-bold text-gray-800">Add New Log</h2>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <div v-if="errorMsg" class="mb-4 p-3 bg-red-100 text-red-700 rounded text-sm">{{ errorMsg }}</div>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Name <span class="text-red-500">*</span></label>
            <input v-model="newLog.name" type="text" placeholder="Enter patient name"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Date <span class="text-red-500">*</span></label>
            <input v-model="newLog.date" type="date"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Purpose <span class="text-red-500">*</span></label>
            <select v-model="newLog.purpose"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500">
              <option value="">— Select purpose —</option>
              <option>Prenatal Checkup</option>
              <option>Postnatal Consultation</option>
              <option>Family Planning</option>
              <option>General Checkup</option>
              <option>Immunization</option>
              <option>Follow-up Visit</option>
              <option>Other</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Address</label>
            <input v-model="newLog.address" type="text" placeholder="Enter address"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Contact No.</label>
            <input v-model="newLog.contactNo" type="tel" placeholder="e.g. 09123456789"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500" />
          </div>
        </div>

        <div class="flex gap-3 mt-6">
          <button @click="closeModal"
            class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-semibold transition">
            Cancel
          </button>
          <button @click="addLog" :disabled="saveLoading"
            class="flex-1 px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 font-semibold transition disabled:opacity-50 disabled:cursor-not-allowed">
            {{ saveLoading ? 'Saving...' : 'Add Log' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
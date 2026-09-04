<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { recordReport } from '@/service/reportHistory'

const BASE_URL = 'http://localhost:8080/api/expenses'

const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)

const expenses = ref([])
const filterFrom = ref('')
const filterTo = ref('')

const form = ref({
  category: '',
  description: '',
  amount: '',
  payee: '',
  expenseDate: ''
})

const filteredExpenses = computed(() => expenses.value.filter(expense => {
  const date = expense.expenseDate ? String(expense.expenseDate).slice(0, 10) : ''
  return (!filterFrom.value || date >= filterFrom.value) &&
    (!filterTo.value || date <= filterTo.value)
}))

const filteredTotal = computed(() => filteredExpenses.value.reduce(
  (total, expense) => total + Number(expense.amount || 0), 0
))

function clearDateFilter() {
  filterFrom.value = ''
  filterTo.value = ''
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, character => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;'
  })[character])
}

function printReport() {
  if (!filteredExpenses.value.length) {
    alert('No expenses found for the selected date range.')
    return
  }

  const range = filterFrom.value || filterTo.value
    ? `${filterFrom.value || 'All'} to ${filterTo.value || 'All'}`
    : 'All dates'
  const rows = filteredExpenses.value.map((expense, index) => `<tr>
    <td>${index + 1}</td>
    <td>${escapeHtml(expense.expenseDate ? String(expense.expenseDate).slice(0, 10) : '—')}</td>
    <td>${escapeHtml(expense.category)}</td>
    <td>${escapeHtml(expense.description)}</td>
    <td>₱${Number(expense.amount || 0).toLocaleString('en-PH', { minimumFractionDigits: 2 })}</td>
    <td>${escapeHtml(expense.payee)}</td>
  </tr>`).join('')
  const win = window.open('', '_blank', 'width=1000,height=800')
  if (!win) return

  win.document.write(`<!DOCTYPE html><html><head><title>Expense Report</title><style>
    body{font-family:Arial,sans-serif;color:#1f2937;margin:28px}h1{color:#6b21a8;margin-bottom:4px}
    p{color:#6b7280;font-size:12px}table{width:100%;border-collapse:collapse;margin-top:20px;font-size:12px}
    th{background:#6b21a8;color:white;text-align:left;padding:9px}td{padding:8px;border-bottom:1px solid #e5e7eb}
    tr:nth-child(even){background:#faf5ff}.total{margin-top:18px;text-align:right;font-weight:bold;font-size:15px}
    @media print{body{margin:12mm}}
  </style></head><body><h1>Expense Report</h1><p>Period: ${range} | ${filteredExpenses.value.length} record(s)</p>
  <table><thead><tr><th>#</th><th>Date</th><th>Category</th><th>Description</th><th>Amount</th><th>Payee</th></tr></thead>
  <tbody>${rows}</tbody></table><div class="total">Total Expenses: ₱${filteredTotal.value.toLocaleString('en-PH', { minimumFractionDigits: 2 })}</div>
  <p>Printed on ${new Date().toLocaleString('en-PH')}</p><script>window.onload=()=>{window.print();window.onafterprint=()=>window.close()}<\/script></body></html>`)
  win.document.close()
  recordReport({
    name: 'Expense Report',
    type: 'Expenses',
    period: range,
    details: `${filteredExpenses.value.length} record(s), total ₱${filteredTotal.value.toLocaleString('en-PH', { minimumFractionDigits: 2 })}`
  })
}

// Returns today's date as YYYY-MM-DD using LOCAL time, not UTC — avoids the
// off-by-one-day bug that .toISOString() causes for PH (UTC+8) users.
function todayLocalDateString() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

// Load all expenses from the backend
async function fetchExpenses() {
  try {
    const response = await axios.get(BASE_URL)
    expenses.value = response.data
  } catch (error) {
    console.error('Failed to load expenses', error)
    alert('Failed to load expenses: ' + (error.response?.data || error.message))
  }
}

function openAddModal() {
  isEditing.value = false
  editingId.value = null
  form.value.expenseDate = todayLocalDateString()
  showModal.value = true
}

function openEditModal(expense) {
  isEditing.value = true
  editingId.value = expense.id
  form.value = {
    category: expense.category,
    description: expense.description,
    amount: expense.amount,
    payee: expense.payee,
    expenseDate: expense.expenseDate ? String(expense.expenseDate).slice(0, 10) : todayLocalDateString()
  }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  resetForm()
}

function resetForm() {
  form.value = {
    category: '',
    description: '',
    amount: '',
    payee: '',
    expenseDate: ''
  }
  isEditing.value = false
  editingId.value = null
}

async function saveExpense() {
  if (!form.value.category || !form.value.amount) return

  try {
    if (isEditing.value) {
      await axios.put(BASE_URL, {
        id: editingId.value,
        category: form.value.category,
        description: form.value.description,
        amount: Number(form.value.amount),
        payee: form.value.payee,
        expenseDate: form.value.expenseDate || todayLocalDateString()
      })
    } else {
      await axios.post(BASE_URL, {
        category: form.value.category,
        description: form.value.description,
        amount: Number(form.value.amount),
        payee: form.value.payee,
        expenseDate: form.value.expenseDate || todayLocalDateString() // YYYY-MM-DD, local time
      })
    }

    await fetchExpenses()
    closeModal()
  } catch (error) {
    console.error('Failed to save expense', error)
    alert('Failed to save expense: ' + (error.response?.data || error.message))
  }
}

async function deleteExpense(id) {
  if (!confirm('Are you sure you want to delete this expense?')) return

  try {
    await axios.delete(`${BASE_URL}/${id}`)
    await fetchExpenses()
  } catch (error) {
    console.error('Failed to delete expense', error)
    alert('Failed to delete expense: ' + (error.response?.data || error.message))
  }
}

onMounted(() => {
  fetchExpenses()
})
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold">Expense Records</h2>
      <div class="flex gap-2">
        <button @click="printReport" class="border border-purple-300 text-purple-700 px-4 py-2 rounded-md hover:bg-purple-50">
          Print Report
        </button>
        <button @click="openAddModal" class="bg-purple-700 text-white px-4 py-2 rounded-md">
          + Add Expense
        </button>
      </div>
    </div>

    <div class="bg-white shadow-sm rounded-lg p-4 mb-4 flex flex-col md:flex-row md:items-end gap-3">
      <div>
        <label class="block text-xs font-semibold text-gray-500 mb-1">From Date</label>
        <input v-model="filterFrom" type="date" class="border rounded px-3 py-2 text-sm" />
      </div>
      <div>
        <label class="block text-xs font-semibold text-gray-500 mb-1">To Date</label>
        <input v-model="filterTo" type="date" class="border rounded px-3 py-2 text-sm" />
      </div>
      <button @click="clearDateFilter" :disabled="!filterFrom && !filterTo" class="px-4 py-2 text-sm border rounded text-gray-600 disabled:opacity-40">
        Clear Filter
      </button>
      <p class="md:ml-auto text-sm font-semibold text-gray-600">Filtered Total: ₱{{ filteredTotal.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}</p>
    </div>

    <!-- EXPENSE TABLE -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="w-full text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 text-left">Expense ID</th>
            <th class="p-3 text-left">Date</th>
            <th class="p-3 text-left">Category</th>
            <th class="p-3 text-left">Description</th>
            <th class="p-3 text-left">Amount</th>
            <th class="p-3 text-left">Payee</th>
            <th class="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="expense in filteredExpenses" :key="expense.id" class="border-t">
            <td class="p-3 font-mono text-xs text-gray-500">EXP-{{ String(expense.id).padStart(5, '0') }}</td>
            <td class="p-3 text-gray-600">
              {{ expense.expenseDate ? new Date(expense.expenseDate).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' }) : '—' }}
            </td>
            <td class="p-3">{{ expense.category }}</td>
            <td class="p-3">{{ expense.description }}</td>
            <td class="p-3 font-semibold">₱{{ expense.amount }}</td>
            <td class="p-3">{{ expense.payee }}</td>
            <td class="p-3 space-x-3">
              <button
                @click="openEditModal(expense)"
                class="text-blue-600 hover:underline"
              >
                Edit
              </button>
              <button
                @click="deleteExpense(expense.id)"
                class="text-red-600 hover:underline"
              >
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="expenses.length === 0" class="text-center py-8 text-gray-400 text-sm">
        No expenses recorded yet.
      </div>
      <div v-else-if="filteredExpenses.length === 0" class="text-center py-8 text-gray-400 text-sm">
        No expenses found for the selected date range.
      </div>
    </div>

    <!-- MODAL -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    >
      <div class="bg-white w-full max-w-lg rounded-lg p-6 shadow-lg">
        <h3 class="text-lg font-semibold mb-1">{{ isEditing ? 'Edit Expense' : 'Add Expense' }}</h3>
        <p v-if="isEditing" class="text-xs text-gray-400 font-mono mb-4">
          EXP-{{ String(editingId).padStart(5, '0') }}
        </p>
        <div v-else class="mb-4"></div>

        <div class="space-y-3">
          <div>
            <label class="text-sm font-medium">Date</label>
            <input type="date" v-model="form.expenseDate" class="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label class="text-sm font-medium">Expense Category</label>
            <select v-model="form.category" class="w-full border rounded px-3 py-2">
              <option disabled value="">Select Category</option>
              <option>Medical Supplies</option>
              <option>Utilities</option>
              <option>Equipment</option>
            </select>
          </div>

          <div>
            <label class="text-sm font-medium">Description</label>
            <textarea v-model="form.description" class="w-full border rounded px-3 py-2"></textarea>
          </div>

          <div>
            <label class="text-sm font-medium">Amount (₱)</label>
            <input type="number" v-model="form.amount" class="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label class="text-sm font-medium">Payee</label>
            <input v-model="form.payee" class="w-full border rounded px-3 py-2" />
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeModal" class="px-6 py-2 bg-gray-200 rounded-md">
            Cancel
          </button>
          <button @click="saveExpense" class="px-6 py-2 bg-purple-700 text-white rounded-md">
            {{ isEditing ? 'Update' : 'Save' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
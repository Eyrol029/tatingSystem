<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const showViewModal = ref(false)
const showAddModal = ref(false)
const showPaymentModal = ref(false)
const showSOAModal = ref(false)
const showReceiptModal = ref(false)
const selectedSOA = ref(null)
const soaList = ref([])
const searchQuery = ref('')
const paymentMessage = ref('')
const paymentForm = ref({
  serviceName: '',
  diagnosis: '',
  paymentDate: '',
  totalAmount: '',
  paidAmount: ''
})

function parseNumber(value) {
  if (value === null || value === undefined) return 0
  const normalized = String(value).replace(/,/g, '').trim()
  return Number(normalized) || 0
}

// Tries every likely casing/field name the backend might be using for the SOA's ID.
function resolveSoaId(soa) {
  if (!soa) return null
  return (
    soa.soaId ??
    soa.soaID ??
    soa.id ??
    soa.ID ??
    soa.statementId ??
    soa.statementOfAccountId ??
    null
  )
}

const paymentBalance = computed(() => {
  const total = parseNumber(paymentForm.value.totalAmount)
  const paid = parseNumber(paymentForm.value.paidAmount)
  return Math.max(0, total - paid)
})

const filteredSoaList = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()
  if (!query) return soaList.value
  return soaList.value.filter((soa) => {
    return [
      String(soa.patientName || ''),
      String(soa.patientId || ''),
      String(soa.otherDiagnosis || '')
    ].some((field) => field.toLowerCase().includes(query))
  })
})

const newSOA = ref({
  patientId: '',
  patientName: '',
  otherDiagnosis: '',
  date: '',
  services: []
})

const newService = ref({ name: '', amount: '' })

const BASE_URL = 'http://localhost:8080/api/billing/soa'
const DASHBOARD_URL = `${BASE_URL}/dashboard`

async function loadSoaList() {
  try {
    const response = await axios.get(DASHBOARD_URL)

    // TEMP DEBUG — check your browser Console tab after refreshing.
    // Remove this line once you've confirmed the real ID field name.
    console.log('RAW SOA DATA:', response.data)

    soaList.value = response.data.map((soa) => {
      const resolvedId = resolveSoaId(soa)
      return {
        id: resolvedId,
        soaId: resolvedId,
        patientId: soa.patientId,
        patientName: soa.patientName,
        date: soa.dueDate || soa.invoiceReceiptNumber || '',
        otherDiagnosis: soa.description || soa.serviceName || 'Statement of Account',
        serviceName: soa.serviceName || 'SOA Service',
        totalAmount: soa.totalAmount || 0,
        amountPaid: soa.amountPaid || 0,
        balanceAmount: soa.balanceAmount || 0,
        services: [
          {
            name: soa.serviceName || 'SOA Service',
            amount: soa.totalAmount || 0
          }
        ]
      }
    })
  } catch (error) {
    console.error('Failed to load SOA list', error)
  }
}

function openView(soa) {
  selectedSOA.value = soa
  showViewModal.value = true
}

function closeView() {
  showViewModal.value = false
}

function goToPaymentDashboard(soa) {
  if (!soa.patientId) return
  router.push({ path: '/uikit/PaymentDashboard', query: { patientId: soa.patientId } })
}

function openAddPayment(soa) {
  selectedSOA.value = { ...soa }
  paymentForm.value.serviceName = soa.serviceName || ''
  paymentForm.value.diagnosis = soa.otherDiagnosis || ''
  paymentForm.value.paymentDate = new Date().toISOString().slice(0, 10)
  paymentForm.value.totalAmount = soa.totalAmount != null ? String(soa.totalAmount) : ''
  paymentForm.value.paidAmount = soa.amountPaid != null ? String(soa.amountPaid) : ''
  paymentMessage.value = ''
  showPaymentModal.value = true
}

function closePaymentModal() {
  showPaymentModal.value = false
  showReceiptModal.value = false
  paymentForm.value.totalAmount = ''
  paymentForm.value.paidAmount = ''
  paymentForm.value.paymentDate = ''
  paymentMessage.value = ''
}

function openReceiptModal() {
  showReceiptModal.value = true
}

function closeReceiptModal() {
  showReceiptModal.value = false
}

function openSOAModal() {
  showSOAModal.value = true
}

function closeSOAModal() {
  showSOAModal.value = false
}

function printReceipt() {
  window.print()
}

async function addPayment() {
  if (!selectedSOA.value) return

  const totalAmount = parseNumber(paymentForm.value.totalAmount)
  const paidAmount = parseNumber(paymentForm.value.paidAmount)

  if (!totalAmount || totalAmount <= 0) {
    paymentMessage.value = 'Enter a valid total amount.'
    return
  }

  if (paidAmount < 0) {
    paymentMessage.value = 'Paid amount cannot be negative.'
    return
  }

  if (paidAmount > totalAmount) {
    paymentMessage.value = 'Paid amount cannot exceed total amount.'
    return
  }

  let soaId = resolveSoaId(selectedSOA.value)
  if (!soaId) {
    try {
      const createResponse = await axios.post(BASE_URL, {
        patientID: Number(selectedSOA.value.patientId),
        patientServiceID: null,
        totalAmount: totalAmount,
        amountPaid: 0.0,
        balanceAmount: totalAmount,
        description: paymentForm.value.diagnosis || 'Statement of Account',
        invoiceReceiptNumber: 'SOA-' + Math.floor(Math.random() * 10000),
        dueDate: paymentForm.value.paymentDate ? new Date(paymentForm.value.paymentDate).toISOString() : null
      })
      const createdSoa = createResponse.data
      soaId = resolveSoaId(createdSoa)
      if (!soaId) {
        throw new Error('Failed to resolve ID for the newly created SOA.')
      }
    } catch (createError) {
      console.error('Failed to auto-create SOA for payment', createError)
      paymentMessage.value = 'Failed to initialize Statement of Account for this patient.'
      return
    }
  }

  try {
    await axios.post(`${BASE_URL}/${soaId}/payments`, {
      amount: paidAmount,
      totalAmount,
      paymentDate: paymentForm.value.paymentDate ? new Date(paymentForm.value.paymentDate).toISOString() : null,
      paymentMethod: 'Cash',
      notes: `${paymentForm.value.serviceName} - ${paymentForm.value.diagnosis}`
    })
    paymentMessage.value = 'Payment recorded successfully.'
    await loadSoaList()
    showPaymentModal.value = false
  } catch (error) {
    console.error('Failed to record payment', error)
    paymentMessage.value = error.response?.data || error.message || 'Unable to record payment.'
  }
}

function addService() {
  if (!newService.value.name || !newService.value.amount) return
  newSOA.value.services.push({
    name: newService.value.name,
    amount: Number(newService.value.amount)
  })
  newService.value = { name: '', amount: '' }
}

function removeService(index) {
  newSOA.value.services.splice(index, 1)
}

async function saveSOA() {
  if (!newSOA.value.patientName || !newSOA.value.patientId) return

  try {
    await axios.post(BASE_URL, {
      patientID: Number(newSOA.value.patientId),
      patientServiceID: null,
      totalAmount: newSOA.value.services.reduce((sum, item) => sum + item.amount, 0),
      amountPaid: 0.0,
      balanceAmount: newSOA.value.services.reduce((sum, item) => sum + item.amount, 0),
      description: newSOA.value.otherDiagnosis,
      invoiceReceiptNumber: 'SOA-' + Math.floor(Math.random() * 10000),
      dueDate: newSOA.value.date ? new Date(newSOA.value.date).toISOString() : null
    })
    await loadSoaList()
    showAddModal.value = false
    newSOA.value = { patientId: '', patientName: '', otherDiagnosis: '', date: '', services: [] }
  } catch (error) {
    console.error('Failed to save SOA', error)
  }
}

function totalAmount(services) {
  return services.reduce((sum, s) => sum + s.amount, 0)
}

function formatCurrency(value) {
  return '₱' + Number(value).toLocaleString()
}

function searchPatient() {
  // Computed filteredSoaList updates automatically when searchQuery changes.
  if (!searchQuery.value.trim()) {
    searchQuery.value = ''
  }
}

function clearSearch() {
  searchQuery.value = ''
}

onMounted(() => {
  loadSoaList()
})
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-6">
      <div>
        <h2 class="text-2xl font-bold">Statement of Account</h2>
      </div>
      <button
        @click="showAddModal = true"
        class="bg-blue-600 text-white px-4 py-2 rounded"
      >
        + Add SOA
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-[1.5fr_auto] gap-3 mb-6">
      <input
        v-model="searchQuery"
        @keyup.enter="searchPatient"
        type="text"
        placeholder="Search patient name, ID, or diagnosis"
        class="w-full border rounded px-4 py-2"
      />
      <div class="flex gap-2">
        <button
          @click="searchPatient"
          class="bg-blue-600 text-white px-4 py-2 rounded w-full"
        >
          Search
        </button>
        <button
          @click="clearSearch"
          class="bg-gray-200 text-gray-700 px-4 py-2 rounded w-full"
        >
          Clear
        </button>
      </div>
    </div>

    <!-- SOA LIST -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div v-for="soa in filteredSoaList" :key="soa.id" class="bg-white p-4 rounded shadow">
        <div class="flex justify-between">
          <div>
            <p class="font-semibold">{{ soa.patientName }}</p>
            <p class="text-sm text-gray-500">{{ soa.date }}</p>
          </div>
          <div class="space-x-3">
            <button
              @click="openView(soa)"
              class="text-blue-600 hover:underline"
            >
              View
            </button>
            <button
              @click="openAddPayment(soa)"
              class="text-indigo-600 hover:underline"
            >
              Add Payment
            </button>
            <button
              @click="goToPaymentDashboard(soa)"
              class="text-green-600 hover:underline"
            >
              Payments
            </button>
          </div>
        </div>

        <p class="mt-2 text-sm">Total: {{ formatCurrency(soa.totalAmount) }}</p>
        <p class="text-sm text-gray-600">Paid: {{ formatCurrency(soa.amountPaid) }}</p>
        <p class="text-sm text-gray-600">Balance: {{ formatCurrency(soa.balanceAmount) }}</p>
      </div>
    </div>

    <!-- VIEW SOA MODAL -->
    <div v-if="showViewModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-gray-200 p-6 rounded-lg w-full max-w-3xl">
        <h2 class="text-xl font-bold mb-4">Statement of Account</h2>

        <p><strong>Patient:</strong> {{ selectedSOA.patientName }}</p>
        <p><strong>Patient ID:</strong> {{ selectedSOA.patientId }}</p>
        <p><strong>Diagnosis:</strong> {{ selectedSOA.otherDiagnosis }}</p>

        <div class="mt-4 border rounded">
          <div class="bg-gray-300 px-4 py-2 font-semibold flex justify-between">
            <span>Service</span><span>Amount</span>
          </div>

          <div v-for="(s, i) in selectedSOA.services" :key="i" class="flex justify-between px-4 py-2 border-t">
            <span>{{ s.name }}</span>
            <span>{{ formatCurrency(s.amount) }}</span>
          </div>

          <div class="flex justify-between px-4 py-2 font-bold bg-gray-100">
            <span>Total</span>
            <span>{{ formatCurrency(totalAmount(selectedSOA.services)) }}</span>
          </div>
        </div>

        <div class="flex justify-end mt-4 gap-2">
          <button @click="closeView" class="px-5 py-2 bg-gray-700 text-white rounded">
            Close
          </button>
        </div>
      </div>
    </div>

    <!-- ADD PAYMENT MODAL -->
    <div v-if="showPaymentModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-full max-w-md">
        <h2 class="text-xl font-bold mb-4">Patient Receipt Payment</h2>
        <p class="mb-4 text-sm text-gray-600">Patient: {{ selectedSOA.patientName }}</p>

        <div class="grid grid-cols-1 gap-4">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Total Amount</span>
              <input
                v-model="paymentForm.totalAmount"
                type="text"
                inputmode="decimal"
                class="w-full border rounded px-4 py-2"
                placeholder="Enter total amount"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Paid Amount</span>
              <input
                v-model="paymentForm.paidAmount"
                type="text"
                inputmode="decimal"
                class="w-full border rounded px-4 py-2"
                placeholder="Enter paid amount"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Total Balance</span>
              <input
                :value="formatCurrency(paymentBalance)"
                type="text"
                readonly
                class="w-full border bg-gray-100 rounded px-4 py-2"
              />
            </label>
          </div>

          <label class="block">
            <span class="text-sm font-semibold mb-1 block">Service Name</span>
            <input
              v-model="paymentForm.serviceName"
              type="text"
              class="w-full border rounded px-4 py-2"
              placeholder="Service name"
            />
          </label>

          <label class="block">
            <span class="text-sm font-semibold mb-1 block">Diagnosis</span>
            <input
              v-model="paymentForm.diagnosis"
              type="text"
              class="w-full border rounded px-4 py-2"
              placeholder="Diagnosis"
            />
          </label>

          <label class="block">
            <span class="text-sm font-semibold mb-1 block">Date</span>
            <input
              v-model="paymentForm.paymentDate"
              type="date"
              class="w-full border rounded px-4 py-2"
            />
          </label>
        </div>

        <div class="flex flex-col sm:flex-row items-center gap-3 mt-4">
          <button @click="addPayment" class="bg-green-600 text-white px-4 py-2 rounded">Save Payment</button>
          <button @click="printReceipt" class="bg-purple-600 text-white px-4 py-2 rounded">Print Receipt</button>
          <button @click="openSOAModal" class="bg-blue-600 text-white px-4 py-2 rounded">SOA</button>
          <button @click="closePaymentModal" class="bg-gray-200 text-gray-700 px-4 py-2 rounded">Cancel</button>
        </div>

        <p v-if="paymentMessage" class="mt-4 text-sm text-gray-700">{{ paymentMessage }}</p>
      </div>
    </div>

    <!-- SOA MODAL (from within payment) -->
    <div v-if="showSOAModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-full max-w-md border-2 border-blue-600">
        <h2 class="text-xl font-bold mb-4">Statement Of Account</h2>
        <p class="text-sm text-gray-600 mb-3">This is the statement of account for the current payment details.</p>

        <div class="space-y-3">
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Patient</span>
            <span>{{ selectedSOA.patientName }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Service</span>
            <span>{{ paymentForm.serviceName || selectedSOA.serviceName || 'N/A' }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Diagnosis</span>
            <span>{{ paymentForm.diagnosis || selectedSOA.otherDiagnosis || 'N/A' }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Amount Paid Now</span>
            <span>{{ formatCurrency(parseNumber(paymentForm.paidAmount)) }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Remaining Balance</span>
            <span>{{ formatCurrency(paymentBalance) }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Payment Date</span>
            <span>{{ paymentForm.paymentDate || 'Not set' }}</span>
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="printReceipt" class="bg-purple-600 text-white px-4 py-2 rounded">Print SOA
          </button>
          <button @click="closeSOAModal" class="bg-gray-200 text-gray-700 px-4 py-2 rounded">Close</button>
        </div>
      </div>
    </div>

    <!-- ADD SOA MODAL -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-2xl p-6 rounded-lg">
        <h2 class="text-xl font-bold mb-4">Add Statement of Account</h2>

        <div class="grid grid-cols-2 gap-4">
          <input v-model="newSOA.patientId" placeholder="Patient ID" class="border p-2 rounded" />
          <input v-model="newSOA.patientName" placeholder="Patient Name" class="border p-2 rounded" />
          <input v-model="newSOA.date" type="date" class="border p-2 rounded" />
          <input v-model="newSOA.otherDiagnosis" placeholder="Diagnosis" class="border p-2 rounded" />
        </div>

        <h3 class="mt-4 font-semibold">Services</h3>
        <div class="flex gap-2 mt-2">
          <input v-model="newService.name" placeholder="Service Name" class="border p-2 flex-1" />
          <input v-model="newService.amount" type="number" placeholder="Amount" class="border p-2 w-32" />
          <button @click="addService" class="bg-blue-600 text-white px-3 rounded">Add</button>
        </div>

        <ul class="mt-3 space-y-1">
          <li v-for="(s, i) in newSOA.services" :key="i" class="flex justify-between bg-gray-100 p-2 rounded">
            <span>{{ s.name }} - ₱{{ s.amount }}</span>
            <button @click="removeService(i)" class="text-red-600">✕</button>
          </li>
        </ul>

        <div class="flex justify-end mt-4 gap-3">
          <button @click="showAddModal = false" class="px-4 py-2 bg-gray-300 rounded">Cancel</button>
          <button @click="saveSOA" class="px-4 py-2 bg-blue-600 text-white rounded">Save</button>
        </div>
      </div>
    </div>

  </div>
</template>
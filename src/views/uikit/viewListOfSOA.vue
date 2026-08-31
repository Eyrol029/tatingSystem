<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const showViewModal = ref(false)
const showAddModal = ref(false)
const caseNumberInput = ref('')
const caseNumberMessage = ref('')
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
  paidAmount: '',
  discountName: '',
  discountAmount: ''
})

// Services availed for the CURRENT payment session (can be many per patient)
const availedServices = ref([])

const servicesList = ref([])
const selectedServiceId = ref('')

function parseNumber(value) {
  if (value === null || value === undefined) return 0
  const normalized = String(value).replace(/,/g, '').trim()
  return Number(normalized) || 0
}

function isPlaceholderServiceName(name) {
  const normalized = (name || '').trim().toLowerCase()
  return !normalized || normalized === 'no services' || normalized === 'soa service'
}

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

// New Balance = Total Amount minus already paid minus amount being paid now
const paymentBalance = computed(() => {
  const total = parseNumber(paymentForm.value.totalAmount)
  const alreadyPaid = selectedSOA.value ? parseNumber(selectedSOA.value.amountPaid) : 0
  const paidNow = parseNumber(paymentForm.value.paidAmount)
  return Math.max(0, total - alreadyPaid - paidNow)
})

// Total New Balance After Discount
const totalNewBalanceAfterDiscount = computed(() => {
  const discount = parseNumber(paymentForm.value.discountAmount)
  return Math.max(0, paymentBalance.value - discount)
})

const availedServicesTotal = computed(() => {
  return availedServices.value.reduce((sum, s) => sum + parseNumber(s.amount), 0)
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

const BASE_URL = 'http://localhost:8080/api/billing/soa'
const DASHBOARD_URL = `${BASE_URL}/dashboard`
const INSTALLMENTS_URL = 'http://localhost:8080/api/billing/installments'
const REVENUE_URL = 'http://localhost:8080/api/revenue'

async function loadServicesList() {
  try {
    const response = await axios.get('http://localhost:8080/api/clinical-services')
    servicesList.value = response.data
  } catch (error) {
    console.error('Failed to load clinical services list', error)
  }
}

function onPaymentServiceChange() {
  if (selectedServiceId.value === 'custom') {
    paymentForm.value.serviceName = ''
    paymentForm.value.totalAmount = ''
  } else {
    const service = servicesList.value.find(s => s.id === Number(selectedServiceId.value))
    if (service) {
      paymentForm.value.serviceName = service.name
      paymentForm.value.totalAmount = String(service.price)
    } else {
      paymentForm.value.serviceName = ''
      paymentForm.value.totalAmount = ''
    }
  }
}

function addAvailedService() {
  if (selectedServiceId.value === 'custom') {
    if (!paymentForm.value.serviceName || !paymentForm.value.totalAmount) return
    availedServices.value.push({
      name: paymentForm.value.serviceName,
      amount: parseNumber(paymentForm.value.totalAmount)
    })
  } else {
    if (!selectedServiceId.value) return
    const service = servicesList.value.find(s => s.id === Number(selectedServiceId.value))
    if (!service) return
    availedServices.value.push({ name: service.name, amount: service.price })
  }

  selectedServiceId.value = ''
  paymentForm.value.serviceName = ''
  paymentForm.value.totalAmount = String(availedServicesTotal.value)
}

function addDiscount() {
  const amount = parseNumber(paymentForm.value.discountAmount)
  const name = paymentForm.value.discountName.trim()

  if (!amount || amount <= 0) return
  if (!name) return

  availedServices.value.push({
    name,
    amount: -amount,
    isDiscount: true
  })

  paymentForm.value.totalAmount = String(availedServicesTotal.value)
  paymentForm.value.discountName = ''
  paymentForm.value.discountAmount = ''
}

function removeAvailedService(index) {
  availedServices.value.splice(index, 1)
  paymentForm.value.totalAmount = String(availedServicesTotal.value)
}

async function loadSoaList() {
  try {
    const response = await axios.get(DASHBOARD_URL)
    soaList.value = response.data.map((soa) => {
      const resolvedId = resolveSoaId(soa)
      return {
        id: resolvedId,
        soaId: resolvedId,
        patientId: soa.patientId,
        patientName: soa.patientName,
        caseNumber: soa.caseNumber || '',
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

// UPDATE: Gi-konek ang View function aron mag-load sa tanang detalye ug service breakdown
async function openView(soa) {
  selectedSOA.value = { ...soa }
  
  // E-set ang default services gikan sa SOA record
  availedServices.value = (soa.serviceName && !isPlaceholderServiceName(soa.serviceName))
    ? [{ name: soa.serviceName, amount: soa.totalAmount || 0, isDiscount: false }]
    : []

  const soaId = resolveSoaId(soa)
  if (soaId) {
    try {
      const response = await axios.get(`${INSTALLMENTS_URL}/soa/${soaId}`, {
        params: { _: Date.now() },
        headers: { 'Cache-Control': 'no-cache' }
      })
      const installments = response.data || []
      const latest = installments.length ? installments[installments.length - 1] : null

      if (latest?.serviceBreakdown) {
        try {
          const parsedBreakdown = JSON.parse(latest.serviceBreakdown)
          if (Array.isArray(parsedBreakdown) && parsedBreakdown.length) {
            availedServices.value = parsedBreakdown
          }
        } catch (parseError) {
          console.error('Failed to parse saved service breakdown', parseError)
        }
      }
    } catch (error) {
      console.error('Failed to load saved installment breakdown', error)
    }
  }

  showViewModal.value = true
}

function closeView() {
  showViewModal.value = false
}

function goToPaymentDashboard(soa) {
  if (!soa.patientId) return
  router.push({ path: '/uikit/viewListOfSOA', query: { patientId: soa.patientId } })
}

function goToMySoa(soa) {
  if (!soa?.patientId) return
  router.push(`/uikit/MySOA/${soa.patientId}`)
}

async function openAddPayment(soa) {
  selectedSOA.value = { ...soa }
  paymentForm.value.serviceName = soa.serviceName || ''
  paymentForm.value.diagnosis = soa.otherDiagnosis || ''
  paymentForm.value.paymentDate = todayLocalDateString()
  paymentForm.value.totalAmount = soa.totalAmount != null ? String(soa.totalAmount) : ''
  paymentForm.value.paidAmount = ''
  paymentForm.value.discountName = ''
  paymentForm.value.discountAmount = ''

  availedServices.value = (soa.serviceName && !isPlaceholderServiceName(soa.serviceName))
    ? [{ name: soa.serviceName, amount: soa.totalAmount || 0, isDiscount: false }]
    : []

  if (soa.serviceName) {
    const found = servicesList.value.find(s => s.name.trim().toLowerCase() === soa.serviceName.trim().toLowerCase())
    if (found) {
      selectedServiceId.value = found.id
    } else {
      selectedServiceId.value = 'custom'
    }
  } else {
    selectedServiceId.value = ''
  }

  paymentMessage.value = ''
  showPaymentModal.value = true

  const soaId = resolveSoaId(soa)
  if (soaId) {
    try {
      const response = await axios.get(`${INSTALLMENTS_URL}/soa/${soaId}`, {
        params: { _: Date.now() },
        headers: { 'Cache-Control': 'no-cache' }
      })
      const installments = response.data || []
      const latest = installments.length ? installments[installments.length - 1] : null

      if (latest?.serviceBreakdown) {
        try {
          const parsedBreakdown = JSON.parse(latest.serviceBreakdown)
          if (Array.isArray(parsedBreakdown) && parsedBreakdown.length) {
            availedServices.value = parsedBreakdown
          }
        } catch (parseError) {
          console.error('Failed to parse saved service breakdown', parseError)
        }
      }

      if (latest?.discountName) {
        paymentForm.value.discountName = latest.discountName
      }
    } catch (error) {
      console.error('Failed to load saved installment breakdown', error)
    }
  }
}

function closePaymentModal() {
  showPaymentModal.value = false
  showReceiptModal.value = false
  paymentForm.value.totalAmount = ''
  paymentForm.value.paidAmount = ''
  paymentForm.value.paymentDate = ''
  paymentForm.value.discountName = ''
  paymentForm.value.discountAmount = ''
  paymentMessage.value = ''
  selectedServiceId.value = ''
  availedServices.value = []
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

  const grossAmount = parseNumber(paymentForm.value.totalAmount)
  const discountAmount = parseNumber(paymentForm.value.discountAmount)
  const paidAmount = parseNumber(paymentForm.value.paidAmount)
  const alreadyPaid = selectedSOA.value ? parseNumber(selectedSOA.value.amountPaid) : 0

  if (!grossAmount || grossAmount <= 0) {
    paymentMessage.value = 'Enter a valid total amount.'
    return
  }

  if (discountAmount < 0) {
    paymentMessage.value = 'Discount cannot be negative.'
    return
  }

  if (discountAmount > 0 && !paymentForm.value.discountName.trim()) {
    paymentMessage.value = 'Enter a name for the discount.'
    return
  }

  if (paidAmount < 0) {
    paymentMessage.value = 'Paid amount cannot be negative.'
    return
  }

  const remainingBalance = grossAmount - alreadyPaid
  if (paidAmount > remainingBalance) {
    paymentMessage.value = `Paid amount cannot exceed the remaining balance of ${formatCurrency(remainingBalance)}.`
    return
  }

  const totalAmount = grossAmount

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

  const serviceNames = availedServices.value
    .filter(s => !s.isDiscount && !isPlaceholderServiceName(s.name))
    .map(s => s.name)
    .join(', ') || (isPlaceholderServiceName(paymentForm.value.serviceName) ? '' : paymentForm.value.serviceName)

  const committedDiscounts = availedServices.value
    .filter(s => s.isDiscount)
    .map(s => `${s.name} (-${formatCurrency(Math.abs(s.amount))})`)

  if (discountAmount > 0 && paymentForm.value.discountName.trim()) {
    committedDiscounts.push(`${paymentForm.value.discountName} (-${formatCurrency(discountAmount)})`)
  }

  const discountNote = committedDiscounts.length
    ? ` | Discount: ${committedDiscounts.join(', ')}`
    : ''

  try {
    const finalBreakdown = availedServices.value.filter(
      s => s.isDiscount || !isPlaceholderServiceName(s.name)
    )
    if (discountAmount > 0 && paymentForm.value.discountName.trim()) {
      finalBreakdown.push({
        name: paymentForm.value.discountName.trim(),
        amount: -discountAmount,
        isDiscount: true
      })
    }

    const discountEntries = finalBreakdown.filter(s => s.isDiscount)
    const aggregatedDiscountName = discountEntries.map(s => s.name).join(', ') || null
    const aggregatedDiscountAmount = discountEntries.reduce((sum, s) => sum + Math.abs(s.amount), 0)

    const payload = {
      amount: paidAmount,
      totalAmount,
      paymentDate: paymentForm.value.paymentDate ? new Date(paymentForm.value.paymentDate).toISOString() : null,
      paymentMethod: 'Cash',
      notes: `${serviceNames} - ${paymentForm.value.diagnosis}${discountNote}`,
      discountName: aggregatedDiscountName,
      discountAmount: aggregatedDiscountAmount || null,
      serviceBreakdown: JSON.stringify(finalBreakdown)
    }

    await axios.post(`${BASE_URL}/${soaId}/payments`, payload)
    paymentMessage.value = 'Payment recorded successfully.'

    if (paidAmount > 0) {
      try {
        await axios.post(REVENUE_URL, {
          dealer: selectedSOA.value.patientName,
          patientID: selectedSOA.value.patientId,
          amount: paidAmount,
          description: `Payment for: ${serviceNames}${discountNote}`,
          revenueDate: paymentForm.value.paymentDate || todayLocalDateString()
        })
      } catch (revenueError) {
        console.error('Payment saved, but failed to auto-record revenue', revenueError)
      }
    }

    await loadSoaList()
    showPaymentModal.value = false
    router.push({ path: '/uikit/viewListOfSOA', query: { patientId: selectedSOA.value?.patientId || undefined } })
  } catch (error) {
    console.error('Failed to record payment', error)
    paymentMessage.value = error.response?.data || error.message || 'Unable to record payment.'
  }
}

function openCaseNumberModal() {
  caseNumberInput.value = soaList.value.find(soa => soa.caseNumber)?.caseNumber || ''
  caseNumberMessage.value = ''
  showAddModal.value = true
}

async function saveCaseNumber() {
  if (!caseNumberInput.value.trim()) {
    caseNumberMessage.value = 'Please enter a case number.'
    return
  }
  try {
    await axios.put(`${BASE_URL}/case-number`, {
      caseNumber: caseNumberInput.value.trim()
    })
    await loadSoaList()
    showAddModal.value = false
  } catch (error) {
    console.error('Failed to save patient SOA case number', error)
    caseNumberMessage.value = 'Unable to save the case number.'
  }
}

function todayLocalDateString() {
  const now = new Date()
  const yyyy = now.getFullYear()
  const mm = String(now.getMonth() + 1).padStart(2, '0')
  const dd = String(now.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

function formatCurrency(value) {
  return '₱' + Number(value).toLocaleString()
}

function searchPatient() {
  if (!searchQuery.value.trim()) {
    searchQuery.value = ''
  }
}

function clearSearch() {
  searchQuery.value = ''
}

onMounted(() => {
  loadSoaList()
  loadServicesList()
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
        @click="openCaseNumberModal"
        class="bg-teal-600 text-white px-4 py-2 rounded hover:bg-teal-700"
      >
        Add Case Number
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
            <p v-if="soa.caseNumber" class="text-sm font-medium text-teal-700">Case: {{ soa.caseNumber }}</p>
          </div>
          <div class="space-x-3">
            <button
              @click="goToMySoa(soa)"
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
          </div>
        </div>

        <p class="mt-2 text-sm">Total: {{ formatCurrency(soa.totalAmount) }}</p>
        <p class="text-sm text-gray-600">Paid: {{ formatCurrency(soa.amountPaid) }}</p>
        <p class="text-sm text-gray-600">Balance: {{ formatCurrency(soa.balanceAmount) }}</p>
      </div>
    </div>

    <!-- VIEW SOA MODAL (UPDATE: Pareho na ang sulod ug function sa SOA Modal) -->
    <div v-if="showViewModal && selectedSOA" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-full max-w-md border-2 border-blue-600 shadow-xl">
        <h2 class="text-xl font-bold mb-2">Statement Of Account</h2>
        <p class="text-sm text-gray-600 mb-4">Patient Statement & Billing Summary</p>

        <div class="space-y-3">
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Patient Name:</span>
            <span>{{ selectedSOA.patientName }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Patient ID:</span>
            <span>{{ selectedSOA.patientId }}</span>
          </div>

          <div v-if="availedServices.length" class="border rounded my-2">
            <div class="bg-gray-100 px-3 py-1 font-semibold text-xs border-b">Availed Services</div>
            <div
              v-for="(s, i) in availedServices"
              :key="i"
              class="flex justify-between px-3 py-1 text-sm border-t border-gray-100"
              :class="{ 'text-red-600': s.isDiscount }"
            >
              <span>{{ s.name }}<span v-if="s.isDiscount" class="text-xs italic"> (Discount)</span></span>
              <span>{{ s.isDiscount ? '- ' : '' }}{{ formatCurrency(Math.abs(s.amount)) }}</span>
            </div>
          </div>
          <div v-else class="flex justify-between text-sm">
            <span class="font-semibold">Service:</span>
            <span>{{ selectedSOA.serviceName || 'N/A' }}</span>
          </div>

          <div class="flex justify-between text-sm">
            <span class="font-semibold">Diagnosis:</span>
            <span>{{ selectedSOA.otherDiagnosis || 'N/A' }}</span>
          </div>

          <hr class="my-2" />

          <div class="flex justify-between text-sm">
            <span class="font-semibold">Total Amount:</span>
            <span class="font-bold">{{ formatCurrency(selectedSOA.totalAmount) }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Amount Paid:</span>
            <span class="text-green-600">{{ formatCurrency(selectedSOA.amountPaid) }}</span>
          </div>
          <div class="flex justify-between text-sm">
            <span class="font-semibold">Remaining Balance:</span>
            <span class="text-red-600 font-bold">{{ formatCurrency(selectedSOA.balanceAmount) }}</span>
          </div>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="printReceipt" class="bg-purple-600 text-white px-4 py-2 rounded text-sm hover:bg-purple-700">
            Print SOA
          </button>
          <button @click="closeView" class="bg-gray-200 text-gray-700 px-4 py-2 rounded text-sm hover:bg-gray-300">
            Close
          </button>
        </div>
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
            <span>{{ selectedSOA?.patientName }}</span>
          </div>

          <div v-if="availedServices.length" class="border rounded">
            <div class="bg-gray-100 px-3 py-1 font-semibold text-xs">Availed Services</div>
            <div
              v-for="(s, i) in availedServices"
              :key="i"
              class="flex justify-between px-3 py-1 border-t text-sm"
              :class="{ 'text-red-600': s.isDiscount }"
            >
              <span>{{ s.name }}<span v-if="s.isDiscount" class="text-xs italic"> (Discount)</span></span>
              <span>{{ s.isDiscount ? '- ' : '' }}{{ formatCurrency(Math.abs(s.amount)) }}</span>
            </div>
          </div>
          <div v-else class="flex justify-between text-sm">
            <span class="font-semibold">Service</span>
            <span>{{ paymentForm.serviceName || selectedSOA?.serviceName || 'N/A' }}</span>
          </div>

          <div class="flex justify-between text-sm">
            <span class="font-semibold">Diagnosis</span>
            <span>{{ paymentForm.diagnosis || selectedSOA?.otherDiagnosis || 'N/A' }}</span>
          </div>
          <div v-if="parseNumber(paymentForm.discountAmount) > 0" class="flex justify-between text-sm text-red-600">
            <span class="font-semibold">Discount ({{ paymentForm.discountName || 'Discount' }})</span>
            <span>- {{ formatCurrency(parseNumber(paymentForm.discountAmount)) }}</span>
          </div>
          <div v-if="parseNumber(paymentForm.discountAmount) > 0" class="flex justify-between text-sm">
            <span class="font-semibold">Total New Balance After Discount</span>
            <span>{{ formatCurrency(totalNewBalanceAfterDiscount) }}</span>
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
          <button @click="printReceipt" class="bg-purple-600 text-white px-4 py-2 rounded">Print SOA</button>
          <button @click="closeSOAModal" class="bg-gray-200 text-gray-700 px-4 py-2 rounded">Close</button>
        </div>
      </div>
    </div>

    <!-- ADD PAYMENT MODAL -->
    <div v-if="showPaymentModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-full max-w-2xl">
        <h2 class="text-xl font-bold mb-4">Patient Receipt Payment</h2>
        <p class="mb-4 text-sm text-gray-600">Patient: {{ selectedSOA?.patientName }}</p>

        <div class="grid grid-cols-1 gap-4">
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
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
              <span class="text-sm font-semibold mb-1 block">Already Paid</span>
              <input
                :value="formatCurrency(selectedSOA ? selectedSOA.amountPaid : 0)"
                type="text"
                readonly
                class="w-full border bg-gray-100 rounded px-4 py-2"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Amount Paid Now</span>
              <input
                v-model="paymentForm.paidAmount"
                type="text"
                inputmode="decimal"
                class="w-full border rounded px-4 py-2"
                placeholder="Enter amount"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">New Balance</span>
              <input
                :value="formatCurrency(paymentBalance)"
                type="text"
                readonly
                class="w-full border bg-gray-100 rounded px-4 py-2"
              />
            </label>
          </div>

          <!-- DISCOUNT -->
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Discount Name</span>
              <input
                v-model="paymentForm.discountName"
                type="text"
                class="w-full border rounded px-4 py-2"
                placeholder="e.g. Senior Citizen, PWD, PhilHealth"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Discount Amount</span>
              <input
                v-model="paymentForm.discountAmount"
                type="text"
                inputmode="decimal"
                class="w-full border rounded px-4 py-2"
                placeholder="Enter discount amount"
              />
            </label>

            <label class="block">
              <span class="text-sm font-semibold mb-1 block">Total New Balance After Discount</span>
              <input
                :value="formatCurrency(totalNewBalanceAfterDiscount)"
                type="text"
                readonly
                class="w-full border bg-gray-100 rounded px-4 py-2 font-semibold"
              />
            </label>
          </div>

          <button
            type="button"
            @click="addDiscount"
            class="bg-red-600 text-white px-4 py-2 rounded text-sm w-fit"
          >
            + Add Discount
          </button>

          <label class="block">
            <span class="text-sm font-semibold mb-1 block">Service Name</span>
            <select
              v-model="selectedServiceId"
              @change="onPaymentServiceChange"
              class="w-full border rounded px-4 py-2"
            >
              <option value="" disabled>Select a service</option>
              <option v-for="service in servicesList" :key="service.id" :value="service.id">
                {{ service.name }} (₱{{ service.price }})
              </option>
              <option value="custom">-- Custom Service --</option>
            </select>
            <input
              v-if="selectedServiceId === 'custom'"
              v-model="paymentForm.serviceName"
              type="text"
              class="w-full border rounded px-4 py-2 mt-2"
              placeholder="Enter custom service name"
            />
            <input
              v-if="selectedServiceId === 'custom'"
              v-model="paymentForm.totalAmount"
              type="number"
              class="w-full border rounded px-4 py-2 mt-2"
              placeholder="Enter amount"
            />

            <button
              type="button"
              @click="addAvailedService"
              class="mt-2 bg-indigo-600 text-white px-4 py-2 rounded text-sm"
            >
              + Add New Availed Service
            </button>
          </label>

          <!-- AVAILED SERVICES LIST -->
          <div class="border rounded">
            <div class="bg-gray-100 px-4 py-2 font-semibold text-sm flex justify-between">
              <span>Availed Services</span>
              <span>{{ formatCurrency(availedServicesTotal) }}</span>
            </div>
            <div v-if="availedServices.length === 0" class="px-4 py-2 text-sm text-gray-400">
              No services added yet
            </div>
            <div
              v-for="(s, i) in availedServices"
              :key="i"
              class="flex justify-between items-center px-4 py-2 border-t text-sm"
              :class="{ 'text-red-600': s.isDiscount }"
            >
              <span>
                {{ s.name }}
                <span v-if="s.isDiscount" class="text-xs italic">(Discount)</span>
              </span>
              <span class="flex items-center gap-3">
                <span>{{ s.isDiscount ? '- ' : '' }}{{ formatCurrency(Math.abs(s.amount)) }}</span>
                <button @click="removeAvailedService(i)" class="text-red-600">✕</button>
              </span>
            </div>
          </div>

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

    <!-- ADD SOA MODAL -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-md p-6 rounded-lg">
        <h2 class="text-xl font-bold mb-2">Add SOA Case Number</h2>
        <p class="mb-4 text-sm text-gray-600">
          This case number will be applied to all SOA records for all patients.
        </p>

        <label class="block">
          <span class="text-sm font-semibold mb-1 block">Case Number</span>
          <input
            v-model="caseNumberInput"
            type="text"
            placeholder="Enter case number"
            class="w-full border p-2 rounded"
          />
        </label>
        <p v-if="caseNumberMessage" class="mt-2 text-sm text-red-600">{{ caseNumberMessage }}</p>

        <div class="flex justify-end mt-4 gap-3">
          <button @click="showAddModal = false" class="px-4 py-2 bg-gray-300 rounded">Cancel</button>
          <button @click="saveCaseNumber" class="px-4 py-2 bg-blue-600 text-white rounded">Save</button>
        </div>
      </div>
    </div>

  </div>
</template>
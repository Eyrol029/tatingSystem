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
  paidAmount: '',
  discountName: '',
  discountAmount: ''
})

// Services availed for the CURRENT payment session (can be many per patient)
const availedServices = ref([])

const servicesList = ref([])
const selectedServiceId = ref('')
const selectedAddSoaServiceId = ref('')

function parseNumber(value) {
  if (value === null || value === undefined) return 0
  const normalized = String(value).replace(/,/g, '').trim()
  return Number(normalized) || 0
}

// Detects placeholder text like "No services" / "SOA Service" that the
// backend uses as a fallback label — these are not real clinical services
// and should never be saved as if they were an actual availed service.
function isPlaceholderServiceName(name) {
  const normalized = (name || '').trim().toLowerCase()
  return !normalized || normalized === 'no services' || normalized === 'soa service'
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

// New Balance = Total Amount minus already paid minus amount being paid now (gross, no discount yet)
const paymentBalance = computed(() => {
  const total = parseNumber(paymentForm.value.totalAmount)
  const alreadyPaid = selectedSOA.value ? parseNumber(selectedSOA.value.amountPaid) : 0
  const paidNow = parseNumber(paymentForm.value.paidAmount)
  return Math.max(0, total - alreadyPaid - paidNow)
})

// Total New Balance After Discount = New Balance minus whatever discount was entered
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

function onAddSoaServiceChange() {
  if (selectedAddSoaServiceId.value === 'custom') {
    newService.value.name = ''
    newService.value.amount = ''
  } else {
    const service = servicesList.value.find(s => s.id === Number(selectedAddSoaServiceId.value))
    if (service) {
      newService.value.name = service.name
      newService.value.amount = service.price
    } else {
      newService.value.name = ''
      newService.value.amount = ''
    }
  }
}

// Adds the currently-selected service (or custom entry) to the Availed Services list
// for this payment session, and recalculates the running Total Amount.
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

  // Reset the picker so another service can be added right away
  selectedServiceId.value = ''
  paymentForm.value.serviceName = ''
  paymentForm.value.totalAmount = String(availedServicesTotal.value)
}

// Commits the current Discount Name/Amount into the Availed Services breakdown
// as a negative-signed line item, then folds it into the running Total Amount.
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

  // Total Amount now reflects services minus discounts already applied
  paymentForm.value.totalAmount = String(availedServicesTotal.value)

  // Clear the discount inputs so the same box can be reused for another discount
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

async function openAddPayment(soa) {
  selectedSOA.value = { ...soa }
  paymentForm.value.serviceName = soa.serviceName || ''
  paymentForm.value.diagnosis = soa.otherDiagnosis || ''
  paymentForm.value.paymentDate = todayLocalDateString()
  paymentForm.value.totalAmount = soa.totalAmount != null ? String(soa.totalAmount) : ''
  paymentForm.value.paidAmount = ''
  paymentForm.value.discountName = ''
  paymentForm.value.discountAmount = ''

  // Default seed from whatever this SOA has on record — but skip placeholder
  // labels like "No services" so they never get saved as a fake line item.
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

  // Pull the last saved installment for this SOA (if any) to rebuild the
  // exact Availed Services breakdown, including any discount line items.
  const soaId = resolveSoaId(soa)
  if (soaId) {
    try {
      const response = await axios.get(`${INSTALLMENTS_URL}/soa/${soaId}`, {
        params: { _: Date.now() }, // cache-buster: forces the browser to always fetch fresh data
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

  // Build a readable list of every service availed in this payment session,
  // and separately list any discounts that were added to the breakdown.
  // Placeholder labels like "No services" are excluded — they're not real
  // services and shouldn't be saved as if they were.
  const serviceNames = availedServices.value
    .filter(s => !s.isDiscount && !isPlaceholderServiceName(s.name))
    .map(s => s.name)
    .join(', ') || (isPlaceholderServiceName(paymentForm.value.serviceName) ? '' : paymentForm.value.serviceName)

  const committedDiscounts = availedServices.value
    .filter(s => s.isDiscount)
    .map(s => `${s.name} (-${formatCurrency(Math.abs(s.amount))})`)

  // Include the discount box too, in case it wasn't clicked into the list yet
  if (discountAmount > 0 && paymentForm.value.discountName.trim()) {
    committedDiscounts.push(`${paymentForm.value.discountName} (-${formatCurrency(discountAmount)})`)
  }

  const discountNote = committedDiscounts.length
    ? ` | Discount: ${committedDiscounts.join(', ')}`
    : ''

  try {
    // Build the full breakdown to persist: everything already in the list
    // (minus any leftover placeholder entries), plus the discount box in
    // case it wasn't clicked into the list yet.
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
      totalAmount, // gross bill total (discount applied on top for display/notes only)
      paymentDate: paymentForm.value.paymentDate ? new Date(paymentForm.value.paymentDate).toISOString() : null,
      paymentMethod: 'Cash',
      notes: `${serviceNames} - ${paymentForm.value.diagnosis}${discountNote}`,
      discountName: aggregatedDiscountName,
      discountAmount: aggregatedDiscountAmount || null,
      serviceBreakdown: JSON.stringify(finalBreakdown)
    }

    await axios.post(`${BASE_URL}/${soaId}/payments`, payload)
    paymentMessage.value = 'Payment recorded successfully.'

    // Automatically record this payment as Revenue — using ONLY the amount
    // paid in THIS transaction (paidAmount), not the running total, since a
    // patient can pay multiple times across several visits.
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
        // Don't block the payment flow if revenue logging fails — just warn.
        console.error('Payment saved, but failed to auto-record revenue', revenueError)
      }
    }

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
  selectedAddSoaServiceId.value = ''
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

// Returns today's date as YYYY-MM-DD using LOCAL time, not UTC — avoids the
// off-by-one-day bug that .toISOString() causes for PH (UTC+8) users.
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
      <div class="bg-white p-6 rounded-lg w-full max-w-2xl">
        <h2 class="text-xl font-bold mb-4">Patient Receipt Payment</h2>
        <p class="mb-4 text-sm text-gray-600">Patient: {{ selectedSOA.patientName }}</p>

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
            <span>{{ paymentForm.serviceName || selectedSOA.serviceName || 'N/A' }}</span>
          </div>

          <div class="flex justify-between text-sm">
            <span class="font-semibold">Diagnosis</span>
            <span>{{ paymentForm.diagnosis || selectedSOA.otherDiagnosis || 'N/A' }}</span>
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
        <div class="flex flex-col gap-2 mt-2">
          <div class="flex gap-2">
            <select
              v-model="selectedAddSoaServiceId"
              @change="onAddSoaServiceChange"
              class="border p-2 flex-1 rounded"
            >
              <option value="" disabled>Select a service</option>
              <option v-for="service in servicesList" :key="service.id" :value="service.id">
                {{ service.name }} (₱{{ service.price }})
              </option>
              <option value="custom">-- Custom Service --</option>
            </select>
            <input v-model="newService.amount" type="number" placeholder="Amount" class="border p-2 w-32 rounded" />
            <button @click="addService" class="bg-blue-600 text-white px-4 rounded">Add</button>
          </div>
          <input
            v-if="selectedAddSoaServiceId === 'custom'"
            v-model="newService.name"
            placeholder="Enter custom service name"
            class="border p-2 rounded"
          />
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
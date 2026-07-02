
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const showViewModal = ref(false)
const showAddModal = ref(false)
const showPaymentModal = ref(false)
const showSOAModal = ref(false)
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
    soaList.value = response.data.map((soa) => ({
      id: soa.soaId,
      soaId: soa.soaId,
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
    }))
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
  paymentForm.value.serviceName = soa.service || ''
  paymentForm.value.diagnosis = soa.otherDiagnosis || ''
  paymentForm.value.paymentDate = new Date().toISOString().slice(0, 10)
  paymentForm.value.totalAmount = soa.totalAmount != null ? String(soa.totalAmount) : ''
  paymentForm.value.paidAmount = soa.amountPaid != null ? String(soa.amountPaid) : ''
  if (typeof paymentMessage !== 'undefined') paymentMessage.value = ''
  showPaymentModal.value = true
}

function closePaymentModal() {
  showPaymentModal.value = false
  showReceiptModal.value = false
  paymentForm.value.totalAmount = ''
  paymentForm.value.paidAmount = ''
  paymentForm.value.paymentDate = ''
  if (typeof paymentMessage !== 'undefined') paymentMessage.value = ''
}

function openReceiptModal() {
  showReceiptModal.value = true
}

function closeReceiptModal() {
  showReceiptModal.value = false
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

  const soaId = selectedSOA.value.id ?? selectedSOA.value.soaId
  if (!soaId) {
    paymentMessage.value = 'Unable to resolve SOA identifier.'
    return
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
  return 'â‚±' + Number(value).toLocaleString()
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


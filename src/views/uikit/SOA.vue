<script setup>
import { ref, computed } from 'vue'
import { usePaymentStore } from '@/stores/PaymentStore'

const store = usePaymentStore()

const showViewModal = ref(false)
const showAddModal = ref(false)
const showReceiptModal = ref(false)
const selectedSOA = ref(null)

const newSOA = ref({
  patientId: '',
  patientName: '',
  otherDiagnosis: '',
  date: '',
  services: []
})

const newService = ref({ name: '', amount: '' })
const receiptAmount = ref('')

// ---------- VIEW ----------
function openView(soa) {
  selectedSOA.value = soa
  showViewModal.value = true
}
function closeView() {
  showViewModal.value = false
}

// ---------- ADD SOA ----------
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

function saveSOA() {
  if (!newSOA.value.patientName || !newSOA.value.patientId) return

  store.addSOA({ ...newSOA.value })

  newSOA.value = {
    patientId: '',
    patientName: '',
    otherDiagnosis: '',
    date: '',
    services: []
  }

  showAddModal.value = false
}

// ---------- PRINT SOA (Scenario 1) ----------
function printSOA(soa) {
  selectedSOA.value = soa
  window.print()
}

// ---------- RECEIPT (Scenario 2 & 3) ----------
function openReceiptModal(soa) {
  selectedSOA.value = soa
  receiptAmount.value = ''
  showReceiptModal.value = true
}

function closeReceiptModal() {
  showReceiptModal.value = false
  receiptAmount.value = ''
}

function confirmReceipt() {
  if (!receiptAmount.value || Number(receiptAmount.value) <= 0) return

  const receipt = store.addReceipt(selectedSOA.value.id, receiptAmount.value)
  if (!receipt) return

  closeReceiptModal()
  // Trigger print of the receipt right after recording it
  window.print()
}

// ---------- HELPERS ----------
function formatCurrency(value) {
  return '₱' + value.toLocaleString()
}

function statusClass(status) {
  if (status === 'Paid') return 'bg-green-100 text-green-700'
  if (status === 'Partial') return 'bg-yellow-100 text-yellow-700'
  return 'bg-red-100 text-red-700'
}

const previewRemaining = computed(() => {
  if (!selectedSOA.value || !receiptAmount.value) return store.remainingBalance(selectedSOA.value)
  const balance = store.remainingBalance(selectedSOA.value)
  const paid = Math.min(Number(receiptAmount.value), balance)
  return balance - paid
})
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-bold">Statement of Account</h2>
      <button
        @click="showAddModal = true"
        class="bg-blue-600 text-white px-4 py-2 rounded"
      >
        + Add SOA
      </button>
    </div>

    <!-- SOA LIST -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div v-for="soa in store.soaList" :key="soa.id" class="bg-white p-4 rounded shadow">
        <div class="flex justify-between items-start">
          <div>
            <p class="font-semibold">{{ soa.patientName }}</p>
            <p class="text-sm text-gray-500">{{ soa.date }}</p>
          </div>
          <span
            class="text-xs font-semibold px-2 py-1 rounded-full"
            :class="statusClass(store.soaStatus(soa))"
          >
            {{ store.soaStatus(soa) }}
          </span>
        </div>

        <p class="mt-2 text-sm">Total: {{ formatCurrency(store.totalAmount(soa)) }}</p>
        <p class="text-sm">Paid: {{ formatCurrency(soa.amountPaid) }}</p>
        <p class="text-sm font-semibold">Balance: {{ formatCurrency(store.remainingBalance(soa)) }}</p>

        <div class="flex flex-wrap gap-3 mt-3">
          <button @click="openView(soa)" class="text-blue-600 hover:underline text-sm">
            View
          </button>
          <button @click="printSOA(soa)" class="text-gray-700 hover:underline text-sm">
            Print SOA
          </button>
          <button
            v-if="store.soaStatus(soa) !== 'Paid'"
            @click="openReceiptModal(soa)"
            class="text-purple-700 hover:underline text-sm"
          >
            Receipt
          </button>
        </div>
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
            <span>{{ formatCurrency(store.totalAmount(selectedSOA)) }}</span>
          </div>
        </div>

        <!-- Payment history -->
        <div v-if="selectedSOA.receipts.length" class="mt-4 border rounded">
          <div class="bg-gray-300 px-4 py-2 font-semibold">Payment History</div>
          <div
            v-for="(r, i) in selectedSOA.receipts"
            :key="i"
            class="flex justify-between px-4 py-2 border-t text-sm"
          >
            <span>{{ r.invoiceNo }} — {{ r.date }}</span>
            <span>{{ formatCurrency(r.amountPaid) }}</span>
          </div>
        </div>

        <div class="flex justify-between mt-4 text-sm font-semibold">
          <span>Amount Paid: {{ formatCurrency(selectedSOA.amountPaid) }}</span>
          <span>Remaining Balance: {{ formatCurrency(store.remainingBalance(selectedSOA)) }}</span>
        </div>

        <div class="flex justify-end mt-4">
          <button @click="closeView" class="px-5 py-2 bg-gray-700 text-white rounded">
            Close
          </button>
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

    <!-- RECEIPT MODAL (Scenario 2 & 3) -->
    <div v-if="showReceiptModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white w-full max-w-lg p-6 rounded-lg">
        <h2 class="text-xl font-bold mb-4">Receipt — {{ selectedSOA.patientName }}</h2>

        <div class="border rounded mb-4">
          <div class="bg-gray-200 px-4 py-2 font-semibold flex justify-between">
            <span>Service</span><span>Amount</span>
          </div>
          <div v-for="(s, i) in selectedSOA.services" :key="i" class="flex justify-between px-4 py-2 border-t text-sm">
            <span>{{ s.name }}</span>
            <span>{{ formatCurrency(s.amount) }}</span>
          </div>
        </div>

        <div class="space-y-2 text-sm mb-4">
          <div class="flex justify-between">
            <span>Total Amount</span>
            <span>{{ formatCurrency(store.totalAmount(selectedSOA)) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Already Paid</span>
            <span>{{ formatCurrency(selectedSOA.amountPaid) }}</span>
          </div>
          <div class="flex justify-between font-semibold">
            <span>Current Balance</span>
            <span>{{ formatCurrency(store.remainingBalance(selectedSOA)) }}</span>
          </div>
        </div>

        <div>
          <label class="text-sm font-medium">Amount Paid Now (₱)</label>
          <input
            type="number"
            v-model="receiptAmount"
            class="w-full border rounded px-3 py-2"
            :max="store.remainingBalance(selectedSOA)"
          />
        </div>

        <div class="flex justify-between text-sm font-semibold mt-3">
          <span>Remaining Balance After Payment</span>
          <span>{{ formatCurrency(previewRemaining) }}</span>
        </div>

        <div class="flex justify-end gap-3 mt-6">
          <button @click="closeReceiptModal" class="px-4 py-2 bg-gray-300 rounded">Cancel</button>
          <button @click="confirmReceipt" class="px-4 py-2 bg-purple-700 text-white rounded">
            Print Receipt
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
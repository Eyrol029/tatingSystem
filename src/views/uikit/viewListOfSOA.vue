<script setup>
import { ref} from 'vue'

const showViewModal = ref(false)
const showAddModal = ref(false)
const selectedSOA = ref(null)

const soaList = ref([
  {
    id: 'SOA001',
    patientId: 'P001',
    patientName: 'Althea Mae Santos',
    date: '2026-05-14',
    otherDiagnosis: 'Normal Delivery',
    services: [
      { name: 'Maternity Package', amount: 15000 },
      { name: 'PHILHEALTH Process', amount: 600 }
    ]
  }
])

const newSOA = ref({
  patientId: '',
  patientName: '',
  otherDiagnosis: '',
  date: '',
  services: []
})

const newService = ref({ name: '', amount: '' })

function openView(soa) {
  selectedSOA.value = soa
  showViewModal.value = true
}

function closeView() {
  showViewModal.value = false
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

function saveSOA() {
  if (!newSOA.value.patientName || !newSOA.value.patientId) return

  soaList.value.push({
    ...newSOA.value,
    id: 'SOA' + Math.floor(Math.random() * 9999)
  })

  newSOA.value = {
    patientId: '',
    patientName: '',
    otherDiagnosis: '',
    date: '',
    services: []
  }

  showAddModal.value = false
}

function totalAmount(services) {
  return services.reduce((sum, s) => sum + s.amount, 0)
}

function formatCurrency(value) {
  return '₱' + value.toLocaleString()
}
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
      <div v-for="soa in soaList" :key="soa.id" class="bg-white p-4 rounded shadow">
        <div class="flex justify-between">
          <div>
            <p class="font-semibold">{{ soa.patientName }}</p>
            <p class="text-sm text-gray-500">{{ soa.date }}</p>
          </div>
          <button
            @click="openView(soa)"
            class="text-blue-600 hover:underline"
          >
            View
          </button>
        </div>

        <p class="mt-2 text-sm">Total: {{ formatCurrency(totalAmount(soa.services)) }}</p>
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

  </div>
</template>

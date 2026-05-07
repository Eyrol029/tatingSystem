<script setup>
import { ref } from 'vue'

const showModal = ref(false)

const revenues = ref([
  {
    id: 1,
    dealer: 'ABC Medical Supplies',
    description: 'Consultation Fee',
    invoiceNo: 'INV-1001',
    amount: 4500
  }
])

const form = ref({
  dealer: '',
  description: '',
  invoiceNo: '',
  amount: ''
})

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
    invoiceNo: '',
    amount: ''
  }
}

function saveRevenue() {
  if (!form.value.dealer || !form.value.amount) return

  revenues.value.push({
    id: Date.now(),
    ...form.value
  })

  closeModal()
}

function deleteRevenue(id) {
  if (confirm('Are you sure you want to delete this record?')) {
    revenues.value = revenues.value.filter(r => r.id !== id)
  }
}
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold">View Revenue Records</h2>
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
            <th class="p-3 text-left">Paid by</th>
            <th class="p-3 text-left">Description</th>
            <th class="p-3 text-left">Invoice No.</th>
            <th class="p-3 text-left">Amount</th>
            <th class="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="rev in revenues" :key="rev.id" class="border-t">
            <td class="p-3">{{ rev.dealer }}</td>
            <td class="p-3">{{ rev.description }}</td>
            <td class="p-3">{{ rev.invoiceNo }}</td>
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
            <label class="text-sm font-medium">Paid by</label>
            <input v-model="form.dealer" class="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label class="text-sm font-medium">Description</label>
            <textarea v-model="form.description" class="w-full border rounded px-3 py-2"></textarea>
          </div>

          <div>
            <label class="text-sm font-medium">Invoice / Receipt No.</label>
            <input v-model="form.invoiceNo" class="w-full border rounded px-3 py-2" />
          </div>

          <div>
            <label class="text-sm font-medium">Amount (₱)</label>
            <input type="number" v-model="form.amount" class="w-full border rounded px-3 py-2" />
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

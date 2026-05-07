<script setup>
import { ref } from 'vue'

const showModal = ref(false)

const expenses = ref([
  {
    id: 1,
    category: 'Medical Supplies',
    description: 'Surgical gloves and masks',
    invoiceNo: '01227321',
    amount: 5000,
    payee: 'MedSupply Corp'
  }
])

const form = ref({
  category: '',
  description: '',
  invoiceNo: '',
  amount: '',
  payee: ''
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
    category: '',
    description: '',
    invoiceNo: '',
    amount: '',
    payee: ''
  }
}

function saveExpense() {
  if (!form.value.category || !form.value.amount) return

  expenses.value.push({
    id: Date.now(),
    ...form.value
  })

  closeModal()
}

function deleteExpense(id) {
  if (confirm('Are you sure you want to delete this expense?')) {
    expenses.value = expenses.value.filter(exp => exp.id !== id)
  }
}
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- HEADER -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold">View Expense Record</h2>
      <button
        @click="openModal"
        class="bg-purple-700 text-white px-4 py-2 rounded-md"
      >
        + Add Expense
      </button>
    </div>

    <!-- EXPENSE TABLE -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="w-full text-sm">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 text-left">Category</th>
            <th class="p-3 text-left">Description</th>
            <th class="p-3 text-left">Amount</th>
            <th class="p-3 text-left">Action</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="expense in expenses" :key="expense.id" class="border-t">
            <td class="p-3">{{ expense.category }}</td>
            <td class="p-3">{{ expense.description }}</td>
            <td class="p-3 font-semibold">₱{{ expense.amount }}</td>
            <td class="p-3 space-x-3">
              <button class="text-blue-600 hover:underline">
                View
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
    </div>

    <!-- MODAL -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
    >
      <div class="bg-white w-full max-w-lg rounded-lg p-6 shadow-lg">
        <h3 class="text-lg font-semibold mb-4">Add Expense</h3>

        <div class="space-y-3">
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
            <label class="text-sm font-medium">Invoice / Receipt No.</label>
            <input v-model="form.invoiceNo" class="w-full border rounded px-3 py-2" />
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
            Save
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

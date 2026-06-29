<script setup>
import { computed, ref, onMounted } from 'vue'
import axios from 'axios'

const BASE = 'http://localhost:8080/api/employees'

const view            = ref('list')
const selectedEmployee = ref(null)
const searchQuery     = ref('')
const employees       = ref([])
const loading         = ref(false)
const submitLoading   = ref(false)
const errorMsg        = ref('')
const successMsg      = ref('')

// ─── Form ─────────────────────────────────────────────────────────────────────
const formData = ref({
  fName: '', lName: '', middleI: '',
  position: '', department: 'Maternity',
  contact: '', status: 'Active'
})

// ─── Load all employees on mount ──────────────────────────────────────────────
onMounted(fetchEmployees)

async function fetchEmployees() {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await axios.get(BASE)
    employees.value = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    errorMsg.value = 'Failed to load employees. Is the server running?'
    console.error(e)
  } finally {
    loading.value = false
  }
}

// ─── Search / filter (client-side on loaded data) ────────────────────────────
const filteredEmployees = computed(() => {
  const q = searchQuery.value.toLowerCase().trim()
  if (!q) return employees.value
  return employees.value.filter(e => {
    const fullName = `${e.fName} ${e.middleI ?? ''} ${e.lName}`.toLowerCase()
    return (
      fullName.includes(q) ||
      String(e.employeeID).includes(q) ||
      (e.position ?? '').toLowerCase().includes(q) ||
      (e.department ?? '').toLowerCase().includes(q)
    )
  })
})

// ─── ADD ──────────────────────────────────────────────────────────────────────
async function handleAddEmployee() {
  submitLoading.value = true
  errorMsg.value = ''
  successMsg.value = ''
  try {
    const res = await axios.post(BASE, {
      fName:      formData.value.fName,
      lName:      formData.value.lName,
      middleI:    formData.value.middleI || null,
      position:   formData.value.position,
      department: formData.value.department,
      contact:    formData.value.contact || null,
      status:     formData.value.status
    })
    employees.value.push(res.data)
    successMsg.value = '✅ Employee added successfully!'
    resetForm()
    view.value = 'list'
  } catch (e) {
    errorMsg.value = '❌ Failed to add employee: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    submitLoading.value = false
  }
}

// ─── EDIT ─────────────────────────────────────────────────────────────────────
async function handleEditEmployee() {
  if (!selectedEmployee.value) return
  submitLoading.value = true
  errorMsg.value = ''
  successMsg.value = ''
  try {
    const payload = {
      employeeID: selectedEmployee.value.employeeID,
      fName:      formData.value.fName,
      lName:      formData.value.lName,
      middleI:    formData.value.middleI || null,
      position:   formData.value.position,
      department: formData.value.department,
      contact:    formData.value.contact || null,
      status:     formData.value.status
    }
    const res = await axios.put(BASE, payload)
    const idx = employees.value.findIndex(e => e.employeeID === selectedEmployee.value.employeeID)
    if (idx !== -1) employees.value[idx] = res.data
    successMsg.value = '✅ Employee updated successfully!'
    resetForm()
    view.value = 'list'
  } catch (e) {
    errorMsg.value = '❌ Failed to update employee: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    submitLoading.value = false
  }
}

// ─── DELETE ───────────────────────────────────────────────────────────────────
async function handleDeleteEmployee(employeeID) {
  if (!confirm('Are you sure you want to delete this employee record?')) return
  errorMsg.value = ''
  try {
    await axios.delete(`${BASE}/${employeeID}`)
    employees.value = employees.value.filter(e => e.employeeID !== employeeID)
    successMsg.value = '✅ Employee deleted.'
  } catch (e) {
    errorMsg.value = '❌ Failed to delete: ' + (e?.response?.data?.message ?? e.message)
  }
}

// ─── Helpers ──────────────────────────────────────────────────────────────────
function fullName(e) {
  return [e.fName, e.middleI, e.lName].filter(Boolean).join(' ')
}

function openEditForm(employee) {
  selectedEmployee.value = employee
  formData.value = {
    fName:      employee.fName      ?? '',
    lName:      employee.lName      ?? '',
    middleI:    employee.middleI    ?? '',
    position:   employee.position   ?? '',
    department: employee.department ?? 'Maternity',
    contact:    employee.contact    ?? '',
    status:     employee.status     ?? 'Active'
  }
  errorMsg.value   = ''
  successMsg.value = ''
  view.value = 'edit'
}

function viewEmployeeDetail(employee) {
  selectedEmployee.value = employee
  view.value = 'detail'
}

function resetForm() {
  formData.value = {
    fName: '', lName: '', middleI: '',
    position: '', department: 'Maternity',
    contact: '', status: 'Active'
  }
  selectedEmployee.value = null
}

function formatDate(dt) {
  if (!dt) return '—'
  return new Date(dt).toLocaleDateString('en-PH', {
    year: 'numeric', month: 'long', day: 'numeric'
  })
}

function goToList() {
  resetForm()
  errorMsg.value   = ''
  successMsg.value = ''
  view.value = 'list'
}
</script>

<template>
  <div class="space-y-6 p-6 bg-white min-h-screen">

    <!-- ── Global alerts ── -->
    <div v-if="errorMsg"
      class="bg-red-50 border border-red-300 text-red-700 text-sm rounded-lg px-4 py-3 flex items-center justify-between">
      {{ errorMsg }}
      <button @click="errorMsg = ''" class="ml-4 text-red-400 hover:text-red-600 font-bold">✕</button>
    </div>
    <div v-if="successMsg && view === 'list'"
      class="bg-green-50 border border-green-300 text-green-700 text-sm rounded-lg px-4 py-3 flex items-center justify-between">
      {{ successMsg }}
      <button @click="successMsg = ''" class="ml-4 text-green-400 hover:text-green-600 font-bold">✕</button>
    </div>

    <!-- ══════════════════════════════════════════════════════════
         ADD / EDIT FORM
    ══════════════════════════════════════════════════════════ -->
    <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl">
      <div class="bg-white rounded-lg shadow p-6">
        <h3 class="text-2xl font-bold text-gray-800 mb-6">
          {{ view === 'add' ? 'Add New Employee' : 'Edit Employee Details' }}
        </h3>

        <div v-if="errorMsg" class="mb-4 bg-red-50 border border-red-300 text-red-700 text-sm rounded px-4 py-2">
          {{ errorMsg }}
        </div>

        <form @submit.prevent="view === 'add' ? handleAddEmployee() : handleEditEmployee()"
          class="space-y-4">

          <!-- Name row -->
          <div class="grid grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">First Name *</label>
              <input v-model="formData.fName" type="text" required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400"
                placeholder="e.g. Carmen" />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">Middle Initial</label>
              <input v-model="formData.middleI" type="text" maxlength="10"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400"
                placeholder="e.g. A." />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">Last Name *</label>
              <input v-model="formData.lName" type="text" required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400"
                placeholder="e.g. Reyes" />
            </div>
          </div>

          <!-- Position + Department -->
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">Position *</label>
              <input v-model="formData.position" type="text" required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400"
                placeholder="e.g. Midwife" />
            </div>
            <div>
              <label class="block text-sm font-semibold text-gray-700 mb-2">Department *</label>
              <select v-model="formData.department"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400">
                <option>Maternity</option>
                <option>Family Planning</option>
                <option>Administration</option>
                <option>Finance</option>
              </select>
            </div>
          </div>

          <!-- Contact -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Contact Number</label>
            <input v-model="formData.contact" type="tel"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400"
              placeholder="e.g. 09123456789" />
          </div>

          <!-- Status -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-2">Status *</label>
            <select v-model="formData.status"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400">
              <option>Active</option>
              <option>Inactive</option>
            </select>
          </div>

          <!-- Buttons -->
          <div class="flex gap-3 pt-4">
            <button type="submit" :disabled="submitLoading"
              class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 disabled:opacity-50 disabled:cursor-not-allowed">
              {{ submitLoading ? 'Saving...' : (view === 'add' ? 'Add Employee' : 'Save Changes') }}
            </button>
            <button type="button" @click="goToList"
              class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- ══════════════════════════════════════════════════════════
         DETAIL VIEW
    ══════════════════════════════════════════════════════════ -->
    <div v-else-if="view === 'detail' && selectedEmployee" class="max-w-3xl">
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-2xl font-bold text-gray-800">Employee Details</h3>
          <button @click="goToList" class="text-gray-500 hover:text-gray-700 text-2xl">✕</button>
        </div>

        <div class="space-y-4">
          <div class="grid grid-cols-2 gap-6">
            <div>
              <p class="text-sm font-semibold text-gray-500">Employee ID</p>
              <p class="text-gray-900 mt-1">{{ selectedEmployee.employeeID }}</p>
            </div>
            <div>
              <p class="text-sm font-semibold text-gray-500">Date Hired</p>
              <p class="text-gray-900 mt-1">{{ formatDate(selectedEmployee.createdAt) }}</p>
            </div>
          </div>

          <div>
            <p class="text-sm font-semibold text-gray-500">Full Name</p>
            <p class="text-gray-900 mt-1">{{ fullName(selectedEmployee) }}</p>
          </div>

          <div class="grid grid-cols-2 gap-6">
            <div>
              <p class="text-sm font-semibold text-gray-500">Position</p>
              <p class="text-gray-900 mt-1">{{ selectedEmployee.position }}</p>
            </div>
            <div>
              <p class="text-sm font-semibold text-gray-500">Department</p>
              <p class="text-gray-900 mt-1">{{ selectedEmployee.department }}</p>
            </div>
          </div>

          <div>
            <p class="text-sm font-semibold text-gray-500">Contact Number</p>
            <p class="text-gray-900 mt-1">{{ selectedEmployee.contact || '—' }}</p>
          </div>

          <div>
            <p class="text-sm font-semibold text-gray-500">Status</p>
            <span :class="['inline-block px-2 py-1 rounded text-xs mt-1 font-semibold',
              selectedEmployee.status === 'Active'
                ? 'bg-green-100 text-green-700'
                : 'bg-gray-100 text-gray-600']">
              {{ selectedEmployee.status }}
            </span>
          </div>

          <div>
            <p class="text-sm font-semibold text-gray-500">Last Updated</p>
            <p class="text-gray-900 mt-1">{{ formatDate(selectedEmployee.updatedAt) }}</p>
          </div>
        </div>

        <div class="flex gap-3 mt-6 pt-6 border-t">
          <button @click="openEditForm(selectedEmployee)"
            class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">
            Edit Details
          </button>
          <button @click="goToList"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300">
            Back to List
          </button>
        </div>
      </div>
    </div>

    <!-- ══════════════════════════════════════════════════════════
         LIST VIEW
    ══════════════════════════════════════════════════════════ -->
    <template v-else>
      <!-- Header -->
      <div class="flex items-center justify-between">
        <div>
          <h3 class="text-2xl font-bold text-gray-800">Employee Management</h3>
          <p class="text-gray-500 text-sm mt-1">Manage employee records and information</p>
        </div>
        <div class="flex items-center gap-3">
          <button @click="fetchEmployees"
            class="px-3 py-2 bg-gray-100 text-gray-600 rounded-lg hover:bg-gray-200 text-sm flex items-center gap-1"
            :disabled="loading">
            <span :class="loading ? 'animate-spin' : ''">↻</span>
            Refresh
          </button>
          <button @click="() => { resetForm(); errorMsg = ''; successMsg = ''; view = 'add' }"
            class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 flex items-center gap-2">
            <span class="text-xl leading-none">+</span>
            Add Employee
          </button>
        </div>
      </div>

      <!-- Search -->
      <div class="bg-white rounded-lg shadow p-4">
        <input v-model="searchQuery" type="text"
          placeholder="Search by name, ID, position, or department..."
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-400" />
      </div>

      <!-- Loading -->
      <div v-if="loading" class="text-center py-12 text-gray-500">
        Loading employees...
      </div>

      <!-- Table -->
      <div v-else class="bg-white rounded-lg shadow overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50 border-b">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Name</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Position</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Department</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Contact</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="employee in filteredEmployees" :key="employee.employeeID"
                class="hover:bg-gray-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-600">
                  #{{ employee.employeeID }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <p class="text-sm font-semibold text-gray-900">{{ fullName(employee) }}</p>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.position }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.department }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{{ employee.contact || '—' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="['px-2 py-1 rounded text-xs font-semibold',
                    employee.status === 'Active'
                      ? 'bg-green-100 text-green-700'
                      : 'bg-gray-100 text-gray-600']">
                    {{ employee.status }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm">
                  <div class="flex gap-2">
                    <button @click="viewEmployeeDetail(employee)"
                      class="px-3 py-1 bg-blue-500 text-white rounded text-sm hover:bg-blue-600 transition-colors">
                      View
                    </button>
                    <button @click="openEditForm(employee)"
                      class="px-3 py-1 bg-teal-600 text-white rounded text-sm hover:bg-teal-700 transition-colors">
                      Edit
                    </button>
                    <button @click="handleDeleteEmployee(employee.employeeID)"
                      class="px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600 transition-colors">
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Summary row -->
        <div class="px-6 py-3 bg-gray-50 border-t text-xs text-gray-500">
          Showing {{ filteredEmployees.length }} of {{ employees.length }} employee(s)
        </div>
      </div>

      <!-- Empty state -->
      <div v-if="!loading && filteredEmployees.length === 0"
        class="bg-white rounded-lg shadow p-12 text-center">
        <p class="text-gray-400 text-lg mb-2">
          {{ searchQuery ? '🔍 No employees match your search.' : '👤 No employees found.' }}
        </p>
        <p v-if="!searchQuery" class="text-gray-400 text-sm">
          Click <strong>Add Employee</strong> to get started.
        </p>
      </div>
    </template>

  </div>
</template>
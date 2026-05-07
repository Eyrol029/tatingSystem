<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

const view = ref('list');
const selectedUser = ref(null);
const users = ref([]);
const searchQuery = ref('');
const roleFilter = ref('');

const formData = ref({
  username: '',
  email: '',
  passwordHash: '',
  confirmPassword: '',
  passwordSalt: '',
  role: 'Admin',
  status: 'Active',
  patientID: null,
  employeeID: null
});

// Fetch all users from backend
async function fetchUsers() {
  try {
    const res = await axios.get(`${BASE_URL}/users`);
    users.value = res.data;
  } catch (e) {
    alert('Failed to fetch users: ' + e.message);
  }
}

fetchUsers();

const filteredUsers = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  return users.value.filter(u => {
    if (roleFilter.value === 'Patient' && u.role !== 'Patient') return false;
    if (roleFilter.value === 'Employee' && u.role === 'Patient') return false;
    if (!q) return true;
    return (
      (u.username || '').toLowerCase().includes(q) ||
      (u.role || '').toLowerCase().includes(q) ||
      (u.email || '').toLowerCase().includes(q) ||
      (u.status || '').toLowerCase().includes(q)
    );
  });
});

function clearSearch() {
  searchQuery.value = '';
}

function openAddForm() {
  resetForm();
  view.value = 'add';
}

function resetForm() {
  formData.value = {
    username: '',
    email: '',
    passwordHash: '',
    confirmPassword: '',
    passwordSalt: '',
    role: 'Admin',
    status: 'Active',
    patientID: null,
    employeeID: null
  };
  selectedUser.value = null;
  view.value = 'list';
}

async function handleAddUser() {
  if (formData.value.passwordHash !== formData.value.confirmPassword) {
    alert('Passwords do not match');
    return;
  }

  try {
    await axios.post(`${BASE_URL}/add`, {
      username: formData.value.username,
      email: formData.value.email,
      passwordHash: formData.value.passwordHash,
      passwordSalt: formData.value.passwordSalt || '',
      role: formData.value.role,
      status: formData.value.status,
      patientID: formData.value.patientID || null,
      employeeID: formData.value.employeeID || null
    });

    await fetchUsers();
    resetForm();
    alert('User added successfully!');
  } catch (e) {
    alert('Failed to add user: ' + (e.response?.data?.message || e.message));
  }
}

function openEditForm(user) {
  selectedUser.value = user;
  formData.value = {
    username: user.username,
    email: user.email,
    passwordHash: '',
    confirmPassword: '',
    passwordSalt: user.passwordSalt || '',
    role: user.role,
    status: user.status,
    patientID: user.patientID || null,
    employeeID: user.employeeID || null
  };
  view.value = 'edit';
}

async function handleEditUser() {
  if (formData.value.passwordHash && formData.value.passwordHash !== formData.value.confirmPassword) {
    alert('Passwords do not match');
    return;
  }

  try {
    await axios.put(`${BASE_URL}/user`, {
      userID: selectedUser.value.userID,
      username: formData.value.username,
      email: formData.value.email,
      passwordHash: formData.value.passwordHash || selectedUser.value.passwordHash,
      passwordSalt: formData.value.passwordSalt || selectedUser.value.passwordSalt,
      role: formData.value.role,
      status: formData.value.status,
      patientID: formData.value.patientID || null,
      employeeID: formData.value.employeeID || null
    });

    await fetchUsers();
    resetForm();
    alert('User updated successfully!');
  } catch (e) {
    alert('Failed to update user: ' + (e.response?.data?.message || e.message));
  }
}

async function handleDeleteUser(userID) {
  if (userID === 1) {
    alert('Cannot delete system administrator');
    return;
  }
  if (confirm('Are you sure you want to delete this user?')) {
    try {
      await axios.delete(`${BASE_URL}/user/${userID}`);
      await fetchUsers();
      alert('User deleted successfully!');
    } catch (e) {
      alert('Failed to delete user: ' + (e.response?.data?.message || e.message));
    }
  }
}
</script>

<template>
  <div class="p-6 bg-white min-h-screen">

    <!-- FORM -->
    <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow">
      <h3 class="text-xl font-semibold mb-6">
        {{ view === 'add' ? 'Add New User Account' : 'Edit User Account' }}
      </h3>

      <form @submit.prevent="view === 'add' ? handleAddUser() : handleEditUser()" class="space-y-4">

        <div>
          <label class="block text-sm font-semibold mb-2">Username *</label>
          <input
            v-model="formData.username"
            type="text"
            required
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
          />
        </div>

        <div>
          <label class="block text-sm font-semibold mb-2">Email</label>
          <input
            type="email"
            v-model="formData.email"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
          />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">Role *</label>
            <select
              v-model="formData.role"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
            >
              <option>Admin</option>
              <option>Midwife</option>
              <option>Patient</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Status *</label>
            <select
              v-model="formData.status"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
            >
              <option>Active</option>
              <option>Inactive</option>
            </select>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">Patient ID</label>
            <input
              type="number"
              v-model="formData.patientID"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
              placeholder="Optional"
            />
          </div>
          <div>
            <label class="block text-sm font-semibold mb-2">Employee ID</label>
            <input
              type="number"
              v-model="formData.employeeID"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
              placeholder="Optional"
            />
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">
              Password {{ view === 'edit' ? '(leave blank to keep current)' : '*' }}
            </label>
            <input
              type="password"
              v-model="formData.passwordHash"
              :required="view === 'add'"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
            />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">
              Confirm Password {{ view === 'edit' ? '' : '*' }}
            </label>
            <input
              type="password"
              v-model="formData.confirmPassword"
              :required="view === 'add'"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
            />
          </div>
        </div>

        <div class="flex gap-3 pt-4">
          <button
            type="submit"
            class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700"
          >
            {{ view === 'add' ? 'Create Account' : 'Save Changes' }}
          </button>
          <button
            type="button"
            @click="resetForm"
            class="px-6 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>

    <!-- TABLE -->
    <div v-else>
      <div class="flex justify-between items-center mb-6">
        <div>
          <h3 class="text-2xl font-bold text-gray-800">Staff Accounts</h3>
          <p class="text-sm text-gray-500 mt-1">Manage Staff Users</p>
        </div>
        <div class="flex items-center gap-4">
          <div class="relative">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search users..."
              class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500"
            />
            <button
              v-if="searchQuery"
              @click="clearSearch"
              class="absolute right-2 top-1/2 -translate-y-1/2 text-sm text-gray-600 px-2 py-1"
            >×</button>
          </div>

          <div class="flex items-center gap-2">
            <button @click="$router.push('/uikit/UserAccount')" class="px-3 py-2 rounded bg-teal-100 text-teal-700 hover:bg-teal-200">Employees</button>
            <button @click="$router.push('/uikit/UserAccountPatient')" class="px-3 py-2 rounded bg-teal-100 text-teal-700 hover:bg-teal-200">Patients</button>
          </div>

          <button
            @click="openAddForm()"
            class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 flex items-center gap-2"
          >
            <span class="text-xl">+</span> Add User
          </button>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">User ID</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Username</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Email</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Role</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Employee ID</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Patient ID</th>
                <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Status</th>
                <th class="px-6 py-4 text-center text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>

            <tbody class="divide-y divide-gray-200">
              <tr v-for="user in filteredUsers" :key="user.userID" class="hover:bg-gray-50">
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.userID }}</td>
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.username }}</td>
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.email }}</td>
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.role }}</td>
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.employeeID ?? '—' }}</td>
                <td class="px-6 py-4 text-sm text-gray-900">{{ user.patientID ?? '—' }}</td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-xs font-semibold',
                      user.status === 'Active' ? 'bg-green-100 text-green-700' : 'bg-gray-200 text-gray-700'
                    ]"
                  >
                    {{ user.status }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <div class="flex justify-center gap-2">
                    <button
                      @click="openEditForm(user)"
                      class="px-3 py-1 bg-blue-50 text-blue-700 rounded hover:bg-blue-100 text-sm font-semibold"
                    >Edit</button>
                    <button
                      @click="handleDeleteUser(user.userID)"
                      :disabled="user.userID === 1"
                      :class="[
                        'px-3 py-1 rounded text-sm font-semibold',
                        user.userID === 1
                          ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                          : 'bg-red-50 text-red-700 hover:bg-red-100'
                      ]"
                    >Delete</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-if="filteredUsers.length === 0" class="text-center py-12">
        <p class="text-gray-500">{{ searchQuery ? 'No users match your search' : 'No users found' }}</p>
      </div>
    </div>
  </div>
</template>
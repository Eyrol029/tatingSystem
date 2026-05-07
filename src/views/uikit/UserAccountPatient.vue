<script setup>
import { ref, computed } from 'vue';

const view = ref('list');
const selectedUser = ref(null);

const users = ref([
  { 
    id: 'P001', 
    username: 'juan.delacruz', 
    fullName: 'Juan Dela Cruz', 
    role: 'Patient', 
    email: 'juan.delacruz@example.com', 
    status: 'Active',
    age: '28',
    birthdate: '1997-03-12',
    contactNo: '09171234567',
    municipality: 'Manila'
  },
  { 
    id: 'P002', 
    username: 'maria.santos', 
    fullName: 'Maria Santos', 
    role: 'Patient', 
    email: 'maria.santos@example.com', 
    status: 'Active',
    age: '34',
    birthdate: '1991-08-04',
    contactNo: '09181234567',
    municipality: 'Quezon City'
  }
]);

const formData = ref({
  username: '',
  fullName: '',
  email: '',
  password: '',
  confirmPassword: '',
  status: 'Active',
  age: '',
  birthdate: '',
  houseNo: '',
  street: '',
  barangay: '',
  contactNo: '',
  province: '',
  municipality: '',
  religion: '',
  education: '',
  occupation: ''
});

const searchQuery = ref('');
const userList = computed(() => users.value);

const filteredUsers = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  return userList.value.filter(u => u.role === 'Patient' && (
    !q || 
    u.username.toLowerCase().includes(q) || 
    u.fullName.toLowerCase().includes(q) || 
    u.email.toLowerCase().includes(q) || 
    u.status.toLowerCase().includes(q)
  ));
});

function clearSearch() {
  searchQuery.value = '';
}

function openAddForm() {
  resetForm();
  formData.value.role = 'Patient';
  view.value = 'add';
}

function resetForm() {
  formData.value = {
    username: '',
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    status: 'Active',
    age: '',
    birthdate: '',
    houseNo: '',
    street: '',
    barangay: '',
    contactNo: '',
    province: '',
    municipality: '',
    religion: '',
    education: '',
    occupation: ''
  };
  selectedUser.value = null;
  view.value = 'list';
}

function handleAddUser() {
  if (formData.value.password !== formData.value.confirmPassword) {
    alert('Passwords do not match');
    return;
  }

  users.value.push({
    id: `P${String(users.value.length + 1).padStart(3, '0')}`,
    username: formData.value.username,
    fullName: formData.value.fullName,
    role: 'Patient',
    email: formData.value.email,
    status: formData.value.status,
    age: formData.value.age,
    birthdate: formData.value.birthdate,
    houseNo: formData.value.houseNo,
    street: formData.value.street,
    barangay: formData.value.barangay,
    contactNo: formData.value.contactNo,
    province: formData.value.province,
    municipality: formData.value.municipality,
    religion: formData.value.religion,
    education: formData.value.education,
    occupation: formData.value.occupation
  });

  resetForm();
  alert('User added successfully!');
}

function openEditForm(user) {
  selectedUser.value = user;
  formData.value = { ...user, password: '', confirmPassword: '' };
  view.value = 'edit';
}

function handleEditUser() {
  if (formData.value.password && formData.value.password !== formData.value.confirmPassword) {
    alert('Passwords do not match');
    return;
  }

  const index = users.value.findIndex(u => u.id === selectedUser.value.id);
  if (index !== -1) {
    users.value[index] = {
      ...selectedUser.value,
      username: formData.value.username,
      fullName: formData.value.fullName,
      email: formData.value.email,
      status: formData.value.status,
      age: formData.value.age,
      birthdate: formData.value.birthdate,
      houseNo: formData.value.houseNo,
      street: formData.value.street,
      barangay: formData.value.barangay,
      contactNo: formData.value.contactNo,
      province: formData.value.province,
      municipality: formData.value.municipality,
      religion: formData.value.religion,
      education: formData.value.education,
      occupation: formData.value.occupation
    };
  }

  resetForm();
  alert('User updated successfully!');
}

function handleDeleteUser(id) {
  if (id === 'U001') {
    alert('Cannot delete system administrator');
    return;
  }
  if (confirm('Are you sure you want to delete this user?')) {
    users.value = users.value.filter(u => u.id !== id);
    alert('User deleted successfully!');
  }
}
</script>

<template>
  <div class="p-6 bg-white min-h-screen">
    <!-- FORM SECTION -->
    <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow">
      <h3 class="text-xl font-semibold mb-6">
        {{ view === 'add' ? 'Add New User Account' : 'Edit User Account' }}
      </h3>

      <form @submit.prevent="view === 'add' ? handleAddUser() : handleEditUser()" class="space-y-4">
        <!-- General Info -->
        <div>
          <label class="block text-sm font-semibold mb-2">Username *</label>
          <input v-model="formData.username" type="text" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
        </div>

        <div>
          <label class="block text-sm font-semibold mb-2">Full Name *</label>
          <input v-model="formData.fullName" type="text" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">Age *</label>
            <input v-model="formData.age" type="number" min="0" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Birthdate *</label>
            <input v-model="formData.birthdate" type="date" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
        </div>

        <!-- Address & Contact -->
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">House No.</label>
            <input v-model="formData.houseNo" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold mb-2">Street</label>
            <input v-model="formData.street" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Barangay</label>
            <input v-model="formData.barangay" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold mb-2">Contact No. *</label>
            <input v-model="formData.contactNo" type="tel" required class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Province</label>
            <input v-model="formData.province" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Municipality</label>
            <input v-model="formData.municipality" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">Religion</label>
            <input v-model="formData.religion" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
          <div>
            <label class="block text-sm font-semibold mb-2">Education</label>
            <input v-model="formData.education" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Occupation</label>
            <input v-model="formData.occupation" type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
        </div>

        <div>
          <label class="block text-sm font-semibold mb-2">Status *</label>
          <select v-model="formData.status" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500">
            <option>Active</option>
            <option>Inactive</option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold mb-2">Email</label>
          <input v-model="formData.email" type="email" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-semibold mb-2">Password {{ view === 'edit' ? '(leave blank to keep current)' : '*' }}</label>
            <input v-model="formData.password" type="password" :required="view==='add'" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>

          <div>
            <label class="block text-sm font-semibold mb-2">Confirm Password {{ view === 'edit' ? '' : '*' }}</label>
            <input v-model="formData.confirmPassword" type="password" :required="view==='add'" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
          </div>
        </div>

        <div class="flex gap-3 pt-4">
          <button type="submit" class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">
            {{ view === 'add' ? 'Create Account' : 'Save Changes' }}
          </button>
          <button type="button" @click="resetForm" class="px-6 py-2 bg-gray-300 text-gray-800 rounded-lg hover:bg-gray-400">
            Cancel
          </button>
        </div>
      </form>
    </div>

    <!-- USER TABLE -->
    <div v-else>
      <div class="flex justify-between items-center mb-6 flex-wrap gap-3">
        <div>
          <h3 class="text-2xl font-bold text-gray-800">Patient Accounts</h3>
          <p class="text-sm text-gray-500 mt-1">Manage patient accounts</p>
        </div>

        <!-- Search + Filter Buttons -->
        <div class="flex items-center gap-2 flex-wrap">
          <div class="relative">
            <input v-model="searchQuery" type="text" placeholder="Search users..." class="w-64 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-teal-500" />
            <button v-if="searchQuery" @click="clearSearch" class="absolute right-2 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700">✕</button>
          </div>
          <div class="flex items-center gap-2">
          <button @click="$router.push('/uikit/UserAccount')" class="px-3 py-2 rounded bg-teal-100 text-teal-700 hover:bg-teal-200">Employees</button>
          <button @click="$router.push('/uikit/UserAccountPatient')" class="px-3 py-2 rounded bg-teal-100 text-teal-700 hover:bg-teal-200">Patients</button>
          </div>
      

          <button @click="openAddForm()" class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 flex items-center gap-2">+ Add User</button>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow overflow-x-auto">
        <table class="w-full">
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>
              <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">ID</th>
              <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Username</th>
              <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Full Name</th>

              <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Email</th>
              <th class="px-6 py-4 text-left text-sm font-semibold text-gray-700">Status</th>
              <th class="px-6 py-4 text-center text-sm font-semibold text-gray-700">Actions</th>
            </tr>
          </thead>

          <tbody class="divide-y divide-gray-200">
            <tr v-for="user in filteredUsers" :key="user.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 text-sm text-gray-900">{{ user.id }}</td>
              <td class="px-6 py-4 text-sm text-gray-900">{{ user.username }}</td>
              <td class="px-6 py-4 text-sm text-gray-900">{{ user.fullName }}</td>
              <td class="px-6 py-4 text-sm text-gray-900">{{ user.email }}</td>
              <td class="px-6 py-4">
                <span :class="['px-3 py-1 rounded-full text-xs font-semibold', user.status==='Active'?'bg-green-100 text-green-700':'bg-gray-200 text-gray-700']">
                  {{ user.status }}
                </span>
              </td>
              <td class="px-6 py-4 flex justify-center gap-2">
                <button @click="openEditForm(user)" class="px-3 py-1 bg-blue-50 text-blue-700 rounded hover:bg-blue-100 text-sm font-semibold">Edit</button>
                <button @click="handleDeleteUser(user.id)" :disabled="user.id==='U001'" :class="['px-3 py-1 rounded text-sm font-semibold', user.id==='U001'?'bg-gray-100 text-gray-400 cursor-not-allowed':'bg-red-50 text-red-700 hover:bg-red-100']">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="filteredUsers.length === 0" class="text-center py-12">
        <p class="text-gray-500">{{ searchQuery ? 'No users match your search' : 'No users found' }}</p>
      </div>
    </div>
  </div>
</template>

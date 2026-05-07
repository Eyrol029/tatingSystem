<script setup>
import { computed, ref } from 'vue';

const view = ref('list');
const selectedWard = ref(null);

const wards = ref([
    {
        id: 'W001',
        name: 'Maternity Ward A',
        type: 'Maternity',
        capacity: 6,
        occupied: 4,
        status: 'Available',
        floor: '2nd Floor',
        facilities: ['Private bathroom', 'Air conditioning', 'Baby crib']
    },
    {
        id: 'W002',
        name: 'Maternity Ward B',
        type: 'Maternity',
        capacity: 4,
        occupied: 4,
        status: 'Full',
        floor: '2nd Floor',
        facilities: ['Private bathroom', 'Air conditioning']
    },
    {
        id: 'W003',
        name: 'Recovery Ward',
        type: 'Recovery',
        capacity: 8,
        occupied: 3,
        status: 'Available',
        floor: '1st Floor',
        facilities: ['Shared bathroom', 'Air conditioning', 'Monitoring equipment']
    },
    {
        id: 'W004',
        name: 'Prenatal Care Ward',
        type: 'Prenatal',
        capacity: 5,
        occupied: 2,
        status: 'Available',
        floor: '2nd Floor',
        facilities: ['Private bathroom', 'Air conditioning', 'Monitoring equipment']
    }
]);

const formData = ref({
    name: '',
    type: 'Maternity',
    capacity: '',
    occupied: '',
    status: 'Available',
    floor: '1st Floor',
    facilities: ''
});

const totalWards = computed(() => wards.value.length);
const totalCapacity = computed(() => wards.value.reduce((sum, w) => sum + w.capacity, 0));
const totalOccupied = computed(() => wards.value.reduce((sum, w) => sum + w.occupied, 0));
const totalAvailable = computed(() => wards.value.reduce((sum, w) => sum + (w.capacity - w.occupied), 0));

function handleAddWard() {
    const newWard = {
        id: `W${String(wards.value.length + 1).padStart(3, '0')}`,
        name: formData.value.name,
        type: formData.value.type,
        capacity: parseInt(formData.value.capacity),
        occupied: parseInt(formData.value.occupied),
        status: formData.value.status,
        floor: formData.value.floor,
        facilities: formData.value.facilities
            .split(',')
            .map((f) => f.trim())
            .filter((f) => f)
    };
    wards.value.push(newWard);
    resetForm();
    view.value = 'list';
}

function handleEditWard() {
    if (!selectedWard.value) return;
    const index = wards.value.findIndex((w) => w.id === selectedWard.value.id);
    if (index !== -1) {
        wards.value[index] = {
            ...selectedWard.value,
            name: formData.value.name,
            type: formData.value.type,
            capacity: parseInt(formData.value.capacity),
            occupied: parseInt(formData.value.occupied),
            status: formData.value.status,
            floor: formData.value.floor,
            facilities: formData.value.facilities
                .split(',')
                .map((f) => f.trim())
                .filter((f) => f)
        };
    }
    resetForm();
    view.value = 'list';
}

function resetForm() {
    formData.value = {
        name: '',
        type: 'Maternity',
        capacity: '',
        occupied: '',
        status: 'Available',
        floor: '1st Floor',
        facilities: ''
    };
    selectedWard.value = null;
}

function openEditForm(ward) {
    selectedWard.value = ward;
    formData.value = {
        name: ward.name,
        type: ward.type,
        capacity: ward.capacity.toString(),
        occupied: ward.occupied.toString(),
        status: ward.status,
        floor: ward.floor,
        facilities: ward.facilities.join(', ')
    };
    view.value = 'edit';
}

function getOccupancyPercentage(ward) {
    return (ward.occupied / ward.capacity) * 100;
}

function getStatusColor(status) {
    switch (status) {
        case 'Available':
            return 'bg-green-100 text-green-700';
        case 'Full':
            return 'bg-red-100 text-red-700';
        case 'Maintenance':
            return 'bg-yellow-100 text-yellow-700';
        default:
            return 'bg-gray-100 text-gray-700';
    }
}

function getOccupancyBarColor(percentage) {
    if (percentage >= 100) return 'bg-red-500';
    if (percentage >= 75) return 'bg-yellow-500';
    return 'bg-green-500';
}

function handleDeleteWard(wardId) {
    if (!wardId) return;
    if (!window.confirm('Are you sure you want to delete this ward? This action cannot be undone.')) return;
    wards.value = wards.value.filter((w) => w.id !== wardId);
    // If the deleted ward was selected for editing, clear it and go back to list
    if (selectedWard.value && selectedWard.value.id === wardId) {
        resetForm();
        view.value = 'list';
    }
}
</script>

<template>
    <div class="space-y-6 p-6 bg-white min-h-screen">
        <!-- Add/Edit Form -->
        <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-2xl font-bold text-gray-800 mb-6">
                    {{ view === 'add' ? 'Add New Ward' : 'Edit Ward Details' }}
                </h3>
                <form @submit.prevent="view === 'add' ? handleAddWard() : handleEditWard()" class="space-y-4">
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Ward Name *</label>
                        <input v-model="formData.name" type="text" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Type *</label>
                            <select v-model="formData.type" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent">
                                <option>Maternity</option>
                                <option>Recovery</option>
                                <option>Prenatal</option>
                                <option>Postnatal</option>
                                <option>ICU</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Floor *</label>
                            <select v-model="formData.floor" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent">
                                <option>Ground Floor</option>
                                <option>1st Floor</option>
                                <option>2nd Floor</option>
                                <option>3rd Floor</option>
                            </select>
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Capacity (Beds) *</label>
                            <input v-model="formData.capacity" type="number" min="1" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Currently Occupied *</label>
                            <input v-model="formData.occupied" type="number" min="0" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Status *</label>
                        <select v-model="formData.status" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent">
                            <option>Available</option>
                            <option>Full</option>
                            <option>Maintenance</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Facilities (comma-separated)</label>
                        <textarea
                            v-model="formData.facilities"
                            placeholder="e.g., Private bathroom, Air conditioning, Baby crib"
                            rows="3"
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                        ></textarea>
                    </div>
                    <div class="flex gap-3 pt-4">
                        <button type="submit" class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">
                            {{ view === 'add' ? 'Add Ward' : 'Save Changes' }}
                        </button>
                        <button
                            type="button"
                            @click="
                                resetForm();
                                view = 'list';
                            "
                            class="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- List View -->
        <template v-else>
            <div class="flex items-center justify-between">
                <div>
                    <h3 class="text-2xl font-bold text-gray-800">Wards Management</h3>
                    <p class="text-gray-600 text-sm mt-1">Monitor and manage ward occupancy</p>
                </div>
                <button @click="view = 'add'" class="px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 flex items-center gap-2">
                    <span class="text-xl">+</span>
                    Add Ward
                </button>
            </div>

            <!-- Summary Stats -->
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div class="bg-white p-4 rounded-lg shadow">
                    <p class="text-gray-500 text-sm font-semibold">Total Wards</p>
                    <p class="text-2xl font-bold text-gray-900 mt-1">{{ totalWards }}</p>
                </div>
                <div class="bg-white p-4 rounded-lg shadow">
                    <p class="text-gray-500 text-sm font-semibold">Total Capacity</p>
                    <p class="text-2xl font-bold text-gray-900 mt-1">{{ totalCapacity }} beds</p>
                </div>
                <div class="bg-white p-4 rounded-lg shadow">
                    <p class="text-gray-500 text-sm font-semibold">Currently Occupied</p>
                    <p class="text-2xl font-bold text-gray-900 mt-1">{{ totalOccupied }} beds</p>
                </div>
                <div class="bg-white p-4 rounded-lg shadow">
                    <p class="text-gray-500 text-sm font-semibold">Available</p>
                    <p class="text-2xl font-bold text-gray-900 mt-1">{{ totalAvailable }} beds</p>
                </div>
            </div>

            <!-- Wards Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div v-for="ward in wards" :key="ward.id" class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-start justify-between mb-4">
                        <div class="flex items-start gap-3">
                            <div class="p-2 bg-teal-100 rounded-lg">
                                <span class="text-xl">🏥</span>
                            </div>
                            <div>
                                <h4 class="font-bold text-gray-800">{{ ward.name }}</h4>
                                <p class="text-sm text-gray-500 mt-1">{{ ward.type }} • {{ ward.floor }}</p>
                            </div>
                        </div>
                        <span :class="['px-2 py-1 rounded text-xs font-semibold', getStatusColor(ward.status)]">
                            {{ ward.status }}
                        </span>
                    </div>

                    <div class="mb-4">
                        <div class="flex justify-between text-sm mb-2">
                            <span class="text-gray-600 font-semibold">Occupancy</span>
                            <span class="text-gray-900 font-semibold">{{ ward.occupied }}/{{ ward.capacity }} beds</span>
                        </div>
                        <div class="w-full bg-gray-200 rounded-full h-2">
                            <div :class="['h-2 rounded-full transition-all', getOccupancyBarColor(getOccupancyPercentage(ward))]" :style="{ width: Math.min(getOccupancyPercentage(ward), 100) + '%' }"></div>
                        </div>
                    </div>

                    <div v-if="ward.facilities.length > 0" class="mb-4">
                        <p class="text-sm text-gray-500 font-semibold mb-2">Facilities:</p>
                        <div class="flex flex-wrap gap-1">
                            <span v-for="(facility, index) in ward.facilities" :key="index" class="px-2 py-1 bg-gray-100 text-gray-600 rounded text-xs">
                                {{ facility }}
                            </span>
                        </div>
                    </div>

                    <div class="flex gap-2">
                        <button @click="openEditForm(ward)" class="flex-1 px-3 py-2 bg-teal-50 text-teal-700 rounded-lg hover:bg-teal-100 flex items-center justify-center gap-2 font-semibold">Edit Ward</button>
                        <button @click="handleDeleteWard(ward.id)" class="flex-1 px-3 py-2 bg-red-50 text-red-700 rounded-lg hover:bg-red-100 flex items-center justify-center gap-2 font-semibold">Delete Ward</button>
                    </div>
                </div>
            </div>

            <div v-if="wards.length === 0" class="bg-white rounded-lg shadow p-12 text-center">
                <p class="text-gray-600">No wards available</p>
            </div>
        </template>
    </div>
</template>

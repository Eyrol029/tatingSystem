<script setup>
import { ref } from 'vue';

const view = ref('list');
const services = ref([
    {
        id: 'S001',
        name: 'Prenatal Checkup',
        description: 'Regular pregnancy monitoring and consultation',
        price: 1500,
        category: 'Maternity',
        duration: '30 minutes'
    },
    {
        id: 'S002',
        name: 'Family Planning Consultation',
        description: 'Contraception advice and planning services',
        price: 800,
        category: 'Family Planning',
        duration: '45 minutes'
    },
    {
        id: 'S003',
        name: 'Normal Delivery Package',
        description: 'Complete birthing service with 24-hour monitoring',
        price: 25000,
        category: 'Maternity',
        duration: 'As needed'
    },
    {
        id: 'S004',
        name: 'Postnatal Care',
        description: 'Post-delivery checkup and care',
        price: 1200,
        category: 'Maternity',
        duration: '30 minutes'
    }
]);

const formData = ref({
    name: '',
    description: '',
    price: '',
    category: 'Maternity',
    duration: ''
});
</script>

<template>
    <div class="space-y-6 p-6 bg-white min-h-screen">
        <!-- Add/Edit Form -->
        <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl">
            <div class="bg-white rounded-lg shadow p-6">
                <h3 class="text-2xl font-bold text-gray-800 mb-6">
                    {{ view === 'add' ? 'Add New Clinical Service' : 'Edit Clinical Service' }}
                </h3>
                <form @submit.prevent="view === 'add' ? handleAddService() : handleEditService()" class="space-y-4">
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Service Name *</label>
                        <input v-model="formData.name" type="text" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Category *</label>
                        <select v-model="formData.category" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent">
                            <option>Maternity</option>
                            <option>Family Planning</option>
                            <option>Prenatal Care</option>
                            <option>Postnatal Care</option>
                            <option>General Consultation</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">Description *</label>
                        <textarea v-model="formData.description" required rows="3" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"></textarea>
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Price (₱) *</label>
                            <input v-model="formData.price" type="number" step="0.01" required class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                        </div>
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">Duration *</label>
                            <input v-model="formData.duration" type="text" required placeholder="e.g., 30 minutes" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent" />
                        </div>
                    </div>
                    <div class="flex gap-3 pt-4">
                        <button type="submit" class="px-6 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700">
                            {{ view === 'add' ? 'Add Service' : 'Save Changes' }}
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
                    <h3 class="text-2xl font-bold text-gray-800">Clinical Services</h3>
                    <p class="text-gray-600 text-sm mt-1">clinical services and pricing</p>
                </div>
            </div>

            <!-- Services Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div v-for="service in services" :key="service.id" class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-start justify-between mb-4">
                        <div>
                            <h4 class="font-bold text-gray-800">{{ service.name }}</h4>
                            <p class="text-sm text-gray-500 mt-1">{{ service.category }}</p>
                        </div>
                        <span class="px-3 py-1 bg-teal-100 text-teal-700 rounded font-semibold text-sm">
                            {{ service.id }}
                        </span>
                    </div>
                    <p class="text-gray-600 text-sm mb-4">{{ service.description }}</p>
                    <div class="space-y-2 mb-4">
                        <div class="flex justify-between text-sm">
                            <span class="text-gray-500 font-semibold">Price:</span>
                            <span class="text-gray-900 font-bold">₱{{ service.price.toLocaleString() }}</span>
                        </div>
                        <div class="flex justify-between text-sm">
                            <span class="text-gray-500 font-semibold">Duration:</span>
                            <span class="text-gray-900 font-semibold">{{ service.duration }}</span>
                        </div>
                    </div>
                    <div class="flex gap-2 pt-4 border-t">
                    </div>
                </div>
            </div>

            <div v-if="services.length === 0" class="bg-white rounded-lg shadow p-12 text-center">
                <p class="text-gray-600">No services available</p>
            </div>
        </template>
    </div>
</template>

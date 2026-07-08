<script setup>
import { ref, computed, onMounted } from 'vue';

const API_URL = 'http://localhost:8080/api/clinical-services';

const services = ref([]);
const loading = ref(false);
const errorMsg = ref('');
const searchQuery = ref('');
const categoryFilter = ref('');

const categories = [
    'Maternity',
    'Family Planning',
    'Prenatal Care',
    'Postnatal Care',
    'General Consultation'
];

async function fetchServices() {
    loading.value = true;
    errorMsg.value = '';
    try {
        const res = await fetch(API_URL);
        if (!res.ok) throw new Error('Failed to fetch services');
        services.value = await res.json();
    } catch (err) {
        errorMsg.value = 'Could not load services. Please try again later.';
    } finally {
        loading.value = false;
    }
}

const filteredServices = computed(() => {
    const q = searchQuery.value.trim().toLowerCase();
    return services.value.filter((service) => {
        if (categoryFilter.value && service.category !== categoryFilter.value) return false;
        if (!q) return true;
        return (
            (service.name || '').toLowerCase().includes(q) ||
            (service.description || '').toLowerCase().includes(q) ||
            (service.category || '').toLowerCase().includes(q)
        );
    });
});

function clearFilters() {
    searchQuery.value = '';
    categoryFilter.value = '';
}

onMounted(fetchServices);
</script>

<template>
    <div class="space-y-6 p-6 bg-white min-h-screen">

        <!-- Error Banner -->
        <div v-if="errorMsg" class="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-lg">
            {{ errorMsg }}
        </div>

        <div>
            <h3 class="text-2xl font-bold text-gray-800">Clinic Services</h3>
            <p class="text-gray-600 text-sm mt-1">Browse the services offered by our clinic</p>
        </div>

        <!-- Search + Category Filter -->
        <div class="flex flex-col sm:flex-row gap-3">
            <div class="relative flex-1">
                <input
                    v-model="searchQuery"
                    type="text"
                    placeholder="Search services…"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
                />
                <button
                    v-if="searchQuery"
                    @click="searchQuery = ''"
                    class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >×</button>
            </div>
            <select
                v-model="categoryFilter"
                class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:border-transparent"
            >
                <option value="">All Categories</option>
                <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
            </select>
            <button
                v-if="searchQuery || categoryFilter"
                @click="clearFilters"
                class="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg hover:bg-gray-200 font-semibold"
            >
                Clear
            </button>
        </div>

        <!-- Loading Indicator -->
        <div v-if="loading" class="text-center py-12">
            <p class="text-gray-500 text-lg">Loading services...</p>
        </div>

        <!-- Services Grid — view-only, no Edit/Delete -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div
                v-for="service in filteredServices"
                :key="service.id"
                class="bg-white rounded-lg shadow p-6 border border-gray-100"
            >
                <div class="flex items-start justify-between mb-4">
                    <div>
                        <h4 class="font-bold text-gray-800">{{ service.name }}</h4>
                        <p class="text-sm text-gray-500 mt-1">{{ service.category }}</p>
                    </div>
                    <span class="px-3 py-1 bg-teal-100 text-teal-700 rounded font-semibold text-sm">
                        {{ service.serviceCode }}
                    </span>
                </div>

                <p class="text-gray-600 text-sm mb-4">{{ service.description }}</p>

                <div class="space-y-2 pt-4 border-t">
                    <div class="flex justify-between text-sm">
                        <span class="text-gray-500 font-semibold">Price:</span>
                        <span class="text-gray-900 font-bold">₱{{ service.price.toLocaleString() }}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                        <span class="text-gray-500 font-semibold">Duration:</span>
                        <span class="text-gray-900 font-semibold">{{ service.duration }}</span>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="!loading && filteredServices.length === 0" class="bg-white rounded-lg shadow p-12 text-center">
            <p class="text-gray-500">
                {{ searchQuery || categoryFilter ? 'No services match your search.' : 'No services available at this time.' }}
            </p>
        </div>
    </div>
</template>
<script setup>
import { ref, computed } from 'vue';

// Initial Mock Data for Clinical Tools Inventory
const inventoryItems = ref([
    {
        id: 'T001',
        name: 'Littmann Classic III Stethoscope',
        quantity: 12,
        minStock: 3,
        cost: 6500
    },
    {
        id: 'T002',
        name: 'Welch Allyn Sphygmomanometer',
        quantity: 8,
        minStock: 2,
        cost: 4200
    },
    {
        id: 'T003',
        name: 'Surgical Delivery Kit (Obstetric)',
        quantity: 15,
        minStock: 5,
        cost: 12000
    },
    {
        id: 'T004',
        name: 'Fetal Doppler Monitor',
        quantity: 2,
        minStock: 2,
        cost: 18500
    },
    {
        id: 'T005',
        name: 'Non-Contact Infrared Thermometer',
        quantity: 18,
        minStock: 4,
        cost: 1800
    },
    {
        id: 'T006',
        name: 'Gynaecological Speculum Set (Metal)',
        quantity: 6,
        minStock: 2,
        cost: 3500
    },
    {
        id: 'T007',
        name: 'Clinical Centrifuge (Benchtop)',
        quantity: 1,
        minStock: 1,
        cost: 45000
    },
    {
        id: 'T008',
        name: 'N95 Respirator Mask (Box of 20)',
        quantity: 0,
        minStock: 5,
        cost: 1200
    }
]);

// Navigation & Interactive states
const view = ref('list'); // 'list' | 'add' | 'edit'
const searchQuery = ref('');
const selectedStockStatus = ref('All');
const selectedToolForAction = ref(null);

// Forms Data
const formData = ref({
    name: '',
    quantity: 0,
    minStock: 2,
    cost: 0
});

// Computed stats
const totalDistinctTools = computed(() => inventoryItems.value.length);
const totalPhysicalCount = computed(() => inventoryItems.value.reduce((sum, item) => sum + item.quantity, 0));

const lowStockItemsCount = computed(() => {
    return inventoryItems.value.filter(item => item.quantity <= item.minStock && item.quantity > 0).length;
});

const outOfStockItemsCount = computed(() => {
    return inventoryItems.value.filter(item => item.quantity === 0).length;
});

// Filtered Items logic
const filteredItems = computed(() => {
    return inventoryItems.value.filter(item => {
        // Search filter
        const matchesSearch = item.name.toLowerCase().includes(searchQuery.value.toLowerCase());
        
        // Stock status filter
        let matchesStock = true;
        if (selectedStockStatus.value === 'Low Stock') {
            matchesStock = item.quantity <= item.minStock && item.quantity > 0;
        } else if (selectedStockStatus.value === 'Out of Stock') {
            matchesStock = item.quantity === 0;
        } else if (selectedStockStatus.value === 'In Stock') {
            matchesStock = item.quantity > item.minStock;
        }

        return matchesSearch && matchesStock;
    });
});

// Helper style methods
const getStockBadgeClass = (item) => {
    if (item.quantity === 0) return 'bg-red-100 text-red-800 border border-red-200';
    if (item.quantity <= item.minStock) return 'bg-amber-100 text-amber-800 border border-amber-200';
    return 'bg-emerald-100 text-emerald-800 border border-emerald-200';
};

// CRUD handlers
function handleAddTool() {
    const newTool = {
        id: `T${String(inventoryItems.value.length + 1).padStart(3, '0')}`,
        name: formData.value.name,
        quantity: parseInt(formData.value.quantity) || 0,
        minStock: parseInt(formData.value.minStock) || 2,
        cost: parseFloat(formData.value.cost) || 0
    };
    
    inventoryItems.value.unshift(newTool);
    resetForm();
    view.value = 'list';
}

function handleEditTool() {
    if (!selectedToolForAction.value) return;
    const index = inventoryItems.value.findIndex(t => t.id === selectedToolForAction.value.id);
    if (index !== -1) {
        inventoryItems.value[index] = {
            ...selectedToolForAction.value,
            name: formData.value.name,
            quantity: parseInt(formData.value.quantity) || 0,
            minStock: parseInt(formData.value.minStock) || 2,
            cost: parseFloat(formData.value.cost) || 0
        };
    }
    resetForm();
    view.value = 'list';
}

function openEditForm(tool) {
    selectedToolForAction.value = tool;
    formData.value = { ...tool };
    view.value = 'edit';
}

function handleDeleteTool(toolId) {
    if (window.confirm('Are you sure you want to remove this clinical tool from the inventory? This action is permanent.')) {
        inventoryItems.value = inventoryItems.value.filter(t => t.id !== toolId);
    }
}

// Quick adjustments
function incrementQuantity(item) {
    item.quantity++;
}

function decrementQuantity(item) {
    if (item.quantity > 0) {
        item.quantity--;
    }
}

function resetForm() {
    formData.value = {
        name: '',
        quantity: 0,
        minStock: 2,
        cost: 0
    };
    selectedToolForAction.value = null;
}
</script>

<template>
    <div class="space-y-6 p-6 bg-slate-50 min-h-screen">
        <!-- HEADER BLOCK -->
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div>
                <h3 class="text-3xl font-extrabold text-slate-800 tracking-tight flex items-center gap-3">
                    <span class="p-2 bg-teal-600 text-white rounded-xl shadow-md">📦</span>
                    Clinical Tools Inventory
                </h3>
                <p class="text-slate-500 text-sm mt-1">Monitor and manage essential hospital tools and equipment.</p>
            </div>
            
            <div class="flex flex-wrap items-center gap-3">
                <button 
                    v-if="view !== 'list'" 
                    @click="view = 'list'; resetForm()" 
                    class="px-4 py-2.5 bg-slate-200 text-slate-700 hover:bg-slate-300 rounded-xl font-semibold flex items-center gap-2 transition-all"
                >
                    ← Back to Inventory
                </button>
                <button 
                    v-else
                    @click="view = 'add'" 
                    class="px-5 py-2.5 bg-teal-600 hover:bg-teal-700 text-white rounded-xl font-semibold flex items-center gap-2 shadow-lg shadow-teal-600/20 transform hover:-translate-y-0.5 transition-all"
                >
                    <span class="text-lg font-bold">+</span> Add Clinical Tool
                </button>
            </div>
        </div>

        <!-- STATS DASHBOARD -->
        <div v-if="view === 'list'" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            <!-- Stat 1 -->
            <div class="bg-white p-5 rounded-2xl shadow-sm border border-slate-200/80 flex items-center gap-4 transition-all hover:shadow-md">
                <div class="p-3.5 bg-teal-50 text-teal-600 rounded-xl">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                    </svg>
                </div>
                <div>
                    <p class="text-xs text-slate-400 font-bold uppercase tracking-wider">Total Unique Tools</p>
                    <h4 class="text-2xl font-black text-slate-800 mt-1">{{ totalDistinctTools }}</h4>
                </div>
            </div>

            <!-- Stat 2 -->
            <div class="bg-white p-5 rounded-2xl shadow-sm border border-slate-200/80 flex items-center gap-4 transition-all hover:shadow-md">
                <div class="p-3.5 bg-sky-50 text-sky-600 rounded-xl">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                    </svg>
                </div>
                <div>
                    <p class="text-xs text-slate-400 font-bold uppercase tracking-wider">Total Stock Units</p>
                    <h4 class="text-2xl font-black text-slate-800 mt-1">{{ totalPhysicalCount }}</h4>
                </div>
            </div>

            <!-- Stat 3 -->
            <div class="bg-white p-5 rounded-2xl shadow-sm border border-slate-200/80 flex items-center gap-4 transition-all hover:shadow-md">
                <div class="p-3.5 bg-red-50 text-red-600 rounded-xl">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                </div>
                <div>
                    <p class="text-xs text-slate-400 font-bold uppercase tracking-wider">Out of Stock</p>
                    <h4 class="text-2xl font-black text-slate-800 mt-1">{{ outOfStockItemsCount }}</h4>
                </div>
            </div>

            <!-- Stat 4 -->
            <div class="bg-white p-5 rounded-2xl shadow-sm border border-slate-200/80 flex items-center gap-4 transition-all hover:shadow-md">
                <div class="p-3.5 bg-amber-50 text-amber-600 rounded-xl">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <div>
                    <p class="text-xs text-slate-400 font-bold uppercase tracking-wider">Low Stock Warnings</p>
                    <h4 class="text-2xl font-black text-slate-800 mt-1">{{ lowStockItemsCount }}</h4>
                </div>
            </div>
        </div>

        <!-- LIST & CONTROLS VIEW -->
        <div v-if="view === 'list'" class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
            <!-- SEARCH AND FILTER CONTROLS -->
            <div class="p-5 border-b border-slate-100 bg-slate-50/50 flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div class="relative flex-1 max-w-lg">
                    <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400 text-base">🔍</span>
                    <input 
                        v-model="searchQuery" 
                        type="text" 
                        placeholder="Search by tool name..." 
                        class="w-full pl-10 pr-4 py-2.5 bg-white border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all shadow-inner"
                    />
                    <button 
                        v-if="searchQuery" 
                        @click="searchQuery = ''" 
                        class="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-slate-600 transition-colors"
                    >
                        ✕
                    </button>
                </div>
                
                <div class="flex items-center gap-3">
                    <!-- Stock level dropdown -->
                    <div class="flex flex-col sm:flex-row sm:items-center gap-2">
                        <span class="text-xs font-bold text-slate-400 uppercase">Stock Level</span>
                        <select v-model="selectedStockStatus" class="px-3.5 py-2.5 bg-white border border-slate-200 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 transition-all font-semibold text-slate-700">
                            <option value="All">All Stock Levels</option>
                            <option value="In Stock">In Stock</option>
                            <option value="Low Stock">Low Stock</option>
                            <option value="Out of Stock">Out of Stock</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- INVENTORY DATA TABLE -->
            <div class="overflow-x-auto">
                <table class="w-full min-w-[700px] text-left border-collapse">
                    <thead>
                        <tr class="bg-slate-100/50 text-slate-500 font-bold text-xs uppercase tracking-wider border-b border-slate-200/80">
                            <th class="py-4 px-6">ID</th>
                            <th class="py-4 px-6">Tool Name</th>
                            <th class="py-4 px-6 text-center">Stock Units</th>
                            <th class="py-4 px-6">Unit Cost</th>
                            <th class="py-4 px-6 text-right">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-slate-100 text-sm">
                        <tr 
                            v-for="item in filteredItems" 
                            :key="item.id" 
                            class="hover:bg-slate-50/80 transition-colors"
                        >
                            <!-- ID -->
                            <td class="py-4 px-6 font-extrabold text-teal-700">{{ item.id }}</td>
                            
                            <!-- Tool Name -->
                            <td class="py-4 px-6">
                                <div class="font-bold text-slate-800 text-base">{{ item.name }}</div>
                            </td>

                            <!-- Stock Units Adjuster -->
                            <td class="py-4 px-6 text-center">
                                <div class="flex flex-col items-center gap-1.5">
                                    <div class="flex items-center justify-center gap-2">
                                        <button 
                                            @click="decrementQuantity(item)" 
                                            class="w-6 h-6 flex items-center justify-center bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-extrabold focus:outline-none transition-colors"
                                            title="Decrease Stock"
                                        >
                                            -
                                        </button>
                                        <span class="w-8 font-black text-slate-800 text-base text-center">{{ item.quantity }}</span>
                                        <button 
                                            @click="incrementQuantity(item)" 
                                            class="w-6 h-6 flex items-center justify-center bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-extrabold focus:outline-none transition-colors"
                                            title="Increase Stock"
                                        >
                                            +
                                        </button>
                                    </div>
                                    <span :class="['px-2 py-0.5 rounded-full text-xxs font-bold uppercase', getStockBadgeClass(item)]">
                                        {{ item.quantity === 0 ? 'Out of Stock' : (item.quantity <= item.minStock ? 'Low Stock' : 'In Stock') }}
                                    </span>
                                </div>
                            </td>

                            <!-- Unit Cost -->
                            <td class="py-4 px-6">
                                <span class="font-bold text-slate-900 text-base">₱{{ item.cost.toLocaleString() }}</span>
                            </td>

                            <!-- Actions -->
                            <td class="py-4 px-6 text-right">
                                <div class="flex items-center justify-end gap-2">
                                    <button 
                                        @click="openEditForm(item)" 
                                        class="px-3.5 py-1.5 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-lg text-xs font-bold transition-all flex items-center gap-1"
                                        title="Edit tool details"
                                    >
                                        ✏️ Edit
                                    </button>
                                    <button 
                                        @click="handleDeleteTool(item.id)" 
                                        class="px-3.5 py-1.5 bg-red-50 hover:bg-red-100 text-red-700 rounded-lg text-xs font-bold transition-all flex items-center gap-1"
                                        title="Delete Tool"
                                    >
                                        🗑️ Delete
                                    </button>
                                </div>
                            </td>
                        </tr>
                        
                        <tr v-if="filteredItems.length === 0">
                            <td colspan="5" class="text-center py-12 text-slate-500 bg-slate-50/30">
                                <div class="text-3xl mb-2">🔍</div>
                                <p class="text-base font-bold text-slate-700">No clinical tools found</p>
                                <p class="text-xs text-slate-400 mt-1">Try resetting your search query.</p>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- ADD / EDIT TOOL FORM -->
        <div v-if="view === 'add' || view === 'edit'" class="max-w-2xl mx-auto bg-white rounded-2xl shadow-lg border border-slate-200/80 overflow-hidden">
            <div class="bg-gradient-to-r from-teal-700 to-emerald-800 px-6 py-4 text-white">
                <h4 class="text-xl font-bold flex items-center gap-2">
                    <span>{{ view === 'add' ? '📝 Add New Clinical Tool' : '✏️ Edit Clinical Tool details' }}</span>
                </h4>
                <p class="text-teal-100 text-xs mt-0.5">Please populate all required fields (*) to correctly manage stock tracking.</p>
            </div>
            
            <form @submit.prevent="view === 'add' ? handleAddTool() : handleEditTool()" class="p-6 space-y-5">
                <div>
                    <label class="block text-xs font-black uppercase text-slate-500 tracking-wider mb-1.5">Tool Name *</label>
                    <input 
                        v-model="formData.name" 
                        type="text" 
                        required 
                        placeholder="e.g. Littmann Classic III Stethoscope"
                        class="w-full px-4 py-2 border border-slate-200 rounded-xl text-sm focus:ring-2 focus:ring-teal-500 focus:border-transparent focus:outline-none font-medium text-slate-800" 
                    />
                </div>

                <div class="grid grid-cols-1 sm:grid-cols-3 gap-5">
                    <div>
                        <label class="block text-xs font-black uppercase text-slate-500 tracking-wider mb-1.5">Stock Units *</label>
                        <input 
                            v-model.number="formData.quantity" 
                            type="number" 
                            min="0" 
                            required
                            class="w-full px-4 py-2 border border-slate-200 rounded-xl text-sm focus:ring-2 focus:ring-teal-500 focus:border-transparent focus:outline-none font-medium text-slate-800" 
                        />
                    </div>

                    <div>
                        <label class="block text-xs font-black uppercase text-slate-500 tracking-wider mb-1.5">Min Stock Limit *</label>
                        <input 
                            v-model.number="formData.minStock" 
                            type="number" 
                            min="1" 
                            required
                            class="w-full px-4 py-2 border border-slate-200 rounded-xl text-sm focus:ring-2 focus:ring-teal-500 focus:border-transparent focus:outline-none font-medium text-slate-800" 
                        />
                    </div>

                    <div>
                        <label class="block text-xs font-black uppercase text-slate-500 tracking-wider mb-1.5">Unit Cost (₱) *</label>
                        <input 
                            v-model.number="formData.cost" 
                            type="number" 
                            min="0" 
                            required
                            class="w-full px-4 py-2 border border-slate-200 rounded-xl text-sm focus:ring-2 focus:ring-teal-500 focus:border-transparent focus:outline-none font-medium text-slate-800" 
                        />
                    </div>
                </div>

                <div class="flex items-center justify-end gap-3 pt-4 border-t border-slate-100">
                    <button 
                        type="button" 
                        @click="view = 'list'; resetForm()" 
                        class="px-5 py-2.5 bg-slate-150 hover:bg-slate-200 text-slate-700 rounded-xl font-bold transition-all text-sm"
                    >
                        Cancel
                    </button>
                    <button 
                        type="submit" 
                        class="px-6 py-2.5 bg-teal-600 hover:bg-teal-700 text-white rounded-xl font-bold shadow-lg shadow-teal-600/10 transition-all text-sm"
                    >
                        {{ view === 'add' ? 'Save Tool' : 'Apply Changes' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
.text-xxs {
  font-size: 0.65rem;
}
.text-xxs {
  line-height: 1rem;
}
</style>

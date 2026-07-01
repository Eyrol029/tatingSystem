<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'

const BASE = 'http://localhost:8080/api/dashboard'

// ── Filter state ──────────────────────────────────────────────────────────────
const dateFilter      = ref('Today')
const customStartDate = ref('')
const customEndDate   = ref('')
const loading         = ref(false)
const errorMsg        = ref('')

// ── Dashboard data ────────────────────────────────────────────────────────────
const stats = ref({
  totalPatients:      0,
  totalDeliveries:    0,
  totalAppointments:  0,
  totalPrenatalRecords: 0
})

const financial = ref({
  totalRevenue:  0,
  totalExpenses: 0,
  netIncome:     0
})

const recentActivities  = ref([])
const pendingAppointments = ref([])

// ── Compute date range from filter ────────────────────────────────────────────
const today = new Date().toISOString().split('T')[0]

const dateRange = computed(() => {
  if (dateFilter.value === 'Custom') {
    return { start: customStartDate.value || null, end: customEndDate.value || null }
  }
  const now  = new Date()
  const yyyy = now.getFullYear()
  const mm   = String(now.getMonth() + 1).padStart(2, '0')
  const dd   = String(now.getDate()).padStart(2, '0')
  const todayStr = `${yyyy}-${mm}-${dd}`

  if (dateFilter.value === 'Today') {
    return { start: todayStr, end: todayStr }
  }
  if (dateFilter.value === 'Last 7 days') {
    const past = new Date(now); past.setDate(now.getDate() - 6)
    return {
      start: past.toISOString().split('T')[0],
      end:   todayStr
    }
  }
  if (dateFilter.value === 'This Month') {
    return { start: `${yyyy}-${mm}-01`, end: todayStr }
  }
  return { start: null, end: null } // All time
})

// ── Fetch ─────────────────────────────────────────────────────────────────────
async function fetchSummary() {
  loading.value  = true
  errorMsg.value = ''
  try {
    const params = {}
    if (dateRange.value.start) params.start = dateRange.value.start
    if (dateRange.value.end)   params.end   = dateRange.value.end

    const res  = await axios.get(`${BASE}/summary`, { params })
    const data = res.data

    stats.value = {
      totalPatients:       data.totalPatients       ?? 0,
      totalDeliveries:     data.totalDeliveries     ?? 0,
      totalAppointments:   data.totalAppointments   ?? 0,
      totalPrenatalRecords:data.totalPrenatalRecords ?? 0
    }

    financial.value = {
      totalRevenue:  data.totalRevenue  ?? 0,
      totalExpenses: data.totalExpenses ?? 0,
      netIncome:     data.netIncome     ?? 0
    }

    recentActivities.value   = data.recentActivities   ?? []
    pendingAppointments.value= data.pendingAppointments ?? []
  } catch (e) {
    errorMsg.value = 'Failed to load dashboard data. Is the server running?'
    console.error(e)
  } finally {
    loading.value = false
  }
}

// ── Activity icon by type ─────────────────────────────────────────────────────
function activityDot(type) {
  return {
    patient:     'bg-blue-500',
    appointment: 'bg-purple-500',
    prenatal:    'bg-pink-500',
    payment:     'bg-green-500'
  }[type] || 'bg-green-500'
}

// ── Generate report ───────────────────────────────────────────────────────────
function generateReport() {
  const { start, end } = dateRange.value
  const label = dateFilter.value === 'Custom'
    ? `${start || 'All'} to ${end || 'Now'}`
    : dateFilter.value
  alert(`Generating report for: ${label}\n\nStats:\n` +
    `• Patients: ${stats.value.totalPatients}\n` +
    `• Deliveries: ${stats.value.totalDeliveries}\n` +
    `• Appointments: ${stats.value.totalAppointments}\n` +
    `• Prenatal Records: ${stats.value.totalPrenatalRecords}`)
}

// ── Set custom filter when dates change ───────────────────────────────────────
watch([customStartDate, customEndDate], () => {
  if (customStartDate.value || customEndDate.value) {
    dateFilter.value = 'Custom'
  }
})

// ── Re-fetch when filter changes ──────────────────────────────────────────────
watch(dateRange, fetchSummary, { deep: true })

onMounted(fetchSummary)
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">

    <!-- Header -->
    <div class="flex justify-between items-start mb-6 gap-4 flex-wrap">
      <div>
        <h1 class="text-2xl font-bold text-gray-800">Welcome to the Dashboard</h1>
        <p class="text-gray-500 text-sm mt-1">Overview of clinic operations and key metrics</p>
      </div>

      <!-- Date filter -->
      <div class="bg-white rounded-lg shadow p-4 min-w-[320px]">
        <div class="flex items-center gap-2 mb-3">
          <span class="text-gray-700 font-semibold text-sm">📅 Filter By Date</span>
          <span v-if="loading"
            class="ml-auto text-xs text-purple-500 animate-pulse">Loading…</span>
        </div>
        <div class="flex gap-2 mb-3 flex-wrap">
          <button v-for="f in ['Today','Last 7 days','This Month']" :key="f"
            @click="dateFilter = f; customStartDate = ''; customEndDate = ''"
            :class="['px-3 py-1.5 rounded text-xs font-medium transition',
              dateFilter === f
                ? 'bg-[#8e4f70] text-white'
                : 'bg-gray-100 text-gray-600 hover:bg-[#8e4f70] hover:text-white']">
            {{ f }}
          </button>
        </div>
        <div>
          <label class="block text-xs text-gray-500 mb-1">Custom range</label>
          <div class="flex gap-2">
            <input v-model="customStartDate" type="date"
              class="flex-1 px-2 py-1.5 border rounded text-xs focus:outline-none focus:ring-1 focus:ring-purple-400" />
            <input v-model="customEndDate" type="date"
              class="flex-1 px-2 py-1.5 border rounded text-xs focus:outline-none focus:ring-1 focus:ring-purple-400" />
          </div>
        </div>
      </div>
    </div>

    <!-- Error -->
    <div v-if="errorMsg"
      class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg text-sm flex justify-between">
      {{ errorMsg }}
      <button @click="errorMsg = ''" class="ml-3 font-bold text-red-400">✕</button>
    </div>

    <!-- Generate Report -->
    <div class="flex justify-end mb-6">
      <button @click="generateReport"
        class="px-6 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-600 text-sm font-semibold transition">
        Generate Report
      </button>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-6">

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
            <span class="text-xl">👤</span>
          </div>
          <span class="text-gray-500 text-sm">Total Patients</span>
        </div>
        <div class="text-3xl font-bold text-gray-800">
          {{ loading ? '—' : stats.totalPatients.toLocaleString() }}
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-pink-100 rounded-full flex items-center justify-center">
            <span class="text-xl">👶</span>
          </div>
          <span class="text-gray-500 text-sm">Total Deliveries</span>
        </div>
        <div class="text-3xl font-bold text-gray-800">
          {{ loading ? '—' : stats.totalDeliveries.toLocaleString() }}
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-teal-100 rounded-full flex items-center justify-center">
            <span class="text-xl">🩺</span>
          </div>
          <span class="text-gray-500 text-sm">Prenatal Records</span>
        </div>
        <div class="text-3xl font-bold text-gray-800">
          {{ loading ? '—' : stats.totalPrenatalRecords.toLocaleString() }}
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-10 h-10 bg-purple-100 rounded-full flex items-center justify-center">
            <span class="text-xl">📅</span>
          </div>
          <span class="text-gray-500 text-sm">Total Appointments</span>
        </div>
        <div class="text-3xl font-bold text-gray-800">
          {{ loading ? '—' : stats.totalAppointments.toLocaleString() }}
        </div>
      </div>
    </div>

    <!-- Recent Activity + Pending Requests -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">

      <!-- Recent Activity -->
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-2 mb-4">
          <span class="text-xl">📊</span>
          <h3 class="font-bold text-gray-800">Recent Activity</h3>
        </div>

        <div v-if="loading" class="text-center py-6 text-gray-400 text-sm">Loading…</div>

        <div v-else-if="recentActivities.length === 0"
          class="text-center py-6 text-gray-400 text-sm italic">
          No recent activity for this period.
        </div>

        <div v-else class="space-y-4">
          <div v-for="(activity, i) in recentActivities" :key="i"
            class="flex items-start gap-3">
            <div class="w-2.5 h-2.5 rounded-full mt-1.5 shrink-0"
              :class="activityDot(activity.type)"></div>
            <div>
              <p class="text-gray-800 text-sm">{{ activity.text }}</p>
              <p class="text-gray-400 text-xs">{{ activity.time }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Pending Appointments -->
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center gap-2 mb-4">
          <span class="text-xl">📋</span>
          <h3 class="font-bold text-gray-800">Upcoming Appointments</h3>
        </div>

        <div v-if="loading" class="text-center py-6 text-gray-400 text-sm">Loading…</div>

        <div v-else-if="pendingAppointments.length === 0"
          class="text-center py-6 text-gray-400 text-sm italic">
          No upcoming appointments.
        </div>

        <div v-else class="space-y-3">
          <div v-for="(req, i) in pendingAppointments" :key="i"
            class="bg-purple-50 rounded-lg p-4">
            <div class="flex justify-between items-start mb-1">
              <div>
                <p class="text-xs text-gray-500">{{ req.type }}</p>
                <p class="font-bold text-gray-800">{{ req.name }}</p>
              </div>
              <span class="px-2 py-0.5 bg-yellow-400 text-yellow-900 text-xs rounded-full font-semibold">
                {{ req.status }}
              </span>
            </div>
            <p class="text-xs text-gray-500">{{ req.subtitle }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Financial Overview -->
    <div class="bg-white rounded-lg shadow p-6">
      <div class="flex justify-between items-center mb-6">
        <div class="flex items-center gap-2">
          <span class="text-xl">💰</span>
          <h3 class="font-bold text-gray-800">
            Financial Overview
            <span class="text-sm font-normal text-gray-400 ml-1">— {{ dateFilter }}</span>
          </h3>
        </div>
        <button @click="$router.push('/uikit/PaymentDashboard')"
          class="text-orange-500 text-sm font-semibold hover:text-orange-700 transition">
          View Details →
        </button>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <div class="bg-orange-50 rounded-lg p-4">
          <p class="text-gray-500 text-sm mb-1">Total Revenue</p>
          <p class="text-2xl font-bold text-orange-500">
            {{ loading ? '—' : '₱' + financial.totalRevenue.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
        <div class="bg-red-50 rounded-lg p-4">
          <p class="text-gray-500 text-sm mb-1">Total Expenses</p>
          <p class="text-2xl font-bold text-red-500">
            {{ loading ? '—' : '₱' + financial.totalExpenses.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
        <div class="bg-green-50 rounded-lg p-4">
          <p class="text-gray-500 text-sm mb-1">Net Income</p>
          <p class="text-2xl font-bold"
            :class="financial.netIncome >= 0 ? 'text-green-600' : 'text-red-600'">
            {{ loading ? '—' : '₱' + financial.netIncome.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
      </div>

      <!-- Placeholder note if no payment table yet -->
      <p v-if="financial.totalRevenue === 0 && financial.totalExpenses === 0"
        class="mt-4 text-xs text-gray-400 italic text-center">
        Financial data will appear once a Payment/Billing table is connected in DashboardServiceImpl.java
      </p>
    </div>

  </div>
</template>
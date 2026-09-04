<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import { recordReport } from '@/service/reportHistory'

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

const financialTrend      = ref([]) // [{ date, revenue, expenses, netIncome }, ...]
const recentActivities    = ref([])
const pendingAppointments = ref([])

// ── Compute date range from filter ────────────────────────────────────────────
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
      totalPatients:        data.totalPatients        ?? 0,
      totalDeliveries:      data.totalDeliveries      ?? 0,
      totalAppointments:    data.totalAppointments    ?? 0,
      totalPrenatalRecords: data.totalPrenatalRecords ?? 0
    }

    financial.value = {
      totalRevenue:  data.totalRevenue  ?? 0,
      totalExpenses: data.totalExpenses ?? 0,
      netIncome:     data.netIncome     ?? 0
    }

    financialTrend.value      = data.financialTrend      ?? []
    recentActivities.value    = data.recentActivities    ?? []
    pendingAppointments.value = data.pendingAppointments ?? []
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

// ── Print report (opens print dialog — user can "Save as PDF") ───────────────
function printReport() {
  const { start, end } = dateRange.value
  const label = dateFilter.value === 'Custom'
    ? `${start || 'All'} to ${end || 'Now'}`
    : dateFilter.value

  const generatedAt = new Date().toLocaleString('en-PH', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })

  const peso = (v) => '₱' + Number(v ?? 0).toLocaleString('en-PH', { minimumFractionDigits: 2 })

  const activityRows = recentActivities.value.length
    ? recentActivities.value.map(a => `
        <tr>
          <td>${a.text}</td>
          <td>${a.time}</td>
          <td style="text-transform:capitalize">${a.type}</td>
        </tr>`).join('')
    : `<tr><td colspan="3" style="text-align:center;color:#999;">No recent activity for this period.</td></tr>`

  const pendingRows = pendingAppointments.value.length
    ? pendingAppointments.value.map(p => `
        <tr>
          <td>${p.name}</td>
          <td>${p.subtitle}</td>
          <td>${p.status}</td>
        </tr>`).join('')
    : `<tr><td colspan="3" style="text-align:center;color:#999;">No upcoming appointments.</td></tr>`

  const trendRows = financialTrend.value.length
    ? financialTrend.value.map(p => `
        <tr>
          <td>${fmtDate(p.date)}</td>
          <td style="text-align:right;">${peso(p.revenue)}</td>
          <td style="text-align:right;">${peso(p.expenses)}</td>
          <td style="text-align:right;">${peso(p.netIncome)}</td>
        </tr>`).join('')
    : ''

  const html = `
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="utf-8" />
      <title>Dashboard Report — ${label}</title>
      <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, Helvetica, sans-serif; color: #1f2937; padding: 32px; }
        h1 { font-size: 20px; margin-bottom: 4px; }
        .subtitle { color: #6b7280; font-size: 13px; margin-bottom: 24px; }
        .stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 24px; }
        .stat-card { border: 1px solid #e5e7eb; border-radius: 8px; padding: 12px; }
        .stat-label { font-size: 11px; color: #6b7280; }
        .stat-value { font-size: 20px; font-weight: 700; margin-top: 4px; }
        .section-title { font-size: 14px; font-weight: 700; margin: 24px 0 8px; border-bottom: 2px solid #8e4f70; padding-bottom: 4px; }
        table { width: 100%; border-collapse: collapse; font-size: 12px; margin-bottom: 8px; }
        th, td { padding: 6px 8px; border-bottom: 1px solid #f0f0f0; text-align: left; }
        th { background: #faf5f7; color: #6b7280; font-weight: 600; }
        .financial-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-top: 12px; }
        .financial-row .stat-card .stat-value.revenue  { color: #4a7ba6; }
        .financial-row .stat-card .stat-value.expenses { color: #b45f7d; }
        .financial-row .stat-card .stat-value.net.positive { color: #16a34a; }
        .financial-row .stat-card .stat-value.net.negative { color: #dc2626; }
        .footer { margin-top: 32px; font-size: 10px; color: #9ca3af; text-align: right; }
        @media print {
          @page { margin: 16mm; }
        }
      </style>
    </head>
    <body>
      <h1>Clinic Dashboard Report</h1>
      <div class="subtitle">Period: ${label}</div>

      <div class="stats">
        <div class="stat-card">
          <div class="stat-label">Total Patients</div>
          <div class="stat-value">${stats.value.totalPatients.toLocaleString()}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Total Deliveries</div>
          <div class="stat-value">${stats.value.totalDeliveries.toLocaleString()}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Prenatal Records</div>
          <div class="stat-value">${stats.value.totalPrenatalRecords.toLocaleString()}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Total Appointments</div>
          <div class="stat-value">${stats.value.totalAppointments.toLocaleString()}</div>
        </div>
      </div>

      <div class="section-title">Financial Overview</div>
      <div class="financial-row">
        <div class="stat-card">
          <div class="stat-label">Total Revenue</div>
          <div class="stat-value revenue">${peso(financial.value.totalRevenue)}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Total Expenses</div>
          <div class="stat-value expenses">${peso(financial.value.totalExpenses)}</div>
        </div>
        <div class="stat-card">
          <div class="stat-label">Net Income</div>
          <div class="stat-value net ${financial.value.netIncome >= 0 ? 'positive' : 'negative'}">${peso(financial.value.netIncome)}</div>
        </div>
      </div>

      ${trendRows ? `
      <div class="section-title">Daily Breakdown</div>
      <table>
        <thead>
          <tr><th>Date</th><th style="text-align:right;">Revenue</th><th style="text-align:right;">Expenses</th><th style="text-align:right;">Net Income</th></tr>
        </thead>
        <tbody>${trendRows}</tbody>
      </table>` : ''}

      <div class="section-title">Recent Activity</div>
      <table>
        <thead><tr><th>Activity</th><th>Time</th><th>Type</th></tr></thead>
        <tbody>${activityRows}</tbody>
      </table>

      <div class="section-title">Upcoming Appointments</div>
      <table>
        <thead><tr><th>Patient</th><th>Service</th><th>Status</th></tr></thead>
        <tbody>${pendingRows}</tbody>
      </table>

      <div class="footer">Generated ${generatedAt}</div>
    </body>
    </html>
  `

  const printWindow = window.open('', '_blank', 'width=900,height=1000')
  if (!printWindow) {
    alert('Please allow pop-ups to print the report.')
    return
  }
  printWindow.document.open()
  printWindow.document.write(html)
  printWindow.document.close()
  recordReport({
    name: 'Clinic Dashboard Report',
    type: 'Dashboard',
    period: label,
    details: `${stats.value.totalPatients} patient(s), ${stats.value.totalAppointments} appointment(s)`
  })

  printWindow.onload = () => {
    printWindow.focus()
    printWindow.print()
  }
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

// ── Financial combo chart (line = Revenue, bars = Expenses) ──────────────────
const chartW    = 900
const chartH    = 340
const padX      = 50
const padY      = 50
const barWidth  = 36

// Rounds a max value up to a "nice" step so the chart uses more of its
// vertical space instead of leaving a huge gap at the top.
function niceCeil(value) {
  if (value <= 0) return 1
  const magnitude = Math.pow(10, Math.floor(Math.log10(value)))
  const normalized = value / magnitude
  let step
  if (normalized <= 1) step = 1
  else if (normalized <= 2) step = 2
  else if (normalized <= 5) step = 5
  else step = 10
  return step * magnitude
}

// Multi-day trend: only meaningful with 2+ points (need a line to draw)
const comboChart = computed(() => {
  const pts = financialTrend.value
  if (pts.length < 2) return null

  const maxVal  = Math.max(...pts.flatMap(p => [p.revenue, p.expenses]), 1)
  const niceMax = niceCeil(maxVal)

  const xStep  = (chartW - padX * 2) / Math.max(pts.length - 1, 1)
  const yScale = v => chartH - padY - (v / niceMax) * (chartH - padY * 2)

  const linePoints = pts.map((p, i) => ({
    x: padX + i * xStep,
    y: yScale(p.revenue),
    value: p.revenue
  }))

  const linePath = linePoints
    .map((pt, i) => `${i === 0 ? 'M' : 'L'} ${pt.x.toFixed(2)} ${pt.y.toFixed(2)}`)
    .join(' ')

  const bars = pts.map((p, i) => {
    const barH = (p.expenses / niceMax) * (chartH - padY * 2)
    return {
      x: padX + i * xStep - barWidth / 2,
      y: chartH - padY - barH,
      height: barH,
      value: p.expenses,
      label: fmtDate(p.date)
    }
  })

  return { linePoints, linePath, bars, baseY: chartH - padY }
})

// Single-day view (e.g. "Today" filter): a trend line means nothing with one
// point, so instead fill the space with a full-width Revenue vs Expenses
// comparison — two large bars side by side.
const singleDayChart = computed(() => {
  const pts = financialTrend.value
  if (pts.length !== 1) return null

  const p = pts[0]
  const maxVal  = Math.max(p.revenue, p.expenses, 1)
  const usableH = chartH - padY * 2
  const barW    = 200
  const gap     = 140
  const centerX = chartW / 2

  const revenueH  = (p.revenue  / maxVal) * usableH
  const expensesH = (p.expenses / maxVal) * usableH

  return {
    baseY: chartH - padY,
    revenue: {
      x: centerX - gap / 2 - barW,
      y: chartH - padY - revenueH,
      width: barW,
      height: revenueH,
      value: p.revenue
    },
    expenses: {
      x: centerX + gap / 2,
      y: chartH - padY - expensesH,
      width: barW,
      height: expensesH,
      value: p.expenses
    },
    date: fmtDate(p.date)
  }
})

function fmtMoney(v) {
  if (v >= 1000) return '₱' + (v / 1000).toFixed(1).replace('.0', '') + 'K'
  return '₱' + Math.round(v)
}

function fmtDate(d) {
  return new Date(d).toLocaleDateString('en-PH', { month: 'short', day: 'numeric' })
}
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

    <!-- Print Report -->
    <div class="flex justify-end mb-6">
      <button @click="printReport"
        class="px-6 py-2 bg-[#8e4f70] text-white rounded-lg hover:bg-purple-600 text-sm font-semibold transition flex items-center gap-2">
        🖨️ Print Report
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

    <!-- Financial Overview (combo chart: line = Revenue, bars = Expenses) -->
    <div class="bg-white rounded-lg shadow p-6">
      <div class="flex justify-between items-center mb-2 flex-wrap gap-2">
        <div class="flex items-center gap-2">
          <span class="text-xl">💰</span>
          <h3 class="font-bold text-gray-800">
            Income &amp; Expense Comparison
            <span class="text-sm font-normal text-gray-400 ml-1">— {{ dateFilter }}</span>
          </h3>
        </div>
        <div class="text-right">
          <p class="text-xs text-gray-400">Net Income</p>
          <p class="text-xl font-bold" :class="financial.netIncome >= 0 ? 'text-green-600' : 'text-red-600'">
            ₱{{ loading ? '—' : financial.netIncome.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
      </div>

      <div v-if="loading" class="text-center py-10 text-gray-400 text-sm">Loading…</div>

      <div v-else-if="!comboChart && !singleDayChart" class="text-center py-10 text-gray-400 text-sm italic">
        Not enough data points for a trend chart. Financial history will appear once
        <code>financialTrend</code> is returned from DashboardServiceImpl.java.
      </div>

      <!-- Multi-day trend: line = Revenue, bars = Expenses -->
      <div v-else-if="comboChart" class="w-full">
        <svg :viewBox="`0 0 ${chartW} ${chartH + 30}`" class="w-full h-[420px]">

          <!-- baseline only, no gridlines -->
          <line :x1="padX" :y1="comboChart.baseY" :x2="chartW - padX" :y2="comboChart.baseY"
                stroke="#e5e7eb" stroke-width="1.5" />

          <!-- Expense bars -->
          <g v-for="(bar, i) in comboChart.bars" :key="'bar-' + i">
            <rect :x="bar.x" :y="bar.y" :width="barWidth" :height="bar.height"
                  fill="#b45f7d" rx="3" />
            <!-- direct label above the bar (skip when zero to avoid clutter) -->
            <text v-if="bar.value > 0" :x="bar.x + barWidth / 2" :y="bar.y - 10" font-size="14" font-weight="600"
                  fill="#374151" text-anchor="middle">
              {{ fmtMoney(bar.value) }}
            </text>
            <!-- x-axis category label -->
            <text :x="bar.x + barWidth / 2" :y="chartH + 22" font-size="13" fill="#9ca3af"
                  text-anchor="middle">
              {{ bar.label }}
            </text>
          </g>

          <!-- Revenue line -->
          <path :d="comboChart.linePath" fill="none" stroke="#4a7ba6" stroke-width="3"
                stroke-linecap="round" stroke-linejoin="round" />

          <g v-for="(pt, i) in comboChart.linePoints" :key="'pt-' + i">
            <circle :cx="pt.x" :cy="pt.y" r="6" fill="white" stroke="#4a7ba6" stroke-width="3" />
            <!-- direct label above the point (skip when zero to avoid clutter) -->
            <text v-if="pt.value > 0" :x="pt.x" :y="pt.y - 18" font-size="15" font-weight="700" fill="#374151"
                  text-anchor="middle">
              {{ fmtMoney(pt.value) }}
            </text>
          </g>
        </svg>

        <!-- Legend -->
        <div class="flex justify-center gap-6 mt-3 text-sm">
          <div class="flex items-center gap-1.5">
            <span class="w-3 h-3 rounded-full bg-[#4a7ba6]"></span>
            <span class="text-gray-600">Revenue</span>
          </div>
          <div class="flex items-center gap-1.5">
            <span class="w-3 h-2 bg-[#b45f7d] rounded-sm"></span>
            <span class="text-gray-600">Expenses</span>
          </div>
        </div>
      </div>

      <!-- Single-day view (e.g. "Today"): full-width Revenue vs Expenses bars -->
      <div v-else-if="singleDayChart" class="w-full">
        <svg :viewBox="`0 0 ${chartW} ${chartH + 30}`" class="w-full h-[420px]">

          <line :x1="padX" :y1="singleDayChart.baseY" :x2="chartW - padX" :y2="singleDayChart.baseY"
                stroke="#e5e7eb" stroke-width="1.5" />

          <!-- Revenue bar -->
          <rect :x="singleDayChart.revenue.x" :y="singleDayChart.revenue.y"
                :width="singleDayChart.revenue.width" :height="singleDayChart.revenue.height"
                fill="#4a7ba6" rx="6" />
          <text :x="singleDayChart.revenue.x + singleDayChart.revenue.width / 2"
                :y="singleDayChart.revenue.y - 16" font-size="20" font-weight="700"
                fill="#374151" text-anchor="middle">
            {{ fmtMoney(singleDayChart.revenue.value) }}
          </text>
          <text :x="singleDayChart.revenue.x + singleDayChart.revenue.width / 2"
                :y="chartH + 22" font-size="14" fill="#9ca3af" text-anchor="middle">
            Revenue
          </text>

          <!-- Expenses bar -->
          <rect :x="singleDayChart.expenses.x" :y="singleDayChart.expenses.y"
                :width="singleDayChart.expenses.width" :height="singleDayChart.expenses.height"
                fill="#b45f7d" rx="6" />
          <text :x="singleDayChart.expenses.x + singleDayChart.expenses.width / 2"
                :y="singleDayChart.expenses.y - 16" font-size="20" font-weight="700"
                fill="#374151" text-anchor="middle">
            {{ fmtMoney(singleDayChart.expenses.value) }}
          </text>
          <text :x="singleDayChart.expenses.x + singleDayChart.expenses.width / 2"
                :y="chartH + 22" font-size="14" fill="#9ca3af" text-anchor="middle">
            Expenses
          </text>
        </svg>

        <p class="text-center text-sm text-gray-400 mt-1">{{ singleDayChart.date }}</p>
      </div>

      <!-- Fallback totals row (kept for quick-glance numbers) -->
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mt-4 pt-4 border-t border-gray-100">
        <div>
          <p class="text-gray-400 text-xs">Total Revenue</p>
          <p class="text-lg font-semibold text-[#4a7ba6]">
            {{ loading ? '—' : '₱' + financial.totalRevenue.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
        <div>
          <p class="text-gray-400 text-xs">Total Expenses</p>
          <p class="text-lg font-semibold text-[#b45f7d]">
            {{ loading ? '—' : '₱' + financial.totalExpenses.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
        <div>
          <p class="text-gray-400 text-xs">Net Income</p>
          <p class="text-lg font-semibold" :class="financial.netIncome >= 0 ? 'text-green-600' : 'text-red-600'">
            {{ loading ? '—' : '₱' + financial.netIncome.toLocaleString('en-PH', { minimumFractionDigits: 2 }) }}
          </p>
        </div>
      </div>

      <!-- Placeholder note if no payment table yet -->
      <p v-if="financial.totalRevenue === 0 && financial.totalExpenses === 0 && !comboChart && !singleDayChart"
        class="mt-4 text-xs text-gray-400 italic text-center">
        Financial data will appear once a Payment/Billing table is connected in DashboardServiceImpl.java
      </p>
    </div>

  </div>
</template>
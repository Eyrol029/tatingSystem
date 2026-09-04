<script setup>
import { computed, onMounted, ref } from 'vue'
import { clearReports, readReports, removeReport } from '@/service/reportHistory'

const reports = ref([])
const searchQuery = ref('')
const selectedReport = ref(null)

const filteredReports = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()
  if (!query) return reports.value
  return reports.value.filter(report =>
    [report.name, report.type, report.period, report.details]
      .some(value => String(value || '').toLowerCase().includes(query))
  )
})

function refreshReports() {
  reports.value = readReports()
}

function formatDate(value) {
  return new Date(value).toLocaleString('en-PH', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })
}

function deleteReport(id) {
  removeReport(id)
  refreshReports()
}

function viewReport(report) {
  selectedReport.value = report
}

function closeReport() {
  selectedReport.value = null
}

function clearHistory() {
  if (!reports.value.length || !confirm('Clear all generated report history?')) return
  clearReports()
  refreshReports()
}

onMounted(refreshReports)
</script>

<template>
  <div class="min-h-screen bg-gray-100 p-6">
    <div class="w-full max-w-[1400px] mx-auto">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
        <div>
          <h1 class="text-2xl font-bold text-gray-800">Generated Reports</h1>
          <p class="text-sm text-gray-500 mt-1">Track reports generated from the clinic system.</p>
        </div>
        <button
          @click="clearHistory"
          :disabled="!reports.length"
          class="px-4 py-2 rounded border border-red-200 text-red-600 hover:bg-red-50 disabled:opacity-40 disabled:cursor-not-allowed"
        >
          Clear History
        </button>
      </div>

      <div class="bg-white rounded-lg shadow-sm p-4 mb-5">
        <input
          v-model="searchQuery"
          type="search"
          placeholder="Search report name, type, or period"
          class="w-full border rounded px-4 py-2"
        />
      </div>

      <div v-if="!reports.length" class="bg-white rounded-lg shadow-sm text-center py-16 text-gray-400">
        No generated reports yet.
      </div>

      <div v-else-if="!filteredReports.length" class="bg-white rounded-lg shadow-sm text-center py-16 text-gray-400">
        No reports match your search.
      </div>

      <div v-else class="bg-white rounded-lg shadow-sm overflow-hidden min-h-[calc(100vh-285px)]">
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead class="bg-gray-50 text-gray-600">
              <tr>
                <th class="text-left px-4 py-3">Report</th>
                <th class="text-left px-4 py-3">Type</th>
                <th class="text-left px-4 py-3">Period</th>
                <th class="text-left px-4 py-3">Generated</th>
                <th class="text-left px-4 py-3">Details</th>
                <th class="px-4 py-3"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="report in filteredReports" :key="report.id" class="border-t">
                <td class="px-4 py-3 font-medium text-gray-800">{{ report.name }}</td>
                <td class="px-4 py-3 text-gray-600">{{ report.type }}</td>
                <td class="px-4 py-3 text-gray-600">{{ report.period }}</td>
                <td class="px-4 py-3 text-gray-600 whitespace-nowrap">{{ formatDate(report.generatedAt) }}</td>
                <td class="px-4 py-3 text-gray-500">{{ report.details || '—' }}</td>
                <td class="px-4 py-3 text-right whitespace-nowrap">
                  <button @click="viewReport(report)" class="text-blue-600 hover:underline mr-3">View</button>
                  <button @click="deleteReport(report.id)" class="text-red-600 hover:underline">Delete</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div v-if="selectedReport" class="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-lg p-6">
        <div class="flex items-start justify-between gap-4 mb-5">
          <div>
            <h2 class="text-xl font-bold text-gray-800">{{ selectedReport.name }}</h2>
            <p class="text-sm text-gray-500">Generated report details</p>
          </div>
          <button @click="closeReport" class="text-gray-400 hover:text-gray-700 text-xl" aria-label="Close">&times;</button>
        </div>

        <dl class="divide-y text-sm">
          <div class="flex justify-between gap-4 py-3">
            <dt class="font-semibold text-gray-600">Report Type</dt>
            <dd class="text-right text-gray-800">{{ selectedReport.type }}</dd>
          </div>
          <div class="flex justify-between gap-4 py-3">
            <dt class="font-semibold text-gray-600">Period</dt>
            <dd class="text-right text-gray-800">{{ selectedReport.period }}</dd>
          </div>
          <div class="flex justify-between gap-4 py-3">
            <dt class="font-semibold text-gray-600">Generated</dt>
            <dd class="text-right text-gray-800">{{ formatDate(selectedReport.generatedAt) }}</dd>
          </div>
          <div class="py-3">
            <dt class="font-semibold text-gray-600 mb-1">Details</dt>
            <dd class="text-gray-800">{{ selectedReport.details || 'No additional details.' }}</dd>
          </div>
        </dl>

        <div class="flex justify-end mt-5">
          <button @click="closeReport" class="px-4 py-2 bg-gray-100 text-gray-700 rounded hover:bg-gray-200">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

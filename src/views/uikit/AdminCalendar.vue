<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import axios from 'axios'

const BASE = 'http://localhost:8080/api/calendar'

const currentDate  = ref(new Date())
const selectedDay  = ref(new Date())
const calendarEvents = ref([])
const isLoading    = ref(false)
const errorMsg     = ref('')
const saveLoading  = ref(false)

// ── Add-event form ────────────────────────────────────────────────────────────
const newEventTitle       = ref('')
const newEventType        = ref('appointment')
const newEventPatientName = ref('')
const newEventDescription = ref('')
const newEventDate        = ref(formatLocalDate(new Date()))

// ── Calendar helpers ──────────────────────────────────────────────────────────
const currentMonth = computed(() => currentDate.value.getMonth())
const currentYear  = computed(() => currentDate.value.getFullYear())

const monthNames = ['January','February','March','April','May','June',
                    'July','August','September','October','November','December']
const dayNames   = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat']

const daysInMonth = computed(() => {
  const year  = currentYear.value
  const month = currentMonth.value
  const days  = []
  const firstDayIndex = new Date(year, month, 1).getDay()

  for (let i = 0; i < firstDayIndex; i++)
    days.push({ empty: true, key: `empty-${i}` })

  const numDays = new Date(year, month + 1, 0).getDate()
  for (let i = 1; i <= numDays; i++)
    days.push({ date: i, fullDate: new Date(year, month, i), empty: false, key: `day-${i}` })

  return days
})

function nextMonth() { currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1) }
function prevMonth() { currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1) }

// ── Date utilities ────────────────────────────────────────────────────────────
// Backend returns LocalDate — Jackson may serialize as "2025-06-15" string
// or as [2025, 6, 15] array. Handle both.
function parseBackendDate(value) {
  if (!value) return null
  // Array form: [year, month, day]
  if (Array.isArray(value)) {
    return new Date(value[0], value[1] - 1, value[2])
  }
  const str = String(value).trim().substring(0, 10)
  const [y, m, d] = str.split('-').map(Number)
  if (!y || !m || !d) return null
  return new Date(y, m - 1, d)
}

function formatLocalDate(date) {
  if (!date) return ''
  const d = date instanceof Date ? date : new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
}

// ── Normalize DTO from backend → internal event shape ─────────────────────────
// Your CalendarEventDto fields:
//   eventID, title, eventDate, eventType, patientId, patientName, description, source
//
// eventType values coming from backend:
//   "appointment"  → blue appointment tile
//   "expected-delivery" | "edc" | "delivery" | "labor" → pink labor tile
//   "prenatal-exam" | "prenatal-visit" | "next-visit"   → teal prenatal tile
//   "manual"       → indigo manual tile
function normalizeEvent(raw) {
  const et = (raw.eventType || raw.source || 'manual').toLowerCase()

  let type = 'appointment'
  if (['expected-delivery','edc','delivery','labor'].includes(et)) {
    type = 'labor'
  } else if (['prenatal-exam','prenatal-visit','next-visit'].includes(et)) {
    type = 'prenatal'
  } else if (et === 'manual') {
    type = 'manual'
  }

  return {
    id:          raw.eventID,
    title:       raw.title        || 'Event',
    date:        parseBackendDate(raw.eventDate),
    type,
    eventType:   et,
    patientId:   raw.patientId    || raw.patientID || null,
    patientName: raw.patientName  || '—',
    description: raw.description  || '',
    source:      raw.source       || 'manual'
  }
}

// ── Fetch events for visible month ────────────────────────────────────────────
async function fetchEventsForMonth() {
  const start = formatLocalDate(new Date(currentYear.value, currentMonth.value, 1))
  const end   = formatLocalDate(new Date(currentYear.value, currentMonth.value + 1, 0))
  isLoading.value = true
  errorMsg.value  = ''
  try {
    const res = await axios.get(`${BASE}/events`, { params: { start, end } })
    calendarEvents.value = Array.isArray(res.data)
      ? res.data.map(normalizeEvent)
      : []
  } catch (e) {
    errorMsg.value = 'Failed to load calendar events.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
}

// ── Per-day helpers ───────────────────────────────────────────────────────────
function getEventsForDay(day) {
  if (day.empty) return []
  return calendarEvents.value.filter(e => {
    if (!e.date) return false
    return e.date.getDate()     === day.date &&
           e.date.getMonth()    === currentMonth.value &&
           e.date.getFullYear() === currentYear.value
  })
}

function isToday(day) {
  if (day.empty) return false
  const t = new Date()
  return day.fullDate.getDate()     === t.getDate() &&
         day.fullDate.getMonth()    === t.getMonth() &&
         day.fullDate.getFullYear() === t.getFullYear()
}

function isSelected(day) {
  if (day.empty) return false
  return day.fullDate.getDate()     === selectedDay.value.getDate() &&
         day.fullDate.getMonth()    === selectedDay.value.getMonth() &&
         day.fullDate.getFullYear() === selectedDay.value.getFullYear()
}

function selectDay(day) {
  if (!day.empty) {
    selectedDay.value  = day.fullDate
    newEventDate.value = formatLocalDate(day.fullDate)
  }
}

// ── Selected-day computed ─────────────────────────────────────────────────────
const selectedDayEvents = computed(() =>
  calendarEvents.value.filter(e => {
    if (!e.date) return false
    return e.date.getDate()     === selectedDay.value.getDate() &&
           e.date.getMonth()    === selectedDay.value.getMonth() &&
           e.date.getFullYear() === selectedDay.value.getFullYear()
  })
)

const manualSelectedDayEvents = computed(() =>
  selectedDayEvents.value.filter(e => e.source === 'manual')
)

// ── Type → style maps ─────────────────────────────────────────────────────────
function tileCss(type) {
  return {
    labor:       'bg-pink-100 text-pink-700 border border-pink-200',
    prenatal:    'bg-teal-100 text-teal-700 border border-teal-200',
    manual:      'bg-indigo-100 text-indigo-700 border border-indigo-200',
    appointment: 'bg-blue-100 text-blue-700 border border-blue-200'
  }[type] || 'bg-gray-100 text-gray-700 border border-gray-200'
}

function cardBorder(type) {
  return {
    labor:       'border-pink-200 bg-pink-50',
    prenatal:    'border-teal-200 bg-teal-50',
    manual:      'border-indigo-200 bg-indigo-50',
    appointment: 'border-blue-200 bg-blue-50'
  }[type] || 'border-gray-200 bg-gray-50'
}

function badgeCss(type) {
  return {
    labor:       'bg-pink-200 text-pink-800',
    prenatal:    'bg-teal-200 text-teal-800',
    manual:      'bg-indigo-200 text-indigo-800',
    appointment: 'bg-blue-200 text-blue-800'
  }[type] || 'bg-gray-200 text-gray-800'
}

function dividerCss(type) {
  return {
    labor:       'border-pink-200',
    prenatal:    'border-teal-200',
    manual:      'border-indigo-200',
    appointment: 'border-blue-200'
  }[type] || 'border-gray-200'
}

function typeLabel(type, eventType) {
  const labels = {
    'expected-delivery': 'Expected Delivery',
    'edc':               'Estimated Delivery',
    'delivery':          'Delivery Date',
    'prenatal-exam':     'Prenatal Exam',
    'prenatal-visit':    'Prenatal Visit',
    'next-visit':        'Next Visit',
    'appointment':       'Appointment',
    'manual':            'Manual Event',
    'labor':             'Labor Day'
  }
  return labels[eventType] || labels[type] || type
}

// ── Add manual event ──────────────────────────────────────────────────────────
async function addManualEvent() {
  if (!newEventTitle.value || !newEventDate.value) {
    errorMsg.value = 'Please fill in a title and date.'
    return
  }
  saveLoading.value = true
  errorMsg.value    = ''
  try {
    await axios.post(`${BASE}/manual`, {
      title:       newEventTitle.value,
      eventDate:   newEventDate.value,
      eventType:   newEventType.value  || 'manual',
      patientName: newEventPatientName.value || null,
      description: newEventDescription.value || null
    })
    // Reset form
    newEventTitle.value       = ''
    newEventType.value        = 'appointment'
    newEventPatientName.value = ''
    newEventDescription.value = ''
    await fetchEventsForMonth()
  } catch (e) {
    errorMsg.value = 'Failed to save event: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    saveLoading.value = false
  }
}

// ── Delete manual events for selected day ────────────────────────────────────
async function deleteSelectedEvents() {
  if (!manualSelectedDayEvents.value.length) {
    alert('Only manually-added events can be deleted here.\nPrenatal & appointment events come from patient records.')
    return
  }
  const count = manualSelectedDayEvents.value.length
  const msg   = count === 1
    ? `Delete manual event "${manualSelectedDayEvents.value[0].title}"?`
    : `Delete ${count} manual events for this day?`
  if (!confirm(msg)) return
  try {
    await Promise.all(manualSelectedDayEvents.value.map(e => axios.delete(`${BASE}/manual/${e.id}`)))
    await fetchEventsForMonth()
  } catch (e) {
    errorMsg.value = 'Failed to delete event(s).'
  }
}

function notifyPatient(event) {
  alert(`Notification sent to ${event.patientName} for their upcoming ${event.title}!`)
}

onMounted(fetchEventsForMonth)
watch([currentMonth, currentYear], fetchEventsForMonth)
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 p-6">

    <!-- ── CALENDAR ── -->
    <div class="lg:col-span-2 bg-white rounded-lg shadow-sm p-6">

      <!-- Header -->
      <div class="flex items-center justify-between mb-6">
        <div>
          <h2 class="text-2xl font-bold text-gray-800">Admin Calendar</h2>
          <p class="text-gray-500 text-sm">Appointments, prenatal dates, and expected deliveries</p>
        </div>
        <!-- Legend -->
        <div class="flex flex-wrap gap-3 text-sm">
          <div class="flex items-center gap-1">
            <span class="w-3 h-3 rounded-full bg-blue-500"></span>
            <span class="text-gray-600">Appointment</span>
          </div>
          <div class="flex items-center gap-1">
            <span class="w-3 h-3 rounded-full bg-pink-500"></span>
            <span class="text-gray-600">Delivery / Labor</span>
          </div>
          <div class="flex items-center gap-1">
            <span class="w-3 h-3 rounded-full bg-teal-500"></span>
            <span class="text-gray-600">Prenatal</span>
          </div>
          <div class="flex items-center gap-1">
            <span class="w-3 h-3 rounded-full bg-indigo-500"></span>
            <span class="text-gray-600">Manual</span>
          </div>
        </div>
      </div>

      <!-- Error -->
      <div v-if="errorMsg"
        class="mb-4 p-3 bg-red-100 text-red-700 rounded-lg text-sm flex justify-between">
        {{ errorMsg }}
        <button @click="errorMsg=''" class="font-bold text-red-400 hover:text-red-600 ml-3">✕</button>
      </div>

      <!-- Month nav -->
      <div class="flex items-center justify-between mb-4 bg-gray-50 p-4 rounded-lg border border-gray-200">
        <button @click="prevMonth" class="p-2 hover:bg-gray-200 rounded-full transition">
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
        </button>
        <h3 class="text-xl font-bold text-gray-800">{{ monthNames[currentMonth] }} {{ currentYear }}</h3>
        <button @click="nextMonth" class="p-2 hover:bg-gray-200 rounded-full transition">
          <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
          </svg>
        </button>
      </div>

      <!-- Loading overlay -->
      <div v-if="isLoading" class="text-center py-8 text-gray-400">Loading calendar…</div>

      <!-- Grid -->
      <div v-else class="grid grid-cols-7 gap-px bg-gray-200 border border-gray-200 rounded-lg overflow-hidden">
        <div v-for="d in dayNames" :key="d"
          class="bg-gray-100 text-center py-3 font-semibold text-xs text-gray-600 uppercase tracking-wide">
          {{ d }}
        </div>

        <div v-for="day in daysInMonth" :key="day.key"
          @click="selectDay(day)"
          class="bg-white min-h-[110px] p-1.5 transition-colors cursor-pointer hover:bg-gray-50"
          :class="{
            'bg-gray-50 pointer-events-none': day.empty,
            'ring-2 ring-inset ring-indigo-400 bg-indigo-50/30': isSelected(day)
          }">
          <div v-if="!day.empty" class="h-full flex flex-col">
            <span class="text-sm font-medium w-7 h-7 flex items-center justify-center rounded-full mb-1 self-start"
              :class="isToday(day) ? 'bg-blue-600 text-white shadow' : 'text-gray-700'">
              {{ day.date }}
            </span>
            <div class="space-y-0.5 overflow-hidden">
              <div v-for="(event, idx) in getEventsForDay(day)" :key="idx"
                class="text-xs px-1.5 py-1 rounded truncate font-medium leading-tight"
                :class="tileCss(event.type)"
                :title="`${event.patientName} — ${event.title}`">
                {{ event.patientName !== '—' ? event.patientName : event.title }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ── SIDEBAR ── -->
    <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200 sticky top-6 max-h-screen overflow-y-auto">

      <!-- Selected day header -->
      <h3 class="text-xl font-bold text-gray-800 mb-1">
        {{ selectedDay.toLocaleDateString('en-US', { weekday:'long', month:'long', day:'numeric' }) }}
      </h3>
      <p class="text-sm text-gray-500 mb-5 border-b pb-4">
        {{ selectedDayEvents.length }} event(s) on this day
      </p>

      <!-- Loading -->
      <div v-if="isLoading" class="text-center py-6 text-gray-400 italic text-sm">Loading…</div>

      <div v-else>
        <!-- No events -->
        <div v-if="selectedDayEvents.length === 0"
          class="text-center py-6 text-gray-400 italic text-sm bg-gray-50 rounded-lg mb-4">
          No events scheduled for this day.
        </div>

        <!-- Event cards -->
        <div class="space-y-3 mb-6">
          <div v-for="event in selectedDayEvents" :key="event.id"
            class="p-4 rounded-xl border transition-all"
            :class="cardBorder(event.type)">

            <!-- Badge + Title -->
            <div class="mb-2">
              <span class="text-xs font-bold uppercase tracking-wider px-2 py-0.5 rounded-full inline-block mb-1"
                :class="badgeCss(event.type)">
                {{ typeLabel(event.type, event.eventType) }}
              </span>
              <h4 class="font-bold text-gray-900">{{ event.title }}</h4>
              <p v-if="event.patientName && event.patientName !== '—'"
                class="text-gray-600 text-sm">👤 {{ event.patientName }}</p>
              <p v-if="event.description"
                class="text-gray-500 text-xs mt-1 italic">{{ event.description }}</p>
            </div>

            <!-- Action button -->
            <div class="pt-3 border-t" :class="dividerCss(event.type)">
              <button v-if="event.type === 'labor'"
                @click="notifyPatient(event)"
                class="w-full flex items-center justify-center gap-2 bg-pink-600 hover:bg-pink-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition shadow-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
                </svg>
                Notify Patient
              </button>

              <button v-else-if="event.type === 'prenatal' || event.type === 'appointment'"
                @click="$router.push(`/uikit/PatientProfiling/${event.patientId || ''}`)"
                class="w-full text-center text-sm font-medium transition"
                :class="event.type === 'prenatal' ? 'text-teal-600 hover:text-teal-800' : 'text-blue-600 hover:text-blue-800'">
                View Patient Record →
              </button>

              <span v-else class="text-xs text-gray-400 block text-center">Manual event</span>
            </div>
          </div>
        </div>

        <!-- ── Add manual event form ── -->
        <div class="border-t border-gray-200 pt-5">
          <h4 class="text-base font-semibold text-gray-800 mb-4">+ Add Manual Event</h4>
          <div class="space-y-3">

            <div>
              <label class="block text-xs font-semibold text-gray-600 mb-1">Title *</label>
              <input v-model="newEventTitle" type="text" placeholder="e.g. Staff Meeting"
                class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none" />
            </div>

            <div>
              <label class="block text-xs font-semibold text-gray-600 mb-1">Date *</label>
              <input v-model="newEventDate" type="date"
                class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none" />
            </div>

            <div>
              <label class="block text-xs font-semibold text-gray-600 mb-1">Type</label>
              <select v-model="newEventType"
                class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none">
                <option value="appointment">Appointment</option>
                <option value="labor">Labor / Delivery</option>
                <option value="prenatal-visit">Prenatal Visit</option>
                <option value="manual">General / Note</option>
              </select>
            </div>

            <div>
              <label class="block text-xs font-semibold text-gray-600 mb-1">Patient Name</label>
              <input v-model="newEventPatientName" type="text" placeholder="Optional"
                class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none" />
            </div>

            <div>
              <label class="block text-xs font-semibold text-gray-600 mb-1">Notes</label>
              <textarea v-model="newEventDescription" rows="2" placeholder="Optional details"
                class="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-400 focus:outline-none resize-none"></textarea>
            </div>

            <button @click="addManualEvent" :disabled="saveLoading"
              class="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-2.5 rounded-lg font-semibold text-sm transition disabled:opacity-50 disabled:cursor-not-allowed">
              {{ saveLoading ? 'Saving…' : 'Add Event' }}
            </button>
          </div>

          <!-- Delete manual events for day -->
          <div v-if="manualSelectedDayEvents.length > 0" class="mt-3">
            <button @click="deleteSelectedEvents"
              class="w-full bg-red-600 hover:bg-red-700 text-white py-2.5 rounded-lg font-semibold text-sm transition">
              🗑 Delete {{ manualSelectedDayEvents.length === 1
                ? `"${manualSelectedDayEvents[0].title}"`
                : `${manualSelectedDayEvents.length} Manual Events` }}
            </button>
          </div>

          <div v-else-if="selectedDayEvents.length > 0"
            class="mt-3 rounded-lg bg-yellow-50 border border-yellow-200 p-3 text-xs text-yellow-800">
            ℹ️ Prenatal and appointment events are read-only — they come from patient records.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
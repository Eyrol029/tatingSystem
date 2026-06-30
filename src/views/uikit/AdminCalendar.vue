<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import axios from 'axios';

const BASE = 'http://localhost:8080/api/calendar';
const currentDate = ref(new Date());

// Selected day for the side/bottom panel
const selectedDay = ref(new Date());

const calendarEvents = ref([]);
const isLoading = ref(false);

const newEventTitle = ref('');
const newEventType = ref('manual');
const newEventPatientName = ref('');
const newEventDescription = ref('');
const newEventDate = ref(formatLocalDate(selectedDay.value));

const currentMonth = computed(() => currentDate.value.getMonth());
const currentYear = computed(() => currentDate.value.getFullYear());

const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
const dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

const daysInMonth = computed(() => {
    const year = currentYear.value;
    const month = currentMonth.value;
    const date = new Date(year, month, 1);
    const days = [];

    const firstDayIndex = date.getDay();

    for (let i = 0; i < firstDayIndex; i++) {
        days.push({ empty: true, key: `empty-${i}` });
    }

    const numDays = new Date(year, month + 1, 0).getDate();
    for (let i = 1; i <= numDays; i++) {
        days.push({
            date: i,
            fullDate: new Date(year, month, i),
            empty: false,
            key: `day-${i}`
        });
    }

    return days;
});

function nextMonth() {
    currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1);
}

function prevMonth() {
    currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1);
}

function parseCalendarDate(value) {
    if (!value) return null;
    const dateString = String(value).trim();
    const parts = dateString.split('-').map(Number);
    if (parts.length === 3 && parts.every(p => !Number.isNaN(p))) {
        return new Date(parts[0], parts[1] - 1, parts[2]);
    }

    const mdy = dateString.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2,4})$/);
    if (mdy) {
        let year = Number(mdy[3]);
        if (year < 100) {
            year += year < 70 ? 2000 : 1900;
        }
        return new Date(year, Number(mdy[1]) - 1, Number(mdy[2]));
    }

    const parsed = new Date(dateString);
    if (Number.isNaN(parsed.getTime())) {
        return null;
    }
    return new Date(parsed.getFullYear(), parsed.getMonth(), parsed.getDate());
}

function formatLocalDate(value) {
    const date = value instanceof Date ? value : parseCalendarDate(value);
    if (!date) return null;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function normalizeEvent(raw) {
    let type = raw.eventType || raw.source || 'manual';
    if (type === 'edc' || raw.source === 'prenatal') {
        type = 'labor';
    }

    return {
        id: raw.eventID,
        title: raw.title || raw.patientName || 'Event',
        date: parseCalendarDate(raw.eventDate),
        type,
        patientId: raw.patientId || raw.patientID || null,
        patientName: raw.patientName || 'Unknown',
        description: raw.description || '',
        source: raw.source || 'manual'
    };
}

async function fetchEventsForMonth() {
    const start = formatLocalDate(new Date(currentYear.value, currentMonth.value, 1));
    const end = formatLocalDate(new Date(currentYear.value, currentMonth.value + 1, 0));
    isLoading.value = true;
    try {
        const response = await axios.get(`${BASE}/events`, { params: { start, end } });
        calendarEvents.value = Array.isArray(response.data)
            ? response.data.map(normalizeEvent)
            : [];
    } catch (error) {
        console.error('Failed to load calendar events', error);
    } finally {
        isLoading.value = false;
    }
}

function getEventsForDay(day) {
    if (day.empty) return [];

    const seenPatients = new Set();
    return calendarEvents.value.filter(e => {
        if (!e.date) return false;
        if (e.date.getDate() !== day.date || e.date.getMonth() !== currentMonth.value || e.date.getFullYear() !== currentYear.value) {
            return false;
        }

        const patientKey = (e.patientName || e.title || 'Event').trim();
        if (seenPatients.has(patientKey)) {
            return false;
        }

        seenPatients.add(patientKey);
        return true;
    });
}

function getEventTileLabel(event) {
    return event.patientName || event.title || 'Event';
}

function isToday(day) {
    if (day.empty) return false;
    const today = new Date();
    return day.fullDate.getDate() === today.getDate() &&
           day.fullDate.getMonth() === today.getMonth() &&
           day.fullDate.getFullYear() === today.getFullYear();
}

function isSelected(day) {
    if (day.empty) return false;
    return day.fullDate.getDate() === selectedDay.value.getDate() &&
           day.fullDate.getMonth() === selectedDay.value.getMonth() &&
           day.fullDate.getFullYear() === selectedDay.value.getFullYear();
}

function selectDay(day) {
    if (!day.empty) {
        selectedDay.value = day.fullDate;
        newEventDate.value = day.fullDate.toISOString().substring(0, 10);
    }
}

const selectedDayEvents = computed(() => {
    return calendarEvents.value.filter(e => {
        if (!e.date) return false;
        return e.date.getDate() === selectedDay.value.getDate() &&
               e.date.getMonth() === selectedDay.value.getMonth() &&
               e.date.getFullYear() === selectedDay.value.getFullYear();
    });
});

const manualSelectedDayEvents = computed(() =>
    selectedDayEvents.value.filter(event => event.source === 'manual')
);

const eventsForSelectedDay = computed(() => selectedDayEvents.value);

async function deleteSelectedEvents() {
    if (manualSelectedDayEvents.value.length === 0) {
        alert('Only manually added events can be deleted. Prenatal events are derived from patient records and will return on refresh.');
        return;
    }

    const message = manualSelectedDayEvents.value.length === 1
        ? `Delete manual event "${manualSelectedDayEvents.value[0].title}"?`
        : `Delete ${manualSelectedDayEvents.value.length} manual events for this day?`;

    if (!confirm(message)) {
        return;
    }

    try {
        await Promise.all(manualSelectedDayEvents.value.map(event =>
            axios.delete(`${BASE}/manual/${event.id}`)
        ));

        await fetchEventsForMonth();
    } catch (error) {
        console.error('Failed to delete selected event(s)', error);
        alert('Could not delete selected event(s).');
    }
}

async function addManualEvent() {
    if (!newEventTitle.value || !newEventDate.value) {
        alert('Please add a title and date for the event.');
        return;
    }

    const event = {
        title: newEventTitle.value,
        eventDate: newEventDate.value,
        eventType: newEventType.value || 'manual',
        patientName: newEventPatientName.value,
        description: newEventDescription.value
    };

    try {
        await axios.post(`${BASE}/manual`, event);
        newEventTitle.value = '';
        newEventType.value = 'manual';
        newEventPatientName.value = '';
        newEventDescription.value = '';
        await fetchEventsForMonth();
    } catch (error) {
        console.error('Failed to add manual event', error);
        alert('Could not save manual event.');
    }
}

function notifyPatient(event) {
    alert(`Notification sent to ${event.patientName} for their upcoming ${event.title}!`);
}

onMounted(fetchEventsForMonth);
watch([currentMonth, currentYear], fetchEventsForMonth);
</script>

<template>
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 p-6">
        <!-- Calendar Section -->
        <div class="lg:col-span-2 bg-white rounded-lg shadow-sm p-6">
            <div class="flex items-center justify-between mb-6">
                <div>
                    <h2 class="text-2xl font-bold text-gray-800">Admin Calendar</h2>
                    <p class="text-gray-600">Overview of patient appointments and labor days</p>
                </div>
                <div class="flex space-x-4">
                    <div class="flex items-center space-x-2">
                        <span class="w-3 h-3 rounded-full bg-blue-500"></span>
                        <span class="text-sm font-medium text-gray-600">Appointment</span>
                    </div>
                    <div class="flex items-center space-x-2">
                        <span class="w-3 h-3 rounded-full bg-pink-500"></span>
                        <span class="text-sm font-medium text-gray-600">Labor Day</span>
                    </div>
                </div>
            </div>

            <!-- Calendar Header -->
            <div class="flex items-center justify-between mb-4 bg-gray-50 p-4 rounded-lg border border-gray-200">
                <button @click="prevMonth" class="p-2 hover:bg-gray-200 rounded-full transition-colors">
                    <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                    </svg>
                </button>
                <h3 class="text-xl font-bold text-gray-800">{{ monthNames[currentMonth] }} {{ currentYear }}</h3>
                <button @click="nextMonth" class="p-2 hover:bg-gray-200 rounded-full transition-colors">
                    <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                </button>
            </div>

            <!-- Calendar Grid -->
            <div class="grid grid-cols-7 gap-px bg-gray-200 border border-gray-200 rounded-lg overflow-hidden">
                <!-- Days of week -->
                <div v-for="day in dayNames" :key="day" class="bg-gray-100 text-center py-3 font-semibold text-sm text-gray-700">
                    {{ day }}
                </div>
                
                <!-- Days -->
                <div 
                    v-for="day in daysInMonth" 
                    :key="day.key" 
                    @click="selectDay(day)"
                    class="bg-white min-h-[120px] p-2 transition-colors cursor-pointer hover:bg-gray-50 relative"
                    :class="{
                        'bg-gray-50': day.empty,
                        'ring-2 ring-inset ring-indigo-500 bg-indigo-50/30': isSelected(day)
                    }"
                >
                    <div v-if="!day.empty" class="h-full flex flex-col pointer-events-none">
                        <div class="flex justify-between items-start mb-2">
                            <span 
                                class="text-sm font-medium w-8 h-8 flex items-center justify-center rounded-full"
                                :class="isToday(day) ? 'bg-blue-600 text-white shadow-md' : 'text-gray-700'"
                            >
                                {{ day.date }}
                            </span>
                        </div>
                        <div class="flex-1 overflow-y-auto space-y-1">
                            <div 
                                v-for="(event, index) in getEventsForDay(day)" 
                                :key="index"
                                class="text-xs px-2 py-1.5 rounded truncate font-medium"
                                :class="event.type === 'labor' ? 'bg-pink-100 text-pink-700 border border-pink-200' : 'bg-blue-100 text-blue-700 border border-blue-200'"
                                :title="event.patientName"
                            >
                                {{ getEventTileLabel(event) }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Daily View Section -->
        <div class="bg-white rounded-lg shadow-sm p-6 border border-gray-200 h-fit sticky top-6">
            <h3 class="text-xl font-bold text-gray-800 mb-2">
                {{ selectedDay.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' }) }}
            </h3>
            <p class="text-sm text-gray-500 mb-6 border-b pb-4">
                {{ selectedDayEvents.length }} event(s) scheduled for this day
            </p>

            <div v-if="isLoading" class="text-center py-8 text-gray-500 italic bg-gray-50 rounded-lg">
                Loading calendar events...
            </div>

            <div v-else>
                <div v-if="selectedDayEvents.length === 0" class="text-center py-8 text-gray-500 italic bg-gray-50 rounded-lg">
                    No appointments or labor scheduled for this day.
                </div>

                <div class="space-y-4">
                    <div 
                        v-for="event in selectedDayEvents" 
                        :key="event.id"
                        class="p-4 rounded-xl border transition-all"
                        :class="event.type === 'labor' ? 'border-pink-200 bg-pink-50' : 'border-blue-200 bg-blue-50'"
                    >
                        <div class="flex justify-between items-start mb-2">
                            <div>
                                <span 
                                    class="text-xs font-bold uppercase tracking-wider px-2 py-1 rounded-full mb-2 inline-block"
                                    :class="event.type === 'labor' ? 'bg-pink-200 text-pink-800' : 'bg-blue-200 text-blue-800'"
                                >
                                    {{ event.type }}
                                </span>
                                <h4 class="font-bold text-gray-900 text-lg">{{ event.title }}</h4>
                                <p class="text-gray-600 text-sm mt-1">{{ event.patientName }}</p>
                            </div>
                        </div>
                        
                        <div class="mt-4 pt-4 border-t" :class="event.type === 'labor' ? 'border-pink-200' : 'border-blue-200'">
                            <button 
                                v-if="event.type === 'labor'"
                                @click="notifyPatient(event)"
                                class="w-full flex items-center justify-center gap-2 bg-pink-600 hover:bg-pink-700 text-white px-4 py-2 rounded-lg font-medium transition-colors shadow-sm"
                            >
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
                                </svg>
                                Notify Patient
                            </button>
                            
                            <button 
                                v-else
                                @click="$router.push(`/uikit/PatientProfiling/${event.patientId || event.id}`)"
                                class="w-full text-blue-600 hover:text-blue-800 font-medium text-sm transition-colors"
                            >
                                View Patient Record &rarr;
                            </button>
                        </div>
                    </div>
                </div>

                <div class="mt-6 pt-6 border-t border-gray-200">
                    <h4 class="text-lg font-semibold text-gray-800 mb-4">Add manual calendar event</h4>
                    <div class="grid gap-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Title</label>
                            <input v-model="newEventTitle" type="text" class="w-full rounded-lg border-gray-300 shadow-sm focus:ring-blue-500 focus:border-blue-500" placeholder="Event title" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Date</label>
                            <input v-model="newEventDate" type="date" class="w-full rounded-lg border-gray-300 shadow-sm focus:ring-blue-500 focus:border-blue-500" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Type</label>
                            <input v-model="newEventType" type="text" class="w-full rounded-lg border-gray-300 shadow-sm focus:ring-blue-500 focus:border-blue-500" placeholder="manual" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Patient Name</label>
                            <input v-model="newEventPatientName" type="text" class="w-full rounded-lg border-gray-300 shadow-sm focus:ring-blue-500 focus:border-blue-500" placeholder="Patient name" />
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                            <textarea v-model="newEventDescription" rows="3" class="w-full rounded-lg border-gray-300 shadow-sm focus:ring-blue-500 focus:border-blue-500" placeholder="Optional details"></textarea>
                        </div>
                        <button @click="addManualEvent" class="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-3 rounded-lg font-semibold transition-colors">
                            Add Manual Event
                        </button>
                    </div>
                    <div v-if="manualSelectedDayEvents.length > 0" class="mt-4">
                        <button
                            @click="deleteSelectedEvents"
                            class="w-full bg-red-600 hover:bg-red-700 text-white py-3 rounded-lg font-semibold transition-colors"
                        >
                            Delete {{ manualSelectedDayEvents.length === 1 ? 'Manual Event' : manualSelectedDayEvents.length + ' Manual Events' }}
                        </button>
                    </div>
                    <div v-else-if="selectedDayEvents.length > 0" class="mt-4 rounded-lg bg-yellow-50 border border-yellow-200 p-4 text-sm text-yellow-800">
                        Only manual events can be deleted here. Prenatal-generated events are stored in patient records and will reappear after refresh.
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

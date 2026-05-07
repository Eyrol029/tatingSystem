<script setup>
/* eslint-disable */
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const props = defineProps({
    patient: {
        type: Object,
        required: false
    }
});

const emit = defineEmits(['close', 'recordPayment', 'sendReminder']);

const paymentAmount = ref('');
const reminderType = ref('email');
const reminderMessage = ref('');

const route = useRoute();
const localPatient = ref(props.patient || null);

function tryResolvePatient() {
    if (localPatient.value) return;

    try {
        // Resolve from Route Query
        const q = route.query.patient;
        if (q) {
            localPatient.value = typeof q === 'string' ? JSON.parse(q) : q;
            return;
        }
        
        // Resolve from History State
        const s = window.history?.state?.patient;
        if (s) {
            localPatient.value = s;
        }
    } catch (e) {
        console.error("Error resolving patient data:", e);
    }
}

onMounted(() => {
    tryResolvePatient();
    if (localPatient.value) {
        reminderMessage.value = `Reminder: Outstanding balance of ${formatCurrency(outstanding.value)} due`;
    }
});

const outstanding = computed(() => (localPatient.value ? localPatient.value.totalBill - localPatient.value.amountPaid : 0));

function formatCurrency(amount) {
    return '₱' + amount.toLocaleString();
}

function formatDate(dateString) {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function handleRecordPayment() {
    if (paymentAmount.value && parseFloat(paymentAmount.value) > 0) {
        emit('recordPayment', parseFloat(paymentAmount.value));
        paymentAmount.value = '';
    }
}

function handleSendReminder() {
    if (reminderMessage.value.trim()) {
        emit('sendReminder', reminderType.value, reminderMessage.value);
        reminderMessage.value = `Reminder: Outstanding balance of ${formatCurrency(outstanding.value)} due`;
    }
}

const router = useRouter();
function handleClose() {
    emit('close');
    try {
        router.back();
    } catch (e) {
        // ignore
    }
}
</script>

<template>
  <div 
    v-if="localPatient"
    :class="route.name === 'PaymentDetails' ? 'p-6 pt-20' : 'fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50'" 
    @click.self="route.name === 'PaymentDetails' ? null : handleClose"
  >
    <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-auto" :class="route.name === 'PaymentDetails' ? 'max-h-[calc(100vh-6rem)]' : 'max-h-[85vh]'" style="overflow-y: auto;">

      <div class="sticky top-0 flex items-center justify-between p-6 border-b border-gray-200 bg-white z-10">
        <div>
          <h2 class="text-2xl font-bold">{{ localPatient.name }}</h2>
          <p class="text-sm text-gray-500 mt-1">{{ localPatient.service }}</p>
        </div>
        <button @click="handleClose" class="p-2 hover:bg-gray-100 rounded-lg transition-colors">
          <svg class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="p-6 space-y-8">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div class="bg-gray-50 p-4 rounded-lg">
            <p class="text-sm text-gray-600 mb-1">Total Bill</p>
            <p class="text-2xl font-bold">{{ formatCurrency(localPatient.totalBill) }}</p>
          </div>
          <div class="bg-green-50 text-green-700 p-4 rounded-lg">
            <p class="text-sm mb-1 font-medium">Amount Paid</p>
            <p class="text-2xl font-bold">{{ formatCurrency(localPatient.amountPaid) }}</p>
          </div>
          <div class="bg-red-50 text-red-700 p-4 rounded-lg">
            <p class="text-sm mb-1 font-medium">Outstanding</p>
            <p class="text-2xl font-bold">{{ formatCurrency(outstanding) }}</p>
          </div>
        </div>

        <div class="bg-blue-50/50 p-6 rounded-xl border border-blue-100">
          <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
            <span>📤</span> Send Reminder
          </h3>
          <div class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
              <label class="flex items-center gap-2 cursor-pointer p-3 border-2 rounded-lg bg-white hover:bg-gray-50 transition-colors" :class="reminderType === 'email' ? 'border-blue-500' : 'border-gray-200'">
                <input v-model="reminderType" type="radio" value="email" class="w-4 h-4" />
                <span class="text-lg">📧</span>
                <span class="text-sm flex-1">Email to {{ localPatient.email }}</span>
              </label>
              <label class="flex items-center gap-2 cursor-pointer p-3 border-2 rounded-lg bg-white hover:bg-gray-50 transition-colors" :class="reminderType === 'sms' ? 'border-blue-500' : 'border-gray-200'">
                <input v-model="reminderType" type="radio" value="sms" class="w-4 h-4" />
                <span class="text-lg">📱</span>
                <span class="text-sm flex-1">SMS to {{ localPatient.phone }}</span>
              </label>
            </div>
            <div>
              <label class="block text-sm font-semibold mb-2">Message Content</label>
              <textarea 
                v-model="reminderMessage" 
                rows="3" 
                class="w-full p-3 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-blue-500 bg-white"
              ></textarea>
            </div>
            <button 
              @click="handleSendReminder" 
              class="w-full px-4 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-bold transition-colors shadow-md"
            >
              Send Reminder Now
            </button>
          </div>
        </div>

        <div v-if="localPatient.reminders && localPatient.reminders.length > 0">
          <h3 class="text-lg font-semibold mb-4">Sent Reminders Log</h3>
          <div class="space-y-3">
            <div v-for="reminder in localPatient.reminders.slice(0, 5)" :key="reminder.id" class="flex items-start gap-3 p-3 bg-gray-50 border rounded-lg">
              <div class="flex-1">
                <div class="flex items-center gap-2 mb-1">
                  <span class="text-lg">{{ reminder.type === 'email' ? '📧' : '📱' }}</span>
                  <span class="text-sm font-bold capitalize">{{ reminder.type }}</span>
                  <span :class="['text-xs px-2 py-1 rounded font-medium', reminder.status === 'sent' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700']">
                    {{ reminder.status.toUpperCase() }}
                  </span>
                </div>
                <p class="text-sm text-gray-700 italic">"{{ reminder.message }}"</p>
                <p class="text-xs text-gray-500 mt-2 font-medium">{{ formatDate(reminder.sentDate) }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
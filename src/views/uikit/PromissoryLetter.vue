<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';

const props = defineProps({
    patientId: { type: [Number, String], default: null },
    patientName: { type: String, default: '' },
    soaDetails: { type: Object, default: null },
    employeeList: { type: Array, default: () => [] }
});

const emit = defineEmits(['close', 'saved']);
const SOA_URL = 'http://localhost:8080/api/billing/soa';

const form = ref({ dueDate: '', staffName: '' });
const saving = ref(false);
const error = ref('');

function formatCurrency(value) {
    return '₱' + Number(value || 0).toLocaleString('en-US', { minimumFractionDigits: 2 });
}

function formatDate(value) {
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? '—' : date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}

function open() {
    form.value.dueDate = props.soaDetails?.dueDate
        ? new Date(props.soaDetails.dueDate).toISOString().slice(0, 10)
        : new Date().toISOString().slice(0, 10);
    form.value.staffName = form.value.staffName || props.employeeList[0]?.name || '';
    error.value = '';
}

watch(() => props.soaDetails, open, { immediate: true });

async function saveAndPrint() {
    if (!props.patientId) {
        error.value = 'No patient selected to print a promissory record.';
        return;
    }

    saving.value = true;
    error.value = '';
    try {
        const soaId = props.soaDetails?.soaId ?? props.soaDetails?.soaID ?? null;
        if (soaId) {
            try {
                await axios.post(`${SOA_URL}/${soaId}/promissory-letter`, {
                    dueDate: form.value.dueDate || new Date().toISOString().slice(0, 10),
                    staffName: (form.value.staffName || 'Tracking').trim(),
                    patientId: Number(props.patientId),
                    patientName: props.patientName || 'Patient'
                });
            } catch (e) {
                console.warn('Promissory tracking record could not be saved, continuing with print-only mode.', e);
            }
        }

        const dueDate = new Date(`${form.value.dueDate || new Date().toISOString().slice(0, 10)}T00:00:00`);
        const issueDate = new Date();
        const total = Number(props.soaDetails?.totalAmount ?? 0);
        const balance = Number(props.soaDetails?.balanceAmount ?? 0);
        const staffName = (form.value.staffName || 'Tracking').trim();
        const printWindow = window.open('', '_blank', 'width=900,height=900');
        if (!printWindow) {
            error.value = 'Your browser blocked the print window. Please allow pop-ups and try again.';
            return;
        }

        const html = `
            <!DOCTYPE html>
            <html><head><title>Billing Due Date</title>
            <style>
              body { font-family: Arial, sans-serif; color: #111827; margin: 40px; }
              .header { text-align: center; margin-bottom: 30px; } h1 { font-size: 24px; margin: 0; }
              .meta { margin-top: 24px; line-height: 1.8; }
              .box { border: 1px solid #d1d5db; padding: 20px 24px; border-radius: 8px; }
              .amount { font-size: 22px; font-weight: bold; } .footer { margin-top: 40px; border-top: 1px solid #d1d5db; padding-top: 16px; }
              @media print { body { margin: 0; } }
            </style></head><body>
              <div class="header"><h1>Billing Due Date</h1></div>
              <div class="box">
                <p><strong>Patient Name:</strong> ${props.patientName || '—'}</p>
                <p><strong>Case Number:</strong> ${props.soaDetails?.caseNumber || '—'}</p>
                <p><strong>Tracking Date:</strong> ${formatDate(dueDate)}</p>
                <p><strong>Handled By:</strong> ${staffName}</p>
                <p><strong>Total Bill:</strong> ${formatCurrency(total)}</p>
                <p class="amount"><strong>Outstanding Balance:</strong> ${formatCurrency(balance)}</p>
              </div>
              <div class="meta">
                <p>Patient payment follow-up record. Please collect the balance on or before the tracked due date.</p>
              </div>
              <div class="footer"><p>Handled by: ${staffName}</p><p>______________________________</p><p>Signature over printed name</p></div>
            </body></html>`;

        printWindow.document.open();
        printWindow.document.write(html);
        printWindow.document.close();
        printWindow.focus();
        emit('saved');
        setTimeout(() => printWindow.print(), 300);
    } catch (e) {
        console.error('Failed to prepare promissory print', e);
        error.value = e?.response?.data?.message || 'Failed to prepare the promissory print. Please try again.';
    } finally {
        saving.value = false;
    }
}
</script>

<template>
    <div class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50 no-print">
        <div class="bg-white rounded-xl shadow-2xl max-w-lg w-full p-6 border border-gray-200">
            <div class="flex justify-between items-center mb-4 pb-3 border-b border-gray-100">
                <h2 class="text-xl font-bold text-gray-800">Set Billing Due Date</h2>
                <button @click="emit('close')" class="text-gray-400 hover:text-gray-600 transition text-2xl leading-none">&times;</button>
            </div>

            <div v-if="error" class="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg">{{ error }}</div>
            <div class="space-y-4">
                <div>
                    <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">Tracking Date</label>
                    <input v-model="form.dueDate" type="date" class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-violet-500 focus:outline-none" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-gray-600 uppercase tracking-wider mb-1">Handled By</label>
                    <input v-model="form.staffName" list="promissory-staff-list" type="text" placeholder="Optional staff name" class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-violet-500 focus:outline-none" />
                    <datalist id="promissory-staff-list">
                        <option v-for="staff in employeeList" :key="staff.id ?? staff.name" :value="staff.name" />
                    </datalist>
                </div>
            </div>
            <div class="flex justify-end gap-2 mt-6 pt-4 border-t border-gray-100">
                <button @click="emit('close')" class="px-4 py-2 text-sm font-medium text-gray-600 hover:bg-gray-100 rounded-lg transition">Cancel</button>
                <button @click="saveAndPrint" :disabled="saving" class="px-5 py-2 text-sm font-medium text-white bg-violet-600 hover:bg-violet-700 disabled:opacity-50 rounded-lg shadow transition">
                    {{ saving ? 'Saving...' : 'Save Due Date' }}
                </button>
            </div>
        </div>
    </div>
</template>

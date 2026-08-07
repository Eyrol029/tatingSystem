<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function goBack() { router.back() }
function printForm() { window.print() }

const form = ref({
  referralType: {
    emergency: false,
    ambulatory: false,
    medicoLegal: false
  },
  referredTo: '',
  dateTime: '',
  address: '',
  age: '',
  sex: '',
  patient: {
    surname: '',
    firstName: '',
    middleName: '',
    address: ''
  },
  chiefComplaints: '',
  medicalHistory: '',
  surgicalOperations: null, // true = Yes, false = No
  surgicalProcedure: '',
  drugAllergy: null,
  drugAllergyDetails: '',
  lastMeal: {
    over6Hours: '',
    under6Hours: ''
  },
  physicalExam: {
    bp: '',
    hr: '',
    rr: '',
    wt: ''
  },
  impression: '',
  actionTaken: '',
  healthInsurance: null,
  insuranceType: '',
  reasonsForReferral: {
    hospitalCapability: false,
    lackOfSpecialist: false,
    financialConstraints: false
  },
  referredBy: '',

  // Return Referral Slip
  returnSlip: {
    referringFacilityName: '',
    dateTime: '',
    address: '',
    patientName: '',
    patientAddress: '',
    diagnosis: '',
    recommendations: '',
    hospitalName: '',
    hospitalContact: ''
  },

  // Acknowledgement Receipt
  acknowledgement: {
    patientName: '',
    receivedDateTime: '',
    recipientHospital: '',
    ackDateTime: ''
  }
})
</script>

<template>
  <!-- Toolbar -->
  <div class="no-print max-w-screen mx-auto px-6 pt-6 flex items-center gap-3">
    <button @click="goBack"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
      </svg>
      Back
    </button>
    <button @click="printForm"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round"
          d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z" />
      </svg>
      Print / Save as PDF
    </button>
  </div>

  <!-- Printable form -->
  <div id="printable-area" class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <p class="text-xs text-gray-500 uppercase tracking-widest">Northern Mindanao Health Facility Referral Form</p>
      <h1 class="text-lg font-bold underline">TATING FAMILY PLANNING CLINIC</h1>
      <p class="text-xs text-gray-500">(Name of Health Facility)</p>
      <p class="text-sm text-gray-700 mt-1">Purok 26 Poblacion, Manticao, Misamis Oriental</p>
      <p class="text-xs text-gray-500">(Address)</p>
    </div>

    <h2 class="text-center font-bold text-base tracking-widest mb-3">CLINICAL REFERRAL FORM</h2>

    <!-- Referral type -->
    <div class="flex justify-center gap-8 mb-6 text-sm">
      <label class="cb"><input type="checkbox" v-model="form.referralType.emergency" /> EMERGENCY</label>
      <label class="cb"><input type="checkbox" v-model="form.referralType.ambulatory" /> AMBULATORY</label>
      <label class="cb"><input type="checkbox" v-model="form.referralType.medicoLegal" /> MEDICO-LEGAL</label>
    </div>

    <!-- Referred to / Date -->
    <div class="grid grid-cols-2 gap-4 mb-3">
      <div>
        <label class="field-label">Referred to</label>
        <input type="text" v-model="form.referredTo" class="input-line w-full" />
      </div>
      <div>
        <label class="field-label">Date &amp; Time</label>
        <input type="datetime-local" v-model="form.dateTime" class="input-line w-full" />
      </div>
    </div>

    <div class="grid grid-cols-3 gap-4 mb-3">
      <div class="col-span-2">
        <label class="field-label">Address</label>
        <input type="text" v-model="form.address" class="input-line w-full" />
      </div>
      <div class="grid grid-cols-2 gap-2">
        <div>
          <label class="field-label">Age</label>
          <input type="number" v-model="form.age" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Sex</label>
          <select v-model="form.sex" class="select-field w-full">
            <option value="">— —</option>
            <option>Male</option>
            <option>Female</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Patient's Name -->
    <div class="grid grid-cols-3 gap-4 mb-1">
      <div>
        <input type="text" v-model="form.patient.surname" class="input-line w-full" />
        <p class="field-caption">(Surname)</p>
      </div>
      <div>
        <input type="text" v-model="form.patient.firstName" class="input-line w-full" />
        <p class="field-caption">(First Name)</p>
      </div>
      <div>
        <input type="text" v-model="form.patient.middleName" class="input-line w-full" />
        <p class="field-caption">(Middle Name)</p>
      </div>
    </div>
    <p class="field-caption -mt-1 mb-3">Patient's Name</p>

    <div class="mb-3">
      <label class="field-label">Address</label>
      <input type="text" v-model="form.patient.address" class="input-line w-full" />
    </div>

    <div class="mb-3">
      <label class="field-label">Chief Complaints</label>
      <input type="text" v-model="form.chiefComplaints" class="input-line w-full" />
    </div>

    <div class="mb-6">
      <label class="field-label">Medical History</label>
      <textarea v-model="form.medicalHistory" rows="4"
        class="w-full border border-gray-300 rounded p-2 text-sm"></textarea>
    </div>

    <!-- Surgical / Allergy / Last meal -->
    <div class="border border-gray-200 rounded p-4 mb-6 space-y-3 text-sm">
      <div class="flex flex-wrap items-center gap-3">
        <span class="font-medium">Surgical Operations:</span>
        <label class="cb"><input type="radio" :value="false" v-model="form.surgicalOperations" /> No</label>
        <label class="cb"><input type="radio" :value="true" v-model="form.surgicalOperations" /> Yes</label>
        <span v-if="form.surgicalOperations" class="flex items-center gap-2 flex-1">
          <span>if yes, what procedure?</span>
          <input type="text" v-model="form.surgicalProcedure" class="input-line flex-1" />
        </span>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <span class="font-medium">Drug allergy:</span>
        <label class="cb"><input type="radio" :value="false" v-model="form.drugAllergy" /> No</label>
        <label class="cb"><input type="radio" :value="true" v-model="form.drugAllergy" /> Yes</label>
        <span v-if="form.drugAllergy" class="flex items-center gap-2 flex-1">
          <span>if yes, what?</span>
          <input type="text" v-model="form.drugAllergyDetails" class="input-line flex-1" />
        </span>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <span class="font-medium">Last Meal:</span>
        <span class="flex items-center gap-1">Time &gt;6 hours:
          <input type="text" v-model="form.lastMeal.over6Hours" class="input-line w-24" />
        </span>
        <span class="flex items-center gap-1">&lt;6 hours:
          <input type="text" v-model="form.lastMeal.under6Hours" class="input-line w-24" />
        </span>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <span class="font-medium">Physical Examination:</span>
        <span class="flex items-center gap-1">BP:
          <input type="text" v-model="form.physicalExam.bp" class="input-line w-20" /> mmhg</span>
        <span class="flex items-center gap-1">HR:
          <input type="text" v-model="form.physicalExam.hr" class="input-line w-16" /> bpm</span>
        <span class="flex items-center gap-1">RR:
          <input type="text" v-model="form.physicalExam.rr" class="input-line w-16" /> cpm</span>
        <span class="flex items-center gap-1">WT:
          <input type="text" v-model="form.physicalExam.wt" class="input-line w-16" /> kg.</span>
      </div>
    </div>

    <div class="mb-3">
      <label class="field-label">Impression</label>
      <input type="text" v-model="form.impression" class="input-line w-full" />
    </div>
    <div class="mb-6">
      <label class="field-label">Action Taken</label>
      <input type="text" v-model="form.actionTaken" class="input-line w-full" />
    </div>

    <!-- Insurance / Reasons -->
    <div class="border-l-4 border-blue-500 bg-blue-50 rounded-r p-4 mb-6 space-y-3 text-sm">
      <div class="flex flex-wrap items-center gap-3">
        <span class="font-medium">Health Insurance Coverage:</span>
        <label class="cb"><input type="radio" :value="false" v-model="form.healthInsurance" /> No</label>
        <label class="cb"><input type="radio" :value="true" v-model="form.healthInsurance" /> Yes</label>
        <span v-if="form.healthInsurance" class="flex items-center gap-2 flex-1">
          <span>if yes, state type of coverage:</span>
          <input type="text" v-model="form.insuranceType" placeholder="e.g. PhilHealth" class="input-line flex-1" />
        </span>
      </div>

      <div class="flex flex-wrap items-center gap-4">
        <span class="font-medium">Reasons for Referral:</span>
        <label class="cb"><input type="checkbox" v-model="form.reasonsForReferral.hospitalCapability" /> Hospital Capability</label>
        <label class="cb"><input type="checkbox" v-model="form.reasonsForReferral.lackOfSpecialist" /> Lack of Specialist</label>
        <label class="cb"><input type="checkbox" v-model="form.reasonsForReferral.financialConstraints" /> Financial Constraints</label>
      </div>
    </div>

    <!-- Referred by -->
    <div class="text-right mb-10">
      <p class="text-sm mb-1">Referred by:</p>
      <input type="text" v-model="form.referredBy" class="input-line w-64 ml-auto text-center" />
      <p class="field-caption">(Printed Name and Signature)</p>
    </div>

    <!-- RETURN REFERRAL SLIP -->
    <div class="border-t-4 border-gray-800 pt-6 mb-6">
      <h2 class="text-center font-bold text-base tracking-widest mb-1">RETURN REFERRAL SLIP</h2>
      <p class="text-center text-xs text-gray-500 mb-4">
        (to be filled-up by recipient Hospital upon discharge; Fax/email/others to referring unit)
      </p>

      <div class="grid grid-cols-2 gap-4 mb-3">
        <div>
          <label class="field-label">Name of Referring Facility</label>
          <input type="text" v-model="form.returnSlip.referringFacilityName" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Date &amp; Time</label>
          <input type="datetime-local" v-model="form.returnSlip.dateTime" class="input-line w-full" />
        </div>
      </div>

      <div class="mb-3">
        <label class="field-label">Address</label>
        <input type="text" v-model="form.returnSlip.address" class="input-line w-full" />
      </div>
      <div class="mb-3">
        <label class="field-label">Patient's Name</label>
        <input type="text" v-model="form.returnSlip.patientName" class="input-line w-full" />
      </div>
      <div class="mb-3">
        <label class="field-label">Address</label>
        <input type="text" v-model="form.returnSlip.patientAddress" class="input-line w-full" />
      </div>
      <div class="mb-3">
        <label class="field-label">Diagnosis / Impressions</label>
        <input type="text" v-model="form.returnSlip.diagnosis" class="input-line w-full" />
      </div>
      <div class="mb-4">
        <label class="field-label">Recommendations / Instructions</label>
        <input type="text" v-model="form.returnSlip.recommendations" class="input-line w-full" />
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="field-label">Name of Hospital</label>
          <input type="text" v-model="form.returnSlip.hospitalName" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Address &amp; Contact Number</label>
          <input type="text" v-model="form.returnSlip.hospitalContact" class="input-line w-full" />
        </div>
      </div>
    </div>

    <!-- ACKNOWLEDGEMENT RECEIPT -->
    <div class="border-t-4 border-gray-800 pt-6">
      <h2 class="text-center font-bold text-base tracking-widest mb-1">ACKNOWLEDGEMENT RECEIPT</h2>
      <p class="text-center text-xs text-gray-500 mb-4">(Slip to be given to the Driver)</p>

      <p class="text-sm leading-8">
        This is acknowledge
        <input type="text" v-model="form.acknowledgement.patientName" placeholder="name of patient"
          class="input-line inline-block w-56 mx-1" />
        Received
        <input type="text" v-model="form.acknowledgement.receivedDateTime" placeholder="date & time"
          class="input-line inline-block w-40 mx-1" />
        by
        <input type="text" v-model="form.acknowledgement.recipientHospital" placeholder="name of recipient hospital"
          class="input-line inline-block w-56 mx-1" />
        This
        <input type="text" v-model="form.acknowledgement.ackDateTime" placeholder="date & time"
          class="input-line inline-block w-40 mx-1" />
      </p>
    </div>

  </div>
</template>

<style scoped>
.input-line { display:block; width:100%; border:none; border-bottom:1px solid #94a3b8; outline:none; padding:2px; background:transparent; font-size:13px; }
.field-label { display:block; font-size:11px; font-weight:600; color:#6b7280; margin-bottom:2px; }
.field-caption { font-size:10px; color:#9ca3af; text-align:center; }
.select-field { padding:4px 6px; border:1px solid #d1d5db; border-radius:6px; font-size:13px; background:white; }
.cb { display:flex; align-items:center; gap:4px; font-size:0.85rem; cursor:pointer; white-space:nowrap; }
</style>

<style>
@page { size: A4 portrait; margin: 12mm; }
@media print {
  .no-print { display: none !important; }
  body, html { margin: 0 !important; padding: 0 !important; background: white !important; }
  #printable-area { box-shadow: none !important; border: none !important; margin: 0 !important; }
  input, textarea, select { border: none !important; background: transparent !important; }
  .input-line { border-bottom: 1px solid #000 !important; }
  textarea { border: 1px solid #555 !important; resize: none !important; }
}
</style>
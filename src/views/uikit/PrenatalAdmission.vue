<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
function goBack() { router.back() }

function handleBeforePrint() { document.body.classList.add('printing-prenatal') }
function handleAfterPrint()  { document.body.classList.remove('printing-prenatal') }

onMounted(() => {
  window.addEventListener('beforeprint', handleBeforePrint)
  window.addEventListener('afterprint', handleAfterPrint)
})
onUnmounted(() => {
  window.removeEventListener('beforeprint', handleBeforePrint)
  window.removeEventListener('afterprint', handleAfterPrint)
})

function printForm() { window.print() }

const form = ref({
  lmp: '', edc: '', gpal: '',
  obstetricRisk: {
    multiplePregnancy: false, ovarianCyst: false, myomaUteri: false,
    thyroidDisorder: false, miscarriage: false, preeclampsia: false,
    eclampsia: false, prematureContraction: false
  },
  medical: {
    hypertension: false, heartDisease: false, diabetes: false,
    thyroidDisorder: false, obesity: false, asthma: false,
    epilepsy: false, renalDisease: false, bleedingDisorder: false,
    previousCS: false, myomectomy: false
  },
  factors: {
    abdomen: { normoactiveBowel: false, nonTender: false, activeFetalMovement: false },
    speculum: { parousVagina: false, cervixSmoothClosed: false },
    leopold: { fundalHeight: '', estFetalWeight: '', fht: '', l1: '', l2: '', l3: '', l4: '' }
  },
  diagnosis: '',
  deliveryPlanning: {
    prenatalExamDate: '', expectedDeliveryDate: '', place: "TATING'S BIRTHING HOME"
  },
  deliveryDetails: {
    date: '', place: '', referralHospitalNeeded: false,
    referralHospitalName: '', type: 'Normal Spontaneous Vaginal Delivery'
  },
  labs: { ua: '', purulentSubstance: '', rbc: '', cbc: '', hemoglobin: '', vdrl: '', hiv: '', ultrasound: '' },
  visits: Array.from({ length: 10 }, (_, i) => ({
    visit: i + 1, date: '', aog: '', fht: '', fh: '',
    position: '', presentation: '', weight: '', bp: '', nextVisit: ''
  })),
  treatment: { palpation: false, labExam: false, dietAdvice: false, vitamins: false, referred: false }
})

function submitForm() {
  console.log('Prenatal Record:', form.value)
  alert('Prenatal record saved successfully!')
}
</script>

<template>
  <!-- Screen-only toolbar -->
  <div class="no-print max-w-screen mx-auto px-6 pt-6 flex items-center gap-3">
    <button @click="goBack"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
      </svg>
      Back
    </button>
    <button @click="printForm"
      class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 rounded-lg shadow transition">
      <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"/>
      </svg>
      Print / Save as PDF
    </button>
  </div>

  <!-- Printable form -->
  <div id="printable-area" class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header — always visible on print -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <div class="flex items-center justify-center gap-3 mb-2">
        <img
          src="/static/TATING (2).png"
          alt="Tating Logo"
          style="height: 3.5rem; width: auto; object-fit: contain;"
        />
        <div class="text-left">
          <p class="text-xs text-gray-500 uppercase tracking-widest leading-tight">Tating's Birthing Home</p>
          <h1 class="text-xl font-bold tracking-wide uppercase leading-tight">Prenatal Consultation Form</h1>
          <p class="text-xs text-gray-400 leading-tight">Maternal Health Record</p>
        </div>
      </div>
    </div>

    <!-- Basic Info -->
    <div class="grid grid-cols-3 gap-4 mb-6">
      <div>
        <label class="field-label">LMP</label>
        <input type="date" v-model="form.lmp" class="input" />
      </div>
      <div>
        <label class="field-label">EDC</label>
        <input type="date" v-model="form.edc" class="input" />
      </div>
      <div>
        <label class="field-label">G-P-T-A-L</label>
        <input type="text" v-model="form.gpal" class="input" />
      </div>
    </div>

    <!-- Risk Factors side by side -->
    <div class="no-break grid grid-cols-2 gap-6 mb-6 border border-gray-200 rounded p-4">
      <div>
        <h3 class="section-title">Obstetric Risk Factors</h3>
        <div class="space-y-1">
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.multiplePregnancy"/> Multiple Pregnancy</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.ovarianCyst"/> Ovarian Cyst</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.myomaUteri"/> Myoma Uteri</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.thyroidDisorder"/> Thyroid Disorder</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.miscarriage"/> History of Miscarriage</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.preeclampsia"/> Preeclampsia</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.eclampsia"/> Eclampsia</label>
          <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.prematureContraction"/> Premature Contraction</label>
        </div>
      </div>
      <div>
        <h3 class="section-title">Medical / Surgical History</h3>
        <div class="space-y-1">
          <label class="cb"><input type="checkbox" v-model="form.medical.hypertension"/> Hypertension</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.heartDisease"/> Heart Disease</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.diabetes"/> Diabetes</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.asthma"/> Asthma</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.obesity"/> Obesity</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.epilepsy"/> Epilepsy</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.previousCS"/> Previous CS</label>
          <label class="cb"><input type="checkbox" v-model="form.medical.myomectomy"/> Myomectomy</label>
        </div>
      </div>
    </div>

    <!-- Medical/Surgical Factors -->
    <div class="no-break border border-gray-200 rounded p-4 mb-6">
      <h3 class="section-title mb-3">Medical / Surgical Factors</h3>
      <div class="grid grid-cols-2 gap-6">
        <!-- Abdomen & Speculum -->
        <div class="space-y-4">
          <div>
            <p class="font-semibold text-sm mb-2">A. Abdomen</p>
            <label class="cb row-between"><span>Normoactive bowel sound</span><input type="checkbox" v-model="form.factors.abdomen.normoactiveBowel"/></label>
            <label class="cb row-between"><span>Non-Tender</span><input type="checkbox" v-model="form.factors.abdomen.nonTender"/></label>
            <label class="cb row-between"><span>Active Fetal Movement</span><input type="checkbox" v-model="form.factors.abdomen.activeFetalMovement"/></label>
          </div>
          <div>
            <p class="font-semibold text-sm mb-2">B. Speculum Exam</p>
            <label class="cb row-between"><span>Parous Vagina</span><input type="checkbox" v-model="form.factors.speculum.parousVagina"/></label>
            <label class="cb row-between"><span>Cervix Smooth Closed</span><input type="checkbox" v-model="form.factors.speculum.cervixSmoothClosed"/></label>
          </div>
        </div>
        <!-- Leopold -->
        <div class="bg-gray-50 p-3 rounded">
          <p class="font-semibold text-sm mb-3">C. Leopold Maneuver</p>
          <div class="grid grid-cols-2 gap-3 text-xs">
            <div>
              <label class="field-label">Fundal Height</label>
              <input type="text" v-model="form.factors.leopold.fundalHeight" class="input-line"/>
              <label class="field-label mt-2">Est. Fetal Weight</label>
              <input type="text" v-model="form.factors.leopold.estFetalWeight" class="input-line"/>
              <label class="field-label mt-2">FHT</label>
              <input type="text" v-model="form.factors.leopold.fht" class="input-line"/>
            </div>
            <div class="space-y-2">
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L1:</span><input type="text" v-model="form.factors.leopold.l1" class="input-line flex-1"/></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L2:</span><input type="text" v-model="form.factors.leopold.l2" class="input-line flex-1"/></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L3:</span><input type="text" v-model="form.factors.leopold.l3" class="input-line flex-1"/></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L4:</span><input type="text" v-model="form.factors.leopold.l4" class="input-line flex-1"/></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Diagnosis & Delivery Planning -->
    <div class="no-break border-l-4 border-blue-500 bg-blue-50 p-4 mb-6 rounded-r">
      <p class="font-bold mb-2">Complete Diagnosis:</p>
      <input type="text" v-model="form.diagnosis" class="input-line w-full mb-4"/>

      <p class="font-bold italic text-sm mb-2">Delivery Planning:</p>
      <div class="grid grid-cols-2 gap-4 text-sm">
        <div>
          <label class="field-label">Prenatal Exam Date</label>
          <input type="date" v-model="form.deliveryPlanning.prenatalExamDate" class="input-line w-full"/>
        </div>
        <div>
          <label class="field-label">Expected Date of Delivery</label>
          <input type="date" v-model="form.deliveryPlanning.expectedDeliveryDate" class="input-line w-full"/>
        </div>
        <div>
          <label class="field-label">Date of Delivery</label>
          <input type="date" v-model="form.deliveryDetails.date" class="input-line w-full"/>
        </div>
        <div>
          <label class="field-label">Place of Delivery</label>
          <input type="text" v-model="form.deliveryDetails.place" class="input-line w-full" :placeholder="form.deliveryPlanning.place"/>
        </div>
        <div>
          <label class="field-label">Type of Delivery</label>
          <input type="text" v-model="form.deliveryDetails.type" class="input-line w-full"/>
        </div>
        <div class="flex items-center gap-3">
          <label class="field-label">Referral Hospital?</label>
          <input type="checkbox" v-model="form.deliveryDetails.referralHospitalNeeded"/>
          <input v-if="form.deliveryDetails.referralHospitalNeeded" type="text"
            placeholder="Specify" v-model="form.deliveryDetails.referralHospitalName" class="input-line flex-1"/>
        </div>
      </div>
    </div>

    <!-- Follow-Up Visits -->
    <div class="mb-6">
      <h3 class="section-title mb-2">Follow-Up Visits</h3>
      <div class="overflow-x-auto">
        <table class="w-full border-collapse text-xs">
          <thead>
            <tr class="bg-gray-100">
              <th class="td">Visit</th>
              <th class="td">Date</th>
              <th class="td">AOG</th>
              <th class="td">FHT</th>
              <th class="td">FH</th>
              <th class="td">Position</th>
              <th class="td">Pres.</th>
              <th class="td">Weight</th>
              <th class="td">BP</th>
              <th class="td">Next Visit</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="v in form.visits" :key="v.visit">
              <td class="td text-center">{{ v.visit }}</td>
              <td class="td"><input v-model="v.date" type="date" class="input-table"/></td>
              <td class="td"><input v-model="v.aog" class="input-table"/></td>
              <td class="td"><input v-model="v.fht" class="input-table"/></td>
              <td class="td"><input v-model="v.fh" class="input-table"/></td>
              <td class="td"><input v-model="v.position" class="input-table"/></td>
              <td class="td"><input v-model="v.presentation" class="input-table"/></td>
              <td class="td"><input v-model="v.weight" class="input-table"/></td>
              <td class="td"><input v-model="v.bp" class="input-table"/></td>
              <td class="td"><input v-model="v.nextVisit" type="date" class="input-table"/></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Laboratory Results -->
    <div class="no-break border border-gray-200 rounded p-4 mb-6">
      <h3 class="text-center font-bold text-sm tracking-widest mb-4">LABORATORY RESULTS</h3>
      <div class="grid grid-cols-2 gap-x-8 gap-y-3 text-sm">
        <div class="col-span-2 flex items-center gap-2">
          <label class="w-28 font-medium">Urinalysis:</label>
          <input v-model="form.labs.ua" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">Purulent:</label>
          <input v-model="form.labs.purulentSubstance" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">Red Blood Cells:</label>
          <input v-model="form.labs.rbc" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">CBC:</label>
          <input v-model="form.labs.cbc" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">Hemoglobin:</label>
          <input v-model="form.labs.hemoglobin" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">VDRL:</label>
          <input v-model="form.labs.vdrl" class="input-line flex-1"/>
        </div>
        <div class="flex items-center gap-2">
          <label class="w-28 font-medium">HIV Test:</label>
          <input v-model="form.labs.hiv" class="input-line flex-1"/>
        </div>
        <div class="col-span-2">
          <label class="block font-medium mb-1">Ultrasound Results:</label>
          <textarea v-model="form.labs.ultrasound" rows="2" class="w-full border border-gray-300 rounded p-2 text-sm"></textarea>
        </div>
      </div>
    </div>

    <!-- Treatment -->
    <div class="no-break border border-gray-200 rounded p-4 mb-6">
      <h3 class="section-title mb-3">Treatment / Management</h3>
      <div class="grid grid-cols-2 gap-2">
        <label class="cb"><input type="checkbox" v-model="form.treatment.palpation"/> Palpation done</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.dietAdvice"/> Diet counseling</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.vitamins"/> Vitamins prescribed</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.referred"/> Referred to physician</label>
      </div>
    </div>

    <!-- Signature lines -->
    <div class="grid grid-cols-2 gap-16 mt-10">
      <div class="text-center">
        <div class="border-t border-gray-800 pt-1 mt-10">
          <p class="text-xs font-semibold">Patient's Signature / Date</p>
        </div>
      </div>
      <div class="text-center">
        <div class="border-t border-gray-800 pt-1 mt-10">
          <p class="text-xs font-semibold">Attending Midwife / Physician</p>
        </div>
      </div>
    </div>

    <!-- Save button — screen only -->
    <button @click="submitForm"
      class="no-print w-full bg-indigo-600 text-white py-3 rounded-lg font-bold hover:bg-indigo-700 transition shadow mt-6">
      SAVE COMPLETE PRENATAL RECORD
    </button>
  </div>
</template>

<!-- SCOPED: screen styles -->
<style scoped>
.input {
  width: 100%; padding: 6px 8px;
  border: 1px solid #d1d5db; border-radius: 6px; outline: none; font-size: 13px;
}
.input-table {
  width: 100%; padding: 2px; border: none;
  text-align: center; font-size: 11px; background: transparent;
}
.input-line {
  display: block; width: 100%;
  border: none; border-bottom: 1px solid #94a3b8;
  outline: none; padding: 2px; background: transparent; font-size: 13px;
}
.field-label { display: block; font-size: 11px; font-weight: 600; color: #6b7280; margin-bottom: 2px; }
.section-title { font-weight: 700; font-size: 0.95rem; color: #1e293b; margin-bottom: 8px; }
.td { border: 1px solid #e5e7eb; padding: 2px 4px; }
.cb { display: flex; align-items: center; gap: 6px; font-size: 0.85rem; cursor: pointer; }
.row-between { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #e5e7eb; padding-bottom: 4px; }
</style>

<!-- GLOBAL: print styles — not scoped, targets the app layout shell too -->
<style>
@page {
  size: A4 portrait;
  margin: 12mm 12mm;
}

@media print {
  /* 1. Force colors */
  * {
    -webkit-print-color-adjust: exact !important;
    print-color-adjust: exact !important;
  }

  /* 2. Hide app chrome — topbar, sidebar, footer, mask, toolbar buttons */
  body.printing-prenatal .layout-topbar,
  body.printing-prenatal .layout-sidebar,
  body.printing-prenatal .layout-footer,
  body.printing-prenatal .layout-mask,
  body.printing-prenatal app-topbar,
  body.printing-prenatal app-sidebar,
  body.printing-prenatal app-footer,
  body.printing-prenatal .no-print {
    display: none !important;
  }

  /* 3. Remove padding/margin from layout containers so form fills the page */
  body.printing-prenatal .layout-wrapper,
  body.printing-prenatal .layout-main-container,
  body.printing-prenatal .layout-main {
    display: block !important;
    padding: 0 !important;
    margin: 0 !important;
    width: 100% !important;
    min-height: unset !important;
  }

  /* 4. Printable area: normal flow (NOT fixed/absolute — that breaks multi-page) */
  body.printing-prenatal #printable-area {
    display: block !important;
    width: 100% !important;
    margin: 0 !important;
    padding: 0 !important;
    border: none !important;
    box-shadow: none !important;
    border-radius: 0 !important;
    background: white !important;
  }

  /* 5. Clean inputs — show as underline fields */
  input[type="text"],
  input[type="date"] {
    border: none !important;
    border-bottom: 1px solid #000 !important;
    border-radius: 0 !important;
    background: transparent !important;
    box-shadow: none !important;
    -webkit-appearance: none !important;
  }
  textarea {
    border: 1px solid #555 !important;
    background: transparent !important;
    resize: none !important;
  }

  /* 6. Table borders */
  table { border-collapse: collapse !important; width: 100% !important; }
  th, td { border: 1px solid #333 !important; }
  thead tr { background-color: #e5e7eb !important; }

  /* 7. Section backgrounds */
  .bg-blue-50  { background-color: #eff6ff !important; }
  .bg-gray-50  { background-color: #f9fafb !important; }
  .bg-gray-100 { background-color: #f3f4f6 !important; }

  /* 8. Prevent awkward mid-section page breaks */
  .no-break { page-break-inside: avoid !important; }

  /* 9. Always show the logo image in the print header */
  body.printing-prenatal #printable-area img {
    display: block !important;
    visibility: visible !important;
    -webkit-print-color-adjust: exact !important;
    print-color-adjust: exact !important;
    max-height: 3.5rem !important;
    width: auto !important;
  }
}
</style>
<script setup>
import { ref } from 'vue'

const form = ref({
  // BASIC INFO
  lmp: '',
  edc: '',
  gpal: '',

  // Obstetric Risk Factors
  obstetricRisk: {
    multiplePregnancy: false,
    ovarianCyst: false,
    myomaUteri: false,
    thyroidDisorder: false,
    miscarriage: false,
    preeclampsia: false,
    eclampsia: false,
    prematureContraction: false
  },

  // Medical / Surgical History
  medical: {
    hypertension: false,
    heartDisease: false,
    diabetes: false,
    thyroidDisorder: false,
    obesity: false,
    asthma: false,
    epilepsy: false,
    renalDisease: false,
    bleedingDisorder: false,
    previousCS: false,
    myomectomy: false
  },

  // 7. Medical/Surgical Factors (Abdomen/Speculum/Leopold)
  factors: {
    abdomen: {
      normoactiveBowel: false,
      nonTender: false,
      activeFetalMovement: false
    },
    speculum: {
      parousVagina: false,
      cervixSmoothClosed: false
    },
    leopold: {
      fundalHeight: '',
      estFetalWeight: '',
      fht: '',
      l1: '',
      l2: '',
      l3: '',
      l4: ''
    }
  },

  // 8. Diagnosis / Delivery Planning
  diagnosis: '',
  deliveryPlanning: {
    orientation: false,
    prenatalExamDate: '2025-06-06',
    expectedDeliveryDate: '2025-07-22',
    place: 'TATING’S BIRTHING HOME'
  },
  deliveryDetails: {
    date: '',
    place: '',
    referralHospitalNeeded: false,
    referralHospitalName: '',
    type: 'Normal Spontaneous Vaginal Delivery'
  },

  // Lab Results
  labs: {
    ua: '',
    purulentSubstance: '',
    rbc: '',
    cbc: '',
    rbcCount: '',
    hemoglobin: '',
    hbsag: '',
    vdrl: '',
    hiv: '',
    ultrasound: '',
    tetanus: ''
  },

  // FOLLOW-UP VISITS
  visits: Array.from({ length: 10 }, (_, i) => ({
    visit: i + 1,
    date: '',
    aog: '',
    fht: '',
    fh: '',
    position: '',
    presentation: '',
    weight: '',
    rr: '',
    hr: '',
    bp: '',
    temp: '',
    nextVisit: ''
  })),

  // Treatment / Management
  treatment: {
    palpation: false,
    labExam: false,
    dietAdvice: false,
    hydration: false,
    walking: false,
    hygiene: false,
    avoidHeavy: false,
    vitamins: false,
    referred: false,
    remarks: ''
  }
})

function submitForm() {
  console.log('Prenatal Record:', form.value)
  alert('Prenatal record saved successfully!')
}
</script>

<template>
  <div class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-10 border border-gray-100">
    <h1 class="text-2xl font-bold text-center mb-6">Prenatal Consultation Form</h1>

    <div class="grid grid-cols-3 gap-4 mb-8">
      <div>
        <label class="block text-sm font-medium">LMP</label>
        <input type="date" v-model="form.lmp" class="input" />
      </div>
      <div>
        <label class="block text-sm font-medium">EDC</label>
        <input type="date" v-model="form.edc" class="input" />
      </div>
      <div>
        <label class="block text-sm font-medium">G-P-T-A-L</label>
        <input type="text" v-model="form.gpal" class="input" />
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
      <div>
        <h3 class="section-title">Obstetric Risk Factors</h3>
        <div class="grid grid-cols-1 gap-1">
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.multiplePregnancy" /> Multiple Pregnancy</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.ovarianCyst" /> Ovarian Cyst</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.myomaUteri" /> Myoma Uteri</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.thyroidDisorder" /> Thyroid Disorder</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.miscarriage" /> History of Miscarriage</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.obstetricRisk.preeclampsia" /> Preeclampsia</label>
        </div>
      </div>

      <div>
        <h3 class="section-title">Medical / Surgical History</h3>
        <div class="grid grid-cols-1 gap-1">
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.hypertension" /> Hypertension</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.heartDisease" /> Heart Disease</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.diabetes" /> Diabetes</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.asthma" /> Asthma</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.previousCS" /> Previous CS</label>
          <label class="checkbox-label"><input type="checkbox" v-model="form.medical.myomectomy" /> Myomectomy</label>
        </div>
      </div>
    </div>

    <hr class="my-8" />

    <h3 class="section-title">Medical/Surgical Factors</h3>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-10 mb-8">
      <div class="space-y-6">
        <div>
          <p class="font-bold mb-2">A. Abdomen</p>
          <div class="space-y-1">
            <label class="flex justify-between items-center border-b pb-1">Normoactive bowel sound <input type="checkbox" v-model="form.factors.abdomen.normoactiveBowel" /></label>
            <label class="flex justify-between items-center border-b pb-1">Non-Tender <input type="checkbox" v-model="form.factors.abdomen.nonTender" /></label>
            <label class="flex justify-between items-center border-b pb-1">Active Fetal Movement <input type="checkbox" v-model="form.factors.abdomen.activeFetalMovement" /></label>
          </div>
        </div>
        <div>
          <p class="font-bold mb-2">B. Speculum Exam</p>
          <div class="space-y-1">
            <label class="flex justify-between items-center border-b pb-1">Parous Vagina <input type="checkbox" v-model="form.factors.speculum.parousVagina" /></label>
            <label class="flex justify-between items-center border-b pb-1">Cervix Smooth Closed <input type="checkbox" v-model="form.factors.speculum.cervixSmoothClosed" /></label>
          </div>
        </div>
      </div>

      <div class="bg-gray-50 p-4 rounded">
        <p class="font-bold mb-4">Leopold Maneuver</p>
        <div class="grid grid-cols-2 gap-4">
          <div class="space-y-2">
            <label class="block text-xs font-bold text-gray-500">Fundal Height</label>
            <input type="text" v-model="form.factors.leopold.fundalHeight" class="input-line" />
            <label class="block text-xs font-bold text-gray-500">Est. Fetal Weight</label>
            <input type="text" v-model="form.factors.leopold.estFetalWeight" class="input-line" />
            <label class="block text-xs font-bold text-gray-500">FHT</label>
            <input type="text" v-model="form.factors.leopold.fht" class="input-line" />
          </div>
          <div class="space-y-2">
            <div class="flex items-center"><span class="w-8 font-bold text-sm">L1:</span><input type="text" v-model="form.factors.leopold.l1" class="input-line flex-grow" /></div>
            <div class="flex items-center"><span class="w-8 font-bold text-sm">L2:</span><input type="text" v-model="form.factors.leopold.l2" class="input-line flex-grow" /></div>
            <div class="flex items-center"><span class="w-8 font-bold text-sm">L3:</span><input type="text" v-model="form.factors.leopold.l3" class="input-line flex-grow" /></div>
            <div class="flex items-center"><span class="w-8 font-bold text-sm">L4:</span><input type="text" v-model="form.factors.leopold.l4" class="input-line flex-grow" /></div>
          </div>
        </div>
      </div>
    </div>

  <section class="mb-8 p-4 border-l-4 border-blue-500 bg-blue-50">
      <div class="mb-4">
        <label class="font-bold block">8. Complete Diagnosis:</label>
        <input type="text" v-model="form.diagnosis" class="input-line w-full" />
      </div>

      <div class="ml-4 space-y-2 text-sm">
        <p class="font-bold italic">Delivery Planning Indicating:</p>
        <p>1. Orientation to BBH package / Availment of Benefits</p>
       <div class="flex items-center gap-2">
        </div>
    <span>2. Schedule of Prenatal Examination Date:</span>
    <input type="date" v-model="form.deliveryPlanning.prenatalExamDate" class="input-line w-40 font-mono" />
  </div>

  <div class="space-y-2">
    <div class="flex items-center gap-2">
      <span>3. Expected Date of Delivery:</span>
      <input type="date" v-model="form.deliveryPlanning.expectedDeliveryDate" class="input-line w-40 font-mono" />
    </div>
        <p class="ml-10 font-bold uppercase">Place: {{ form.deliveryPlanning.place }}</p>
      </div>

      <div class="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
        <div>
          <label class="block">Date of Delivery:</label>
          <input type="date" v-model="form.deliveryDetails.date" class="input-line w-full" />
        </div>
        <div>
          <label class="block">Place of Delivery:</label>
          <input type="text" v-model="form.deliveryDetails.place" class="input-line w-full" />
        </div>
        <div class="flex items-center gap-4">
          <label>Referral Hospital?</label>
          <input type="checkbox" v-model="form.deliveryDetails.referralHospitalNeeded" />
          <input 
            v-if="form.deliveryDetails.referralHospitalNeeded"
            type="text" 
            placeholder="Specify Hospital" 
            v-model="form.deliveryDetails.referralHospitalName" 
            class="input-line flex-grow" 
          />
        </div>
        <div>
          <label class="block font-bold">Type of delivery:</label>
          <input type="text" v-model="form.deliveryDetails.type" class="input-line w-full" />
        </div>
      </div>
    </section>

    <h3 class="text-lg font-semibold mt-6 mb-2">Follow-Up Visits</h3>
    <div class="overflow-x-auto mb-8">
      <table class="min-w-full border text-xs">
        <thead class="bg-gray-100">
          <tr>
            <th class="td">Visit</th><th class="td">Date</th><th class="td">AOG</th><th class="td">FHT</th><th class="td">FH</th>
            <th class="td">Position</th><th class="td">Pres.</th><th class="td">Weight</th><th class="td">BP</th><th class="td">Next</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="v in form.visits" :key="v.visit">
            <td class="td">{{ v.visit }}</td>
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

    <div class="mb-10">
      <h3 class="text-center font-bold text-lg mb-6 tracking-widest">LABORATORY RESULTS</h3>
      <div class="space-y-4">
        <div class="flex items-center"><label class="w-32 text-sm">Urinalysis:</label><input v-model="form.labs.ua" class="input-pill flex-grow" /></div>
        
        <div class="grid grid-cols-2 gap-4">
          <div class="flex items-center"><label class="w-32 text-xs">Purulent:</label><input v-model="form.labs.purulentSubstance" class="input-pill flex-grow" /></div>
          <div class="flex items-center"><label class="w-32 text-xs">Red Blood Cells:</label><input v-model="form.labs.rbc" class="input-pill flex-grow" /></div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div class="flex items-center"><label class="w-32 text-xs">CBC:</label><input v-model="form.labs.cbc" class="input-pill flex-grow" /></div>
          <div class="flex items-center"><label class="w-32 text-xs">Hemoglobin:</label><input v-model="form.labs.hemoglobin" class="input-pill flex-grow" /></div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div class="flex items-center"><label class="w-32 text-xs">VDRL:</label><input v-model="form.labs.vdrl" class="input-pill flex-grow" /></div>
          <div class="flex items-center"><label class="w-32 text-xs">HIV TEST:</label><input v-model="form.labs.hiv" class="input-pill flex-grow" /></div>
        </div>

        <div>
          <label class="block text-sm mb-1">Ultrasound Results:</label>
          <textarea v-model="form.labs.ultrasound" rows="2" class="w-full border rounded-lg p-2"></textarea>
        </div>
      </div>
    </div>

    <h3 class="section-title">Treatment / Management</h3>
    <div class="grid grid-cols-2 gap-2 mb-8">
      <label class="checkbox-label"><input type="checkbox" v-model="form.treatment.palpation" /> Palpation done</label>
      <label class="checkbox-label"><input type="checkbox" v-model="form.treatment.dietAdvice" /> Diet counseling</label>
      <label class="checkbox-label"><input type="checkbox" v-model="form.treatment.vitamins" /> Vitamins prescribed</label>
      <label class="checkbox-label"><input type="checkbox" v-model="form.treatment.referred" /> Referred to physician</label>
    </div>

    <button @click="submitForm" class="w-full bg-indigo-600 text-white py-4 rounded-lg font-bold hover:bg-indigo-700 transition shadow-lg">
      SAVE COMPLETE PRENATAL RECORD
    </button>
  </div>
</template>

<style scoped>
.input {
  width: 100%;
  padding: 8px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  outline: none;
}
.input-table {
  width: 100%;
  padding: 2px;
  border: none;
  text-align: center;
  font-size: 11px;
}
.input-line {
  border: none;
  border-bottom: 1px solid #94a3b8;
  outline: none;
  padding: 2px;
}
.input-pill {
  border: 1px solid #cbd5e1;
  border-radius: 20px;
  padding: 4px 12px;
  font-size: 13px;
  outline: none;
}
.section-title {
  font-weight: 700;
  font-size: 1.1rem;
  margin-bottom: 12px;
  color: #1e293b;
}
.td {
  border: 1px solid #e5e7eb;
  padding: 2px;
  text-align: center;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
}
</style>
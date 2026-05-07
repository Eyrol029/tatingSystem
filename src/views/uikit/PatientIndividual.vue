<script setup>
import { ref } from 'vue'

const activeTab = ref('clinical')
const lowRiskAnswer = ref('yes')
const selectedRiskFactors = ref(['Multiple pregnancy'])

const patient = {
  name: 'Althea Mae Santos',
  id: 'P001',
  consultationDate: '2025-06-15',
  gestationalAge: '14 weeks AOG',
  riskLevel: 'Low Risk (Initial)'
}

const tabs = [
  { id: 'clinical', name: 'Clinical History' },
  { id: 'delivery', name: 'Delivery Record' },
  { id: 'laboratory', name: 'Laboratory Result' },
  { id: 'consultation', name: 'Consultation Record' },
  { id: 'treatment', name: 'Treatment Mgmt' }
]

const clinicalData = {
  lmp: '2025-02-10',
  gravida: 1,
  para: 0,
  term: 0,
  preterm: 0
}

const riskFactors = [
  'Multiple pregnancy',
  'Ovarian Cyst',
  'Myoma Uteri',
  'Thyroid Disorder',
  'History of 3 miscarriage',
  'Stillbirth',
  'Preeclampsia',
  'Eclampsia',
  'Premature Contraction'
]

const medicalConditions = [
  'Hypertension',
  'Heart diseases',
  'Diabetes',
  'Thyroid Disorder',
  'Obesity',
  'Moderate to Severe Asthma',
  'Epilepsy',
  'Renal Diseases',
  'Bleeding Disorder',
  'Previous Cesarean Section',
  'History of Myomectomy'
]

const selectedMedicalConditions = ref(['Hypertension', 'Obesity'])

const observations = [
  'Nonmactive bowel sounds',
  'Non-Tender',
  'Active Fetal Movement'
]

const selectedObservations = ref(['Nonmactive bowel sounds', 'Active Fetal Movement'])

const measurements = ref({
  fundalHeight: '28cm',
  estimatedFetalWeight: '1.2kg',
  fetalHeartTone: '140bpm',
  notEnlarge: 'Not enlarge'
})

</script>

<template>
  <div class="space-y-6 p-6 bg-white rounded-lg shadow-sm">
    <!-- HEADER -->
    <div class="border-b pb-6">
      <h2 class="text-3xl font-bold text-gray-900 mb-4">{{ patient.name }}</h2>

      <div class="flex flex-wrap gap-3 mb-4">
        <span class="bg-blue-100 text-blue-700 px-4 py-2 rounded-full text-sm">Patient ID: {{ patient.id }}</span>
        <span class="bg-blue-100 text-blue-700 px-4 py-2 rounded-full text-sm">Date: {{ patient.consultationDate }}</span>
      </div>

      <div class="flex flex-wrap gap-3">
        <span class="bg-blue-100 text-blue-700 px-4 py-2 rounded-full text-sm">{{ patient.gestationalAge }}</span>
        <span class="bg-blue-100 text-blue-700 px-4 py-2 rounded-full text-sm">{{ patient.riskLevel }}</span>
      </div>
    </div>

    <!-- TABS -->
    <div class="flex gap-2 border-b pb-4">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        @click="activeTab = tab.id"
        :class="[
          'px-6 py-2 font-medium text-sm rounded transition',
          activeTab === tab.id
            ? 'bg-purple-600 text-white'
            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
        ]"
      >
        {{ tab.name }}
      </button>
    </div>

    <!-- CONTENT -->
    <div v-if="activeTab === 'clinical'" class="space-y-6">

      <!-- Vital Signs -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">💗</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Normal Vital Signs</h3>
            <p class="text-sm text-gray-500">Baseline reference values</p>
          </div>
        </div>

        <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded text-sm">BP: 90–120 / 60–80 mmHg</span>
        <p class="text-sm text-gray-600 mt-3">Used as reference during prenatal visits</p>
      </div>

      <!-- Menstrual History -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">📅</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Menstrual History</h3>
            <p class="text-sm text-gray-500">Pregnancy information</p>
          </div>
        </div>

        <p class="text-sm text-gray-600 mb-2">Last Menstrual Period</p>
        <p class="text-lg font-semibold text-gray-900 mb-6">{{ clinicalData.lmp }}</p>

        <div class="grid grid-cols-4 gap-4">
          <div class="text-center">
            <p class="text-sm text-gray-600">Gravida</p>
            <p class="text-2xl font-bold text-gray-900">{{ clinicalData.gravida }}</p>
          </div>
          <div class="text-center">
            <p class="text-sm text-gray-600">Para</p>
            <p class="text-2xl font-bold text-gray-900">{{ clinicalData.para }}</p>
          </div>
          <div class="text-center">
            <p class="text-sm text-gray-600">Term</p>
            <p class="text-2xl font-bold text-gray-900">{{ clinicalData.term }}</p>
          </div>
          <div class="text-center">
            <p class="text-sm text-gray-600">Preterm</p>
            <p class="text-2xl font-bold text-gray-900">{{ clinicalData.preterm }}</p>
          </div>
        </div>
      </div>

      <!-- Initial Pregnancy Screening -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start justify-between mb-4">
          <div class="flex items-start gap-3">
            <span class="text-2xl">🤰</span>
            <div>
              <h3 class="text-lg font-bold text-gray-900">Initial Pregnancy Screening</h3>
              <p class="text-sm text-gray-500">Overall risk assessment at first consultation</p>
            </div>
          </div>
          <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs font-medium">Low risk</span>
        </div>

        <p class="text-sm text-gray-600 mb-4">Is this low risk of pregnancy?</p>
        <div class="flex gap-6">
          <label class="flex items-center gap-2 cursor-pointer">
            <input v-model="lowRiskAnswer" type="radio" value="yes" class="w-4 h-4" />
            <span class="text-sm">Yes</span>
          </label>
          <label class="flex items-center gap-2 cursor-pointer">
            <input v-model="lowRiskAnswer" type="radio" value="no" class="w-4 h-4" />
            <span class="text-sm">No</span>
          </label>
        </div>
      </div>

      <!-- Obstetric Risk Factors -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">⚠️</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Obstetric Risk Factors</h3>
            <p class="text-sm text-gray-500">Check all applicable risk factor for this pregnancy</p>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div v-for="factor in riskFactors" :key="factor" class="flex items-center gap-2">
            <input
              :checked="selectedRiskFactors.includes(factor)"
              @change="(e) => {
                if (e.target.checked) {
                  selectedRiskFactors.push(factor)
                } else {
                  selectedRiskFactors = selectedRiskFactors.filter(f => f !== factor)
                }
              }"
              type="checkbox"
              class="w-4 h-4"
            />
            <label class="text-sm text-gray-700 cursor-pointer">{{ factor }}</label>
          </div>
        </div>
      </div>

      <!-- Medical & Surgical Factors Med Condition -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">⚕️</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Medical & Surgical Factors Med Condition</h3>
            <p class="text-sm text-gray-500">Existing comorbidities that may affect pregnancy</p>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div v-for="condition in medicalConditions" :key="condition" class="flex items-center gap-2">
            <input
              :checked="selectedMedicalConditions.includes(condition)"
              @change="(e) => {
                if (e.target.checked) {
                  selectedMedicalConditions.push(condition)
                } else {
                  selectedMedicalConditions = selectedMedicalConditions.filter(c => c !== condition)
                }
              }"
              type="checkbox"
              class="w-4 h-4"
            />
            <label class="text-sm text-gray-700 cursor-pointer">{{ condition }}</label>
          </div>
        </div>
      </div>

      <!-- Medical & Surgical Factors Measurement -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">📏</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Medical & Surgical Factors Measurement</h3>
            <p class="text-sm text-gray-500">Baseline abdominal and fetal examination</p>
          </div>
        </div>

        <div class="space-y-4">
          <div>
            <p class="text-sm font-semibold text-gray-900 mb-3">Observations</p>
            <div class="grid grid-cols-2 gap-4">
              <div v-for="obs in observations" :key="obs" class="flex items-center gap-2">
                <input
                  :checked="selectedObservations.includes(obs)"
                  @change="(e) => {
                    if (e.target.checked) {
                      selectedObservations.push(obs)
                    } else {
                      selectedObservations = selectedObservations.filter(o => o !== obs)
                    }
                  }"
                  type="checkbox"
                  class="w-4 h-4"
                />
                <label class="text-sm text-gray-700 cursor-pointer">{{ obs }}</label>
              </div>
            </div>
          </div>

          <div class="border-t pt-4 space-y-2">
            <p class="text-sm text-gray-600"><strong>L1. Fundal Height:</strong> {{ measurements.fundalHeight }}</p>
            <p class="text-sm text-gray-600"><strong>L2. Estimated fetal weight:</strong> {{ measurements.estimatedFetalWeight }}</p>
            <p class="text-sm text-gray-600"><strong>L3. Fetal Heart tone:</strong> {{ measurements.fetalHeartTone }}</p>
            <p class="text-sm text-gray-600"><strong>L4.</strong> {{ measurements.notEnlarge }}</p>
          </div>
        </div>
      </div>

      <!-- Complete Diagnostic -->
      <div class="border border-gray-300 rounded-lg p-6">
        <div class="flex items-start gap-3 mb-4">
          <span class="text-2xl">📋</span>
          <div>
            <h3 class="text-lg font-bold text-gray-900">Complete Diagnostic</h3>
            <p class="text-sm text-gray-500">Final working diagnostic and relevant notes</p>
          </div>
        </div>

        <p class="text-sm font-semibold text-gray-900 mb-3">Diagnostic notes:</p>
        <p class="text-sm text-gray-700 leading-relaxed">
          G1P0, 14 weeks AOG by LMP. Singleton intrauterine pregnancy, low risk at present. Co-existing primary hypertension anf Obesity, advise close monitoring of blood pressure and weight gain. No current signs of pre-eclampsia. Return for routine prenatal visit in 4 weeks or sooner if warning sign occur
        </p>
      </div>

    </div>

    <!-- Other Tab Content - Coming Soon -->
    <div v-else class="bg-gray-50 p-6 rounded-lg text-center">
      <p class="text-gray-600">{{ tabs.find(t => t.id === activeTab)?.name }} - Coming soon</p>
    </div>
  </div>
</template>

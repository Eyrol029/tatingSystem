<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const BASE        = 'http://localhost:8080/api'
const PRENATAL    = `${BASE}/prenatal`
const PATIENT_SVC = `${BASE}/patient-services`

// ── State ─────────────────────────────────────────────────────────────────────
const patients       = ref([])   // enriched patient objects
const loading        = ref(false)
const errorMsg       = ref('')
const filterStatus   = ref('all')
const searchQuery    = ref('')
const selectedPatient= ref(null)
const alertPatient   = ref(null)

// ── Load all data ─────────────────────────────────────────────────────────────
async function loadPatients() {
  loading.value = true
  errorMsg.value= ''
  try {
    // 1. All patients
    const pRes   = await axios.get(`${BASE}/patients`)
    const allPats= Array.isArray(pRes.data) ? pRes.data : []

    // 2. For each patient: get their PatientServices → filter Prenatal
    //    then get PrenatalRecord → VitalSigns → HighRisk flag
    const enriched = await Promise.all(allPats.map(async (p) => {
      try {
        const svcRes  = await axios.get(`${PATIENT_SVC}/patient/${p.patientID}`)
        const services= Array.isArray(svcRes.data) ? svcRes.data : []
        const prenatalSvcs = services.filter(
          s => s.serviceName?.toLowerCase() === 'prenatal'
        )

        if (!prenatalSvcs.length) return null  // not a prenatal patient

        // Get the latest prenatal record across all prenatal services
        let latestRecord = null
        for (const svc of prenatalSvcs) {
          try {
            const recRes = await axios.get(`${PRENATAL}/records/service/${svc.patientServiceID}`)
            const recs   = Array.isArray(recRes.data) ? recRes.data : []
            const latest = recs.reduce((a, b) =>
              (!a || b.prenatalrecordID > a.prenatalrecordID) ? b : a, null)
            if (latest && (!latestRecord || latest.prenatalrecordID > latestRecord.prenatalrecordID))
              latestRecord = latest
          } catch {}
        }

        if (!latestRecord) return null

        const prenatalRecordID = latestRecord.prenatalrecordID

        // 3. Clinical history → medsurg → measurements (vitals)
        let vitals         = null
        let medHistory     = []
        let obstetricRisks = []

        try {
          const clinRes = await axios.get(`${PRENATAL}/clinicalhistory/prenatal/${prenatalRecordID}`)
          const clinList= Array.isArray(clinRes.data) ? clinRes.data : []
          const clin    = clinList[0]

          if (clin) {
            const clinID = clin.clinicalHistoryID

            // Obstetric risk factors
            try {
              const obsRes = await axios.get(`${PRENATAL}/obstetric-risk-factor/clinical/${clinID}`)
              const obs    = Array.isArray(obsRes.data) ? obsRes.data[0] : null
              if (obs) {
                const riskFields = {
                  multiplePregnancy:    'Multiple Pregnancy',
                  ovarianCyst:          'Ovarian Cyst',
                  myomaUteri:           'Myoma Uteri',
                  thyroidDisorder:      'Thyroid Disorder',
                  historyOfMiscarriage: 'History of Miscarriage',
                  preEclampsia:         'Pre-Eclampsia',
                  prematureContraction: 'Premature Contraction',
                  stillbirth:           'Stillbirth'
                }
                obstetricRisks = Object.entries(riskFields)
                  .filter(([key]) => obs[key])
                  .map(([, label]) => label)
              }
            } catch {}

            // Medical conditions
            try {
              const msRes  = await axios.get(`${PRENATAL}/medical-surgical-factors/clinical/${clinID}`)
              const msList = Array.isArray(msRes.data) ? msRes.data : []
              const ms     = msList[0]
              if (ms) {
                const condsRes = await axios.get(`${PRENATAL}/medsurg-conditions/medsurg/${ms.medsurgID}`)
                const conds    = Array.isArray(condsRes.data) ? condsRes.data[0] : null
                if (conds) {
                  const condFields = {
                    hypertension:          'Hypertension',
                    heartDisease:          'Heart Disease',
                    diabetes:              'Diabetes',
                    thyroidDisorder:       'Thyroid Disorder',
                    obesity:               'Obesity',
                    moderateToSevereAsthma:'Moderate to Severe Asthma',
                    epilepsy:              'Epilepsy',
                    renalDisease:          'Renal Disease',
                    bleedingDisorder:      'Bleeding Disorder',
                    previousCesarianSection:'Previous CS',
                    historyOfMyomectomy:   'History of Myomectomy'
                  }
                  medHistory = Object.entries(condFields)
                    .filter(([key]) => conds[key])
                    .map(([, label]) => label)
                }

                // Measurements → vitals (FHT, fundal height)
                try {
                  const measRes  = await axios.get(`${PRENATAL}/medsurg-measurements/medsurg/${ms.medsurgID}`)
                  const measList = Array.isArray(measRes.data) ? measRes.data : []
                  const meas     = measList[0]
                  if (meas) {
                    vitals = {
                      fetalHeartTone: meas.fetalHeartTone,
                      fundalHeight:   meas.fundalWeight,
                      estFetalWeight: meas.estimatedFetalWeight
                    }
                  }
                } catch {}
              }
            } catch {}
          }
        } catch {}

        // 4. Follow-up vital signs (BP from latest visit)
        let latestBP      = null
        let latestFHT     = null
        let latestVisitDate = latestRecord.prenatalExamDate || null

        try {
          const consultRes = await axios.get(`${PRENATAL}/consultationrecord/prenatal/${prenatalRecordID}`)
          const consults   = Array.isArray(consultRes.data) ? consultRes.data : []
          const consult    = consults[0]
          if (consult) {
            const vsRes = await axios.get(`${PRENATAL}/vital-signs/consultation/${consult.consultationRecordID}`)
            const vsList= Array.isArray(vsRes.data) ? vsRes.data : []
            if (vsList.length) {
              const lastVS    = vsList[vsList.length - 1]
              latestBP        = lastVS.bloodPressure   || null
              latestFHT       = lastVS.fetalHeartTone  || null
              latestVisitDate = lastVS.nextVisitDate   || latestVisitDate

              // Merge vital signs
              vitals = {
                ...vitals,
                bp:           lastVS.bloodPressure,
                fetalHeartTone: lastVS.fetalHeartTone || vitals?.fetalHeartTone,
                presentation: lastVS.fetalPresentation,
                weight:       lastVS.maternalWeight
              }
            }
          }
        } catch {}

        // 5. Backend high-risk assessment
        let backendHighRisk   = false
        let backendRiskReasons= []
        try {
          const riskRes = await axios.get(`${BASE}/prenatal/high-risk-assessment/${prenatalRecordID}`)
          backendHighRisk    = riskRes.data?.highRisk    ?? false
          backendRiskReasons = riskRes.data?.reasons     ?? []
        } catch {}

        // 6. Frontend live high-risk check
        const frontendHighRisk = checkFrontendHighRisk(vitals, obstetricRisks, medHistory, latestRecord)

        const isHighRisk = backendHighRisk || frontendHighRisk

        // Parse AOG from edc/lmp
        const aog = computeAOG(latestRecord.edc || latestRecord.expectedDeliveryDate)

        return {
          id:              String(p.patientID),
          patientID:       p.patientID,
          prenatalRecordID,
          name:            buildName(p),
          age:             p.age ?? computeAge(p.birthDate || p.dob || p.dateOfBirth),
          contact:         p.contactNo || p.contact || '—',
          gestationalWeek: aog,
          vitals:          vitals || {},
          riskStatus:      isHighRisk ? 'high-risk' : 'normal',
          backendRiskReasons,
          lastVisit:       formatRawDate(latestVisitDate),
          medicalHistory:  [...medHistory, ...obstetricRisks],
          deliveryType:    latestRecord.typeOfDelivery   || null,
          referralNeeded:  latestRecord.referralHospitalNeeded || false
        }
      } catch (e) {
        console.error('Error enriching patient', p.patientID, e)
        return null
      }
    }))

    // Remove non-prenatal patients
    patients.value = enriched.filter(Boolean)
  } catch (e) {
    errorMsg.value = 'Failed to load patients: ' + (e?.response?.data?.message ?? e.message)
  } finally {
    loading.value = false
  }
}

// ── Frontend live high-risk logic ─────────────────────────────────────────────
function checkFrontendHighRisk(vitals, obstetricRisks, medHistory, record) {
  if (obstetricRisks.length > 0) return true
  if (medHistory.length > 0)     return true
  if (record?.referralHospitalNeeded) return true

  if (vitals?.bp) {
    const parts = String(vitals.bp).split('/')
    if (parts.length === 2) {
      const s = parseInt(parts[0]), d = parseInt(parts[1])
      if (!isNaN(s) && !isNaN(d) && (s >= 140 || d >= 90)) return true
    }
  }
  if (vitals?.fetalHeartTone) {
    const fht = parseInt(vitals.fetalHeartTone)
    if (!isNaN(fht) && (fht < 110 || fht > 160)) return true
  }
  if (vitals?.presentation) {
    const p = String(vitals.presentation).toLowerCase()
    if (['breech','transverse','oblique','face','brow'].some(k => p.includes(k))) return true
  }
  return false
}

// ── Helpers ───────────────────────────────────────────────────────────────────
function buildName(p) {
  const f = p.fName || p.fname || ''
  const m = p.middleI || p.middleName || ''
  const l = p.lName  || p.lname  || ''
  return [f, m, l].filter(Boolean).join(' ') || 'Unknown'
}

function computeAge(dob) {
  if (!dob) return null
  const d = new Date(dob)
  if (isNaN(d.getTime())) return null
  return Math.floor((Date.now() - d.getTime()) / (365.25 * 24 * 3600 * 1000))
}

function computeAOG(edcRaw) {
  if (!edcRaw) return null
  const edc = Array.isArray(edcRaw)
    ? new Date(edcRaw[0], edcRaw[1]-1, edcRaw[2])
    : new Date(String(edcRaw).substring(0,10))
  if (isNaN(edc.getTime())) return null
  const daysLeft = Math.floor((edc - Date.now()) / (24*3600*1000))
  const aog = 40 - Math.round(daysLeft / 7)
  return aog > 0 && aog <= 45 ? aog : null
}

function formatRawDate(val) {
  if (!val) return '—'
  const d = Array.isArray(val)
    ? new Date(val[0], val[1]-1, val[2])
    : new Date(String(val).substring(0,10))
  if (isNaN(d.getTime())) return String(val)
  return d.toLocaleDateString('en-US', { year:'numeric', month:'short', day:'numeric' })
}

function parseBP(bp) {
  if (!bp) return { s: null, d: null }
  const parts = String(bp).split('/')
  if (parts.length < 2) return { s: null, d: null }
  return { s: parseInt(parts[0]), d: parseInt(parts[1]) }
}

// ── Computed ──────────────────────────────────────────────────────────────────
const highRiskCount = computed(() => patients.value.filter(p => p.riskStatus === 'high-risk').length)
const normalCount   = computed(() => patients.value.filter(p => p.riskStatus === 'normal').length)

const filteredPatients = computed(() => {
  let list = patients.value
  if (filterStatus.value !== 'all')
    list = list.filter(p => p.riskStatus === filterStatus.value)
  const q = (searchQuery.value || '').trim().toLowerCase()
  if (!q) return list
  return list.filter(p =>
    `${p.name} ${p.contact} ${p.id}`.toLowerCase().includes(q)
  )
})

// ── Actions ───────────────────────────────────────────────────────────────────
function handleViewPatientDetails(patient) { selectedPatient.value = patient }
function handleGenerateReferralClick(patient) { alertPatient.value = patient }
function handleCloseAlert() { alertPatient.value = null }
function handleViewDetails(id) {
  const p = patients.value.find(p => p.id === id)
  if (p) { alertPatient.value = null; selectedPatient.value = p }
}

function handleGenerateReferral(patientId) {
  const p = patients.value.find(p => p.id === patientId)
  if (p) {
    try { localStorage.setItem('referral_patient', JSON.stringify(p)) } catch {}
    router.push({ path: '/uikit/ReferralForm', query: { patient: JSON.stringify(p) } })
  }
  alertPatient.value  = null
  selectedPatient.value = null
}

function getRiskStatusClass(status) {
  return status === 'high-risk'
    ? 'bg-red-100 text-red-800 px-4 py-2 rounded-full text-sm font-semibold'
    : 'bg-green-100 text-green-800 px-4 py-2 rounded-full text-sm font-semibold'
}

onMounted(loadPatients)
</script>

<template>
  <div class="min-h-screen bg-gray-50">

    <!-- Header -->
    <div class="bg-white border-b border-gray-200 shadow-sm">
      <div class="max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <button @click="router.push('/uikit/PatientsMain')"
              class="text-purple-700 hover:text-purple-900 font-bold flex items-center gap-1.5 transition mb-3">
              ← Back to Patient Management
            </button>
            <h1 class="text-3xl font-bold text-gray-900">Patient Dashboard</h1>
            <p class="text-gray-500 mt-1">Prenatal patients pulled from medical records</p>
          </div>
          <div class="flex items-center gap-4 flex-wrap">
            <div class="bg-red-50 border border-red-200 rounded-lg px-4 py-3 flex items-center gap-2">
              <span class="text-2xl font-bold text-red-600">{{ highRiskCount }}</span>
              <span class="text-sm text-gray-600">High-Risk</span>
            </div>
            <div class="bg-green-50 border border-green-200 rounded-lg px-4 py-3 flex items-center gap-2">
              <span class="text-2xl font-bold text-green-600">{{ normalCount }}</span>
              <span class="text-sm text-gray-600">Normal</span>
            </div>
            <button @click="loadPatients" :disabled="loading"
              class="px-4 py-2 bg-white border border-gray-300 rounded-lg text-sm hover:bg-gray-50 flex items-center gap-1 transition">
              <span :class="loading ? 'animate-spin inline-block' : ''">↻</span> Refresh
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Main -->
    <div class="max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

      <!-- Error -->
      <div v-if="errorMsg" class="mb-4 p-4 bg-red-50 border border-red-200 text-red-700 rounded-lg flex justify-between">
        {{ errorMsg }}
        <button @click="errorMsg=''" class="font-bold ml-3">✕</button>
      </div>

      <!-- Filters -->
      <div class="mb-6 flex flex-wrap items-center gap-4">
        <button v-for="f in [
          { id:'all',       label:'All Patients',  cls:'bg-blue-600 text-white',   dflt:'bg-white border border-gray-300 text-gray-700 hover:border-blue-300' },
          { id:'high-risk', label:'High-Risk Only', cls:'bg-red-600 text-white',    dflt:'bg-white border border-gray-300 text-gray-700 hover:border-red-300'  },
          { id:'normal',    label:'Normal Only',    cls:'bg-green-600 text-white',  dflt:'bg-white border border-gray-300 text-gray-700 hover:border-green-300'}
        ]" :key="f.id"
          @click="filterStatus = f.id"
          :class="['px-4 py-2 rounded-lg font-medium transition', filterStatus === f.id ? f.cls : f.dflt]">
          {{ f.label }}
        </button>
        <div class="ml-auto flex items-center gap-2">
          <input v-model="searchQuery" type="search"
            placeholder="Search name, contact or ID"
            class="px-4 py-2 border border-gray-300 rounded-lg w-64 focus:outline-none focus:ring-2 focus:ring-blue-400" />
        </div>
      </div>

      <!-- Loading skeleton -->
      <div v-if="loading" class="space-y-3">
        <div v-for="i in 4" :key="i" class="h-16 bg-gray-200 animate-pulse rounded-lg"></div>
      </div>

      <!-- Table -->
      <div v-else class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full min-w-[1100px] table-auto">
            <thead class="bg-gray-100 border-b border-gray-200">
              <tr>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800 w-1/4">Patient</th>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800">Age</th>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800">AOG</th>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800">BP / FHT</th>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800">Status</th>
                <th class="px-6 py-4 text-left text-base font-semibold text-gray-800">Last Visit</th>
                <th class="px-6 py-4 text-center text-base font-semibold text-gray-800">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="patient in filteredPatients" :key="patient.id"
                class="border-b border-gray-200 hover:bg-gray-50 transition"
                :class="patient.riskStatus === 'high-risk' ? 'border-l-4 border-l-red-400' : ''">

                <!-- Name -->
                <td class="px-6 py-5">
                  <p class="font-semibold text-gray-900">{{ patient.name }}</p>
                  <p class="text-sm text-gray-500">{{ patient.contact }}</p>
                </td>

                <!-- Age -->
                <td class="px-6 py-5 text-gray-700">
                  {{ patient.age != null ? patient.age + ' yrs' : '—' }}
                </td>

                <!-- AOG -->
                <td class="px-6 py-5 text-gray-700">
                  {{ patient.gestationalWeek != null ? 'Week ' + patient.gestationalWeek : '—' }}
                </td>

                <!-- BP / FHT -->
                <td class="px-6 py-5">
                  <div class="text-sm space-y-1">
                    <!-- BP with live highlight -->
                    <p v-if="patient.vitals.bp">
                      <span :class="(() => {
                        const {s,d} = parseBP(patient.vitals.bp)
                        return (s >= 140 || d >= 90) ? 'text-red-600 font-bold' : 'text-gray-800'
                      })()">
                        {{ patient.vitals.bp }} mmHg
                        <span v-if="(() => { const {s,d} = parseBP(patient.vitals.bp); return s >= 140 || d >= 90 })()"
                          class="ml-1 text-xs">⚠</span>
                      </span>
                    </p>
                    <p v-else class="text-gray-400 text-xs">BP not recorded</p>

                    <!-- FHT with live highlight -->
                    <p v-if="patient.vitals.fetalHeartTone">
                      <span :class="(() => {
                        const fht = parseInt(patient.vitals.fetalHeartTone)
                        return (!isNaN(fht) && (fht < 110 || fht > 160)) ? 'text-red-600 font-bold' : 'text-gray-800'
                      })()">
                        FHT: {{ patient.vitals.fetalHeartTone }} bpm
                        <span v-if="(() => { const f = parseInt(patient.vitals.fetalHeartTone); return !isNaN(f) && (f < 110 || f > 160) })()"
                          class="ml-1 text-xs">⚠</span>
                      </span>
                    </p>
                    <p v-else class="text-gray-400 text-xs">FHT not recorded</p>
                  </div>
                </td>

                <!-- Status badge -->
                <td class="px-6 py-5">
                  <span :class="getRiskStatusClass(patient.riskStatus)">
                    {{ patient.riskStatus === 'high-risk' ? '🔴 High Risk' : '✅ Normal' }}
                  </span>
                </td>

                <!-- Last visit -->
                <td class="px-6 py-5 text-gray-700 text-sm">{{ patient.lastVisit }}</td>

                <!-- Actions -->
                <td class="px-6 py-5">
                  <div class="flex justify-center gap-3">
                    <button @click="handleViewPatientDetails(patient)"
                      class="text-blue-600 hover:text-blue-800 font-medium text-sm hover:underline">
                      View
                    </button>
                    <button v-if="patient.riskStatus === 'high-risk'"
                      @click="handleGenerateReferralClick(patient)"
                      class="text-red-600 hover:text-red-800 font-medium text-sm hover:underline">
                      Refer
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Empty state -->
        <div v-if="filteredPatients.length === 0 && !loading"
          class="text-center py-16 text-gray-400">
          <p class="text-lg">{{ patients.length === 0 ? 'No prenatal patients found.' : 'No patients match your filter.' }}</p>
        </div>

        <!-- Count footer -->
        <div v-if="filteredPatients.length > 0"
          class="px-6 py-3 bg-gray-50 border-t text-xs text-gray-400">
          Showing {{ filteredPatients.length }} of {{ patients.length }} prenatal patient(s)
        </div>
      </div>
    </div>

    <!-- ══ PATIENT DETAIL MODAL ══ -->
    <div v-if="selectedPatient"
      class="fixed inset-0 bg-black/50 z-40 flex items-center justify-center p-4"
      @click.self="selectedPatient = null">
      <div class="bg-white rounded-xl shadow-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">

        <div class="bg-blue-600 text-white p-6 rounded-t-xl flex items-center justify-between">
          <div>
            <h2 class="text-2xl font-bold">{{ selectedPatient.name }}</h2>
            <p class="text-blue-200 text-sm mt-0.5">Prenatal Record #{{ selectedPatient.prenatalRecordID }}</p>
          </div>
          <button @click="selectedPatient = null" class="text-white hover:opacity-80 text-2xl">&times;</button>
        </div>

        <div class="p-6 space-y-5">

          <!-- Basic info -->
          <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
            <div>
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Age</p>
              <p class="font-semibold text-gray-900">{{ selectedPatient.age != null ? selectedPatient.age + ' yrs' : '—' }}</p>
            </div>
            <div>
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Contact</p>
              <p class="font-semibold text-gray-900">{{ selectedPatient.contact }}</p>
            </div>
            <div>
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Gestational Week</p>
              <p class="font-semibold text-gray-900">
                {{ selectedPatient.gestationalWeek != null ? 'Week ' + selectedPatient.gestationalWeek : '—' }}
              </p>
            </div>
            <div>
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Status</p>
              <span :class="getRiskStatusClass(selectedPatient.riskStatus)">
                {{ selectedPatient.riskStatus === 'high-risk' ? '🔴 High Risk' : '✅ Normal' }}
              </span>
            </div>
            <div>
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Last Visit</p>
              <p class="font-semibold text-gray-900">{{ selectedPatient.lastVisit }}</p>
            </div>
            <div v-if="selectedPatient.deliveryType">
              <p class="text-xs text-gray-500 uppercase font-semibold mb-0.5">Delivery Type</p>
              <p class="font-semibold text-gray-900">{{ selectedPatient.deliveryType }}</p>
            </div>
          </div>

          <!-- Vitals -->
          <div>
            <p class="text-sm font-bold text-gray-700 mb-2 uppercase tracking-wider">Vitals</p>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-3">
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Blood Pressure</p>
                <p class="font-bold text-sm"
                  :class="(() => { const {s,d} = parseBP(selectedPatient.vitals.bp); return (s>=140||d>=90) ? 'text-red-600' : 'text-gray-800' })()">
                  {{ selectedPatient.vitals.bp || '—' }}
                  <span v-if="(() => { const {s,d} = parseBP(selectedPatient.vitals.bp); return s>=140||d>=90 })()"> ⚠</span>
                </p>
              </div>
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Fetal Heart Tone</p>
                <p class="font-bold text-sm"
                  :class="(() => { const f=parseInt(selectedPatient.vitals.fetalHeartTone); return (!isNaN(f)&&(f<110||f>160)) ? 'text-red-600' : 'text-gray-800' })()">
                  {{ selectedPatient.vitals.fetalHeartTone ? selectedPatient.vitals.fetalHeartTone + ' bpm' : '—' }}
                </p>
              </div>
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Fundal Height</p>
                <p class="font-bold text-sm text-gray-800">
                  {{ selectedPatient.vitals.fundalHeight ? selectedPatient.vitals.fundalHeight + ' cm' : '—' }}
                </p>
              </div>
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Est. Fetal Weight</p>
                <p class="font-bold text-sm text-gray-800">
                  {{ selectedPatient.vitals.estFetalWeight ? selectedPatient.vitals.estFetalWeight + ' g' : '—' }}
                </p>
              </div>
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Presentation</p>
                <p class="font-bold text-sm"
                  :class="selectedPatient.vitals.presentation && ['breech','transverse','oblique'].some(k => selectedPatient.vitals.presentation.toLowerCase().includes(k)) ? 'text-red-600' : 'text-gray-800'">
                  {{ selectedPatient.vitals.presentation || '—' }}
                </p>
              </div>
              <div class="bg-gray-50 p-3 rounded-lg border">
                <p class="text-xs text-gray-500">Weight</p>
                <p class="font-bold text-sm text-gray-800">
                  {{ selectedPatient.vitals.weight ? selectedPatient.vitals.weight + ' kg' : '—' }}
                </p>
              </div>
            </div>
          </div>

          <!-- Medical history -->
          <div v-if="selectedPatient.medicalHistory.length">
            <p class="text-sm font-bold text-gray-700 mb-2 uppercase tracking-wider">Medical / Obstetric History</p>
            <div class="flex flex-wrap gap-2">
              <span v-for="(h, i) in selectedPatient.medicalHistory" :key="i"
                class="text-xs bg-red-50 text-red-700 border border-red-200 px-2 py-1 rounded-full font-medium">
                {{ h }}
              </span>
            </div>
          </div>

          <!-- Backend risk reasons -->
          <div v-if="selectedPatient.backendRiskReasons.length" class="bg-red-50 border border-red-200 rounded-lg p-4">
            <p class="text-sm font-bold text-red-700 mb-2">🚨 Risk Factors (Server Assessment)</p>
            <ul class="space-y-1">
              <li v-for="(r, i) in selectedPatient.backendRiskReasons" :key="i"
                class="text-sm text-red-700 flex items-start gap-1">
                <span class="mt-0.5">•</span> {{ r }}
              </li>
            </ul>
          </div>

          <!-- Actions -->
          <div class="flex gap-3 pt-2 border-t">
            <button @click="selectedPatient = null"
              class="flex-1 bg-gray-100 hover:bg-gray-200 text-gray-800 font-semibold py-2 px-4 rounded-lg transition">
              Close
            </button>
            <button v-if="selectedPatient.riskStatus === 'high-risk'"
              @click="handleGenerateReferral(selectedPatient.id)"
              class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg transition">
              Generate Referral
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- ══ ALERT MODAL ══ -->
    <div v-if="alertPatient"
      class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4"
      @click.self="handleCloseAlert">
      <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6">
        <div class="text-center mb-5">
          <div class="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-3">
            <span class="text-3xl">⚠️</span>
          </div>
          <h3 class="text-xl font-bold text-gray-900 mb-1">High-Risk Patient Alert</h3>
          <p class="text-gray-500">{{ alertPatient.name }} requires immediate attention</p>
          <div v-if="alertPatient.backendRiskReasons.length" class="mt-3 text-left bg-red-50 rounded-lg p-3">
            <p class="text-xs font-bold text-red-700 mb-1">Risk Reasons:</p>
            <p v-for="(r,i) in alertPatient.backendRiskReasons.slice(0,3)" :key="i"
              class="text-xs text-red-600">• {{ r }}</p>
            <p v-if="alertPatient.backendRiskReasons.length > 3"
              class="text-xs text-red-400 mt-1">
              +{{ alertPatient.backendRiskReasons.length - 3 }} more…
            </p>
          </div>
        </div>
        <div class="flex gap-2">
          <button @click="handleCloseAlert"
            class="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 px-3 rounded-lg transition text-sm">
            Close
          </button>
          <button @click="handleViewDetails(alertPatient.id)"
            class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-3 rounded-lg transition text-sm">
            View Details
          </button>
          <button @click="handleGenerateReferral(alertPatient.id)"
            class="flex-1 bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-3 rounded-lg transition text-sm">
            Generate Referral
          </button>
        </div>
      </div>
    </div>

  </div>
</template>
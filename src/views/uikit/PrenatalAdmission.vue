<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useUserDataStore, UserRole } from '@/stores/userData'

const route = useRoute()
const router = useRouter()
const BASE = 'http://localhost:8080/api/prenatal'
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services'

// ── Read-only mode for Patient accounts ──────────────────────────────────────
// Patients can view and print this form but cannot type/check/select anything.
// Wrapping the form fields in a <fieldset :disabled="isReadOnly"> below handles
// this for every input/select/textarea/checkbox at once.
const userStore = useUserDataStore()
if (!userStore.user) {
  userStore.init()
}
const isReadOnly = computed(() => userStore.userRole === UserRole.PATIENT)

// ─── Get both clientId and serviceId from route ───────────────────────────────
const clientId  = route.params.clientId
const serviceId = route.params.serviceId  // ← specific service record

function goBack() { router.back() }
function handleBeforePrint() { document.body.classList.add('printing-prenatal') }
function handleAfterPrint()  { document.body.classList.remove('printing-prenatal') }

// ─── State ────────────────────────────────────────────────────────────────────
const serviceID    = ref(null)
const prenatalID   = ref(null)
const submitStatus = ref({ loading: false, error: '', success: '' })

const riskResult   = ref(null)
const riskLoading  = ref(false)
const employeesList = ref([])

const onAttendingStaffChange = () => {
  const staffID = form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null
  const staff = employeesList.value.find(e => Number(e.employeeID) === staffID)
  form.value.attendingMidwife = staff ? `${staff.fName || ''} ${staff.lName || ''}`.trim() : ''
}

const form = ref({
  lmp: '', edc: '', gpal: '', attendingMidwife: '', attendingStaffID: null,
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
    abdomen:  { normoactiveBowel: false, nonTender: false, activeFetalMovement: false },
    speculum: { parousVagina: false, cervixSmoothClosed: false },
    leopold:  { fundalHeight: '', estFetalWeight: '', fht: '', l1: '', l2: '', l3: '', l4: '' }
  },
  diagnosis: '',
  deliveryPlanning: {
    prenatalExamDate: '', expectedDeliveryDate: '', place: "TATING'S BIRTHING HOME"
  },
  deliveryDetails: {
    date: '', place: '', referralHospitalNeeded: false,
    referralHospitalName: '', type: 'Normal Spontaneous Vaginal Delivery'
  },
  labs: {
    ua: '', purulentSubstance: '', rbc: '', cbc: '',
    hemoglobin: '', vdrl: '', hiv: '', ultrasound: ''
  },
  visits: Array.from({ length: 10 }, (_, i) => ({
    visit: i + 1, date: '', aog: '', fht: '', fh: '',
    position: '', presentation: '', weight: '', bp: '', nextVisit: ''
  })),
  treatment: {
    palpation: false, labExam: false, dietAdvice: false,
    vitamins: false, referred: false
  }
})

// ─── Reactive high-risk computeds ─────────────────────────────────────────────
const obstetricHighRisk = computed(() =>
  Object.values(form.value.obstetricRisk).some(Boolean)
)
const medicalHighRisk = computed(() =>
  Object.values(form.value.medical).some(Boolean)
)
const leopoldFhtHighRisk = computed(() => {
  const fht = Number(form.value.factors.leopold.fht)
  return fht > 0 && (fht < 110 || fht > 160)
})
const labHighRisk = computed(() => {
  const v = form.value.labs
  const isPositive = (s) => {
    if (!s) return false
    const l = s.toLowerCase()
    return l.includes('positive') || l.includes('pos') || l === '+' || l.includes('reactive')
  }
  const hgb = parseFloat(v.hemoglobin)
  return isPositive(v.vdrl) || isPositive(v.hiv) || (!isNaN(hgb) && hgb < 11.0)
})
const referralHighRisk = computed(() => form.value.deliveryDetails.referralHospitalNeeded)
const visitHighRisk = computed(() => {
  return form.value.visits.some(v => {
    if (v.bp) {
      const parts = v.bp.split('/')
      if (parts.length === 2) {
        const s = parseInt(parts[0]), d = parseInt(parts[1])
        if (!isNaN(s) && !isNaN(d) && (s >= 140 || d >= 90)) return true
      }
    }
    if (v.fht) {
      const fht = parseInt(v.fht)
      if (!isNaN(fht) && (fht < 110 || fht > 160)) return true
    }
    if (v.presentation) {
      const p = v.presentation.toLowerCase()
      if (['breech','transverse','oblique','face','brow','shoulder'].some(k => p.includes(k))) return true
    }
    return false
  })
})

// ─── Ultrasound tag picker ────────────────────────────────────────────────────
const HIGH_RISK_US_TAGS = ['Placenta Previa', 'Oligohydramnios', 'Fetal Anomaly', 'IUGR']
const ultrasoundTags = ref([])

function toggleUltrasoundTag(tag) {
  if (isReadOnly.value) return
  const idx = ultrasoundTags.value.indexOf(tag)
  if (idx === -1) { ultrasoundTags.value.push(tag) }
  else { ultrasoundTags.value.splice(idx, 1) }
  form.value.labs.ultrasound = ultrasoundTags.value.join(', ')
}

const ultrasoundHighRisk = computed(() =>
  ultrasoundTags.value.some(t => HIGH_RISK_US_TAGS.includes(t))
)

const isLiveHighRisk = computed(() =>
  obstetricHighRisk.value || medicalHighRisk.value || leopoldFhtHighRisk.value ||
  labHighRisk.value || ultrasoundHighRisk.value || referralHighRisk.value || visitHighRisk.value
)

// ─── Per-visit row risk helpers ───────────────────────────────────────────────
function isVisitBpHigh(v) {
  if (!v.bp) return false
  const parts = v.bp.split('/')
  if (parts.length < 2) return false
  const s = parseInt(parts[0]), d = parseInt(parts[1])
  return !isNaN(s) && !isNaN(d) && (s >= 140 || d >= 90)
}
function isVisitFhtAbnormal(v) {
  if (!v.fht) return false
  const fht = parseInt(v.fht)
  return !isNaN(fht) && (fht < 110 || fht > 160)
}
function isVisitPresentationAbnormal(v) {
  if (!v.presentation) return false
  const p = v.presentation.toLowerCase()
  return ['breech','transverse','oblique','face','brow','shoulder'].some(k => p.includes(k))
}
function isVisitRowHighRisk(v) {
  return isVisitBpHigh(v) || isVisitFhtAbnormal(v) || isVisitPresentationAbnormal(v)
}

// ── Load the employees/staff list for the Attending Staff dropdown ──────────
// Defensive: normalizes a few common response shapes (raw array, {data:[...]},
// {employees:[...]}) so an empty dropdown is easy to diagnose.
async function fetchEmployees() {
  try {
    const res = await axios.get('http://localhost:8080/api/employees')
    const raw = res.data
    employeesList.value = Array.isArray(raw)
      ? raw
      : (Array.isArray(raw?.data) ? raw.data
        : (Array.isArray(raw?.employees) ? raw.employees : []))

    if (!employeesList.value.length) {
      console.warn('Employees list loaded but is empty. Raw response:', raw)
    }
  } catch (error) {
    console.error('Failed to load employees list', error)
    submitStatus.value.error = '⚠️ Could not load staff list: ' + (error?.response?.status || error.message)
  }
}

// ─── Lifecycle ────────────────────────────────────────────────────────────────
onMounted(async () => {
  window.addEventListener('beforeprint', handleBeforePrint)
  window.addEventListener('afterprint',  handleAfterPrint)

  await fetchEmployees()

  if (serviceId) {
    // ✅ KEY CHANGE: directly use the serviceId from URL
    // This ensures each service record loads its OWN data only
    serviceID.value = Number(serviceId)
    await loadExistingPrenatalData(serviceID.value)
  } else if (clientId) {
    // Fallback (should not happen with new routes)
    await ensurePrenatalService(clientId)
    if (serviceID.value) await loadExistingPrenatalData(serviceID.value)
  }
})

onUnmounted(() => {
  window.removeEventListener('beforeprint', handleBeforePrint)
  window.removeEventListener('afterprint',  handleAfterPrint)
})

// ─── High-risk assessment API call ───────────────────────────────────────────
async function fetchRiskAssessment(id) {
  if (!id) return
  riskLoading.value = true
  try {
    const res = await axios.get(`${BASE}/high-risk-assessment/${id}`)
    riskResult.value = res.data
  } catch (e) {
    console.error('Risk assessment fetch failed:', e)
  } finally {
    riskLoading.value = false
  }
}

// ─── Utilities ────────────────────────────────────────────────────────────────
function printForm() { window.print() }

function parseLocalDate(value) {
  if (!value) return null
  const dateString = String(value).trim()

  const isoMatch = dateString.match(/^(\d{4})-(\d{2})-(\d{2})$/)
  if (isoMatch) {
    return new Date(Number(isoMatch[1]), Number(isoMatch[2]) - 1, Number(isoMatch[3]))
  }

  const mdyMatch = dateString.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2,4})$/)
  if (mdyMatch) {
    let year = Number(mdyMatch[3])
    if (year < 100) {
      year += year < 70 ? 2000 : 1900
    }
    return new Date(Number(year), Number(mdyMatch[1]) - 1, Number(mdyMatch[2]))
  }

  const parsed = new Date(dateString)
  if (Number.isNaN(parsed.getTime())) {
    return null
  }
  return new Date(parsed.getFullYear(), parsed.getMonth(), parsed.getDate())
}

function formatLocalDate(value) {
  const date = parseLocalDate(value)
  if (!date) return null
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${date.getFullYear()}-${month}-${day}`
}

function normalizeDate(value) {
  return formatLocalDate(value)
}

function parseDateForForm(value) {
  return formatLocalDate(value) || ''
}

function buildPrenatalUrl(path, id) {
  if (id === undefined || id === null || id === '')
    throw new Error(`Invalid prenatalRecordID for ${path}`)
  return `${BASE}/${path}/${id}`
}

function buildGpal(value) {
  if (!value) return ''
  return [value.gravida, value.para, value.term, value.preterm, value.abortion, value.living]
    .map(v => (v === null || v === undefined) ? '' : String(v)).join('-')
}

// ─── Load ─────────────────────────────────────────────────────────────────────
async function ensurePrenatalService(clientId) {
  try {
    const res = await axios.get(`${PATIENT_SERVICE_BASE}/patient/${clientId}`)
    const prenatalServices = Array.isArray(res.data)
      ? res.data.filter(s => s.serviceName?.toLowerCase() === 'prenatal')
      : []
    if (prenatalServices.length > 0) {
      serviceID.value = prenatalServices[prenatalServices.length - 1].patientServiceID
      return
    }
    const createRes = await axios.post(PATIENT_SERVICE_BASE, {
      patientID: Number(clientId),
      serviceName: 'Prenatal',
      employeeName: '---',
      wardName: '---',
      dateAvailed: new Date().toISOString().split('T')[0],
      remarks: ''
    })
    serviceID.value = createRes.data.patientServiceID
  } catch (e) { console.error('Failed to load or create prenatal service', e) }
}

async function loadExistingPrenatalData(sid) {
  try {
    // ✅ Load records linked to THIS specific serviceID only
    const res = await axios.get(`${BASE}/records/service/${sid}`)
    const records = Array.isArray(res.data) ? res.data : []
    if (!records.length) return

    const latestRecord = records.reduce((latest, current) =>
      (!latest || current.prenatalrecordID > latest.prenatalrecordID) ? current : latest, null)
    if (!latestRecord) return

    prenatalID.value = latestRecord.prenatalrecordID

    form.value.deliveryPlanning.prenatalExamDate    = parseDateForForm(latestRecord.prenatalExamDate)
    form.value.deliveryPlanning.expectedDeliveryDate = parseDateForForm(latestRecord.expectedDeliveryDate)
    form.value.edc                                  = parseDateForForm(latestRecord.edc)
    form.value.deliveryDetails.date                 = parseDateForForm(latestRecord.deliveryDate)
    form.value.deliveryDetails.place                = latestRecord.placeOfDelivery || ''
    form.value.deliveryDetails.type                 = latestRecord.typeOfDelivery || ''
    form.value.deliveryDetails.referralHospitalNeeded = !!latestRecord.referralHospitalNeeded
    form.value.deliveryDetails.referralHospitalName   = latestRecord.referralHospitalName || ''

    // Restore attending staff selection, matching against the loaded employee list.
    // Coerce to Number on both sides — the API may return attendingStaffID (and/or
    // employeeID) as a string, which would otherwise fail the select's v-model match.
    if (latestRecord.attendingStaffID != null && latestRecord.attendingStaffID !== '') {
      const staffID = Number(latestRecord.attendingStaffID)
      form.value.attendingStaffID = staffID
      const staff = employeesList.value.find(e => Number(e.employeeID) === staffID)
      form.value.attendingMidwife = staff ? `${staff.fName || ''} ${staff.lName || ''}`.trim() : (latestRecord.attendingMidwife || '')
    } else {
      form.value.attendingStaffID = null
      form.value.attendingMidwife = latestRecord.attendingMidwife || ''
    }

    await loadClinicalHistoryData(latestRecord.prenatalrecordID)
    await loadPrenatalDetailData(latestRecord.prenatalrecordID)
    await fetchRiskAssessment(latestRecord.prenatalrecordID)
  } catch (e) { console.error('Load existing prenatal data error:', e) }
}

async function loadClinicalHistoryData(prenatalrecordID) {
  try {
    const clinicalRes = await axios.get(buildPrenatalUrl('clinicalhistory/prenatal', prenatalrecordID))
    const clinicalList = Array.isArray(clinicalRes.data) ? clinicalRes.data : []
    const clinical = clinicalList[0]
    if (!clinical) return

    const clinicalHistoryID = clinical.clinicalHistoryID
    const [menstrualRes, obstetricRes, medsurgRes, diagnosisRes] = await Promise.all([
      axios.get(`${BASE}/menstrual/clinical/${clinicalHistoryID}`),
      axios.get(`${BASE}/obstetric-risk-factor/clinical/${clinicalHistoryID}`),
      axios.get(`${BASE}/medical-surgical-factors/clinical/${clinicalHistoryID}`),
      axios.get(`${BASE}/complete-diagnosis/clinical/${clinicalHistoryID}`)
    ])

    const menstrual = Array.isArray(menstrualRes.data) ? menstrualRes.data[0] : null
    if (menstrual) {
      form.value.lmp  = parseDateForForm(menstrual.LMP)
      form.value.gpal = buildGpal(menstrual)
    }

    const obstetric = Array.isArray(obstetricRes.data) ? obstetricRes.data[0] : null
    if (obstetric) {
      form.value.obstetricRisk.multiplePregnancy    = !!obstetric.multiplePregnancy
      form.value.obstetricRisk.ovarianCyst          = !!obstetric.ovarianCyst
      form.value.obstetricRisk.myomaUteri           = !!obstetric.myomaUteri
      form.value.obstetricRisk.thyroidDisorder      = !!obstetric.thyroidDisorder
      form.value.obstetricRisk.miscarriage          = !!obstetric.historyOfMiscarriage
      form.value.obstetricRisk.preeclampsia         = !!obstetric.preEclampsia
      form.value.obstetricRisk.eclampsia            = !!obstetric.preEclampsia
      form.value.obstetricRisk.prematureContraction = !!obstetric.prematureContraction
    }

    const medsurg = Array.isArray(medsurgRes.data) ? medsurgRes.data[0] : null
    if (medsurg) {
      const medsurgID = medsurg.medsurgID
      const [condsRes, measurementsRes] = await Promise.all([
        axios.get(`${BASE}/medsurg-conditions/medsurg/${medsurgID}`),
        axios.get(`${BASE}/medsurg-measurements/medsurg/${medsurgID}`)
      ])
      const conds = Array.isArray(condsRes.data) ? condsRes.data[0] : null
      if (conds) {
        form.value.medical.hypertension    = !!conds.hypertension
        form.value.medical.heartDisease    = !!conds.heartDisease
        form.value.medical.diabetes        = !!conds.diabetes
        form.value.medical.thyroidDisorder = !!conds.thyroidDisorder
        form.value.medical.obesity         = !!conds.obesity
        form.value.medical.asthma          = !!conds.moderateToSevereAsthma
        form.value.medical.epilepsy        = !!conds.epilepsy
        form.value.medical.renalDisease    = !!conds.renalDisease
        form.value.medical.bleedingDisorder= !!conds.bleedingDisorder
        form.value.medical.previousCS      = !!conds.previousCesarianSection
        form.value.medical.myomectomy      = !!conds.historyOfMyomectomy
      }
      const measurements = Array.isArray(measurementsRes.data) ? measurementsRes.data[0] : null
      if (measurements) {
        form.value.factors.abdomen.normoactiveBowel    = !!measurements.normoActiveBowelSounds
        form.value.factors.abdomen.nonTender           = !!measurements.nonTender
        form.value.factors.abdomen.activeFetalMovement = !!measurements.activeFetalMovement
        form.value.factors.speculum.parousVagina       = !!measurements.parousVagina
        form.value.factors.speculum.cervixSmoothClosed = !!measurements.cervixSmoothClosed
        form.value.factors.leopold.fundalHeight   = measurements.fundalWeight != null ? String(measurements.fundalWeight) : ''
        form.value.factors.leopold.estFetalWeight = measurements.estimatedFetalWeight != null ? String(measurements.estimatedFetalWeight) : ''
        form.value.factors.leopold.fht            = measurements.fetalHeartTone != null ? String(measurements.fetalHeartTone) : ''
        form.value.factors.leopold.l1             = measurements.leopoldL1 || ''
        form.value.factors.leopold.l2             = measurements.leopoldL2 || ''
        form.value.factors.leopold.l3             = measurements.leopoldL3 || ''
        form.value.factors.leopold.l4             = measurements.leopoldL4 || ''
      }
    }

    const diagnosis = Array.isArray(diagnosisRes.data) ? diagnosisRes.data[0] : null
    if (diagnosis) form.value.diagnosis = diagnosis.diagnosis || ''
  } catch (e) { console.error('Load clinical history data error:', e) }
}

async function loadPrenatalDetailData(prenatalrecordID) {
  try {
    const [labRes, treatmentRes, consultationRes] = await Promise.all([
      axios.get(`${BASE}/laboratory-results/prenatal/${prenatalrecordID}`),
      axios.get(`${BASE}/treatment-management/prenatal/${prenatalrecordID}`),
      axios.get(`${BASE}/consultationrecord/prenatal/${prenatalrecordID}`)
    ])

    const labRecords = Array.isArray(labRes.data) ? labRes.data : []
    const lab = labRecords.sort((a, b) => (b.laboratoryResultID || 0) - (a.laboratoryResultID || 0))[0] || null
    if (lab) {
      form.value.labs.ua                = lab.urinalysis || ''
      form.value.labs.purulentSubstance = lab.pusCells || ''
      form.value.labs.rbc               = lab.redBloodCells || ''
      form.value.labs.cbc               = lab.completeBloodCount || ''
      form.value.labs.hemoglobin        = lab.hemoglobin != null ? String(lab.hemoglobin) : ''
      form.value.labs.vdrl              = lab.venerealDiseaseResearchLaboratoryTest || ''
      form.value.labs.hiv               = lab.humanImmunodeficiencyVirusTest || ''
      form.value.labs.ultrasound        = lab.ultrasoundResult || ''
    }

    const treatmentRecords = Array.isArray(treatmentRes.data) ? treatmentRes.data : []
    const treatment = treatmentRecords.sort((a, b) => (b.treatmentID || 0) - (a.treatmentID || 0))[0] || null
    if (treatment) {
      form.value.treatment.palpation  = !!treatment.palpitationDone
      form.value.treatment.labExam    = !!treatment.referToRoutineLabExams
      form.value.treatment.dietAdvice = !!treatment.advisedDiet
      form.value.treatment.vitamins   = !!treatment.advisedVitaminIntake
      form.value.treatment.referred   = Boolean(treatment.referralDoctor)
    }

    const consultationRecords = Array.isArray(consultationRes.data) ? consultationRes.data : []
    const consultation = consultationRecords.find(r => r.prenatalRecordID === prenatalrecordID)
    if (!consultation) return

    const visitRes = await axios.get(`${BASE}/consultation-visits/consultation/${consultation.consultationRecordID}`)
    const vitalRes = await axios.get(`${BASE}/vital-signs/consultation/${consultation.consultationRecordID}`)

    const visits = Array.isArray(visitRes.data) ? visitRes.data : []
    visits.forEach(visit => {
      const index = Number(visit.visitNumber) - 1
      if (index >= 0 && index < form.value.visits.length) {
        form.value.visits[index].date = parseDateForForm(visit.visitDate)
        form.value.visits[index].aog  = visit.ageOfGestationInWeeks != null ? String(visit.ageOfGestationInWeeks) : ''
      }
    })

    const vitals = Array.isArray(vitalRes.data) ? vitalRes.data : []
    vitals.forEach((vital, index) => {
      if (index >= 0 && index < form.value.visits.length) {
        form.value.visits[index].fht          = vital.fetalHeartTone || ''
        form.value.visits[index].fh           = vital.fundalHeight || ''
        form.value.visits[index].position     = vital.maternalPosition || ''
        form.value.visits[index].presentation = vital.fetalPresentation || ''
        form.value.visits[index].weight       = vital.maternalWeight != null ? String(vital.maternalWeight) : ''
        form.value.visits[index].bp           = vital.bloodPressure || ''
        form.value.visits[index].nextVisit    = parseDateForForm(vital.nextVisitDate)
      }
    })
  } catch (e) { console.error('Load prenatal detail data error:', e) }
}

// ── Sync the assigned employee name back onto PatientService so it displays
// correctly in the "Services Availed" tables (patient profile + patient's own
// "My Services" view). PUT /api/patient-services has no /{id} path — it
// requires the FULL PatientService object in the body — so we fetch the
// current record first, merge in the new employeeName, then PUT it back.
async function syncEmployeeNameToPatientService() {
  if (!form.value.attendingMidwife || !serviceID.value) return
  try {
    const currentRes = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceID.value}`)
    const currentService = currentRes.data
    await axios.put(PATIENT_SERVICE_BASE, {
      ...currentService,
      employeeName: form.value.attendingMidwife
    })
  } catch (syncErr) {
    console.error('Failed to sync employee name to PatientService', syncErr)
  }
}

// ─── Submit ───────────────────────────────────────────────────────────────────
async function submitForm() {
  // Extra safety net — even if the button were somehow clicked while
  // read-only, block the actual save from happening.
  if (isReadOnly.value) return

  submitStatus.value.loading = true
  submitStatus.value.error   = ''
  submitStatus.value.success = ''

  // Use serviceId from URL directly
  if (!serviceID.value && serviceId) {
    serviceID.value = Number(serviceId)
  }

  if (!serviceID.value) {
    submitStatus.value.error   = 'Service ID not found. Please go back and try again.'
    submitStatus.value.loading = false
    return
  }

  const today = new Date().toISOString().split('T')[0]

  try {
    // 1. PrenatalRecord — always creates a NEW record linked to THIS serviceID
    const prenatalRes = await axios.post(`${BASE}/records`, {
      serviceID: serviceID.value,
      initialPreConsultationDate: today,
      prenatalExamDate:     normalizeDate(form.value.deliveryPlanning.prenatalExamDate),
      expectedDeliveryDate: normalizeDate(form.value.deliveryPlanning.expectedDeliveryDate),
      edc:                  normalizeDate(form.value.edc),
      deliveryDate:         normalizeDate(form.value.deliveryDetails.date),
      placeOfDelivery:      form.value.deliveryDetails.place || null,
      typeOfDelivery:       form.value.deliveryDetails.type || null,
      referralHospitalNeeded: form.value.deliveryDetails.referralHospitalNeeded,
      referralHospitalName:   form.value.deliveryDetails.referralHospitalName || null,
      attendingMidwife:     form.value.attendingMidwife || null,
      attendingStaffID:     form.value.attendingStaffID != null ? Number(form.value.attendingStaffID) : null
    })
    const prenatalrecordID = prenatalRes.data.prenatalrecordID
    prenatalID.value = prenatalrecordID

    // 2. ClinicalHistory
    const clinicalRes = await axios.post(`${BASE}/clinicalhistory`, { prenatalrecordID })
    const clinicalHistoryID = clinicalRes.data.clinicalHistoryID

    // 3. MenstrualHistory
    try {
      const gpalParts = form.value.gpal.split('-')
      await axios.post(`${BASE}/menstrual`, {
        clinicalHistoryID,
        LMP:      normalizeDate(form.value.lmp),
        gravida:  gpalParts[0] ? Number(gpalParts[0]) : null,
        para:     gpalParts[1] ? Number(gpalParts[1]) : null,
        term:     gpalParts[2] ? Number(gpalParts[2]) : null,
        preterm:  gpalParts[3] ? Number(gpalParts[3]) : null,
        abortion: gpalParts[4] ? Number(gpalParts[4]) : null,
        living:   gpalParts[5] ? Number(gpalParts[5]) : null
      })
    } catch (e) { console.error('MenstrualHistory save error:', e) }

    // 4. ObstetricRiskFactor
    try {
      await axios.post(`${BASE}/obstetric-risk-factor`, {
        clinicalHistoryID,
        multiplePregnancy:    form.value.obstetricRisk.multiplePregnancy,
        ovarianCyst:          form.value.obstetricRisk.ovarianCyst,
        myomaUteri:           form.value.obstetricRisk.myomaUteri,
        thyroidDisorder:      form.value.obstetricRisk.thyroidDisorder,
        historyOfMiscarriage: form.value.obstetricRisk.miscarriage,
        stillbirth:           false,
        preEclampsia:         form.value.obstetricRisk.preeclampsia || form.value.obstetricRisk.eclampsia,
        prematureContraction: form.value.obstetricRisk.prematureContraction
      })
    } catch (e) { console.error('ObstetricRiskFactor save error:', e) }

    // 5. MedicalSurgicalFactors
    let medsurgID = null
    try {
      const medsurgRes = await axios.post(`${BASE}/medical-surgical-factors`, { clinicalHistoryID })
      medsurgID = medsurgRes.data.medsurgID

      try {
        await axios.post(`${BASE}/medsurg-conditions`, {
          medsurgID,
          hypertension:            form.value.medical.hypertension,
          heartDisease:            form.value.medical.heartDisease,
          diabetes:                form.value.medical.diabetes,
          thyroidDisorder:         form.value.medical.thyroidDisorder,
          obesity:                 form.value.medical.obesity,
          moderateToSevereAsthma:  form.value.medical.asthma,
          epilepsy:                form.value.medical.epilepsy,
          renalDisease:            form.value.medical.renalDisease,
          bleedingDisorder:        form.value.medical.bleedingDisorder,
          previousCesarianSection: form.value.medical.previousCS,
          historyOfMyomectomy:     form.value.medical.myomectomy
        })
      } catch (e) { console.error('MedConditions save error:', e) }

      try {
        await axios.post(`${BASE}/medsurg-measurements`, {
          medsurgID,
          normoActiveBowelSounds: form.value.factors.abdomen.normoactiveBowel,
          nonTender:              form.value.factors.abdomen.nonTender,
          activeFetalMovement:    form.value.factors.abdomen.activeFetalMovement,
          parousVagina:           form.value.factors.speculum.parousVagina,
          cervixSmoothClosed:     form.value.factors.speculum.cervixSmoothClosed,
          fundalWeight:        form.value.factors.leopold.fundalHeight ? Number(form.value.factors.leopold.fundalHeight) : null,
          estimatedFetalWeight:form.value.factors.leopold.estFetalWeight ? Number(form.value.factors.leopold.estFetalWeight) : null,
          fetalHeartTone:      form.value.factors.leopold.fht ? Number(form.value.factors.leopold.fht) : null,
          leopoldL1: form.value.factors.leopold.l1 || null,
          leopoldL2: form.value.factors.leopold.l2 || null,
          leopoldL3: form.value.factors.leopold.l3 || null,
          leopoldL4: form.value.factors.leopold.l4 || null
        })
      } catch (e) { console.error('MedMeasurements save error:', e) }
    } catch (e) { console.error('MedicalSurgicalFactors save error:', e) }

    // 6. CompleteDiagnosis
    try {
      await axios.post(`${BASE}/complete-diagnosis`, {
        clinicalHistoryID,
        diagnosis: form.value.diagnosis || null
      })
    } catch (e) { console.error('CompleteDiagnosis save error:', e) }

    // 7. LaboratoryResults
    await axios.post(`${BASE}/laboratory-results`, {
      prenatalRecordID: prenatalrecordID,
      urinalysis:    form.value.labs.ua || null,
      pusCells:      form.value.labs.purulentSubstance || null,
      redBloodCells: form.value.labs.rbc || null,
      completeBloodCount: form.value.labs.cbc || null,
      hemoglobin:    form.value.labs.hemoglobin ? Number(form.value.labs.hemoglobin) : null,
      venerealDiseaseResearchLaboratoryTest: form.value.labs.vdrl || null,
      humanImmunodeficiencyVirusTest:        form.value.labs.hiv  || null,
      ultrasoundResult: form.value.labs.ultrasound || null
    })

    // 8. TreatmentManagement
    await axios.post(`${BASE}/treatment-management`, {
      prenatalRecordID: prenatalrecordID,
      palpitationDone:       form.value.treatment.palpation,
      referToRoutineLabExams:form.value.treatment.labExam,
      advisedDiet:           form.value.treatment.dietAdvice,
      advisedVitaminIntake:  form.value.treatment.vitamins,
      referralDoctor:        form.value.treatment.referred ? 'Yes' : null
    })

    // 9. ConsultationRecord + Visits + VitalSigns
    try {
      const consultRes = await axios.post(`${BASE}/consultationrecord`, {
        prenatalRecordID: prenatalrecordID, notes: ''
      })
      const consultationRecordID = consultRes.data.consultationRecordID

      for (const visit of form.value.visits) {
        if (!visit.date) continue
        try {
          await axios.post(`${BASE}/consultation-visits`, {
            consultationRecordID,
            visitNumber: visit.visit,
            visitDate:   normalizeDate(visit.date),
            ageOfGestationInWeeks: visit.aog ? Number(visit.aog) : null
          })
          try {
            await axios.post(`${BASE}/vital-signs`, {
              consultationRecordID,
              fetalHeartTone:   visit.fht || null,
              fundalHeight:     visit.fh  || null,
              maternalPosition: visit.position || null,
              fetalPresentation:visit.presentation || null,
              maternalWeight:   visit.weight ? Number(visit.weight) : null,
              bloodPressure:    visit.bp || null,
              nextVisitDate:    normalizeDate(visit.nextVisit)
            })
          } catch (e) { console.error(`VitalSigns visit ${visit.visit} error:`, e) }
        } catch (e) { console.error(`ConsultationVisit ${visit.visit} error:`, e) }
      }
    } catch (e) { console.error('ConsultationRecord save error:', e) }

    submitStatus.value.success = '✅ Prenatal record saved successfully!'
    await fetchRiskAssessment(prenatalrecordID)
    await syncEmployeeNameToPatientService()

  } catch (error) {
    const msg = error?.response?.data?.message || error?.message || 'Unknown error'
    submitStatus.value.error = '❌ Save failed: ' + msg
    console.error('Prenatal save error:', error)
  } finally {
    submitStatus.value.loading = false
  }
}
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

    <!-- View-only banner for Patient accounts -->
    <span v-if="isReadOnly" class="ml-2 text-xs font-semibold text-indigo-700 bg-indigo-100 px-3 py-1.5 rounded-full">
      👁️ View Only
    </span>
  </div>

  <!-- Printable form -->
  <div id="printable-area"
    class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

    <!-- Header -->
    <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
      <div class="flex items-center justify-center gap-3 mb-2">
        <img src="/static/TATING (2).png" alt="Tating Logo"
          style="height: 3.5rem; width: auto; object-fit: contain;" />
        <div class="text-left">
          <p class="text-xs text-gray-500 uppercase tracking-widest leading-tight">Tating's Birthing Home</p>
          <h1 class="text-xl font-bold tracking-wide uppercase leading-tight">Prenatal Consultation Form</h1>
          <p class="text-xs text-gray-400 leading-tight">Maternal Health Record</p>
        </div>
      </div>
    </div>

    <!-- HIGH-RISK BANNER (live) -->
    <div v-if="isLiveHighRisk && !riskResult" class="no-print mb-4 rounded-lg border-2 border-red-500 bg-red-50 p-4">
      <div class="flex items-center gap-2 mb-2">
        <span class="text-2xl">🚨</span>
        <span class="font-bold text-red-700 text-lg">HIGH RISK PATIENT DETECTED</span>
        <span class="ml-auto text-xs text-red-500 italic">Live preview — save to confirm</span>
      </div>
      <ul class="list-disc list-inside text-sm text-red-700 space-y-1">
        <li v-if="obstetricHighRisk">One or more Obstetric Risk Factors are checked</li>
        <li v-if="medicalHighRisk">One or more Medical / Surgical conditions are checked</li>
        <li v-if="leopoldFhtHighRisk">Abnormal Fetal Heart Tone in Leopold section ({{ form.factors.leopold.fht }} bpm)</li>
        <li v-if="labHighRisk">Lab result(s) indicate risk (VDRL / HIV positive or low Hemoglobin)</li>
        <li v-if="ultrasoundHighRisk">Ultrasound findings indicate high-risk condition ({{ ultrasoundTags.filter(t => HIGH_RISK_US_TAGS.includes(t)).join(', ') }})</li>
        <li v-if="referralHighRisk">Referral to hospital is marked as needed</li>
        <li v-if="visitHighRisk">One or more follow-up visits have abnormal vitals or presentation</li>
      </ul>
    </div>

    <!-- HIGH-RISK BANNER (confirmed from backend) -->
    <div v-if="riskResult" class="no-print mb-4 rounded-lg border-2 p-4"
      :class="riskResult.highRisk ? 'border-red-500 bg-red-50' : 'border-green-500 bg-green-50'">
      <div class="flex items-center gap-2 mb-2">
        <span class="text-2xl">{{ riskResult.highRisk ? '🚨' : '✅' }}</span>
        <span class="font-bold text-lg"
          :class="riskResult.highRisk ? 'text-red-700' : 'text-green-700'">
          {{ riskResult.highRisk ? 'HIGH RISK PATIENT' : 'LOW RISK PATIENT' }}
        </span>
        <span class="ml-auto text-xs italic"
          :class="riskResult.highRisk ? 'text-red-500' : 'text-green-500'">
          Assessed by server
        </span>
      </div>
      <ul v-if="riskResult.highRisk" class="list-disc list-inside text-sm text-red-700 space-y-1">
        <li v-for="reason in riskResult.reasons" :key="reason">{{ reason }}</li>
      </ul>
      <p v-else class="text-sm text-green-700">No high-risk factors detected in this record.</p>
    </div>

    <!-- Status messages -->
    <div class="no-print mb-4">
      <div v-if="submitStatus.error"
        class="text-sm text-red-700 bg-red-100 border border-red-200 rounded p-3 mb-2">
        {{ submitStatus.error }}
      </div>
      <div v-if="submitStatus.success"
        class="text-sm text-green-700 bg-green-100 border border-green-200 rounded p-3 mb-2">
        {{ submitStatus.success }}
      </div>
    </div>

    <!-- READ-ONLY WRAPPER: everything inside this fieldset becomes non-editable
         (inputs, selects, textareas, checkboxes, radios) whenever a Patient is
         logged in — one attribute handles the whole form. -->
    <fieldset :disabled="isReadOnly" style="border:none;padding:0;margin:0;min-width:0;">

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
        <input type="text" v-model="form.gpal" class="input" placeholder="e.g. 2-1-1-0-1" />
      </div>
    </div>

    <!-- Risk Factors -->
    <div class="no-break grid grid-cols-2 gap-6 mb-6 border rounded p-4"
      :class="obstetricHighRisk || medicalHighRisk ? 'border-red-400 bg-red-50' : 'border-gray-200'">

      <!-- Obstetric Risk Factors -->
      <div>
        <h3 class="section-title flex items-center gap-2">
          Obstetric Risk Factors
          <span v-if="obstetricHighRisk" class="text-xs font-bold text-white bg-red-500 rounded px-2 py-0.5">HIGH RISK</span>
        </h3>
        <div class="space-y-1">
          <label class="cb" :class="form.obstetricRisk.multiplePregnancy ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.multiplePregnancy" /> Multiple Pregnancy</label>
          <label class="cb" :class="form.obstetricRisk.ovarianCyst ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.ovarianCyst" /> Ovarian Cyst</label>
          <label class="cb" :class="form.obstetricRisk.myomaUteri ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.myomaUteri" /> Myoma Uteri</label>
          <label class="cb" :class="form.obstetricRisk.thyroidDisorder ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.thyroidDisorder" /> Thyroid Disorder</label>
          <label class="cb" :class="form.obstetricRisk.miscarriage ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.miscarriage" /> History of Miscarriage</label>
          <label class="cb" :class="form.obstetricRisk.preeclampsia ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.preeclampsia" /> Preeclampsia</label>
          <label class="cb" :class="form.obstetricRisk.eclampsia ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.eclampsia" /> Eclampsia</label>
          <label class="cb" :class="form.obstetricRisk.prematureContraction ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.obstetricRisk.prematureContraction" /> Premature Contraction</label>
        </div>
      </div>

      <!-- Medical / Surgical History -->
      <div>
        <h3 class="section-title flex items-center gap-2">
          Medical / Surgical History
          <span v-if="medicalHighRisk" class="text-xs font-bold text-white bg-red-500 rounded px-2 py-0.5">HIGH RISK</span>
        </h3>
        <div class="space-y-1">
          <label class="cb" :class="form.medical.hypertension ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.hypertension" /> Hypertension</label>
          <label class="cb" :class="form.medical.heartDisease ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.heartDisease" /> Heart Disease</label>
          <label class="cb" :class="form.medical.diabetes ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.diabetes" /> Diabetes</label>
          <label class="cb" :class="form.medical.asthma ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.asthma" /> Asthma</label>
          <label class="cb" :class="form.medical.obesity ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.obesity" /> Obesity</label>
          <label class="cb" :class="form.medical.epilepsy ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.epilepsy" /> Epilepsy</label>
          <label class="cb" :class="form.medical.previousCS ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.previousCS" /> Previous CS</label>
          <label class="cb" :class="form.medical.myomectomy ? 'text-red-600 font-semibold' : ''">
            <input type="checkbox" v-model="form.medical.myomectomy" /> Myomectomy</label>
        </div>
      </div>
    </div>

    <!-- Medical/Surgical Factors -->
    <div class="no-break border border-gray-200 rounded p-4 mb-6">
      <h3 class="section-title mb-3">Medical / Surgical Factors</h3>
      <div class="grid grid-cols-2 gap-6">
        <div class="space-y-4">
          <div>
            <p class="font-semibold text-sm mb-2">A. Abdomen</p>
            <label class="cb row-between"><span>Normoactive bowel sound</span>
              <input type="checkbox" v-model="form.factors.abdomen.normoactiveBowel" /></label>
            <label class="cb row-between"><span>Non-Tender</span>
              <input type="checkbox" v-model="form.factors.abdomen.nonTender" /></label>
            <label class="cb row-between"><span>Active Fetal Movement</span>
              <input type="checkbox" v-model="form.factors.abdomen.activeFetalMovement" /></label>
          </div>
          <div>
            <p class="font-semibold text-sm mb-2">B. Speculum Exam</p>
            <label class="cb row-between"><span>Parous Vagina</span>
              <input type="checkbox" v-model="form.factors.speculum.parousVagina" /></label>
            <label class="cb row-between"><span>Cervix Smooth Closed</span>
              <input type="checkbox" v-model="form.factors.speculum.cervixSmoothClosed" /></label>
          </div>
        </div>
        <div class="bg-gray-50 p-3 rounded">
          <p class="font-semibold text-sm mb-3">C. Leopold Maneuver</p>
          <div class="grid grid-cols-2 gap-3 text-xs">
            <div>
              <label class="field-label">Fundal Height</label>
              <input type="text" v-model="form.factors.leopold.fundalHeight" class="input-line" />
              <label class="field-label mt-2">Est. Fetal Weight</label>
              <input type="text" v-model="form.factors.leopold.estFetalWeight" class="input-line" />
              <label class="field-label mt-2 flex items-center gap-1">
                FHT
                <span v-if="leopoldFhtHighRisk" class="text-red-600 font-bold text-xs">(⚠ Abnormal)</span>
              </label>
              <input type="text" v-model="form.factors.leopold.fht" class="input-line"
                :class="leopoldFhtHighRisk ? 'border-red-500 text-red-600 font-bold' : ''" />
            </div>
            <div class="space-y-2">
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L1:</span>
                <input type="text" v-model="form.factors.leopold.l1" class="input-line flex-1" /></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L2:</span>
                <input type="text" v-model="form.factors.leopold.l2" class="input-line flex-1" /></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L3:</span>
                <input type="text" v-model="form.factors.leopold.l3" class="input-line flex-1" /></div>
              <div class="flex items-center gap-1"><span class="w-6 font-bold">L4:</span>
                <input type="text" v-model="form.factors.leopold.l4" class="input-line flex-1" /></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Diagnosis & Delivery Planning -->
    <div class="no-break border-l-4 p-4 mb-6 rounded-r"
      :class="referralHighRisk ? 'border-red-500 bg-red-50' : 'border-blue-500 bg-blue-50'">
      <p class="font-bold mb-2">Complete Diagnosis:</p>
      <input type="text" v-model="form.diagnosis" class="input-line w-full mb-4" />

      <p class="font-bold italic text-sm mb-2">Delivery Planning:</p>
      <div class="grid grid-cols-2 gap-4 text-sm">
        <div>
          <label class="field-label">Prenatal Exam Date</label>
          <input type="date" v-model="form.deliveryPlanning.prenatalExamDate" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Expected Date of Delivery</label>
          <input type="date" v-model="form.deliveryPlanning.expectedDeliveryDate" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Date of Delivery</label>
          <input type="date" v-model="form.deliveryDetails.date" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label">Place of Delivery</label>
          <input type="text" v-model="form.deliveryDetails.place" class="input-line w-full" />
        </div>
        <div>
          <label class="field-label flex items-center gap-1">
            Type of Delivery
            <span v-if="isLiveHighRisk || riskResult?.highRisk"
              class="text-xs font-bold text-white bg-red-500 rounded px-1.5 py-0.5">⚠ HIGH RISK</span>
          </label>
          <select v-model="form.deliveryDetails.type" class="select-field w-full"
            :class="(isLiveHighRisk || riskResult?.highRisk) ? 'select-risk' : ''">
            <option value="">— Select —</option>
            <option>Normal Spontaneous Vaginal Delivery</option>
            <option>High Risk Delivery</option>
          </select>
        </div>
        <div class="flex items-center gap-3">
          <label class="field-label flex items-center gap-1">
            Referral Hospital?
            <span v-if="referralHighRisk" class="text-red-600 font-bold text-xs ml-1">⚠ HIGH RISK</span>
          </label>
          <input type="checkbox" v-model="form.deliveryDetails.referralHospitalNeeded" class="accent-red-500" />
          <input v-if="form.deliveryDetails.referralHospitalNeeded" type="text"
            placeholder="Specify" v-model="form.deliveryDetails.referralHospitalName"
            class="input-line flex-1" />
        </div>
      </div>
    </div>

    <!-- Follow-Up Visits -->
    <div class="mb-6">
      <h3 class="section-title mb-2 flex items-center gap-2">
        Follow-Up Visits
        <span v-if="visitHighRisk"
          class="text-xs font-bold text-white bg-red-500 rounded px-2 py-0.5">ABNORMAL VITALS DETECTED</span>
      </h3>
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
            <tr v-for="v in form.visits" :key="v.visit"
              :class="isVisitRowHighRisk(v) ? 'bg-red-50' : ''">
              <td class="td text-center font-semibold"
                :class="isVisitRowHighRisk(v) ? 'text-red-600' : ''">{{ v.visit }}</td>
              <td class="td"><input v-model="v.date" type="date" class="input-table" /></td>
              <td class="td"><input v-model="v.aog" class="input-table" /></td>
              <td class="td">
                <input v-model="v.fht" class="input-table"
                  :class="isVisitFhtAbnormal(v) ? 'text-red-600 font-bold' : ''" />
              </td>
              <td class="td"><input v-model="v.fh" class="input-table" /></td>
              <td class="td"><input v-model="v.position" class="input-table" /></td>
              <td class="td">
                <input v-model="v.presentation" class="input-table"
                  :class="isVisitPresentationAbnormal(v) ? 'text-red-600 font-bold' : ''" />
              </td>
              <td class="td"><input v-model="v.weight" class="input-table" /></td>
              <td class="td">
                <input v-model="v.bp" class="input-table"
                  :class="isVisitBpHigh(v) ? 'text-red-600 font-bold' : ''" />
              </td>
              <td class="td"><input v-model="v.nextVisit" type="date" class="input-table" /></td>
            </tr>
          </tbody>
        </table>
        <p v-if="visitHighRisk" class="text-xs text-red-600 mt-1 no-print">
          🔴 Red values indicate: FHT &lt;110 or &gt;160 bpm | BP ≥ 140/90 mmHg | Non-vertex presentation
        </p>
      </div>
    </div>

    <!-- Laboratory Results -->
    <div class="no-break border rounded p-4 mb-6"
      :class="labHighRisk ? 'border-red-400 bg-red-50' : 'border-gray-200'">
      <h3 class="text-center font-bold text-sm tracking-widest mb-1">LABORATORY RESULTS</h3>
      <p v-if="labHighRisk" class="text-center text-xs text-red-600 font-bold mb-3">⚠ Abnormal lab result(s) detected</p>
      <div class="grid grid-cols-2 gap-x-8 gap-y-4 text-sm">

        <div class="col-span-2 flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">Urinalysis:</label>
          <select v-model="form.labs.ua" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>Normal</option>
            <option>Protein+</option>
            <option>Protein++</option>
            <option>Protein+++</option>
            <option>Pus Cells Present</option>
            <option>Protein + Pus Cells</option>
            <option>Glucose Present</option>
            <option>RBC Present</option>
          </select>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">Purulent (Pus Cells):</label>
          <select v-model="form.labs.purulentSubstance" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>None</option>
            <option>Few</option>
            <option>Moderate</option>
            <option>Many</option>
            <option>TNTC</option>
          </select>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">Red Blood Cells:</label>
          <select v-model="form.labs.rbc" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>Normal</option>
            <option>Few</option>
            <option>Moderate</option>
            <option>Many</option>
            <option>TNTC</option>
          </select>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">CBC:</label>
          <select v-model="form.labs.cbc" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>Normal</option>
            <option>Low WBC</option>
            <option>High WBC</option>
            <option>Anemia</option>
            <option>Thrombocytopenia</option>
            <option>Pancytopenia</option>
          </select>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">Hemoglobin (g/dL):</label>
          <input v-model="form.labs.hemoglobin" type="number" step="0.1" min="0" max="20"
            placeholder="e.g. 12.5" class="input-line flex-1"
            :class="form.labs.hemoglobin && parseFloat(form.labs.hemoglobin) < 11 ? 'text-red-600 font-bold border-red-400' : ''" />
          <span v-if="form.labs.hemoglobin && parseFloat(form.labs.hemoglobin) < 11"
            class="text-red-600 font-bold text-xs shrink-0">⚠ Anemia</span>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">VDRL:</label>
          <select v-model="form.labs.vdrl" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>Non-Reactive</option>
            <option>Reactive (Positive)</option>
            <option>Weakly Reactive</option>
          </select>
        </div>

        <div class="flex items-center gap-2">
          <label class="w-36 font-medium shrink-0">HIV Test:</label>
          <select v-model="form.labs.hiv" class="select-field flex-1">
            <option value="">— Select —</option>
            <option>Non-Reactive</option>
            <option>Reactive (Positive)</option>
            <option>Indeterminate</option>
          </select>
        </div>

        <div class="col-span-2">
          <label class="block font-medium mb-1">Ultrasound Results:</label>
          <div class="flex gap-2 mb-2 flex-wrap">
            <button type="button"
              v-for="tag in ['Normal','Placenta Previa','Oligohydramnios','Polyhydramnios','Fetal Anomaly','IUGR','Breech Presentation','Multiple Gestation']"
              :key="tag"
              @click="toggleUltrasoundTag(tag)"
              :disabled="isReadOnly"
              class="text-xs px-2 py-1 rounded border transition disabled:opacity-50 disabled:cursor-not-allowed"
              :class="ultrasoundTags.includes(tag)
                ? (['Placenta Previa','Oligohydramnios','Fetal Anomaly','IUGR'].includes(tag) ? 'bg-red-500 text-white border-red-500' : 'bg-indigo-500 text-white border-indigo-500')
                : 'bg-white text-gray-600 border-gray-300 hover:border-indigo-400'">
              {{ tag }}
            </button>
          </div>
          <textarea v-model="form.labs.ultrasound" rows="2"
            placeholder="Additional findings or notes..."
            class="w-full border border-gray-300 rounded p-2 text-sm"></textarea>
        </div>
      </div>
    </div>

    <!-- Treatment -->
    <div class="no-break border border-gray-200 rounded p-4 mb-6">
      <h3 class="section-title mb-3">Treatment / Management</h3>
      <div class="grid grid-cols-2 gap-2">
        <label class="cb"><input type="checkbox" v-model="form.treatment.palpation" /> Palpation done</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.dietAdvice" /> Diet counseling</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.vitamins" /> Vitamins prescribed</label>
        <label class="cb"><input type="checkbox" v-model="form.treatment.referred" /> Referred to physician</label>
      </div>
    </div>

    <!-- Signatures -->
    <div class="grid grid-cols-2 gap-16 mt-10">
      <div class="text-center flex flex-col justify-end min-h-[4rem]">
        <div class="border-t border-gray-800 pt-1">
          <p class="text-xs font-semibold">Patient's Signature / Date</p>
        </div>
      </div>
      <div class="text-center flex flex-col justify-end min-h-[4rem]">
        <select v-model="form.attendingStaffID" @change="onAttendingStaffChange" class="input-line text-center w-full mb-1">
          <option :value="null">-- Select Attending Staff --</option>
          <option v-for="staff in employeesList" :key="staff.employeeID" :value="Number(staff.employeeID)">
            {{ staff.fName }} {{ staff.lName }}
          </option>
        </select>
        <div class="border-t border-gray-800 pt-1">
          <p class="text-xs font-semibold">Attending Midwife / Physician</p>
        </div>
      </div>
    </div>

    </fieldset>
    <!-- END read-only wrapper -->

    <!-- Save button — hidden entirely for Patient accounts -->
    <div v-if="!isReadOnly" class="no-print mt-6">
      <div v-if="submitStatus.error"
        class="text-sm text-red-700 bg-red-100 border border-red-200 rounded p-3 mb-2">
        {{ submitStatus.error }}
      </div>
      <div v-if="submitStatus.success"
        class="text-sm text-green-700 bg-green-100 border border-green-200 rounded p-3 mb-2">
        {{ submitStatus.success }}
      </div>
      <button @click="submitForm" :disabled="submitStatus.loading"
        class="w-full bg-indigo-600 text-white py-3 rounded-lg font-bold hover:bg-indigo-700 transition shadow disabled:opacity-50 disabled:cursor-not-allowed">
        {{ submitStatus.loading ? 'Saving...' : 'SAVE COMPLETE PRENATAL RECORD' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.input { width:100%; padding:6px 8px; border:1px solid #d1d5db; border-radius:6px; outline:none; font-size:13px; }
.input-table { width:100%; padding:2px; border:none; text-align:center; font-size:11px; background:transparent; }
.input-line { display:block; width:100%; border:none; border-bottom:1px solid #94a3b8; outline:none; padding:2px; background:transparent; font-size:13px; }
.field-label { display:block; font-size:11px; font-weight:600; color:#6b7280; margin-bottom:2px; }
.section-title { font-weight:700; font-size:0.95rem; color:#1e293b; margin-bottom:8px; }
.td { border:1px solid #e5e7eb; padding:2px 4px; }
.cb { display:flex; align-items:center; gap:6px; font-size:0.85rem; cursor:pointer; }
.row-between { display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid #e5e7eb; padding-bottom:4px; }
.select-field { width:100%; padding:4px 8px; border:1px solid #d1d5db; border-radius:6px; font-size:13px; background:white; outline:none; cursor:pointer; transition:border-color 0.15s; }
.select-field:focus { border-color: #6366f1; }
.select-risk { border-color:#f87171 !important; color:#dc2626; font-weight:600; background-color:#fff5f5; }

/* Fieldset is disabled but keep it looking like normal text, not greyed-out
   form controls, since patients should read this like a document. */
fieldset:disabled input,
fieldset:disabled select,
fieldset:disabled textarea {
  opacity: 1;
  color: inherit;
  cursor: default;
}
</style>

<style>
@page { size: A4 portrait; margin: 12mm 12mm; }
@media print {
  * { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; }
  body.printing-prenatal .layout-topbar,
  body.printing-prenatal .layout-sidebar,
  body.printing-prenatal .layout-footer,
  body.printing-prenatal .no-print { display: none !important; }
  body.printing-prenatal .layout-wrapper,
  body.printing-prenatal .layout-main-container,
  body.printing-prenatal .layout-main { display:block !important; padding:0 !important; margin:0 !important; width:100% !important; }
  body.printing-prenatal #printable-area { display:block !important; width:100% !important; margin:0 !important; padding:0 !important; border:none !important; box-shadow:none !important; }
  input[type="text"], input[type="date"], select { border:none !important; border-bottom:1px solid #000 !important; border-radius:0 !important; background:transparent !important; -webkit-appearance:none; -moz-appearance:none; appearance:none; }
  textarea { border:1px solid #555 !important; background:transparent !important; resize:none !important; }
  table { border-collapse:collapse !important; width:100% !important; }
  th, td { border:1px solid #333 !important; }
  thead tr { background-color:#e5e7eb !important; }
  .bg-red-50 { background-color:#fef2f2 !important; }
  .bg-blue-50 { background-color:#eff6ff !important; }
  .bg-gray-50 { background-color:#f9fafb !important; }
  .no-break { page-break-inside:avoid !important; }
}
</style>
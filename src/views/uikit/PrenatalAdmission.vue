<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const BASE = 'http://localhost:8080/api/prenatal'
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services'
const clientId = route.params.clientId

function goBack() { router.back() }

function handleBeforePrint() { document.body.classList.add('printing-prenatal') }
function handleAfterPrint() { document.body.classList.remove('printing-prenatal') }

const serviceID = ref(null)
const submitStatus = ref({ loading: false, error: '', success: '' })

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

onMounted(async () => {
    window.addEventListener('beforeprint', handleBeforePrint)
    window.addEventListener('afterprint', handleAfterPrint)

    if (!clientId) return

    await ensurePrenatalService(clientId)
    if (serviceID.value) {
        await loadExistingPrenatalData(serviceID.value)
    }
})

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
    } catch (e) {
        console.error('Failed to load or create prenatal service', e)
    }
}

onUnmounted(() => {
    window.removeEventListener('beforeprint', handleBeforePrint)
    window.removeEventListener('afterprint', handleAfterPrint)
})

function printForm() { window.print() }

function normalizeDate(value) {
    if (!value) return null
    const date = new Date(value)
    if (Number.isNaN(date.getTime())) return null
    return date.toISOString().split('T')[0]
}

function parseDateForForm(value) {
    if (!value) return ''
    const date = new Date(value)
    return Number.isNaN(date.getTime()) ? '' : date.toISOString().split('T')[0]
}

function buildPrenatalUrl(path, id) {
    if (id === undefined || id === null || id === '') {
        throw new Error(`Invalid prenatalRecordID for ${path}`)
    }
    return `${BASE}/${path}/${id}`
}

function buildGpal(value) {
    if (!value) return ''
    return [
        value.gravida,
        value.para,
        value.term,
        value.preterm,
        value.abortion,
        value.living
    ].map(v => (v === null || v === undefined) ? '' : String(v)).join('-')
}

async function loadExistingPrenatalData(serviceID) {
    try {
        const res = await axios.get(`${BASE}/records/service/${serviceID}`)
        const records = Array.isArray(res.data) ? res.data : []
        if (!records.length) return

        const latestRecord = records.reduce((latest, current) => {
            return (!latest || current.prenatalrecordID > latest.prenatalrecordID) ? current : latest
        }, null)

        if (!latestRecord) return

        form.value.deliveryPlanning.prenatalExamDate = parseDateForForm(latestRecord.prenatalExamDate)
        form.value.deliveryPlanning.expectedDeliveryDate = parseDateForForm(latestRecord.expectedDeliveryDate)
        form.value.edc = parseDateForForm(latestRecord.edc)
        form.value.deliveryDetails.date = parseDateForForm(latestRecord.deliveryDate)
        form.value.deliveryDetails.place = latestRecord.placeOfDelivery || ''
        form.value.deliveryDetails.type = latestRecord.typeOfDelivery || ''
        form.value.deliveryDetails.referralHospitalNeeded = !!latestRecord.referralHospitalNeeded
        form.value.deliveryDetails.referralHospitalName = latestRecord.referralHospitalName || ''

        await loadClinicalHistoryData(latestRecord.prenatalrecordID)
        await loadPrenatalDetailData(latestRecord.prenatalrecordID)
    } catch (e) {
        console.error('Load existing prenatal data error:', e)
    }
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
            form.value.lmp = parseDateForForm(menstrual.LMP)
            form.value.gpal = buildGpal(menstrual)
        }

        const obstetric = Array.isArray(obstetricRes.data) ? obstetricRes.data[0] : null
        if (obstetric) {
            form.value.obstetricRisk.multiplePregnancy = !!obstetric.multiplePregnancy
            form.value.obstetricRisk.ovarianCyst = !!obstetric.ovarianCyst
            form.value.obstetricRisk.myomaUteri = !!obstetric.myomaUteri
            form.value.obstetricRisk.thyroidDisorder = !!obstetric.thyroidDisorder
            form.value.obstetricRisk.miscarriage = !!obstetric.historyOfMiscarriage
            form.value.obstetricRisk.preeclampsia = !!obstetric.preEclampsia
            form.value.obstetricRisk.eclampsia = !!obstetric.preEclampsia
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
                form.value.medical.hypertension = !!conds.hypertension
                form.value.medical.heartDisease = !!conds.heartDisease
                form.value.medical.diabetes = !!conds.diabetes
                form.value.medical.thyroidDisorder = !!conds.thyroidDisorder
                form.value.medical.obesity = !!conds.obesity
                form.value.medical.asthma = !!conds.moderateToSevereAsthma
                form.value.medical.epilepsy = !!conds.epilepsy
                form.value.medical.renalDisease = !!conds.renalDisease
                form.value.medical.bleedingDisorder = !!conds.bleedingDisorder
                form.value.medical.previousCS = !!conds.previousCesarianSection
                form.value.medical.myomectomy = !!conds.historyOfMyomectomy
            }

            const measurements = Array.isArray(measurementsRes.data) ? measurementsRes.data[0] : null
            if (measurements) {
                form.value.factors.abdomen.normoactiveBowel = !!measurements.normoActiveBowelSounds
                form.value.factors.abdomen.nonTender = !!measurements.nonTender
                form.value.factors.abdomen.activeFetalMovement = !!measurements.activeFetalMovement
                form.value.factors.speculum.parousVagina = !!measurements.parousVagina
                form.value.factors.speculum.cervixSmoothClosed = !!measurements.cervixSmoothClosed
                form.value.factors.leopold.fundalHeight = measurements.fundalWeight != null ? String(measurements.fundalWeight) : ''
                form.value.factors.leopold.estFetalWeight = measurements.estimatedFetalWeight != null ? String(measurements.estimatedFetalWeight) : ''
                form.value.factors.leopold.fht = measurements.fetalHeartTone != null ? String(measurements.fetalHeartTone) : ''
                form.value.factors.leopold.l1 = measurements.leopoldL1 || ''
                form.value.factors.leopold.l2 = measurements.leopoldL2 || ''
                form.value.factors.leopold.l3 = measurements.leopoldL3 || ''
                form.value.factors.leopold.l4 = measurements.leopoldL4 || ''
            }
        }

        const diagnosis = Array.isArray(diagnosisRes.data) ? diagnosisRes.data[0] : null
        if (diagnosis) {
            form.value.diagnosis = diagnosis.diagnosis || ''
        }
    } catch (e) {
        console.error('Load clinical history data error:', e)
    }
}

async function loadPrenatalDetailData(prenatalrecordID) {
  console.log('URLs being called:', 
        `${BASE}/laboratory-results/prenatal/${prenatalrecordID}`,
        `${BASE}/treatment-management/prenatal/${prenatalrecordID}`,
        `${BASE}/consultationrecord/prenatal/${prenatalrecordID}`
    ) // ✅ add this
    console.log('Loading detail data for prenatalrecordID:', prenatalrecordID)
    try {
        const [labRes, treatmentRes, consultationRes] = await Promise.all([
            axios.get(`${BASE}/laboratory-results/prenatal/${prenatalrecordID}`),
            axios.get(`${BASE}/treatment-management/prenatal/${prenatalrecordID}`),
            axios.get(`${BASE}/consultationrecord/prenatal/${prenatalrecordID}`)
        ])

        // Load Lab Results
        const labRecords = Array.isArray(labRes.data) ? labRes.data : []
        const lab = labRecords.sort((a, b) => (b.laboratoryResultID || 0) - (a.laboratoryResultID || 0))[0] || null
        if (lab) {
            form.value.labs.ua = lab.urinalysis || ''
            form.value.labs.purulentSubstance = lab.pusCells || ''
            form.value.labs.rbc = lab.redBloodCells || ''
            form.value.labs.cbc = lab.completeBloodCount || ''
            form.value.labs.hemoglobin = lab.hemoglobin != null ? String(lab.hemoglobin) : ''
            form.value.labs.vdrl = lab.venerealDiseaseResearchLaboratoryTest || ''
            form.value.labs.hiv = lab.humanImmunodeficiencyVirusTest || ''
            form.value.labs.ultrasound = lab.ultrasoundResult || ''
        }

        // Load Treatment
        const treatmentRecords = Array.isArray(treatmentRes.data) ? treatmentRes.data : []
        const treatment = treatmentRecords.sort((a, b) => (b.treatmentID || 0) - (a.treatmentID || 0))[0] || null
        if (treatment) {
            form.value.treatment.palpation = !!treatment.palpitationDone
            form.value.treatment.labExam = !!treatment.referToRoutineLabExams
            form.value.treatment.dietAdvice = !!treatment.advisedDiet
            form.value.treatment.vitamins = !!treatment.advisedVitaminIntake
            form.value.treatment.referred = Boolean(treatment.referralDoctor)
        }

        // Load Consultation
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
                form.value.visits[index].aog = visit.ageOfGestationInWeeks != null ? String(visit.ageOfGestationInWeeks) : ''
            }
        })

        const vitals = Array.isArray(vitalRes.data) ? vitalRes.data : []
        if (vitals.length) {
            vitals.forEach((vital, index) => {
                if (index >= 0 && index < form.value.visits.length) {
                    form.value.visits[index].fht = vital.fetalHeartTone || ''
                    form.value.visits[index].fh = vital.fundalHeight || ''
                    form.value.visits[index].position = vital.maternalPosition || ''
                    form.value.visits[index].presentation = vital.fetalPresentation || ''
                    form.value.visits[index].weight = vital.maternalWeight != null ? String(vital.maternalWeight) : ''
                    form.value.visits[index].bp = vital.bloodPressure || ''
                    form.value.visits[index].nextVisit = parseDateForForm(vital.nextVisitDate)
                }
            })
        }
    } catch (e) {
        console.error('Load prenatal detail data error:', e)
    }
}

async function submitForm() {
    submitStatus.value.loading = true
    submitStatus.value.error = ''
    submitStatus.value.success = ''

    if (!serviceID.value) {
        await ensurePrenatalService(clientId)
    }

    if (!serviceID.value) {
        submitStatus.value.error = 'Service ID not found. Please go back and try again.'
        submitStatus.value.loading = false
        return
    }

    const today = new Date().toISOString().split('T')[0]

    try {
        // 1. Save PrenatalRecord
        const prenatalRes = await axios.post(`${BASE}/records`, {
            serviceID: serviceID.value,
            initialPreConsultationDate: today,
            prenatalExamDate: normalizeDate(form.value.deliveryPlanning.prenatalExamDate),
            expectedDeliveryDate: normalizeDate(form.value.deliveryPlanning.expectedDeliveryDate),
            edc: normalizeDate(form.value.edc),
            deliveryDate: normalizeDate(form.value.deliveryDetails.date),
            placeOfDelivery: form.value.deliveryDetails.place || null,
            typeOfDelivery: form.value.deliveryDetails.type || null,
            referralHospitalNeeded: form.value.deliveryDetails.referralHospitalNeeded,
            referralHospitalName: form.value.deliveryDetails.referralHospitalName || null
        })
        const prenatalrecordID = prenatalRes.data.prenatalrecordID

        // 2. Save ClinicalHistory
        const clinicalRes = await axios.post(`${BASE}/clinicalhistory`, {
            prenatalrecordID
        })
        const clinicalHistoryID = clinicalRes.data.clinicalHistoryID

        // 3. Save MenstrualHistory
        try {
            const gpalParts = form.value.gpal.split('-')
            await axios.post(`${BASE}/menstrual`, {
                clinicalHistoryID,
                LMP: normalizeDate(form.value.lmp),
                gravida: gpalParts[0] ? Number(gpalParts[0]) : null,
                para: gpalParts[1] ? Number(gpalParts[1]) : null,
                term: gpalParts[2] ? Number(gpalParts[2]) : null,
                preterm: gpalParts[3] ? Number(gpalParts[3]) : null,
                abortion: gpalParts[4] ? Number(gpalParts[4]) : null,
                living: gpalParts[5] ? Number(gpalParts[5]) : null
            })
        } catch (e) { console.error('MenstrualHistory save error:', e) }

        // 4. Save ObstetricRiskFactor
        try {
            await axios.post(`${BASE}/obstetric-risk-factor`, {
                clinicalHistoryID,
                multiplePregnancy: form.value.obstetricRisk.multiplePregnancy,
                ovarianCyst: form.value.obstetricRisk.ovarianCyst,
                myomaUteri: form.value.obstetricRisk.myomaUteri,
                thyroidDisorder: form.value.obstetricRisk.thyroidDisorder,
                historyOfMiscarriage: form.value.obstetricRisk.miscarriage,
                stillbirth: false,
                preEclampsia: form.value.obstetricRisk.preeclampsia || form.value.obstetricRisk.eclampsia,
                prematureContraction: form.value.obstetricRisk.prematureContraction
            })
        } catch (e) { console.error('ObstetricRiskFactor save error:', e) }

        // 5. Save MedicalSurgicalFactors
        let medsurgID = null
        try {
            const medsurgRes = await axios.post(`${BASE}/medical-surgical-factors`, {
                clinicalHistoryID
            })
            medsurgID = medsurgRes.data.medsurgID

            // 6. Save MedicalSurgicalFactorMedConditions
            try {
                await axios.post(`${BASE}/medsurg-conditions`, {
                    medsurgID,
                    hypertension: form.value.medical.hypertension,
                    heartDisease: form.value.medical.heartDisease,
                    diabetes: form.value.medical.diabetes,
                    thyroidDisorder: form.value.medical.thyroidDisorder,
                    obesity: form.value.medical.obesity,
                    moderateToSevereAsthma: form.value.medical.asthma,
                    epilepsy: form.value.medical.epilepsy,
                    renalDisease: form.value.medical.renalDisease,
                    bleedingDisorder: form.value.medical.bleedingDisorder,
                    previousCesarianSection: form.value.medical.previousCS,
                    historyOfMyomectomy: form.value.medical.myomectomy
                })
            } catch (e) { console.error('MedConditions save error:', e) }

            // 7. Save MedicalSurgicalFactorMeasurements
            try {
                await axios.post(`${BASE}/medsurg-measurements`, {
                    medsurgID,
                    normoActiveBowelSounds: form.value.factors.abdomen.normoactiveBowel,
                    nonTender: form.value.factors.abdomen.nonTender,
                    activeFetalMovement: form.value.factors.abdomen.activeFetalMovement,
                    parousVagina: form.value.factors.speculum.parousVagina,
                    cervixSmoothClosed: form.value.factors.speculum.cervixSmoothClosed,
                    fundalWeight: form.value.factors.leopold.fundalHeight ? Number(form.value.factors.leopold.fundalHeight) : null,
                    estimatedFetalWeight: form.value.factors.leopold.estFetalWeight ? Number(form.value.factors.leopold.estFetalWeight) : null,
                    fetalHeartTone: form.value.factors.leopold.fht ? Number(form.value.factors.leopold.fht) : null,
                    leopoldL1: form.value.factors.leopold.l1 || null,
                    leopoldL2: form.value.factors.leopold.l2 || null,
                    leopoldL3: form.value.factors.leopold.l3 || null,
                    leopoldL4: form.value.factors.leopold.l4 || null
                })
            } catch (e) { console.error('MedMeasurements save error:', e) }

        } catch (e) { console.error('MedicalSurgicalFactors save error:', e) }

        // 8. Save CompleteDiagnosis
        try {
            await axios.post(`${BASE}/complete-diagnosis`, {
                clinicalHistoryID,
                diagnosis: form.value.diagnosis || null
            })
        } catch (e) { console.error('CompleteDiagnosis save error:', e) }

        // 9. Save LaboratoryResults
        try {
            await axios.post(`${BASE}/laboratory-results`, {
                prenatalRecordID: prenatalrecordID,
                urinalysis: form.value.labs.ua || null,
                pusCells: form.value.labs.purulentSubstance || null,
                redBloodCells: form.value.labs.rbc || null,
                completeBloodCount: form.value.labs.cbc || null,
                hemoglobin: form.value.labs.hemoglobin ? Number(form.value.labs.hemoglobin) : null,
                venerealDiseaseResearchLaboratoryTest: form.value.labs.vdrl || null,
                humanImmunodeficiencyVirusTest: form.value.labs.hiv || null,
                ultrasoundResult: form.value.labs.ultrasound || null
            })
        } catch (e) {
            console.error('LaboratoryResults save error:', e)
            submitStatus.value.error = '❌ Save failed while storing lab results.'
            submitStatus.value.loading = false
            return
        }

        // 10. Save TreatmentManagement
        try {
            await axios.post(`${BASE}/treatment-management`, {
                prenatalRecordID: prenatalrecordID,
                palpitationDone: form.value.treatment.palpation,
                referToRoutineLabExams: form.value.treatment.labExam,
                advisedDiet: form.value.treatment.dietAdvice,
                advisedVitaminIntake: form.value.treatment.vitamins,
                referralDoctor: form.value.treatment.referred ? 'Yes' : null
            })
        } catch (e) {
            console.error('TreatmentManagement save error:', e)
            submitStatus.value.error = '❌ Save failed while storing treatment/management.'
            submitStatus.value.loading = false
            return
        }

        // 11. Save ConsultationRecord
        let consultationRecordID = null
        try {
            const consultRes = await axios.post(`${BASE}/consultationrecord`, {
                prenatalRecordID: prenatalrecordID,
                notes: ''
            })
            consultationRecordID = consultRes.data.consultationRecordID

            // 12. Save ConsultationVisits + VitalSigns
            for (const visit of form.value.visits) {
                if (!visit.date) continue
                try {
                    await axios.post(`${BASE}/consultation-visits`, {
                        consultationRecordID,
                        visitNumber: visit.visit,
                        visitDate: normalizeDate(visit.date),
                        ageOfGestationInWeeks: visit.aog ? Number(visit.aog) : null
                    })

                    // 13. Save VitalSigns
                    try {
                        await axios.post(`${BASE}/vital-signs`, {
                            consultationRecordID,
                            fetalHeartTone: visit.fht || null,
                            fundalHeight: visit.fh || null,
                            maternalPosition: visit.position || null,
                            fetalPresentation: visit.presentation || null,
                            maternalWeight: visit.weight ? Number(visit.weight) : null,
                            bloodPressure: visit.bp || null,
                            nextVisitDate: normalizeDate(visit.nextVisit)
                        })
                    } catch (e) { console.error(`VitalSigns visit ${visit.visit} error:`, e) }

                } catch (e) { console.error(`ConsultationVisit ${visit.visit} error:`, e) }
            }
        } catch (e) { console.error('ConsultationRecord save error:', e) }

        submitStatus.value.success = '✅ Prenatal record saved successfully!'

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
    <!-- Screen-only toolbar -->
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
    <div id="printable-area"
        class="max-w-screen mx-auto p-6 bg-white shadow-lg rounded-lg my-4 border border-gray-100">

        <!-- Header -->
        <div class="text-center border-b-2 border-gray-800 pb-4 mb-5">
            <div class="flex items-center justify-center gap-3 mb-2">
                <img src="/static/TATING (2).png" alt="Tating Logo"
                    style="height: 3.5rem; width: auto; object-fit: contain;" />
                <div class="text-left">
                    <p class="text-xs text-gray-500 uppercase tracking-widest leading-tight">Tating's Birthing Home
                    </p>
                    <h1 class="text-xl font-bold tracking-wide uppercase leading-tight">Prenatal Consultation Form
                    </h1>
                    <p class="text-xs text-gray-400 leading-tight">Maternal Health Record</p>
                </div>
            </div>
        </div>

        <!-- Status Messages -->
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
        <div class="no-break grid grid-cols-2 gap-6 mb-6 border border-gray-200 rounded p-4">
            <div>
                <h3 class="section-title">Obstetric Risk Factors</h3>
                <div class="space-y-1">
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.multiplePregnancy" /> Multiple Pregnancy</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.ovarianCyst" /> Ovarian Cyst</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.myomaUteri" /> Myoma Uteri</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.thyroidDisorder" /> Thyroid Disorder</label>
                    <label class="cb"><input type="checkbox" v-model="form.obstetricRisk.miscarriage" /> History of
                        Miscarriage</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.preeclampsia" /> Preeclampsia</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.eclampsia" /> Eclampsia</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.obstetricRisk.prematureContraction" /> Premature Contraction</label>
                </div>
            </div>
            <div>
                <h3 class="section-title">Medical / Surgical History</h3>
                <div class="space-y-1">
                    <label class="cb"><input type="checkbox"
                            v-model="form.medical.hypertension" /> Hypertension</label>
                    <label class="cb"><input type="checkbox"
                            v-model="form.medical.heartDisease" /> Heart Disease</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.diabetes" /> Diabetes</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.asthma" /> Asthma</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.obesity" /> Obesity</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.epilepsy" /> Epilepsy</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.previousCS" /> Previous CS</label>
                    <label class="cb"><input type="checkbox" v-model="form.medical.myomectomy" /> Myomectomy</label>
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
                        <label class="cb row-between"><span>Normoactive bowel sound</span><input type="checkbox"
                                v-model="form.factors.abdomen.normoactiveBowel" /></label>
                        <label class="cb row-between"><span>Non-Tender</span><input type="checkbox"
                                v-model="form.factors.abdomen.nonTender" /></label>
                        <label class="cb row-between"><span>Active Fetal Movement</span><input type="checkbox"
                                v-model="form.factors.abdomen.activeFetalMovement" /></label>
                    </div>
                    <div>
                        <p class="font-semibold text-sm mb-2">B. Speculum Exam</p>
                        <label class="cb row-between"><span>Parous Vagina</span><input type="checkbox"
                                v-model="form.factors.speculum.parousVagina" /></label>
                        <label class="cb row-between"><span>Cervix Smooth Closed</span><input type="checkbox"
                                v-model="form.factors.speculum.cervixSmoothClosed" /></label>
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
                            <label class="field-label mt-2">FHT</label>
                            <input type="text" v-model="form.factors.leopold.fht" class="input-line" />
                        </div>
                        <div class="space-y-2">
                            <div class="flex items-center gap-1"><span class="w-6 font-bold">L1:</span><input
                                    type="text" v-model="form.factors.leopold.l1"
                                    class="input-line flex-1" /></div>
                            <div class="flex items-center gap-1"><span class="w-6 font-bold">L2:</span><input
                                    type="text" v-model="form.factors.leopold.l2"
                                    class="input-line flex-1" /></div>
                            <div class="flex items-center gap-1"><span class="w-6 font-bold">L3:</span><input
                                    type="text" v-model="form.factors.leopold.l3"
                                    class="input-line flex-1" /></div>
                            <div class="flex items-center gap-1"><span class="w-6 font-bold">L4:</span><input
                                    type="text" v-model="form.factors.leopold.l4"
                                    class="input-line flex-1" /></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Diagnosis & Delivery Planning -->
        <div class="no-break border-l-4 border-blue-500 bg-blue-50 p-4 mb-6 rounded-r">
            <p class="font-bold mb-2">Complete Diagnosis:</p>
            <input type="text" v-model="form.diagnosis" class="input-line w-full mb-4" />

            <p class="font-bold italic text-sm mb-2">Delivery Planning:</p>
            <div class="grid grid-cols-2 gap-4 text-sm">
                <div>
                    <label class="field-label">Prenatal Exam Date</label>
                    <input type="date" v-model="form.deliveryPlanning.prenatalExamDate"
                        class="input-line w-full" />
                </div>
                <div>
                    <label class="field-label">Expected Date of Delivery</label>
                    <input type="date" v-model="form.deliveryPlanning.expectedDeliveryDate"
                        class="input-line w-full" />
                </div>
                <div>
                    <label class="field-label">Date of Delivery</label>
                    <input type="date" v-model="form.deliveryDetails.date" class="input-line w-full" />
                </div>
                <div>
                    <label class="field-label">Place of Delivery</label>
                    <input type="text" v-model="form.deliveryDetails.place" class="input-line w-full"
                        :placeholder="form.deliveryPlanning.place" />
                </div>
                <div>
                    <label class="field-label">Type of Delivery</label>
                    <input type="text" v-model="form.deliveryDetails.type" class="input-line w-full" />
                </div>
                <div class="flex items-center gap-3">
                    <label class="field-label">Referral Hospital?</label>
                    <input type="checkbox" v-model="form.deliveryDetails.referralHospitalNeeded" />
                    <input v-if="form.deliveryDetails.referralHospitalNeeded" type="text" placeholder="Specify"
                        v-model="form.deliveryDetails.referralHospitalName" class="input-line flex-1" />
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
                            <td class="td"><input v-model="v.date" type="date" class="input-table" /></td>
                            <td class="td"><input v-model="v.aog" class="input-table" /></td>
                            <td class="td"><input v-model="v.fht" class="input-table" /></td>
                            <td class="td"><input v-model="v.fh" class="input-table" /></td>
                            <td class="td"><input v-model="v.position" class="input-table" /></td>
                            <td class="td"><input v-model="v.presentation" class="input-table" /></td>
                            <td class="td"><input v-model="v.weight" class="input-table" /></td>
                            <td class="td"><input v-model="v.bp" class="input-table" /></td>
                            <td class="td"><input v-model="v.nextVisit" type="date" class="input-table" /></td>
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
                    <input v-model="form.labs.ua" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">Purulent:</label>
                    <input v-model="form.labs.purulentSubstance" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">Red Blood Cells:</label>
                    <input v-model="form.labs.rbc" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">CBC:</label>
                    <input v-model="form.labs.cbc" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">Hemoglobin:</label>
                    <input v-model="form.labs.hemoglobin" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">VDRL:</label>
                    <input v-model="form.labs.vdrl" class="input-line flex-1" />
                </div>
                <div class="flex items-center gap-2">
                    <label class="w-28 font-medium">HIV Test:</label>
                    <input v-model="form.labs.hiv" class="input-line flex-1" />
                </div>
                <div class="col-span-2">
                    <label class="block font-medium mb-1">Ultrasound Results:</label>
                    <textarea v-model="form.labs.ultrasound" rows="2"
                        class="w-full border border-gray-300 rounded p-2 text-sm"></textarea>
                </div>
            </div>
        </div>

        <!-- Treatment -->
        <div class="no-break border border-gray-200 rounded p-4 mb-6">
            <h3 class="section-title mb-3">Treatment / Management</h3>
            <div class="grid grid-cols-2 gap-2">
                <label class="cb"><input type="checkbox" v-model="form.treatment.palpation" /> Palpation
                    done</label>
                <label class="cb"><input type="checkbox" v-model="form.treatment.dietAdvice" /> Diet
                    counseling</label>
                <label class="cb"><input type="checkbox" v-model="form.treatment.vitamins" /> Vitamins
                    prescribed</label>
                <label class="cb"><input type="checkbox" v-model="form.treatment.referred" /> Referred to
                    physician</label>
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

        <!-- Save button -->
        <div class="no-print mt-6">
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
.input {
    width: 100%;
    padding: 6px 8px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    outline: none;
    font-size: 13px;
}

.input-table {
    width: 100%;
    padding: 2px;
    border: none;
    text-align: center;
    font-size: 11px;
    background: transparent;
}

.input-line {
    display: block;
    width: 100%;
    border: none;
    border-bottom: 1px solid #94a3b8;
    outline: none;
    padding: 2px;
    background: transparent;
    font-size: 13px;
}

.field-label {
    display: block;
    font-size: 11px;
    font-weight: 600;
    color: #6b7280;
    margin-bottom: 2px;
}

.section-title {
    font-weight: 700;
    font-size: 0.95rem;
    color: #1e293b;
    margin-bottom: 8px;
}

.td {
    border: 1px solid #e5e7eb;
    padding: 2px 4px;
}

.cb {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 0.85rem;
    cursor: pointer;
}

.row-between {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #e5e7eb;
    padding-bottom: 4px;
}
</style>

<style>
@page {
    size: A4 portrait;
    margin: 12mm 12mm;
}

@media print {
    * {
        -webkit-print-color-adjust: exact !important;
        print-color-adjust: exact !important;
    }

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

    body.printing-prenatal .layout-wrapper,
    body.printing-prenatal .layout-main-container,
    body.printing-prenatal .layout-main {
        display: block !important;
        padding: 0 !important;
        margin: 0 !important;
        width: 100% !important;
        min-height: unset !important;
    }

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

    table {
        border-collapse: collapse !important;
        width: 100% !important;
    }

    th,
    td {
        border: 1px solid #333 !important;
    }

    thead tr {
        background-color: #e5e7eb !important;
    }

    .bg-blue-50 {
        background-color: #eff6ff !important;
    }

    .bg-gray-50 {
        background-color: #f9fafb !important;
    }

    .bg-gray-100 {
        background-color: #f3f4f6 !important;
    }

    .no-break {
        page-break-inside: avoid !important;
    }

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
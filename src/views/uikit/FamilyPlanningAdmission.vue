<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFamilyPlanningRecord } from '../../service/FamilyPlanningService.js';
import axios from 'axios';
import { useUserDataStore, UserRole } from '@/stores/userData';

const route = useRoute();
const router = useRouter();
const isViewMode = ref(false);
const existingRecordId = ref(null);

const userStore = useUserDataStore();
if (!userStore.user) {
    userStore.init();
}
const isReadOnly = computed(() => userStore.userRole === UserRole.PATIENT);

// ── Endpoints ────────────────────────────────────────────────────────────────
const BASE = 'http://localhost:8080/api/familyplanning';
const ACK_BASE = 'http://localhost:8080/api/familyplanning/acknowledgements';
const PATIENT_SERVICE_BASE = 'http://localhost:8080/api/patient-services';
const CALENDAR_BASE = 'http://localhost:8080/api/calendar';
const PATIENTS_BASE = 'http://localhost:8080/api/patients';

// Route Params
const patientID = route.params.patientID;
const serviceId = route.params.serviceId;

function goBack() {
    router.back();
}

function parseLocalDate(value) {
    if (!value) return null;
    const dateString = String(value).trim();

    const isoMatch = dateString.match(/^(\d{4})-(\d{2})-(\d{2})/);
    if (isoMatch) {
        return new Date(Number(isoMatch[1]), Number(isoMatch[2]) - 1, Number(isoMatch[3]));
    }

    const mdyMatch = dateString.match(/^(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2,4})$/);
    if (mdyMatch) {
        let year = Number(mdyMatch[3]);
        if (year < 100) {
            year += year < 70 ? 2000 : 1900;
        }
        return new Date(Number(year), Number(mdyMatch[1]) - 1, Number(mdyMatch[2]));
    }

    const parsed = new Date(dateString);
    if (Number.isNaN(parsed.getTime())) {
        return null;
    }
    return new Date(parsed.getFullYear(), parsed.getMonth(), parsed.getDate());
}

function formatLocalDate(value) {
    const date = parseLocalDate(value);
    if (!date) return null;
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${date.getFullYear()}-${month}-${day}`;
}

function parseDateForForm(value) {
    return formatLocalDate(value) || '';
}

const submitStatus = ref({
    loading: false,
    error: '',
    success: ''
});

const failedSections = ref([]);
const patientName = ref('');
const originalFollowUpDate = ref('');

async function fetchPatientName(clientId) {
    try {
        const res = await axios.get(`${PATIENTS_BASE}/${clientId}`);
        const p = res.data;
        patientName.value = `${p.fName || ''} ${p.lName || ''}`.trim();
    } catch (e) {
        console.error('Failed to fetch patient name for calendar sync', e);
    }
}

const formData = ref({
    clientId: '',
    philhealthNo: '',
    spouseName: '',
    birthDate: '',
    age: '',
    occupation: '',
    civilStatus: '',
    serviceID: serviceId ? Number(serviceId) : 1,
    nhtsYes: false,
    nhtsNo: false,
    _4psMember: false,
    averageMonthlyIncome: '',
    numLivingChildren: '',
    planMoreChildren: null,
    clientType: {
        newAcceptor: false,
        currentUser: false,
        changingMethod: false,
        changingClinic: false,
        dropoutRestart: false
    },
    reasonSpacing: false,
    reasonLimiting: false,
    reasonOthers: '',
    medicalCondition: false,
    sideEffects: '',
    currentMethod: {
        coc: false,
        iud: false,
        bom_ccm: false,
        pop: false,
        interval: false,
        bbt: false,
        injectable: false,
        postPartum: false,
        stm: false,
        implant: false,
        condom: false,
        sdm: false,
        lam: false,
        others: ''
    },
    medicalHistory: {
        severeHeadache: null,
        strokeHistory: null,
        hematoma: null,
        breastCancer: null,
        chestPain: null,
        cough: null,
        jaundice: null,
        vaginalBleeding: null,
        abnormalVaginalDischarge: null,
        abnormalPenileDischarge: null,
        phenobarbital: null,
        smoker: null,
        disability: null,
        disabilitySpecify: ''
    },
    obstetric: {
        numPregnancies: '',
        para: '',
        fullTerm: '',
        premature: '',
        abortion: '',
        livingChildren: '',
        lastDeliveryDate: '',
        deliveryType: null,
        lastMenstrualStart: '',
        lastMenstrualEnd: '',
        previousMenstrualPeriod: '',
        menstrualFlow: null,
        dysmenorrhea: false,
        hydatidiformMole: false,
        ectopicPregnancy: false
    },
    vaw: {
        unpleasantRelationship: null,
        partnerDisapproval: null,
        domesticViolence: null,
        referredTo: {
            dswd: false,
            wcpu: false,
            ngos: false,
            othersSpecify: ''
        }
    },
    physical: {
        weight: '',
        bloodPressure: '',
        height: '',
        pulseRate: '',
        skin: { normal: false, pale: false, yellowish: false, hematoma: false },
        conjunctiva: { normal: false, pale: false, yellowish: false },
        neck: { normal: false, neckMass: false, enlargedLymphNodes: false },
        breast: { normal: false, mass: false, nippleDischarge: false },
        abdomen: { normal: false, abdominalMass: false, varicosities: false },
        extremities: { normal: false, edema: false, varicosities: false },
        pelvicExam: {
            normal: false, mass: false, abnormalDischarge: false,
            warts: false, polypCyst: false, inflammationErosion: false,
            bloodyDischarge: false, cervicalConsistency: null,
            cervicalTenderness: false, adnexalMass: false,
            uterinePosition: null, uterineDepth: ''
        },
        sideB: {
            dateOfVisit: new Date().toISOString().split('T')[0],
            serviceProvider: '',
            medicalFindings: '',
            methodAccepted: '',
            followUpDate: '',
            pregnancyCheck: {
                isFullyBreastfeeding: null,
                abstinence: null,
                babyInLast4Weeks: null,
                mensesInPast7Days: null,
                miscarriageInPast7Days: null,
                reliableContraceptive: null
            }
        }
    },
    acknowledgement: {
        acknowledgementId: null,
        chosenMethod: '',
        clientSignature: '',
        clientSignatureDate: new Date().toISOString().split('T')[0],
        wraConsentName: '',
        parentSignature: '',
        parentSignatureDate: ''
    }
});

const employeesList = ref([]);
const selectedServiceProviderId = ref('');

async function fetchEmployees() {
    try {
        const res = await axios.get('http://localhost:8080/api/employees');
        const raw = res.data;
        employeesList.value = Array.isArray(raw)
            ? raw
            : (Array.isArray(raw?.data) ? raw.data
                : (Array.isArray(raw?.employees) ? raw.employees : []));
    } catch (e) {
        console.error('Failed to fetch employees list', e);
    }
}

function onServiceProviderChange() {
    if (selectedServiceProviderId.value === 'custom') {
        formData.value.physical.sideB.serviceProvider = '';
        return;
    }
    const employee = employeesList.value.find(
        emp => Number(emp.employeeID) === Number(selectedServiceProviderId.value)
    );
    formData.value.physical.sideB.serviceProvider = employee
        ? `${employee.fName || ''} ${employee.lName || ''}`.trim()
        : '';
}

function matchServiceProviderToEmployee(name) {
    if (!name) return;
    const found = employeesList.value.find(
        emp => `${emp.fName || ''} ${emp.lName || ''}`.trim().toLowerCase() === name.trim().toLowerCase()
    );
    selectedServiceProviderId.value = found ? Number(found.employeeID) : 'custom';
}

async function syncEmployeeNameToPatientService() {
    const providerName = formData.value.physical.sideB.serviceProvider;
    if (!providerName || !serviceId) return;
    try {
        const currentRes = await axios.get(`${PATIENT_SERVICE_BASE}/${serviceId}`);
        const currentService = currentRes.data;
        await axios.put(PATIENT_SERVICE_BASE, {
            ...currentService,
            employeeName: providerName
        });
    } catch (syncErr) {
        console.error('Failed to sync employee name to PatientService', syncErr);
        failedSections.value.push('Service provider sync');
    }
}

async function submitForm() {
    submitStatus.value.loading = true;
    submitStatus.value.error = '';
    submitStatus.value.success = '';
    failedSections.value = [];

    if (!formData.value.clientId) {
        if (patientID) formData.value.clientId = String(patientID);
    }

    const clientID = Number(formData.value.clientId) || null;
    const today = new Date().toISOString().split('T')[0];

    if (!clientID) {
        submitStatus.value.error = 'Client ID is required.';
        submitStatus.value.loading = false;
        return;
    }

    try {
        // 1. Save Client
        try {
            await axios.post(`${BASE}/clients`, {
                clientID,
                nhts: formData.value.nhtsYes ? 'Y' : 'N',
                is4PSMember: formData.value._4psMember ? 'Y' : 'N',
                civilStatus: formData.value.civilStatus || null
            });
        } catch (e) { console.error('Client save error:', e); failedSections.value.push('Client info'); }

        // 2. Save Spouse
        try {
            if (formData.value.spouseName) {
                const nameParts = formData.value.spouseName.trim().split(' ');
                await axios.post(`${BASE}/spouses`, {
                    clientID,
                    fName: nameParts[0] || '',
                    lName: nameParts[nameParts.length - 1] || '',
                    midInitial: nameParts.length > 2 ? nameParts[1] : '',
                    dateOfBirth: formatLocalDate(formData.value.birthDate),
                    age: formData.value.age ? Number(formData.value.age) : null,
                    occupation: formData.value.occupation || null
                });
            }
        } catch (e) { console.error('Spouse save error:', e); failedSections.value.push('Spouse info'); }

        // 3. Save FamilyPlanningRecord
        await createFamilyPlanningRecord({
            serviceID: serviceId ? Number(serviceId) : (formData.value.serviceID || 1),
            clientID,
            philHealthNumber: formData.value.philhealthNo || null,
            civilStatus: formData.value.civilStatus || null,
            averageMonthlyIncome: formData.value.averageMonthlyIncome ? Number(formData.value.averageMonthlyIncome) : null,
            planToHaveMoreChildren:
                formData.value.planMoreChildren === true ? 'Y' :
                formData.value.planMoreChildren === false ? 'N' : null,
            noOfLivingChildren: formData.value.numLivingChildren ? String(formData.value.numLivingChildren) : null,
            dateRegistered: today
        });

        // 4. Save TypeOfClient
        let typeID = null;
        try {
            const reasonFp = formData.value.reasonSpacing ? 'Spacing' :
                             formData.value.reasonLimiting ? 'Limiting' : 'Others';
            const typeRes = await axios.post(`${BASE}/typeofclient`, {
                clientID,
                isNewAcceptor: formData.value.clientType.newAcceptor,
                isCurrentUser: formData.value.clientType.currentUser,
                isChangingMethod: formData.value.clientType.changingMethod,
                isChangingClinic: formData.value.clientType.changingClinic,
                isDropOutRestart: formData.value.clientType.dropoutRestart,
                reasonForFp: reasonFp,
                reasonOtherDetails: formData.value.reasonOthers || '',
                medicalCondition: formData.value.medicalCondition,
                changeReason: formData.value.sideEffects || '',
                changeReasonOthers: ''
            });
            typeID = typeRes.data.typeID;

            // 5. Save MethodCurrentlyUsed
            try {
                await axios.post(`${BASE}/methods`, {
                    typeID,
                    coc: formData.value.currentMethod.coc,
                    iud: formData.value.currentMethod.iud,
                    pop: formData.value.currentMethod.pop,
                    injectable: formData.value.currentMethod.injectable,
                    implant: formData.value.currentMethod.implant,
                    iudInterval: formData.value.currentMethod.interval,
                    iudPostpartum: formData.value.currentMethod.postPartum,
                    condom: formData.value.currentMethod.condom,
                    bomCmm: formData.value.currentMethod.bom_ccm,
                    bbt: formData.value.currentMethod.bbt,
                    stm: formData.value.currentMethod.stm,
                    sdm: formData.value.currentMethod.sdm,
                    lam: formData.value.currentMethod.lam,
                    otherMethod: formData.value.currentMethod.others || ''
                });
            } catch (e) { console.error('Method save error:', e); failedSections.value.push('Method currently used'); }
        } catch (e) { console.error('TypeOfClient save error:', e); failedSections.value.push('Type of client'); }

        // 6. Save MedicalHistory
        let medicalHistoryID = null;
        try {
            const mhRes = await axios.post(`${BASE}/medicalhistory`, {
                clientID,
                hasDisability: formData.value.medicalHistory.disability === true,
                disabilityDescription: formData.value.medicalHistory.disabilitySpecify || '',
                dateRecorded: today
            });
            medicalHistoryID = mhRes.data.medicalHistoryid;

            const medicalConditions = [
                { id: 1, value: formData.value.medicalHistory.severeHeadache },
                { id: 2, value: formData.value.medicalHistory.strokeHistory },
                { id: 3, value: formData.value.medicalHistory.hematoma },
                { id: 4, value: formData.value.medicalHistory.breastCancer },
                { id: 5, value: formData.value.medicalHistory.chestPain },
                { id: 6, value: formData.value.medicalHistory.cough },
                { id: 7, value: formData.value.medicalHistory.jaundice },
                { id: 8, value: formData.value.medicalHistory.vaginalBleeding },
                { id: 9, value: formData.value.medicalHistory.abnormalVaginalDischarge },
                { id: 10, value: formData.value.medicalHistory.abnormalPenileDischarge },
                { id: 11, value: formData.value.medicalHistory.phenobarbital },
                { id: 12, value: formData.value.medicalHistory.smoker },
            ];

            try {
                await axios.delete(`${BASE}/medicalhistory/detail/medicalHistory/${medicalHistoryID}`);
            } catch (e) {}

            let medicalDetailFailed = false;
            for (const cond of medicalConditions) {
                if (cond.value === true || cond.value === false) {
                    try {
                        await axios.post(`${BASE}/medicalhistory/detail`, {
                            medicalHistoryid: medicalHistoryID,
                            medicalConditionID: cond.id,
                            hasCondition: cond.value
                        });
                    } catch (e) { console.error(`MedicalHistoryDetail ${cond.id} error:`, e); medicalDetailFailed = true; }
                }
            }
            if (medicalDetailFailed) failedSections.value.push('Medical history details');
        } catch (e) { console.error('MedicalHistory save error:', e); failedSections.value.push('Medical history'); }

        // 7. Save ObstetricalHistory
        let obstetricalHistoryID = null;
        try {
            const ohRes = await axios.post(`${BASE}/obstetrical-history`, {
                clientID,
                gravida: formData.value.obstetric.numPregnancies ? Number(formData.value.obstetric.numPregnancies) : null,
                para: formData.value.obstetric.para ? Number(formData.value.obstetric.para) : null,
                fullTerm: formData.value.obstetric.fullTerm ? Number(formData.value.obstetric.fullTerm) : null,
                premature: formData.value.obstetric.premature ? Number(formData.value.obstetric.premature) : null,
                abortion: formData.value.obstetric.abortion ? Number(formData.value.obstetric.abortion) : null,
                livingChildren: formData.value.obstetric.livingChildren ? Number(formData.value.obstetric.livingChildren) : null,
                dateOfLastDelivery: formatLocalDate(formData.value.obstetric.lastDeliveryDate),
                typeOfLastDelivery: formData.value.obstetric.deliveryType || null,
                lastMenstrualPeriod: formatLocalDate(formData.value.obstetric.lastMenstrualStart),
                previousMenstrualPeriod: formatLocalDate(formData.value.obstetric.previousMenstrualPeriod),
                menstrualFlowType: formData.value.obstetric.menstrualFlow || null
            });
            obstetricalHistoryID = ohRes.data.obstetricalHistoryID;

            const obstetricConditions = [
                { id: 1, value: formData.value.obstetric.dysmenorrhea },
                { id: 2, value: formData.value.obstetric.hydatidiformMole },
                { id: 3, value: formData.value.obstetric.ectopicPregnancy },
            ];
            let obstetricDetailFailed = false;
            for (const cond of obstetricConditions) {
                if (cond.value === true) {
                    try {
                        await axios.post(`${BASE}/obstetrical-condition-detail`, {
                            obstetricalHistoryID,
                            obstetricConditionID: cond.id
                        });
                    } catch (e) { console.error(`ObstetricalConditionDetail ${cond.id} error:`, e); obstetricDetailFailed = true; }
                }
            }
            if (obstetricDetailFailed) failedSections.value.push('Obstetric condition details');
        } catch (e) { console.error('ObstetricalHistory save error:', e); failedSections.value.push('Obstetric history'); }

        // 8. Save RiskForVAW
        try {
            const referredAgencies = [
                formData.value.vaw.referredTo.dswd ? 'DSWD' : '',
                formData.value.vaw.referredTo.wcpu ? 'WCPU' : '',
                formData.value.vaw.referredTo.ngos ? "NGO's" : '',
                formData.value.vaw.referredTo.othersSpecify || ''
            ].filter(Boolean).join(', ');

            await axios.post(`${BASE}/risk-vaw`, {
                clientID,
                hasUnpleasantSituation: formData.value.vaw.unpleasantRelationship === true,
                partnerDisapproveVisit: formData.value.vaw.partnerDisapproval === true,
                historyOfDomesticViolence: formData.value.vaw.domesticViolence === true,
                referredToAgency: referredAgencies
            });
        } catch (e) { console.error('RiskForVAW save error:', e); failedSections.value.push('Risk for VAW'); }

        // 9. Save PhysicalExamination
        let pExamID = null;
        try {
            const existingPE = await axios.get(`${BASE}/physical-exam/client/${clientID}`);
            if (existingPE.data && existingPE.data.length > 0) {
                pExamID = existingPE.data[existingPE.data.length - 1].pExamID;
                await axios.put(`${BASE}/physical-exam`, {
                    pExamID,
                    clientID,
                    weight: formData.value.physical.weight ? Number(formData.value.physical.weight) : null,
                    height: formData.value.physical.height ? Number(formData.value.physical.height) : null,
                    bloodPressure: formData.value.physical.bloodPressure || null,
                    pulseRate: formData.value.physical.pulseRate ? Number(formData.value.physical.pulseRate) : null,
                    dateExamined: today,
                    examinerName: formData.value.physical.sideB.serviceProvider || null
                });
            } else {
                const peRes = await axios.post(`${BASE}/physical-exam`, {
                    clientID,
                    weight: formData.value.physical.weight ? Number(formData.value.physical.weight) : null,
                    height: formData.value.physical.height ? Number(formData.value.physical.height) : null,
                    bloodPressure: formData.value.physical.bloodPressure || null,
                    pulseRate: formData.value.physical.pulseRate ? Number(formData.value.physical.pulseRate) : null,
                    dateExamined: today,
                    examinerName: formData.value.physical.sideB.serviceProvider || null
                });
                pExamID = peRes.data.pExamID;
            }

            try { await axios.delete(`${BASE}/skin/exam/${pExamID}`); } catch (e) {}
            const skinConditions = [
                { condition: 'normal', value: formData.value.physical.skin.normal },
                { condition: 'pale', value: formData.value.physical.skin.pale },
                { condition: 'yellowish', value: formData.value.physical.skin.yellowish },
                { condition: 'hematoma', value: formData.value.physical.skin.hematoma },
            ];
            for (const s of skinConditions) {
                if (s.value) {
                    try { await axios.post(`${BASE}/skin`, { pExamID, condition: s.condition }); }
                    catch (e) { console.error('Skin save error:', e); failedSections.value.push('Physical exam - skin'); }
                }
            }

            try { await axios.delete(`${BASE}/conjunctiva/exam/${pExamID}`); } catch (e) {}
            const conjunctivaConditions = [
                { condition: 'normal', value: formData.value.physical.conjunctiva.normal },
                { condition: 'pale', value: formData.value.physical.conjunctiva.pale },
                { condition: 'yellowish', value: formData.value.physical.conjunctiva.yellowish },
            ];
            for (const c of conjunctivaConditions) {
                if (c.value) {
                    try { await axios.post(`${BASE}/conjunctiva`, { pExamID, condition: c.condition }); }
                    catch (e) { console.error('Conjunctiva save error:', e); failedSections.value.push('Physical exam - conjunctiva'); }
                }
            }

            try { await axios.delete(`${BASE}/neck/pExam/${pExamID}`); } catch (e) {}
            const neckConditions = [
                { condition: 'normal', value: formData.value.physical.neck.normal },
                { condition: 'neck mass', value: formData.value.physical.neck.neckMass },
                { condition: 'enlarged lymph nodes', value: formData.value.physical.neck.enlargedLymphNodes },
            ];
            for (const n of neckConditions) {
                if (n.value) {
                    try { await axios.post(`${BASE}/neck`, { pExamID, condition: n.condition }); }
                    catch (e) { console.error('Neck save error:', e); failedSections.value.push('Physical exam - neck'); }
                }
            }

            try { await axios.delete(`${BASE}/breast/pExam/${pExamID}`); } catch (e) {}
            const breastConditions = [
                { condition: 'normal', value: formData.value.physical.breast.normal },
                { condition: 'mass', value: formData.value.physical.breast.mass },
                { condition: 'nipple discharge', value: formData.value.physical.breast.nippleDischarge },
            ];
            for (const b of breastConditions) {
                if (b.value) {
                    try { await axios.post(`${BASE}/breast`, { pExamID, condition: b.condition }); }
                    catch (e) { console.error('Breast save error:', e); failedSections.value.push('Physical exam - breast'); }
                }
            }

            try { await axios.delete(`${BASE}/abdomen/pExam/${pExamID}`); } catch (e) {}
            const abdomenConditions = [
                { condition: 'normal', value: formData.value.physical.abdomen.normal },
                { condition: 'abdominal mass', value: formData.value.physical.abdomen.abdominalMass },
                { condition: 'varicosities', value: formData.value.physical.abdomen.varicosities },
            ];
            for (const a of abdomenConditions) {
                if (a.value) {
                    try { await axios.post(`${BASE}/abdomen`, { pExamID, condition: a.condition }); }
                    catch (e) { console.error('Abdomen save error:', e); failedSections.value.push('Physical exam - abdomen'); }
                }
            }

            try { await axios.delete(`${BASE}/extremities/pExam/${pExamID}`); } catch (e) {}
            const extremitiesConditions = [
                { condition: 'normal', value: formData.value.physical.extremities.normal },
                { condition: 'edema', value: formData.value.physical.extremities.edema },
                { condition: 'varicosities', value: formData.value.physical.extremities.varicosities },
            ];
            for (const ex of extremitiesConditions) {
                if (ex.value) {
                    try { await axios.post(`${BASE}/extremities`, { pExamID, condition: ex.condition }); }
                    catch (e) { console.error('Extremities save error:', e); failedSections.value.push('Physical exam - extremities'); }
                }
            }

            const pelvicConditions = [
                formData.value.physical.pelvicExam.normal ? 'normal' : '',
                formData.value.physical.pelvicExam.mass ? 'mass' : '',
                formData.value.physical.pelvicExam.abnormalDischarge ? 'abnormal discharge' : '',
                formData.value.physical.pelvicExam.warts ? 'warts' : '',
                formData.value.physical.pelvicExam.polypCyst ? 'polyp or cyst' : '',
                formData.value.physical.pelvicExam.inflammationErosion ? 'inflammation or erosion' : '',
                formData.value.physical.pelvicExam.bloodyDischarge ? 'bloody discharge' : '',
            ].filter(Boolean).join(', ');

            try {
                await axios.post(`${BASE}/pelvic-examination`, {
                    pExamID,
                    condition: pelvicConditions || null,
                    cervicalConsistency: formData.value.physical.pelvicExam.cervicalConsistency || null,
                    cervicalTenderness: formData.value.physical.pelvicExam.cervicalTenderness,
                    adnexalMassTenderness: formData.value.physical.pelvicExam.adnexalMass,
                    uterinePosition: formData.value.physical.pelvicExam.uterinePosition || null,
                    uterineDepth: formData.value.physical.pelvicExam.uterineDepth ? Number(formData.value.physical.pelvicExam.uterineDepth) : null
                });
            } catch (e) { console.error('PelvicExamination save error:', e); failedSections.value.push('Pelvic examination'); }

        } catch (e) { console.error('PhysicalExamination save error:', e); failedSections.value.push('Physical examination'); }

        // 10. FpAssessmentRecord
        try {
            await axios.post(`${BASE}/fpassessment`, {
                clientID,
                dateOfVisit: formatLocalDate(formData.value.physical.sideB.dateOfVisit) || today,
                medicalFindings: formData.value.physical.sideB.medicalFindings || null,
                methodAccepted: formData.value.physical.sideB.methodAccepted || null,
                dateOfFollowUpVisit: formatLocalDate(formData.value.physical.sideB.followUpDate)
            });
        } catch (e) { console.error('FpAssessmentRecord save error:', e); failedSections.value.push('FP assessment record'); }

        // 10b. Calendar Sync
        const followUpDateFormatted = formatLocalDate(formData.value.physical.sideB.followUpDate);
        if (followUpDateFormatted && followUpDateFormatted !== originalFollowUpDate.value) {
            try {
                await axios.post(`${CALENDAR_BASE}/manual`, {
                    title: 'Family Planning Follow-up',
                    eventDate: followUpDateFormatted,
                    eventType: 'family-planning-followup',
                    patientName: patientName.value || `Client ID ${clientID}`,
                    description: formData.value.physical.sideB.methodAccepted
                        ? `Follow-up visit — method accepted: ${formData.value.physical.sideB.methodAccepted}`
                        : 'Family Planning follow-up visit'
                });
                originalFollowUpDate.value = followUpDateFormatted;
            } catch (e) {
                console.error('Calendar sync error:', e);
                failedSections.value.push('Calendar sync (follow-up date)');
            }
        }

        // 11. PregnancyExclusionChecklist
        try {
            await axios.post(`${BASE}/pregnancychecklist`, {
                clientID,
                dateRecorded: today,
                question1: formData.value.physical.sideB.pregnancyCheck.isFullyBreastfeeding,
                question2: formData.value.physical.sideB.pregnancyCheck.abstinence,
                question3: formData.value.physical.sideB.pregnancyCheck.babyInLast4Weeks,
                question4: formData.value.physical.sideB.pregnancyCheck.mensesInPast7Days,
                question5: formData.value.physical.sideB.pregnancyCheck.miscarriageInPast7Days,
                question6: formData.value.physical.sideB.pregnancyCheck.reliableContraceptive
            });
        } catch (e) { console.error('PregnancyChecklist save error:', e); failedSections.value.push('Pregnancy checklist'); }

        // 12. Save ACKNOWLEDGEMENT (FIXED ReferenceError HERE)
        try {
            const ackPayload = {
                acknowledgementId: formData.value.acknowledgement.acknowledgementId || null,
                clientID: Number(formData.value.clientId || patientID), // Fixed variable access
                serviceID: serviceId ? Number(serviceId) : null,
                chosenMethod: formData.value.acknowledgement.chosenMethod || null,
                clientSignature: formData.value.acknowledgement.clientSignature || null,
                clientSignatureDate: formatLocalDate(formData.value.acknowledgement.clientSignatureDate),
                wraConsentName: formData.value.acknowledgement.wraConsentName || null,
                parentSignature: formData.value.acknowledgement.parentSignature || null,
                parentSignatureDate: formatLocalDate(formData.value.acknowledgement.parentSignatureDate)
            };
            const ackRes = await axios.post(ACK_BASE, ackPayload);
            if (ackRes.data && ackRes.data.acknowledgementId) {
                formData.value.acknowledgement.acknowledgementId = ackRes.data.acknowledgementId;
            }
        } catch (e) {
            console.error('Acknowledgement save error:', e);
            failedSections.value.push('Acknowledgement section');
        }

        // 13. Sync employee name to PatientService
        await syncEmployeeNameToPatientService();

        if (failedSections.value.length) {
            submitStatus.value.error = `⚠️ Saved, but these sections failed: ${failedSections.value.join(', ')}. Check the console for details and try again.`;
        } else {
            submitStatus.value.success = '✅ All records saved successfully!';
        }

    } catch (error) {
        const msg = error?.response?.data?.message || error?.response?.data || error?.message || 'Unknown error';
        submitStatus.value.error = '❌ Save failed: ' + msg;
        console.error('Family Planning save error:', error?.response?.data || error);
    } finally {
        submitStatus.value.loading = false;
    }
}

function resetForm() {
    if (confirm('Are you sure you want to reset the form?')) {
        location.reload();
    }
}

function printForm() {
    window.print();
}

onMounted(async () => {
    await fetchEmployees();

    if (patientID) formData.value.clientId = String(patientID);

    const clientId = formData.value.clientId;
    if (!clientId) return;

    isViewMode.value = true;
    fetchPatientName(clientId);

    let hasRecordForThisService = false;

    try {
        const res = await axios.get(`${BASE}/records/client/${clientId}`);
        if (res.data && res.data.length > 0) {
            let record = null;
            if (serviceId) {
                const matches = res.data.filter(r => String(r.serviceID) === String(serviceId));
                if (matches.length) {
                    record = matches.reduce((latest, current) =>
                        (!latest || current.fpRecordID > latest.fpRecordID) ? current : latest, null);
                    hasRecordForThisService = true;
                }
            }
            if (!record) {
                record = res.data[res.data.length - 1];
            }
            if (hasRecordForThisService) {
                existingRecordId.value = record.fpRecordID;
                formData.value.philhealthNo = record.philHealthNumber || '';
                formData.value.civilStatus = record.civilStatus || '';
                formData.value.averageMonthlyIncome = record.averageMonthlyIncome || '';
                formData.value.numLivingChildren = record.noOfLivingChildren || '';
                if (record.planToHaveMoreChildren === 'Y') formData.value.planMoreChildren = true;
                else if (record.planToHaveMoreChildren === 'N') formData.value.planMoreChildren = false;
                submitStatus.value.success = `Viewing record #${record.fpRecordID} for Client ID ${clientId}`;
            } else {
                submitStatus.value.success = `New Family Planning record for Client ID ${clientId}`;
            }
        }
    } catch (e) { console.error('Failed to load FP record', e); }

    if (hasRecordForThisService) {
        try {
            const res = await axios.get(`${BASE}/typeofclient/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const t = res.data[res.data.length - 1];
                formData.value.clientType.newAcceptor = t.isNewAcceptor || false;
                formData.value.clientType.currentUser = t.isCurrentUser || false;
                formData.value.clientType.changingMethod = t.isChangingMethod || false;
                formData.value.clientType.changingClinic = t.isChangingClinic || false;
                formData.value.clientType.dropoutRestart = t.isDropOutRestart || false;
                formData.value.reasonSpacing = t.reasonForFp === 'Spacing';
                formData.value.reasonLimiting = t.reasonForFp === 'Limiting';
                formData.value.reasonOthers = t.reasonOtherDetails || '';
                formData.value.medicalCondition = t.medicalCondition || false;
                formData.value.sideEffects = t.changeReason || '';

                try {
                    const mRes = await axios.get(`${BASE}/methods/type/${t.typeID}`);
                    if (mRes.data) {
                        const m = Array.isArray(mRes.data) ? mRes.data[0] : mRes.data;
                        if (m) {
                            formData.value.currentMethod.coc = m.coc || false;
                            formData.value.currentMethod.iud = m.iud || false;
                            formData.value.currentMethod.pop = m.pop || false;
                            formData.value.currentMethod.injectable = m.injectable || false;
                            formData.value.currentMethod.implant = m.implant || false;
                            formData.value.currentMethod.interval = m.iudInterval || false;
                            formData.value.currentMethod.postPartum = m.iudPostpartum || false;
                            formData.value.currentMethod.condom = m.condom || false;
                            formData.value.currentMethod.bom_ccm = m.bomCmm || false;
                            formData.value.currentMethod.bbt = m.bbt || false;
                            formData.value.currentMethod.stm = m.stm || false;
                            formData.value.currentMethod.sdm = m.sdm || false;
                            formData.value.currentMethod.lam = m.lam || false;
                            formData.value.currentMethod.others = m.otherMethod || '';
                        }
                    }
                } catch (e) { console.error('Failed to load Method', e); }
            }
        } catch (e) { console.error('Failed to load TypeOfClient', e); }

        try {
            const res = await axios.get(`${BASE}/medicalhistory/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const m = res.data[res.data.length - 1];
                formData.value.medicalHistory.disability = m.hasDisability || false;
                formData.value.medicalHistory.disabilitySpecify = m.disabilityDescription || '';
                try {
                    const detailRes = await axios.get(`${BASE}/medicalhistory/detail/medicalHistory/${m.medicalHistoryid}`);
                    if (detailRes.data) {
                        const conditionAnswers = new Map(
                            detailRes.data.map(d => [d.medicalConditionID, d.hasCondition])
                        );
                        const conditionMap = {
                            1: 'severeHeadache', 2: 'strokeHistory', 3: 'hematoma',
                            4: 'breastCancer', 5: 'chestPain', 6: 'cough',
                            7: 'jaundice', 8: 'vaginalBleeding', 9: 'abnormalVaginalDischarge',
                            10: 'abnormalPenileDischarge', 11: 'phenobarbital', 12: 'smoker'
                        };
                        for (const [id, key] of Object.entries(conditionMap)) {
                            if (conditionAnswers.has(Number(id))) {
                                const answer = conditionAnswers.get(Number(id));
                                formData.value.medicalHistory[key] = answer === false ? false : true;
                            }
                        }
                    }
                } catch (e) { console.error('Failed to load MedicalHistoryDetails', e); }
            }
        } catch (e) { console.error('Failed to load MedicalHistory', e); }

        try {
            const res = await axios.get(`${BASE}/obstetrical-history/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const o = res.data[res.data.length - 1];
                formData.value.obstetric.numPregnancies = o.gravida || '';
                formData.value.obstetric.para = o.para || '';
                formData.value.obstetric.fullTerm = o.fullTerm || '';
                formData.value.obstetric.premature = o.premature || '';
                formData.value.obstetric.abortion = o.abortion || '';
                formData.value.obstetric.livingChildren = o.livingChildren || '';
                formData.value.obstetric.lastDeliveryDate = parseDateForForm(o.dateOfLastDelivery);
                formData.value.obstetric.deliveryType = o.typeOfLastDelivery || '';
                formData.value.obstetric.lastMenstrualStart = parseDateForForm(o.lastMenstrualPeriod);
                formData.value.obstetric.previousMenstrualPeriod = parseDateForForm(o.previousMenstrualPeriod);
                formData.value.obstetric.menstrualFlow = o.menstrualFlowType || '';

                try {
                    const ocdRes = await axios.get(`${BASE}/obstetrical-condition-detail/history/${o.obstetricalHistoryID}`);
                    if (ocdRes.data) {
                        ocdRes.data.forEach(d => {
                            if (d.obstetricConditionID === 1) formData.value.obstetric.dysmenorrhea = true;
                            if (d.obstetricConditionID === 2) formData.value.obstetric.hydatidiformMole = true;
                            if (d.obstetricConditionID === 3) formData.value.obstetric.ectopicPregnancy = true;
                        });
                    }
                } catch (e) { console.error('Failed to load ObstetricalConditionDetails', e); }
            }
        } catch (e) { console.error('Failed to load ObstetricalHistory', e); }

        try {
            const res = await axios.get(`${BASE}/risk-vaw/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const v = res.data[res.data.length - 1];
                if (v.hasUnpleasantSituation === true) formData.value.vaw.unpleasantRelationship = true;
                else if (v.hasUnpleasantSituation === false) formData.value.vaw.unpleasantRelationship = false;
                if (v.partnerDisapproveVisit === true) formData.value.vaw.partnerDisapproval = true;
                else if (v.partnerDisapproveVisit === false) formData.value.vaw.partnerDisapproval = false;
                if (v.historyOfDomesticViolence === true) formData.value.vaw.domesticViolence = true;
                else if (v.historyOfDomesticViolence === false) formData.value.vaw.domesticViolence = false;
                const agencies = v.referredToAgency || '';
                formData.value.vaw.referredTo.dswd = agencies.includes('DSWD');
                formData.value.vaw.referredTo.wcpu = agencies.includes('WCPU');
                formData.value.vaw.referredTo.ngos = agencies.includes("NGO's");
            }
        } catch (e) { console.error('Failed to load RiskForVAW', e); }

        try {
            const res = await axios.get(`${BASE}/spouses/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const s = res.data[res.data.length - 1];
                formData.value.spouseName = `${s.fName || ''} ${s.midInitial || ''} ${s.lName || ''}`.trim();
                formData.value.birthDate = parseDateForForm(s.dateOfBirth);
                formData.value.age = s.age || '';
                formData.value.occupation = s.occupation || '';
            }
        } catch (e) { console.error('Failed to load Spouse', e); }

        try {
            const res = await axios.get(`${BASE}/clients/${clientId}`);
            if (res.data) {
                formData.value.nhtsYes = res.data.nhts === 'Y';
                formData.value.nhtsNo = res.data.nhts === 'N';
                formData.value._4psMember = res.data.is4PSMember === 'Y';
            }
        } catch (e) { console.error('Failed to load Client', e); }

        try {
            const res = await axios.get(`${BASE}/physical-exam/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const p = res.data[res.data.length - 1];
                formData.value.physical.weight = p.weight || '';
                formData.value.physical.height = p.height || '';
                formData.value.physical.bloodPressure = p.bloodPressure || '';
                formData.value.physical.pulseRate = p.pulseRate || '';
                if (p.examinerName) {
                    formData.value.physical.sideB.serviceProvider = p.examinerName;
                }

                try {
                    const skinRes = await axios.get(`${BASE}/skin/exam/${p.pExamID}`);
                    if (skinRes.data) {
                        skinRes.data.forEach(s => {
                            if (s.condition === 'normal') formData.value.physical.skin.normal = true;
                            if (s.condition === 'pale') formData.value.physical.skin.pale = true;
                            if (s.condition === 'yellowish') formData.value.physical.skin.yellowish = true;
                            if (s.condition === 'hematoma') formData.value.physical.skin.hematoma = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Skin', e); }

                try {
                    const conjRes = await axios.get(`${BASE}/conjunctiva/exam/${p.pExamID}`);
                    if (conjRes.data) {
                        conjRes.data.forEach(c => {
                            if (c.condition === 'normal') formData.value.physical.conjunctiva.normal = true;
                            if (c.condition === 'pale') formData.value.physical.conjunctiva.pale = true;
                            if (c.condition === 'yellowish') formData.value.physical.conjunctiva.yellowish = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Conjunctiva', e); }

                try {
                    const neckRes = await axios.get(`${BASE}/neck/pExam/${p.pExamID}`);
                    if (neckRes.data) {
                        neckRes.data.forEach(n => {
                            if (n.condition === 'normal') formData.value.physical.neck.normal = true;
                            if (n.condition === 'neck mass') formData.value.physical.neck.neckMass = true;
                            if (n.condition === 'enlarged lymph nodes') formData.value.physical.neck.enlargedLymphNodes = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Neck', e); }

                try {
                    const breastRes = await axios.get(`${BASE}/breast/pExam/${p.pExamID}`);
                    if (breastRes.data) {
                        breastRes.data.forEach(b => {
                            if (b.condition === 'normal') formData.value.physical.breast.normal = true;
                            if (b.condition === 'mass') formData.value.physical.breast.mass = true;
                            if (b.condition === 'nipple discharge') formData.value.physical.breast.nippleDischarge = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Breast', e); }

                try {
                    const abdRes = await axios.get(`${BASE}/abdomen/pExam/${p.pExamID}`);
                    if (abdRes.data) {
                        abdRes.data.forEach(a => {
                            if (a.condition === 'normal') formData.value.physical.abdomen.normal = true;
                            if (a.condition === 'abdominal mass') formData.value.physical.abdomen.abdominalMass = true;
                            if (a.condition === 'varicosities') formData.value.physical.abdomen.varicosities = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Abdomen', e); }

                try {
                    const extRes = await axios.get(`${BASE}/extremities/pExam/${p.pExamID}`);
                    if (extRes.data) {
                        extRes.data.forEach(e => {
                            if (e.condition === 'normal') formData.value.physical.extremities.normal = true;
                            if (e.condition === 'edema') formData.value.physical.extremities.edema = true;
                            if (e.condition === 'varicosities') formData.value.physical.extremities.varicosities = true;
                        });
                    }
                } catch (e) { console.error('Failed to load Extremities', e); }

                try {
                    const pelvicRes = await axios.get(`${BASE}/pelvic-examination/pExam/${p.pExamID}`);
                    const records = Array.isArray(pelvicRes.data) ? pelvicRes.data : [];
                    const pv = records.length ? records[records.length - 1] : null;
                    if (pv) {
                        formData.value.physical.pelvicExam.cervicalConsistency = pv.cervicalConsistency || null;
                        formData.value.physical.pelvicExam.cervicalTenderness = pv.cervicalTenderness || false;
                        formData.value.physical.pelvicExam.adnexalMass = pv.adnexalMassTenderness || false;
                        formData.value.physical.pelvicExam.uterinePosition = pv.uterinePosition || null;
                        formData.value.physical.pelvicExam.uterineDepth = pv.uterineDepth || '';
                        const conditions = pv.condition ? pv.condition.split(', ') : [];
                        conditions.forEach(c => {
                            if (c === 'normal') formData.value.physical.pelvicExam.normal = true;
                            if (c === 'mass') formData.value.physical.pelvicExam.mass = true;
                            if (c === 'abnormal discharge') formData.value.physical.pelvicExam.abnormalDischarge = true;
                            if (c === 'warts') formData.value.physical.pelvicExam.warts = true;
                            if (c === 'polyp or cyst') formData.value.physical.pelvicExam.polypCyst = true;
                            if (c === 'inflammation or erosion') formData.value.physical.pelvicExam.inflammationErosion = true;
                            if (c === 'bloody discharge') formData.value.physical.pelvicExam.bloodyDischarge = true;
                        });
                    }
                } catch (e) { console.error('Failed to load PelvicExamination', e); }
            }
        } catch (e) { console.error('Failed to load PhysicalExamination', e); }

        try {
            const res = await axios.get(`${BASE}/fpassessment/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const a = res.data[res.data.length - 1];
                formData.value.physical.sideB.dateOfVisit = parseDateForForm(a.dateOfVisit) || new Date().toISOString().split('T')[0];
                formData.value.physical.sideB.medicalFindings = a.medicalFindings || '';
                formData.value.physical.sideB.methodAccepted = a.methodAccepted || '';
                formData.value.physical.sideB.followUpDate = parseDateForForm(a.dateOfFollowUpVisit);
                originalFollowUpDate.value = formData.value.physical.sideB.followUpDate;
            }
        } catch (e) { console.error('Failed to load FpAssessmentRecord', e); }

        try {
            const res = await axios.get(`${BASE}/pregnancychecklist/client/${clientId}`);
            if (res.data && res.data.length > 0) {
                const p = res.data[res.data.length - 1];
                if (p.question1 === true) formData.value.physical.sideB.pregnancyCheck.isFullyBreastfeeding = true;
                else if (p.question1 === false) formData.value.physical.sideB.pregnancyCheck.isFullyBreastfeeding = false;
                if (p.question2 === true) formData.value.physical.sideB.pregnancyCheck.abstinence = true;
                else if (p.question2 === false) formData.value.physical.sideB.pregnancyCheck.abstinence = false;
                if (p.question3 === true) formData.value.physical.sideB.pregnancyCheck.babyInLast4Weeks = true;
                else if (p.question3 === false) formData.value.physical.sideB.pregnancyCheck.babyInLast4Weeks = false;
                if (p.question4 === true) formData.value.physical.sideB.pregnancyCheck.mensesInPast7Days = true;
                else if (p.question4 === false) formData.value.physical.sideB.pregnancyCheck.mensesInPast7Days = false;
                if (p.question5 === true) formData.value.physical.sideB.pregnancyCheck.miscarriageInPast7Days = true;
                else if (p.question5 === false) formData.value.physical.sideB.pregnancyCheck.miscarriageInPast7Days = false;
                if (p.question6 === true) formData.value.physical.sideB.pregnancyCheck.reliableContraceptive = true;
                else if (p.question6 === false) formData.value.physical.sideB.pregnancyCheck.reliableContraceptive = false;
            }
        } catch (e) { console.error('Failed to load PregnancyChecklist', e); }

        // LOAD SAVED ACKNOWLEDGEMENT
        try {
            const url = serviceId ? `${ACK_BASE}/service/${serviceId}` : `${ACK_BASE}/client/${clientId}`;
            const ackRes = await axios.get(url);
            const ack = Array.isArray(ackRes.data) ? ackRes.data[ackRes.data.length - 1] : ackRes.data;
            if (ack) {
                formData.value.acknowledgement.acknowledgementId = ack.acknowledgementId || ack.acknowledgementID || null;
                formData.value.acknowledgement.chosenMethod = ack.chosenMethod || '';
                formData.value.acknowledgement.clientSignature = ack.clientSignature || '';
                formData.value.acknowledgement.clientSignatureDate = parseDateForForm(ack.clientSignatureDate);
                formData.value.acknowledgement.wraConsentName = ack.wraConsentName || '';
                formData.value.acknowledgement.parentSignature = ack.parentSignature || '';
                formData.value.acknowledgement.parentSignatureDate = parseDateForForm(ack.parentSignatureDate);
            }
        } catch (e) { console.error('Failed to load Acknowledgement', e); }
    }

    matchServiceProviderToEmployee(formData.value.physical.sideB.serviceProvider);
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4">
        <div class="max-w-screen mx-auto bg-white rounded-lg shadow-lg p-8">
            <!-- Header -->
            <div class="border-b-2 border-gray-800 pb-4 mb-6">
                <div class="mb-4">
                    <button @click="goBack"
                        class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg shadow transition">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                        </svg>
                        Back
                    </button>
                </div>
                <h1 class="text-2xl font-bold text-center mb-2 flex items-center justify-center gap-3">
                    FAMILY PLANNING CLIENT ASSESSMENT RECORD
                    <span v-if="isReadOnly" class="text-xs font-semibold text-blue-700 bg-blue-100 px-3 py-1.5 rounded-full">
                      👁️ View Only
                    </span>
                </h1>
                <div class="bg-yellow-50 border border-yellow-300 rounded p-3 text-sm">
                    <p><strong>Instructions for Physician, Nurses and Midwife:</strong> Make sure that the client is not Pregnant by using the questions Listed by Side B. Completely fill out or check the required information; refer accordingly for any abnormal history or medical evaluation</p>
                </div>
            </div>

            <form @submit.prevent="submitForm" class="space-y-8">
                <fieldset :disabled="isReadOnly" class="space-y-8" style="border:none;padding:0;margin:0;min-width:0;">
                    <div class="space-y-2">
                        <div v-if="submitStatus.error" class="text-sm text-red-700 bg-red-100 border border-red-200 rounded p-3">
                            {{ submitStatus.error }}
                        </div>
                        <div v-if="submitStatus.success" class="text-sm text-green-700 bg-green-100 border border-green-200 rounded p-3">
                            {{ submitStatus.success }}
                        </div>
                    </div>

                <!-- Client Information -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Client ID</label>
                        <input v-model="formData.clientId" type="text" readonly
                            class="w-full px-3 py-2 border border-gray-200 bg-gray-100 rounded cursor-not-allowed" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">PHILHEALTH NO</label>
                        <input v-model="formData.philhealthNo" type="text" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Name Of The Spouse</label>
                        <input v-model="formData.spouseName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Birth Date</label>
                        <input v-model="formData.birthDate" type="date" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Age</label>
                        <input v-model="formData.age" type="number" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Occupation</label>
                        <input v-model="formData.occupation" type="text" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Civil Status</label>
                        <select v-model="formData.civilStatus" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                            <option value="">Select status</option>
                            <option value="Single">Single</option>
                            <option value="Married">Married</option>
                            <option value="Widowed">Widowed</option>
                            <option value="Separated">Separated</option>
                            <option value="Divorced">Divorced</option>
                        </select>
                    </div>
                </div>

                <div class="flex items-center gap-6">
                    <span class="font-medium">NHTS?</span>
                    <label class="flex items-center gap-2">
                        <input v-model="formData.nhtsYes" type="checkbox" class="w-4 h-4" />
                        <span>YES</span>
                    </label>
                    <label class="flex items-center gap-2">
                        <input v-model="formData.nhtsNo" type="checkbox" class="w-4 h-4" />
                        <span>NO</span>
                    </label>
                    <label class="flex items-center gap-2 ml-8">
                        <span>4Ps Member:</span>
                        <input v-model="formData._4psMember" type="checkbox" class="w-4 h-4" />
                        <span>YES</span>
                    </label>
                </div>

                <div class="border-t pt-4">
                    <label class="block text-sm font-bold mb-2">AVERAGE MONTHLY INCOME:</label>
                    <input v-model="formData.averageMonthlyIncome" type="text" class="w-full md:w-1/2 px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
                </div>

                <div class="flex items-center gap-4">
                    <label class="font-medium">NO. OF LIVING CHILDREN:</label>
                    <input v-model="formData.numLivingChildren" type="number" class="w-20 px-3 py-2 border border-gray-300 rounded" />
                    <label class="font-medium ml-8">PLAN TO HAVE MORE CHILDREN?</label>
                    <label class="flex items-center gap-2">
                        <input v-model="formData.planMoreChildren" type="radio" :value="true" class="w-4 h-4" />
                        <span>YES</span>
                    </label>
                    <label class="flex items-center gap-2">
                        <input v-model="formData.planMoreChildren" type="radio" :value="false" class="w-4 h-4" />
                        <span>NO</span>
                    </label>
                </div>

                <!-- Type of Client -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">Type Of Client</h3>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div class="space-y-2">
                            <label class="flex items-center gap-2"><input v-model="formData.clientType.newAcceptor" type="checkbox" class="w-4 h-4" /><span>New Acceptor</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.clientType.currentUser" type="checkbox" class="w-4 h-4" /><span>Current User</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.clientType.changingMethod" type="checkbox" class="w-4 h-4" /><span>Changing Method</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.clientType.changingClinic" type="checkbox" class="w-4 h-4" /><span>Changing Clinic</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.clientType.dropoutRestart" type="checkbox" class="w-4 h-4" /><span>Dropout/Restart</span></label>
                        </div>
                        <div class="space-y-4">
                            <div>
                                <p class="font-medium mb-2">Reason for FP:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.reasonSpacing" type="checkbox" class="w-4 h-4" /><span>Spacing</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.reasonLimiting" type="checkbox" class="w-4 h-4" /><span>Limiting</span></label>
                                    <label class="flex items-center gap-2"><span>Others</span><input v-model="formData.reasonOthers" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" /></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">Reason:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.medicalCondition" type="checkbox" class="w-4 h-4" /><span>Medical condition</span></label>
                                    <label class="flex items-center gap-2"><span>Side-effects</span><input v-model="formData.sideEffects" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" /></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Method Currently Used -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">Method currently used (for Changing Method)</h3>
                    <div class="grid grid-cols-2 md:grid-cols-3 gap-3">
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.coc" type="checkbox" class="w-4 h-4" /><span>COC</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.iud" type="checkbox" class="w-4 h-4" /><span>IUD</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.bom_ccm" type="checkbox" class="w-4 h-4" /><span>BOM/CCM</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.pop" type="checkbox" class="w-4 h-4" /><span>POP</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.interval" type="checkbox" class="w-4 h-4" /><span>INTERVAL</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.bbt" type="checkbox" class="w-4 h-4" /><span>BBT</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.injectable" type="checkbox" class="w-4 h-4" /><span>INJECTABLE</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.postPartum" type="checkbox" class="w-4 h-4" /><span>POST-PARTUM</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.stm" type="checkbox" class="w-4 h-4" /><span>STM</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.implant" type="checkbox" class="w-4 h-4" /><span>IMPLANT</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.condom" type="checkbox" class="w-4 h-4" /><span>CONDOM</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.sdm" type="checkbox" class="w-4 h-4" /><span>SDM</span></label>
                        <label class="flex items-center gap-2"><input v-model="formData.currentMethod.lam" type="checkbox" class="w-4 h-4" /><span>LAM</span></label>
                        <label class="flex items-center gap-2"><span>OTHERS:</span><input v-model="formData.currentMethod.others" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" /></label>
                    </div>
                </div>

                <!-- Medical History -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">I. Medical History</h3>
                    <p class="text-sm mb-4">Does the client have any of the following</p>
                    <div class="space-y-2">
                        <div v-for="(item, idx) in [
                            { label: '● Severe headache / migraine', key: 'severeHeadache' },
                            { label: '● History of stroke / heart attack/ hypertension', key: 'strokeHistory' },
                            { label: '● Non-traumatic hematoma / frequent bruising or gum bleeding', key: 'hematoma' },
                            { label: '● Current or history of breast cancer / breast mass', key: 'breastCancer' },
                            { label: '● Severe chest pain', key: 'chestPain' },
                            { label: '● Cough for more than 14 days', key: 'cough' },
                            { label: '● Jaundice', key: 'jaundice' },
                            { label: '● Unexplained vaginal bleeding', key: 'vaginalBleeding' },
                            { label: '● Abnormal vaginal discharge', key: 'abnormalVaginalDischarge' },
                            { label: '● Abnormal penile discharge', key: 'abnormalPenileDischarge' },
                            { label: '● Intake of phenobarbital (anti-seizure) or rifampicin', key: 'phenobarbital' },
                            { label: '● Is the client smoker?', key: 'smoker' },
                            { label: '● With Disability?', key: 'disability' },
                        ]" :key="idx" class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">{{ item.label }}</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory[item.key]" type="radio" :value="true" class="w-4 h-4" /><span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory[item.key]" type="radio" :value="false" class="w-4 h-4" /><span>No</span>
                            </label>
                        </div>
                        <div class="ml-8" v-if="formData.medicalHistory.disability">
                            <label class="text-sm">If yes then please specify</label>
                            <input v-model="formData.medicalHistory.disabilitySpecify" type="text" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                        </div>
                    </div>
                </div>

                <!-- Obstetric History -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">II. OBSTETRIC HISTORY</h3>
                    <div class="space-y-4">
                        <div class="flex items-center gap-4">
                            <span class="text-sm">Number of pregnancies: G</span>
                            <input v-model="formData.obstetric.numPregnancies" type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                            <span>P</span>
                            <input v-model="formData.obstetric.para" type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                        </div>
                        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <div class="flex items-center gap-2"><label class="text-sm">Full Term</label><input v-model="formData.obstetric.fullTerm" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" /></div>
                            <div class="flex items-center gap-2"><label class="text-sm">Premature</label><input v-model="formData.obstetric.premature" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" /></div>
                            <div class="flex items-center gap-2"><label class="text-sm">Abortion</label><input v-model="formData.obstetric.abortion" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" /></div>
                            <div class="flex items-center gap-2"><label class="text-sm">Living Children</label><input v-model="formData.obstetric.livingChildren" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" /></div>
                        </div>
                        <div class="flex items-center gap-4">
                            <label class="text-sm">Date of last delivery:</label>
                            <input v-model="formData.obstetric.lastDeliveryDate" type="date" class="px-3 py-2 border border-gray-300 rounded" />
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label class="text-sm">Last menstrual period:</label>
                                <input v-model="formData.obstetric.lastMenstrualStart" type="date" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                            </div>
                            <div>
                                <label class="text-sm">Previous menstrual period:</label>
                                <input v-model="formData.obstetric.previousMenstrualPeriod" type="date" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                            </div>
                        </div>
                        <div>
                            <label class="text-sm font-medium mb-2 block">Menstrual flow:</label>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2"><input v-model="formData.obstetric.menstrualFlow" type="radio" value="scanty" class="w-4 h-4" /><span class="text-sm">scanty (1-2 pads per day)</span></label>
                                <label class="flex items-center gap-2"><input v-model="formData.obstetric.menstrualFlow" type="radio" value="moderate" class="w-4 h-4" /><span class="text-sm">moderate (3-5 pads per day)</span></label>
                                <label class="flex items-center gap-2"><input v-model="formData.obstetric.menstrualFlow" type="radio" value="heavy" class="w-4 h-4" /><span class="text-sm">heavy (>5 pads per day)</span></label>
                            </div>
                        </div>
                        <div class="space-y-2">
                            <label class="flex items-center gap-2"><input v-model="formData.obstetric.dysmenorrhea" type="checkbox" class="w-4 h-4" /><span class="text-sm">Dysmenorrhea</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.obstetric.hydatidiformMole" type="checkbox" class="w-4 h-4" /><span class="text-sm">Hydatidiform mole (within the last 12 months)</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.obstetric.ectopicPregnancy" type="checkbox" class="w-4 h-4" /><span class="text-sm">History of ectopic pregnancy</span></label>
                        </div>
                    </div>
                </div>

                <!-- VAW -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">IV. Risk For Violence Against Women (VAW)</h3>
                    <div class="space-y-3">
                        <div v-for="(item, idx) in [
                            { label: '● Unpleasant relationship with partner', key: 'unpleasantRelationship' },
                            { label: '● Partner does not approve visit to FP clinic', key: 'partnerDisapproval' },
                            { label: '● History of domestic violence or VAW', key: 'domesticViolence' },
                        ]" :key="idx" class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">{{ item.label }}</span>
                            <label class="flex items-center gap-2"><input v-model="formData.vaw[item.key]" type="radio" :value="true" class="w-4 h-4" /><span>Yes</span></label>
                            <label class="flex items-center gap-2"><input v-model="formData.vaw[item.key]" type="radio" :value="false" class="w-4 h-4" /><span>No</span></label>
                        </div>
                        <div class="ml-8 mt-4">
                            <p class="text-sm font-medium mb-2">referred to:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2"><input v-model="formData.vaw.referredTo.dswd" type="checkbox" class="w-4 h-4" /><span class="text-sm">DSWD</span></label>
                                <label class="flex items-center gap-2"><input v-model="formData.vaw.referredTo.wcpu" type="checkbox" class="w-4 h-4" /><span class="text-sm">WCPU</span></label>
                                <label class="flex items-center gap-2"><input v-model="formData.vaw.referredTo.ngos" type="checkbox" class="w-4 h-4" /><span class="text-sm">NGO's</span></label>
                                <label class="flex items-center gap-2"><span class="text-sm">Others please specify</span><input v-model="formData.vaw.referredTo.othersSpecify" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" /></label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Physical Examination -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">V. PHYSICAL EXAMINATION</h3>
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                        <div><label class="text-sm">Weight:</label><input v-model="formData.physical.weight" type="text" placeholder="kg" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" /></div>
                        <div><label class="text-sm">Blood pressure:</label><input v-model="formData.physical.bloodPressure" type="text" placeholder="mmHg" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" /></div>
                        <div><label class="text-sm">Height:</label><input v-model="formData.physical.height" type="text" placeholder="cm" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" /></div>
                        <div><label class="text-sm">Pulse rate:</label><input v-model="formData.physical.pulseRate" type="text" placeholder="/min" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" /></div>
                    </div>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div class="space-y-6">
                            <div>
                                <p class="font-medium mb-2">SKIN:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.skin.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.skin.pale" type="checkbox" class="w-4 h-4" /><span class="text-sm">pale</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.skin.yellowish" type="checkbox" class="w-4 h-4" /><span class="text-sm">yellowish</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.skin.hematoma" type="checkbox" class="w-4 h-4" /><span class="text-sm">hematoma</span></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">CONJUNCTIVA:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.conjunctiva.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.conjunctiva.pale" type="checkbox" class="w-4 h-4" /><span class="text-sm">pale</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.conjunctiva.yellowish" type="checkbox" class="w-4 h-4" /><span class="text-sm">yellowish</span></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">NECK:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.neck.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.neck.neckMass" type="checkbox" class="w-4 h-4" /><span class="text-sm">neck mass</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.neck.enlargedLymphNodes" type="checkbox" class="w-4 h-4" /><span class="text-sm">enlarged lymph nodes</span></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">BREAST:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.breast.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.breast.mass" type="checkbox" class="w-4 h-4" /><span class="text-sm">mass</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.breast.nippleDischarge" type="checkbox" class="w-4 h-4" /><span class="text-sm">nipple discharge</span></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">ABDOMEN:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.abdomen.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.abdomen.abdominalMass" type="checkbox" class="w-4 h-4" /><span class="text-sm">abdominal mass</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.abdomen.varicosities" type="checkbox" class="w-4 h-4" /><span class="text-sm">varicosities</span></label>
                                </div>
                            </div>
                        </div>
                        <div class="space-y-6">
                            <div>
                                <p class="font-medium mb-2">EXTREMITIES:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.extremities.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.extremities.edema" type="checkbox" class="w-4 h-4" /><span class="text-sm">edema</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.extremities.varicosities" type="checkbox" class="w-4 h-4" /><span class="text-sm">varicosities</span></label>
                                </div>
                            </div>
                            <div>
                                <p class="font-medium mb-2">PELVIC EXAMINATION:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.normal" type="checkbox" class="w-4 h-4" /><span class="text-sm">normal</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.mass" type="checkbox" class="w-4 h-4" /><span class="text-sm">mass</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.abnormalDischarge" type="checkbox" class="w-4 h-4" /><span class="text-sm">abnormal discharge</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.warts" type="checkbox" class="w-4 h-4" /><span class="text-sm">warts</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.polypCyst" type="checkbox" class="w-4 h-4" /><span class="text-sm">polyp or cyst</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.inflammationErosion" type="checkbox" class="w-4 h-4" /><span class="text-sm">inflammation or erosion</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.bloodyDischarge" type="checkbox" class="w-4 h-4" /><span class="text-sm">bloody discharge</span></label>
                                </div>
                                <div class="mt-4 space-y-3">
                                    <div>
                                        <p class="text-sm mb-2">cervical consistency:</p>
                                        <div class="flex gap-4">
                                            <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.cervicalConsistency" type="radio" value="firm" class="w-4 h-4" /><span class="text-sm">firm</span></label>
                                            <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.cervicalConsistency" type="radio" value="soft" class="w-4 h-4" /><span class="text-sm">soft</span></label>
                                        </div>
                                    </div>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.cervicalTenderness" type="checkbox" class="w-4 h-4" /><span class="text-sm">cervical tenderness</span></label>
                                    <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.adnexalMass" type="checkbox" class="w-4 h-4" /><span class="text-sm">adnexal mass / tenderness</span></label>
                                    <div>
                                        <p class="text-sm mb-2">uterine position:</p>
                                        <div class="space-y-2">
                                            <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="mid" class="w-4 h-4" /><span class="text-sm">mid</span></label>
                                            <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="anteflexed" class="w-4 h-4" /><span class="text-sm">anteflexed</span></label>
                                            <label class="flex items-center gap-2"><input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="retroflexed" class="w-4 h-4" /><span class="text-sm">retroflexed</span></label>
                                        </div>
                                    </div>
                                    <div class="flex items-center gap-2">
                                        <label class="text-sm">uterine depth:</label>
                                        <input v-model="formData.physical.pelvicExam.uterineDepth" type="text" placeholder="cm" class="w-24 px-2 py-1 border border-gray-300 rounded" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </fieldset>
                
                        <!-- ACKNOWLEDGEMENT SECTION -->
                        <div class="border-2 border-gray-800 p-6 mt-8 rounded bg-gray-50">
                            <h3 class="font-bold text-base uppercase mb-3 border-b border-gray-300 pb-1 tracking-wider text-gray-900">
                                ACKNOWLEDGEMENT:
                            </h3>
                            
                            <p class="text-sm leading-relaxed text-gray-800 mb-6">
                                This is to certify that the Physician/Nurse/Midwife of the clinic has fully explained to me the different methods available in family planning and I freely choose the
                                <input 
                                    v-model="formData.acknowledgement.chosenMethod" 
                                    type="text" 
                                    placeholder="e.g. COC / Injectable" 
                                    class="inline-input font-semibold px-2 text-center border-b border-gray-800 focus:outline-none" 
                                    style="min-width: 180px;"
                                />
                                method.
                            </p>

                            <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mb-6">
                                <div class="text-center">
                                    <input 
                                        v-model="formData.acknowledgement.clientSignature" 
                                        type="text" 
                                        placeholder="Enter Client Name/Signature" 
                                        class="w-full text-center border-b border-gray-800 pb-1 mb-1 focus:outline-none bg-transparent"
                                    />
                                    <p class="text-xs font-semibold text-gray-600 uppercase">Client Signature</p>
                                </div>
                                <div class="text-center">
                                    <input 
                                        v-model="formData.acknowledgement.clientSignatureDate" 
                                        type="date" 
                                        class="w-full text-center border-b border-gray-800 pb-1 mb-1 focus:outline-none bg-transparent"
                                    />
                                    <p class="text-xs font-semibold text-gray-600 uppercase">Date</p>
                                </div>
                            </div>

                            <div class="border-t border-gray-300 pt-4">
                                <p class="text-xs font-bold text-gray-700 uppercase mb-3">For WRA below 18 yrs. Old:</p>
                                
                                <p class="text-sm leading-relaxed text-gray-800 mb-6">
                                    I hereby consent 
                                    <input 
                                        v-model="formData.acknowledgement.wraConsentName" 
                                        type="text" 
                                        placeholder="Name of Client" 
                                        class="inline-input font-semibold px-2 text-center border-b border-gray-800 focus:outline-none" 
                                        style="min-width: 220px;"
                                    />
                                    to accept the Family Planning method.
                                </p>

                                <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                                    <div class="text-center">
                                        <input 
                                            v-model="formData.acknowledgement.parentSignature" 
                                            type="text" 
                                            placeholder="Parent/Guardian Signature" 
                                            class="w-full text-center border-b border-gray-800 pb-1 mb-1 focus:outline-none bg-transparent"
                                        />
                                        <p class="text-xs font-semibold text-gray-600 uppercase">Parent/Guardian Signature</p>
                                    </div>
                                    <div class="text-center">
                                        <input 
                                            v-model="formData.acknowledgement.parentSignatureDate" 
                                            type="date" 
                                            class="w-full text-center border-b border-gray-800 pb-1 mb-1 focus:outline-none bg-transparent"
                                        />
                                        <p class="text-xs font-semibold text-gray-600 uppercase">Date</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                <!-- Side B -->
                <div class="border-t-4 border-blue-600 pt-8 mt-12">
                    <h2 class="text-xl font-bold mb-6 text-blue-900">SIDE B: FAMILY PLANNING ASSESSMENT RECORD</h2>
                    <div class="bg-white border rounded-xl shadow-sm p-6 space-y-6">
                        <fieldset :disabled="isReadOnly" class="space-y-6" style="border:none;padding:0;margin:0;min-width:0;">
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-semibold mb-1">Date of Visit</label>
                                <input v-model="formData.physical.sideB.dateOfVisit" type="date" class="w-full p-2 border rounded-md" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold mb-1">Service Provider Name</label>
                                <select
                                    v-model="selectedServiceProviderId"
                                    @change="onServiceProviderChange"
                                    class="w-full p-2 border rounded-md"
                                >
                                    <option value="" disabled>-- Select service provider --</option>
                                    <option
                                        v-for="employee in employeesList"
                                        :key="employee.employeeID"
                                        :value="Number(employee.employeeID)"
                                    >
                                        {{ employee.fName }} {{ employee.lName }}
                                    </option>
                                    <option value="custom">-- Custom / Not Listed --</option>
                                </select>
                                <input
                                    v-if="selectedServiceProviderId === 'custom'"
                                    v-model="formData.physical.sideB.serviceProvider"
                                    type="text"
                                    placeholder="Enter provider name"
                                    class="w-full p-2 border rounded-md mt-2"
                                />
                                <p v-if="employeesList.length === 0" class="text-xs text-gray-400 mt-1">
                                    No employees found — check the employees endpoint.
                                </p>
                            </div>
                        </div>
                        <div>
                            <label class="block text-sm font-semibold mb-1">Medical Findings</label>
                            <textarea v-model="formData.physical.sideB.medicalFindings" rows="3" class="w-full p-2 border rounded-md"></textarea>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                            </div>
                            <div>
                                <label class="block text-sm font-semibold mb-1">
                                    Follow-up Date
                                    <span class="text-xs font-normal text-gray-400">(syncs to Calendar on save)</span>
                                </label>
                                <input v-model="formData.physical.sideB.followUpDate" type="date" class="w-full p-2 border rounded-md" />
                            </div>
                        </div>
                        <div class="mt-8">
                            <h3 class="font-bold text-lg mb-4 text-blue-900 underline">How to be Reasonably Sure a Client is Not Pregnant</h3>
                            <div class="space-y-2">
                                <div class="flex items-center justify-between p-2 bg-gray-50 rounded">
                                    <span class="text-sm">1. Baby &lt;6 months old / Fully Breastfeeding / No period?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.isFullyBreastfeeding"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.isFullyBreastfeeding"> No</label>
                                    </div>
                                </div>
                                <div class="flex items-center justify-between p-2 rounded">
                                    <span class="text-sm">2. Abstained from sex since last period/delivery?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.abstinence"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.abstinence"> No</label>
                                    </div>
                                </div>
                                <div class="flex items-center justify-between p-2 bg-gray-50 rounded">
                                    <span class="text-sm">3. Had a baby in the last 4 weeks?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.babyInLast4Weeks"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.babyInLast4Weeks"> No</label>
                                    </div>
                                </div>
                                <div class="flex items-center justify-between p-2 rounded">
                                    <span class="text-sm">4. Period started within the past 7 days?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.mensesInPast7Days"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.mensesInPast7Days"> No</label>
                                    </div>
                                </div>
                                <div class="flex items-center justify-between p-2 bg-gray-50 rounded">
                                    <span class="text-sm">5. Miscarriage/Abortion in the last 7 days?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.miscarriageInPast7Days"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.miscarriageInPast7Days"> No</label>
                                    </div>
                                </div>
                                <div class="flex items-center justify-between p-2 rounded">
                                    <span class="text-sm">6. Using reliable contraceptive consistently?</span>
                                    <div class="flex gap-4">
                                        <label><input type="radio" :value="true" v-model="formData.physical.sideB.pregnancyCheck.reliableContraceptive"> Yes</label>
                                        <label><input type="radio" :value="false" v-model="formData.physical.sideB.pregnancyCheck.reliableContraceptive"> No</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </fieldset>
                        <!-- Action Buttons -->
                        <div class="flex gap-4 mt-8 no-print">
                            <button v-if="!isReadOnly" type="submit" :disabled="submitStatus.loading" class="bg-blue-600 text-white px-8 py-3 rounded font-bold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed">
                                {{ submitStatus.loading ? 'Saving...' : 'SUBMIT RECORD' }}
                            </button>
                            <button v-if="!isReadOnly" @click="resetForm" type="button" class="bg-gray-200 px-8 py-3 rounded font-bold">RESET</button>
                            <button @click="printForm" type="button" class="bg-green-600 text-white px-8 py-3 rounded font-bold hover:bg-green-700 flex items-center gap-2">
                                🖨️ PRINT / SAVE PDF
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<style>
.inline-input {
    background: transparent;
    border-radius: 0;
}

@media print {
    .no-print, nav, aside, header, .sidebar, #sidebar { display: none !important; }
    body, html { margin: 0 !important; padding: 0 !important; background: white !important; }
    .min-h-screen { min-height: unset !important; background: white !important; padding: 0 !important; }
    .max-w-screen { max-width: 100% !important; box-shadow: none !important; padding: 10px !important; }
    * { color: black !important; background: white !important; }
    input[type="checkbox"], input[type="radio"] { -webkit-print-color-adjust: exact; print-color-adjust: exact; }
    input[type="text"], input[type="date"] { border-bottom: 1px solid black !important; }
}
</style>
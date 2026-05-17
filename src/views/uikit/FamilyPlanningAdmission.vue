<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createFamilyPlanningRecord } from '../../service/FamilyPlanningService.js';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const isViewMode = ref(false);
const existingRecordId = ref(null);

function goBack() {
    router.back();
}

const submitStatus = ref({
    loading: false,
    error: '',
    success: ''
});

const formData = ref({
    clientId: '',
    philhealthNo: '',
    spouseName: '',
    birthDate: '',
    age: '',
    occupation: '',
    civilStatus: '',
    serviceID: 1,
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
                isFullyBreastfeeding: false,
                abstinence: false,
                babyInLast4Weeks: false,
                mensesInPast7Days: false,
                miscarriageInPast7Days: false,
                reliableContraceptive: false
            }
        }
    }
});

async function submitForm() {
    submitStatus.value.loading = true;
    submitStatus.value.error = '';
    submitStatus.value.success = '';

    // ✅ Auto-get patientID from route if clientId is empty
    if (!formData.value.clientId) {
        const patientID = route.params.patientID;
        if (patientID) {
            formData.value.clientId = String(patientID);
        }
    }

    const clientID = Number(formData.value.clientId) || null;
    const today = new Date().toISOString().split('T')[0];

    if (!clientID) {
        submitStatus.value.error = 'Client ID is required.';
        submitStatus.value.loading = false;
        return;
    }

    try {
        // ============================
        // 1. Save Client (NHTS, 4PS)
        // ============================
        try {
            await axios.post('http://localhost:8080/api/familyplanning/clients', {
                clientID,
                nhts: formData.value.nhtsYes ? 'Y' : 'N',
                is4PSMember: formData.value._4psMember ? 'Y' : 'N',
                civilStatus: formData.value.civilStatus || null
            });
        } catch (e) { console.error('Client save error:', e); }

        // ============================
        // 2. Save Spouse
        // ============================
        try {
            if (formData.value.spouseName) {
                const nameParts = formData.value.spouseName.trim().split(' ');
                await axios.post('http://localhost:8080/api/familyplanning/spouse', {
                    clientID,
                    fName: nameParts[0] || '',
                    lName: nameParts[nameParts.length - 1] || '',
                    midInitial: nameParts.length > 2 ? nameParts[1] : '',
                    dateOfBirth: formData.value.birthDate || null,
                    age: formData.value.age ? Number(formData.value.age) : null,
                    occupation: formData.value.occupation || null
                });
            }
        } catch (e) { console.error('Spouse save error:', e); }

        // ============================
        // 3. Save FamilyPlanningRecord
        // ============================
        const savedRecord = await createFamilyPlanningRecord({
            serviceID: formData.value.serviceID || 1,
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
        submitStatus.value.success = '✅ Record saved successfully. ID: ' + savedRecord.fpRecordID;

        // ============================
        // 4. Save TypeOfClient
        // ============================
        let typeID = null;
        try {
            const reasonFp = formData.value.reasonSpacing ? 'Spacing' :
                             formData.value.reasonLimiting ? 'Limiting' : 'Others';
            const typeRes = await axios.post('http://localhost:8080/api/familyplanning/typeofclient', {
                clientID,
                isNewAcceptor: formData.value.clientType.newAcceptor,
                isCurrentUser: formData.value.clientType.currentUser,
                isChangingMethod: formData.value.clientType.changingMethod,
                isChangingClinic: formData.value.clientType.changingClinic,
                isDropOutRestart: formData.value.clientType.dropoutRestart,
                reasonForFp: reasonFp,
                reasonOtherDetails: formData.value.reasonOthers || '',
                changeReason: formData.value.sideEffects || '',
                changeReasonOthers: ''
            });
            typeID = typeRes.data.typeID;

            // ============================
            // 5. Save MethodCurrentlyUsed
            // ============================
            try {
                await axios.post('http://localhost:8080/api/familyplanning/methods', {
                    typeID,
                    coc: formData.value.currentMethod.coc,
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
            } catch (e) { console.error('MethodCurrentlyUsed save error:', e); }
        } catch (e) { console.error('TypeOfClient save error:', e); }

        // ============================
        // 6. Save MedicalHistory
        // ============================
        let medicalHistoryID = null;
        try {
            const mhRes = await axios.post('http://localhost:8080/api/familyplanning/medicalhistory', {
                clientID,
                hasDisability: formData.value.medicalHistory.disability === true,
                disabilityDescription: formData.value.medicalHistory.disabilitySpecify || '',
                dateRecorded: today
            });
            medicalHistoryID = mhRes.data.medicalHistoryid;

            // ============================
            // 7. Save MedicalHistoryDetails
            // (severeHeadache=1, strokeHistory=2, hematoma=3,
            //  breastCancer=4, chestPain=5, cough=6, jaundice=7,
            //  vaginalBleeding=8, abnormalVaginalDischarge=9,
            //  abnormalPenileDischarge=10, phenobarbital=11,
            //  smoker=12)
            // ============================
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

            for (const cond of medicalConditions) {
                if (cond.value === true) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/medicalhistory/detail', {
                            medicalHistoryid: medicalHistoryID,
                            medicalConditionID: cond.id
                        });
                    } catch (e) { console.error(`MedicalHistoryDetail ${cond.id} error:`, e); }
                }
            }
        } catch (e) { console.error('MedicalHistory save error:', e); }

        // ============================
        // 8. Save ObstetricalHistory
        // ============================
        let obstetricalHistoryID = null;
        try {
            const ohRes = await axios.post('http://localhost:8080/api/familyplanning/obstetrical-history', {
                clientID,
                gravida: formData.value.obstetric.numPregnancies ? Number(formData.value.obstetric.numPregnancies) : null,
                para: null,
                fullTerm: formData.value.obstetric.fullTerm ? Number(formData.value.obstetric.fullTerm) : null,
                premature: formData.value.obstetric.premature ? Number(formData.value.obstetric.premature) : null,
                abortion: formData.value.obstetric.abortion ? Number(formData.value.obstetric.abortion) : null,
                livingChildren: formData.value.obstetric.livingChildren ? Number(formData.value.obstetric.livingChildren) : null,
                dateOfLastDelivery: formData.value.obstetric.lastDeliveryDate || null,
                typeOfLastDelivery: formData.value.obstetric.deliveryType || null,
                lastMenstrualPeriod: formData.value.obstetric.lastMenstrualStart || null,
                previousMenstrualPeriod: formData.value.obstetric.previousMenstrualPeriod || null,
                menstrualFlowType: formData.value.obstetric.menstrualFlow || null
            });
            obstetricalHistoryID = ohRes.data.obstetricalHistoryID;

            // ============================
            // 9. Save ObstetricalConditionDetails
            // (dysmenorrhea=1, hydatidiformMole=2, ectopicPregnancy=3)
            // ============================
            const obstetricConditions = [
                { id: 1, value: formData.value.obstetric.dysmenorrhea },
                { id: 2, value: formData.value.obstetric.hydatidiformMole },
                { id: 3, value: formData.value.obstetric.ectopicPregnancy },
            ];

            for (const cond of obstetricConditions) {
                if (cond.value === true) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/obstetrical-condition-detail', {
                            obstetricalHistoryID,
                            obstetricConditionID: cond.id
                        });
                    } catch (e) { console.error(`ObstetricalConditionDetail ${cond.id} error:`, e); }
                }
            }
        } catch (e) { console.error('ObstetricalHistory save error:', e); }

        // ============================
        // 10. Save RiskForVAW
        // ============================
        try {
            const referredAgencies = [
                formData.value.vaw.referredTo.dswd ? 'DSWD' : '',
                formData.value.vaw.referredTo.wcpu ? 'WCPU' : '',
                formData.value.vaw.referredTo.ngos ? "NGO's" : '',
                formData.value.vaw.referredTo.othersSpecify || ''
            ].filter(Boolean).join(', ');

            await axios.post('http://localhost:8080/api/familyplanning/risk-vaw', {
                clientID,
                hasUnpleasantSituation: formData.value.vaw.unpleasantRelationship === true,
                partnerDisapproveVisit: formData.value.vaw.partnerDisapproval === true,
                historyOfDomesticViolence: formData.value.vaw.domesticViolence === true,
                referredToAgency: referredAgencies
            });
        } catch (e) { console.error('RiskForVAW save error:', e); }

        // ============================
        // 11. Save PhysicalExamination
        // ============================
        let pExamID = null;
        try {
            const peRes = await axios.post('http://localhost:8080/api/familyplanning/physical-examination', {
                clientID,
                weight: formData.value.physical.weight ? Number(formData.value.physical.weight) : null,
                height: formData.value.physical.height ? Number(formData.value.physical.height) : null,
                bloodPressure: formData.value.physical.bloodPressure || null,
                pulseRate: formData.value.physical.pulseRate ? Number(formData.value.physical.pulseRate) : null,
                dateExamined: today,
                examinerName: formData.value.physical.sideB.serviceProvider || null
            });
            pExamID = peRes.data.pExamID;

            // ============================
            // 12. Save Skin conditions
            // ============================
            const skinConditions = [
                { condition: 'normal', value: formData.value.physical.skin.normal },
                { condition: 'pale', value: formData.value.physical.skin.pale },
                { condition: 'yellowish', value: formData.value.physical.skin.yellowish },
                { condition: 'hematoma', value: formData.value.physical.skin.hematoma },
            ];
            for (const s of skinConditions) {
                if (s.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/skin', {
                            pExamID, condition: s.condition
                        });
                    } catch (e) { console.error('Skin save error:', e); }
                }
            }

            // ============================
            // 13. Save Conjunctiva conditions
            // ============================
            const conjunctivaConditions = [
                { condition: 'normal', value: formData.value.physical.conjunctiva.normal },
                { condition: 'pale', value: formData.value.physical.conjunctiva.pale },
                { condition: 'yellowish', value: formData.value.physical.conjunctiva.yellowish },
            ];
            for (const c of conjunctivaConditions) {
                if (c.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/conjunctiva', {
                            pExamID, condition: c.condition
                        });
                    } catch (e) { console.error('Conjunctiva save error:', e); }
                }
            }

            // ============================
            // 14. Save Neck conditions
            // ============================
            const neckConditions = [
                { condition: 'normal', value: formData.value.physical.neck.normal },
                { condition: 'neck mass', value: formData.value.physical.neck.neckMass },
                { condition: 'enlarged lymph nodes', value: formData.value.physical.neck.enlargedLymphNodes },
            ];
            for (const n of neckConditions) {
                if (n.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/neck', {
                            pExamID, condition: n.condition
                        });
                    } catch (e) { console.error('Neck save error:', e); }
                }
            }

            // ============================
            // 15. Save Breast conditions
            // ============================
            const breastConditions = [
                { condition: 'normal', value: formData.value.physical.breast.normal },
                { condition: 'mass', value: formData.value.physical.breast.mass },
                { condition: 'nipple discharge', value: formData.value.physical.breast.nippleDischarge },
            ];
            for (const b of breastConditions) {
                if (b.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/breast', {
                            pExamID, condition: b.condition
                        });
                    } catch (e) { console.error('Breast save error:', e); }
                }
            }

            // ============================
            // 16. Save Abdomen conditions
            // ============================
            const abdomenConditions = [
                { condition: 'normal', value: formData.value.physical.abdomen.normal },
                { condition: 'abdominal mass', value: formData.value.physical.abdomen.abdominalMass },
                { condition: 'varicosities', value: formData.value.physical.abdomen.varicosities },
            ];
            for (const a of abdomenConditions) {
                if (a.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/abdomen', {
                            pExamID, condition: a.condition
                        });
                    } catch (e) { console.error('Abdomen save error:', e); }
                }
            }

            // ============================
            // 17. Save Extremities conditions
            // ============================
            const extremitiesConditions = [
                { condition: 'normal', value: formData.value.physical.extremities.normal },
                { condition: 'edema', value: formData.value.physical.extremities.edema },
                { condition: 'varicosities', value: formData.value.physical.extremities.varicosities },
            ];
            for (const ex of extremitiesConditions) {
                if (ex.value) {
                    try {
                        await axios.post('http://localhost:8080/api/familyplanning/extremities', {
                            pExamID, condition: ex.condition
                        });
                    } catch (e) { console.error('Extremities save error:', e); }
                }
            }

            // ============================
            // 18. Save PelvicExamination
            // ============================
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
                await axios.post('http://localhost:8080/api/familyplanning/pelvic-examination', {
                    pExamID,
                    condition: pelvicConditions || null,
                    cervicalConsistency: formData.value.physical.pelvicExam.cervicalConsistency || null,
                    cervicalTenderness: formData.value.physical.pelvicExam.cervicalTenderness,
                    adnexalMassTenderness: formData.value.physical.pelvicExam.adnexalMass,
                    uterinePosition: formData.value.physical.pelvicExam.uterinePosition || null,
                    uterineDepth: formData.value.physical.pelvicExam.uterineDepth ? Number(formData.value.physical.pelvicExam.uterineDepth) : null
                });
            } catch (e) { console.error('PelvicExamination save error:', e); }

        } catch (e) { console.error('PhysicalExamination save error:', e); }

        // ============================
        // 19. Save FpAssessmentRecord (Side B)
        // ============================
        try {
            await axios.post('http://localhost:8080/api/familyplanning/assessment-records', {
                clientID,
                dateOfVisit: formData.value.physical.sideB.dateOfVisit || today,
                medicalFindings: formData.value.physical.sideB.medicalFindings || null,
                methodAccepted: formData.value.physical.sideB.methodAccepted || null,
                dateOfFollowUpVisit: formData.value.physical.sideB.followUpDate || null
            });
        } catch (e) { console.error('FpAssessmentRecord save error:', e); }

        // ============================
        // 20. Save PregnancyExclusionChecklist
        // ============================
        try {
            await axios.post('http://localhost:8080/api/familyplanning/pregnancy-checklist', {
                clientID,
                dateRecorded: today,
                question1: formData.value.physical.sideB.pregnancyCheck.isFullyBreastfeeding,
                question2: formData.value.physical.sideB.pregnancyCheck.abstinence,
                question3: formData.value.physical.sideB.pregnancyCheck.babyInLast4Weeks,
                question4: formData.value.physical.sideB.pregnancyCheck.mensesInPast7Days,
                question5: formData.value.physical.sideB.pregnancyCheck.miscarriageInPast7Days,
                question6: formData.value.physical.sideB.pregnancyCheck.reliableContraceptive
            });
        } catch (e) { console.error('PregnancyChecklist save error:', e); }

        submitStatus.value.success = '✅ All records saved successfully!';

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
    // ✅ Get patientID from route params
    const patientID = route.params.patientID;

    if (patientID) {
        formData.value.clientId = String(patientID);
    }

    const clientId = formData.value.clientId;
    if (!clientId) return;

    isViewMode.value = true;

    // 1. Load main FP record
    try {
        const res = await axios.get(`http://localhost:8080/api/familyplanning/records/client/${clientId}`);
        if (res.data && res.data.length > 0) {
            const record = res.data[res.data.length - 1];
            existingRecordId.value = record.fpRecordID;
            formData.value.philhealthNo = record.philHealthNumber || '';
            formData.value.civilStatus = record.civilStatus || '';
            formData.value.averageMonthlyIncome = record.averageMonthlyIncome || '';
            formData.value.numLivingChildren = record.noOfLivingChildren || '';
            if (record.planToHaveMoreChildren === 'Y') formData.value.planMoreChildren = true;
            else if (record.planToHaveMoreChildren === 'N') formData.value.planMoreChildren = false;
            submitStatus.value.success = `Viewing record #${record.fpRecordID} for Client ID ${clientId}`;
        }
    } catch (e) { console.error('Failed to load FP record', e); }

    // 2. Load TypeOfClient
    try {
        const res = await axios.get(`http://localhost:8080/api/familyplanning/typeofclient/client/${clientId}`);
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
            formData.value.sideEffects = t.changeReason || '';

            // 3. Load MethodCurrentlyUsed
            try {
                const mRes = await axios.get(`http://localhost:8080/api/familyplanning/methods/type/${t.typeID}`);
                if (mRes.data) {
                    const m = Array.isArray(mRes.data) ? mRes.data[0] : mRes.data;
                    if (m) {
                        formData.value.currentMethod.coc = m.coc || false;
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
            } catch (e) { console.error('Failed to load MethodCurrentlyUsed', e); }
        }
    } catch (e) { console.error('Failed to load TypeOfClient', e); }

    // 4. Load MedicalHistory
    try {
        const res = await axios.get(`http://localhost:8080/api/familyplanning/medicalhistory/client/${clientId}`);
        if (res.data && res.data.length > 0) {
            const m = res.data[res.data.length - 1];
            formData.value.medicalHistory.disability = m.hasDisability || false;
            formData.value.medicalHistory.disabilitySpecify = m.disabilityDescription || '';
        }
    } catch (e) { console.error('Failed to load MedicalHistory', e); }

    // 5. Load ObstetricalHistory
    try {
        const res = await axios.get(`http://localhost:8080/api/familyplanning/obstetrical-history/client/${clientId}`);
        if (res.data && res.data.length > 0) {
            const o = res.data[res.data.length - 1];
            formData.value.obstetric.numPregnancies = o.gravida || '';
            formData.value.obstetric.fullTerm = o.fullTerm || '';
            formData.value.obstetric.premature = o.premature || '';
            formData.value.obstetric.abortion = o.abortion || '';
            formData.value.obstetric.livingChildren = o.livingChildren || '';
            formData.value.obstetric.lastDeliveryDate = o.dateOfLastDelivery || '';
            formData.value.obstetric.deliveryType = o.typeOfLastDelivery || '';
            formData.value.obstetric.lastMenstrualStart = o.lastMenstrualPeriod || '';
            formData.value.obstetric.previousMenstrualPeriod = o.previousMenstrualPeriod || '';
            formData.value.obstetric.menstrualFlow = o.menstrualFlowType || '';
        }
    } catch (e) { console.error('Failed to load ObstetricalHistory', e); }

    // 6. Load RiskForVAW
    try {
        const res = await axios.get(`http://localhost:8080/api/familyplanning/risk-vaw/client/${clientId}`);
        if (res.data && res.data.length > 0) {
            const v = res.data[res.data.length - 1];
            formData.value.vaw.unpleasantRelationship = v.hasUnpleasantSituation || false;
            formData.value.vaw.partnerDisapproval = v.partnerDisapproveVisit || false;
            formData.value.vaw.domesticViolence = v.historyOfDomesticViolence || false;
            const agencies = v.referredToAgency || '';
            formData.value.vaw.referredTo.dswd = agencies.includes('DSWD');
            formData.value.vaw.referredTo.wcpu = agencies.includes('WCPU');
            formData.value.vaw.referredTo.ngos = agencies.includes("NGO's");
        }
    } catch (e) { console.error('Failed to load RiskForVAW', e); }
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4">
        <div class="max-w-screen mx-auto bg-white rounded-lg shadow-lg p-8">
            <!-- Header -->
            <div class="border-b-2 border-gray-800 pb-4 mb-6">
                <!-- Back Button -->
                <div class="mb-4">
                    <button
                        @click="goBack"
                        class="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg shadow transition"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                        </svg>
                        Back
                    </button>
                </div>
                <h1 class="text-2xl font-bold text-center mb-2">FAMILY PLANNING CLIENT ASSESSMENT RECORD</h1>
                <div class="bg-yellow-50 border border-yellow-300 rounded p-3 text-sm">
                    <p><strong>Instructions for Physician, Nurses and Midwife:</strong> Make sure that the client is not Pregnant by using the questions Listed by Side B. Completely fill out or check the required information; refer accordingly for any abnormal history or medical evaluation</p>
                </div>
            </div>

            <form @submit.prevent="submitForm" class="space-y-8">
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
                  <!-- To this: -->
<div>
    <label class="block text-sm font-medium mb-1">Client ID</label>
    <input v-model="formData.clientId" type="text" 
        readonly
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
                    <label class="flex items-center gap-2">
                        <input v-model="formData.no_4psMember" type="checkbox" class="w-4 h-4" />
                        <span>NO</span>
                    </label>
                </div>

                <!-- Average Monthly Income -->
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
                            <label class="flex items-center gap-2">
                                <input v-model="formData.clientType.newAcceptor" type="checkbox" class="w-4 h-4" />
                                <span>New Acceptor</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.clientType.currentUser" type="checkbox" class="w-4 h-4" />
                                <span>Current User</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.clientType.changingMethod" type="checkbox" class="w-4 h-4" />
                                <span>Changing Method</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.clientType.changingClinic" type="checkbox" class="w-4 h-4" />
                                <span>Changing Clinic</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.clientType.dropoutRestart" type="checkbox" class="w-4 h-4" />
                                <span>Dropout/Restart</span>
                            </label>
                        </div>

                        <div class="space-y-4">
                            <div>
                                <p class="font-medium mb-2">Reason for FP:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2">
                                        <input v-model="formData.reasonSpacing" type="checkbox" class="w-4 h-4" />
                                        <span>Spacing</span>
                                    </label>
                                    <label class="flex items-center gap-2">
                                        <input v-model="formData.reasonLimiting" type="checkbox" class="w-4 h-4" />
                                        <span>Limiting</span>
                                    </label>
                                    <label class="flex items-center gap-2">
                                        <span>Others</span>
                                        <input v-model="formData.reasonOthers" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" />
                                    </label>
                                </div>
                            </div>

                            <div>
                                <p class="font-medium mb-2">Reason:</p>
                                <div class="space-y-2">
                                    <label class="flex items-center gap-2">
                                        <input v-model="formData.medicalCondition" type="checkbox" class="w-4 h-4" />
                                        <span>Medical condition</span>
                                    </label>
                                    <label class="flex items-center gap-2">
                                        <span>Side-effects</span>
                                        <input v-model="formData.sideEffects" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" />
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Method Currently Used -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">Method currently used (for Changing Method)</h3>
                    <div class="grid grid-cols-2 md:grid-cols-3 gap-3">
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.coc" type="checkbox" class="w-4 h-4" />
                            <span>COC</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.iud" type="checkbox" class="w-4 h-4" />
                            <span>IUD</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.bom_ccm" type="checkbox" class="w-4 h-4" />
                            <span>BOM/CCM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.pop" type="checkbox" class="w-4 h-4" />
                            <span>POP</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.interval" type="checkbox" class="w-4 h-4" />
                            <span>INTERVAL</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.bbt" type="checkbox" class="w-4 h-4" />
                            <span>BBT</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.injectable" type="checkbox" class="w-4 h-4" />
                            <span>INJECTABLE</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.postPartum" type="checkbox" class="w-4 h-4" />
                            <span>POST-PARTUM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.stm" type="checkbox" class="w-4 h-4" />
                            <span>STM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.implant" type="checkbox" class="w-4 h-4" />
                            <span>IMPLANT</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.condom" type="checkbox" class="w-4 h-4" />
                            <span>CONDOM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.sdm" type="checkbox" class="w-4 h-4" />
                            <span>SDM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.currentMethod.lam" type="checkbox" class="w-4 h-4" />
                            <span>LAM</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <span>OTHERS:</span>
                            <input v-model="formData.currentMethod.others" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" />
                        </label>
                    </div>
                </div>

                <!-- I. Medical History -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">I. Medical History</h3>
                    <p class="text-sm mb-4">Does the client have any of the following</p>
                    <div class="space-y-2">
                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Severe headache / migraine</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.severeHeadache" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.severeHeadache" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● History of stroke / heart attack/ hypertension</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.strokeHistory" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.strokeHistory" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Non-traumatic hematoma / frequent bruising or gum bleeding</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.hematoma" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.hematoma" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Current or history of breast cancer / breast mass</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.breastCancer" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.breastCancer" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Severe chest pain</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.chestPain" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.chestPain" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Cough for more than 14 days</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.cough" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.cough" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Jaundice</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.jaundice" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.jaundice" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Unexplained vaginal bleeding</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.vaginalBleeding" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.vaginalBleeding" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Abnormal vaginal discharge</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.abnormalVaginalDischarge" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.abnormalVaginalDischarge" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Abnormal penile discharge</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.abnormalPenileDischarge" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.abnormalPenileDischarge" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Intake of phenobarbital (anti-seizure) or rifampicin</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.phenobarbital" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.phenobarbital" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● Is the client smoker?</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.smoker" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.smoker" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="grid grid-cols-3 gap-4 items-center">
                            <span class="text-sm">● With Disability?</span>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.disability" type="radio" :value="true" class="w-4 h-4" />
                                <span>Yes</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.medicalHistory.disability" type="radio" :value="false" class="w-4 h-4" />
                                <span>No</span>
                            </label>
                        </div>

                        <div class="ml-8" v-if="formData.medicalHistory.disability">
                            <label class="text-sm">If yes then please specify</label>
                            <input v-model="formData.medicalHistory.disabilitySpecify" type="text" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                        </div>
                    </div>
                </div>

                <!-- II. OBSTETRIC HISTORY -->
                <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">II. OBSTETRIC HISTORY</h3>
                    <div class="space-y-4">
                        <div class="flex items-center gap-4">
                            <span class="text-sm">Number of pregnancies: G</span>
                            <input v-model="formData.obstetric.numPregnancies" type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                            <span>P</span>
                            <input type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                        </div>

                        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Full Term</label>
                                <input v-model="formData.obstetric.fullTerm" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Premature</label>
                                <input v-model="formData.obstetric.premature" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Abortion</label>
                                <input v-model="formData.obstetric.abortion" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Living Children</label>
                                <input v-model="formData.obstetric.livingChildren" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                        </div>

                        <div class="flex items-center gap-4">
                            <label class="text-sm">Date of last delivery:</label>
                            <input v-model="formData.obstetric.lastDeliveryDate" type="date" class="px-3 py-2 border border-gray-300 rounded" />
                        </div>

                        <div>
                            <label class="text-sm font-medium mb-2 block">Type of last delivery</label>
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
                            <label class="flex items-center gap-2">
                                <input v-model="formData.obstetric.menstrualFlow" type="radio" value="scanty" class="w-4 h-4" />
                                <span class="text-sm">scanty (1-2 pads per day)</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.obstetric.menstrualFlow" type="radio" value="moderate" class="w-4 h-4" />
                                <span class="text-sm">moderate (3-5 pads per day)</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.obstetric.menstrualFlow" type="radio" value="heavy" class="w-4 h-4" />
                                <span class="text-sm">heavy (>5 pads per day)</span>
                            </label>
                        </div>
                    </div>

                    <div class="space-y-2">
                        <label class="flex items-center gap-2">
                            <input v-model="formData.obstetric.dysmenorrhea" type="checkbox" class="w-4 h-4" />
                            <span class="text-sm">Dysmenorrhea</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.obstetric.hydatidiformMole" type="checkbox" class="w-4 h-4" />
                            <span class="text-sm">Hydatidiform mole (within the last 12 months)</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.obstetric.ectopicPregnancy" type="checkbox" class="w-4 h-4" />
                            <span class="text-sm">History of ectopic pregnancy</span>
                        </label>
                    </div>
                </div>
                </div>
            </div>

            <!-- IV. Risk for Violence Against Women (VAW) -->
            <div class="border-t pt-4">
                <h3 class="font-bold mb-4">IV. Risk For Violence Against Women (VAW)</h3>
                <div class="space-y-3">
                    <div class="grid grid-cols-3 gap-4 items-center">
                        <span class="text-sm">● Unpleasant relationship with partner</span>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.unpleasantRelationship" type="radio" :value="true" class="w-4 h-4" />
                            <span>Yes</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.unpleasantRelationship" type="radio" :value="false" class="w-4 h-4" />
                            <span>No</span>
                        </label>
                    </div>

                    <div class="grid grid-cols-3 gap-4 items-center">
                        <span class="text-sm">● Partner does not approve visit to FP clinic</span>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.partnerDisapproval" type="radio" :value="true" class="w-4 h-4" />
                            <span>Yes</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.partnerDisapproval" type="radio" :value="false" class="w-4 h-4" />
                            <span>No</span>
                        </label>
                    </div>

                    <div class="grid grid-cols-3 gap-4 items-center">
                        <span class="text-sm">● History of domestic violence or VAW</span>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.domesticViolence" type="radio" :value="true" class="w-4 h-4" />
                            <span>Yes</span>
                        </label>
                        <label class="flex items-center gap-2">
                            <input v-model="formData.vaw.domesticViolence" type="radio" :value="false" class="w-4 h-4" />
                            <span>No</span>
                        </label>
                    </div>

                    <div class="ml-8 mt-4">
                        <p class="text-sm font-medium mb-2">referred to:</p>
                        <div class="space-y-2">
                            <label class="flex items-center gap-2">
                                <input v-model="formData.vaw.referredTo.dswd" type="checkbox" class="w-4 h-4" />
                                <span class="text-sm">DSWD</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.vaw.referredTo.wcpu" type="checkbox" class="w-4 h-4" />
                                <span class="text-sm">WCPU</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <input v-model="formData.vaw.referredTo.ngos" type="checkbox" class="w-4 h-4" />
                                <span class="text-sm">NGO's</span>
                            </label>
                            <label class="flex items-center gap-2">
                                <span class="text-sm">Others please specify</span>
                                <input v-model="formData.vaw.referredTo.othersSpecify" type="text" class="flex-1 px-2 py-1 border border-gray-300 rounded text-sm" />
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- V. PHYSICAL EXAMINATION -->
            <div class="border-t pt-4">
                <h3 class="font-bold mb-4">V. PHYSICAL EXAMINATION</h3>
                
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                    <div>
                        <label class="text-sm">Weight:</label>
                        <input v-model="formData.physical.weight" type="text" placeholder="kg" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                    </div>
                    <div>
                        <label class="text-sm">Blood pressure:</label>
                        <input v-model="formData.physical.bloodPressure" type="text" placeholder="mmHg" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                    </div>
                    <div>
                        <label class="text-sm">Height:</label>
                        <input v-model="formData.physical.height" type="text" placeholder="cm" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                    </div>
                    <div>
                        <label class="text-sm">Pulse rate:</label>
                        <input v-model="formData.physical.pulseRate" type="text" placeholder="/min" class="w-full px-3 py-2 border border-gray-300 rounded mt-1" />
                    </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Left Column -->
                    <div class="space-y-6">
                        <div>
                            <p class="font-medium mb-2">SKIN:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.skin.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.skin.pale" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">pale</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.skin.yellowish" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">yellowish</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.skin.hematoma" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">hematoma</span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <p class="font-medium mb-2">CONJUNCTIVA:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.conjunctiva.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.conjunctiva.pale" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">pale</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.conjunctiva.yellowish" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">yellowish</span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <p class="font-medium mb-2">NECK:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.neck.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.neck.neckMass" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">neck mass</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.neck.enlargedLymphNodes" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">enlarged lymph nodes</span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <p class="font-medium mb-2">BREAST:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.breast.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.breast.mass" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">mass</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.breast.nippleDischarge" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">nipple discharge</span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <p class="font-medium mb-2">ABDOMEN:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.abdomen.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.abdomen.abdominalMass" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">abdominal mass</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.abdomen.varicosities" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">varicosities</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- Right Column -->
                    <div class="space-y-6">
                        <div>
                            <p class="font-medium mb-2">EXTREMITIES:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.extremities.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.extremities.edema" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">edema</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.extremities.varicosities" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">varicosities</span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <p class="font-medium mb-2">PELVIC EXAMINATION:</p>
                            <div class="space-y-2">
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.normal" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">normal</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.mass" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">mass</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.abnormalDischarge" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">abnormal discharge</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.warts" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">warts</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.polypCyst" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">polyp or cyst</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.inflammationErosion" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">inflammation or erosion</span>
                                </label>
                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.bloodyDischarge" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">bloody discharge</span>
                                </label>
                            </div>

                            <div class="mt-4 space-y-3">
                                <div>
                                    <p class="text-sm mb-2">cervical consistency:</p>
                                    <div class="flex gap-4">
                                        <label class="flex items-center gap-2">
                                            <input v-model="formData.physical.pelvicExam.cervicalConsistency" type="radio" value="firm" class="w-4 h-4" />
                                            <span class="text-sm">firm</span>
                                        </label>
                                        <label class="flex items-center gap-2">
                                            <input v-model="formData.physical.pelvicExam.cervicalConsistency" type="radio" value="soft" class="w-4 h-4" />
                                            <span class="text-sm">soft</span>
                                        </label>
                                    </div>
                                </div>

                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.cervicalTenderness" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">cervical tenderness</span>
                                </label>

                                <label class="flex items-center gap-2">
                                    <input v-model="formData.physical.pelvicExam.adnexalMass" type="checkbox" class="w-4 h-4" />
                                    <span class="text-sm">adnexal mass / tenderness</span>
                                </label>

                                <div>
                                    <p class="text-sm mb-2">uterine position:</p>
                                    <div class="space-y-2">
                                        <label class="flex items-center gap-2">
                                            <input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="mid" class="w-4 h-4" />
                                            <span class="text-sm">mid</span>
                                        </label>
                                        <label class="flex items-center gap-2">
                                            <input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="anteflexed" class="w-4 h-4" />
                                            <span class="text-sm">anteflexed</span>
                                        </label>
                                        <label class="flex items-center gap-2">
                                            <input v-model="formData.physical.pelvicExam.uterinePosition" type="radio" value="retroflexed" class="w-4 h-4" />
                                            <span class="text-sm">retroflexed</span>
                                        </label>
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
            <!-- SIDE B -->
      <div class="border-t pt-4">
                    <h3 class="font-bold mb-4">II. OBSTETRIC HISTORY</h3>
                    <div class="space-y-4">
                        <div class="flex items-center gap-4">
                            <span class="text-sm">Number of pregnancies: G</span>
                            <input v-model="formData.obstetric.numPregnancies" type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                            <span>P</span>
                            <input v-model="formData.obstetric.fullTerm" type="text" class="w-16 px-2 py-1 border border-gray-300 rounded" />
                        </div>

                        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Full Term</label>
                                <input v-model="formData.obstetric.fullTerm" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Premature</label>
                                <input v-model="formData.obstetric.premature" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Abortion</label>
                                <input v-model="formData.obstetric.abortion" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
                            <div class="flex items-center gap-2">
                                <label class="text-sm">Living Children</label>
                                <input v-model="formData.obstetric.livingChildren" type="text" class="w-20 px-2 py-1 border border-gray-300 rounded" />
                            </div>
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
                    </div>
                </div>

                <div class="border-t-4 border-blue-600 pt-8 mt-12">
                    <h2 class="text-xl font-bold mb-6 text-blue-900">SIDE B: FAMILY PLANNING ASSESSMENT RECORD</h2>
                    
                    <div class="bg-white border rounded-xl shadow-sm p-6 space-y-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label class="block text-sm font-semibold mb-1">Date of Visit</label>
                                <input v-model="formData.physical.sideB.dateOfVisit" type="date" class="w-full p-2 border rounded-md" />
                            </div>
                            <div>
                                <label class="block text-sm font-semibold mb-1">Service Provider Name</label>
                                <input v-model="formData.physical.sideB.serviceProvider" type="text" class="w-full p-2 border rounded-md" />
                            </div>
                        </div>

                        <div>
                            <label class="block text-sm font-semibold mb-1">Medical Findings</label>
                            <textarea v-model="formData.physical.sideB.medicalFindings" rows="3" class="w-full p-2 border rounded-md"></textarea>
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

                        <div class="flex gap-4 mt-8 no-print">
                            <button type="submit" :disabled="submitStatus.loading" class="bg-blue-600 text-white px-8 py-3 rounded font-bold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed">
                            {{ submitStatus.loading ? 'Saving...' : 'SUBMIT RECORD' }}
                        </button>
                            <button @click="resetForm" type="button" class="bg-gray-200 px-8 py-3 rounded font-bold">RESET</button>
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
@media print {
    /* Hide navigation, buttons, status messages */
    .no-print,
    nav,
    aside,
    header,
    .sidebar,
    #sidebar {
        display: none !important;
    }
    /* Make the form fill the full printed page */
    body, html {
        margin: 0 !important;
        padding: 0 !important;
        background: white !important;
    }
    .min-h-screen {
        min-height: unset !important;
        background: white !important;
        padding: 0 !important;
    }
    .max-w-screen {
        max-width: 100% !important;
        box-shadow: none !important;
        padding: 10px !important;
    }
    /* Ensure all text is black for printing */
    * {
        color: black !important;
        background: white !important;
    }
    /* Keep borders and checkboxes visible */
    input[type="checkbox"], input[type="radio"] {
        -webkit-print-color-adjust: exact;
        print-color-adjust: exact;
    }
}
</style>
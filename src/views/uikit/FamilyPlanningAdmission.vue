<script setup>
import { ref } from 'vue';

const formData = ref({
    // Header Information
    clientId: '',
    philhealthNo: '',
    spouseName: '',
    birthDate: '',
    age: '',
    occupation: '',
    nhtsYes: false,
    nhtsNo: false,
    _4psMember: false,
    averageMonthlyIncome: '',
    numLivingChildren: '',
    planMoreChildren: null,
    
    // Type of Client
    clientType: {
        newAcceptor: false,
        currentUser: false,
        changingMethod: false,
        changingClinic: false,
        dropoutRestart: false
    },
    
    // Reason for FP
    reasonSpacing: false,
    reasonLimiting: false,
    reasonOthers: '',
    
    // Reason
    medicalCondition: false,
    sideEffects: '',
    
    // Method Currently Used
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
    
    // Medical History
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
    
    // Obstetric History
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
    
    // VAW Risk
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
    
    // Physical Examination
    physical: {
        weight: '',
        bloodPressure: '',
        height: '',
        pulseRate: '',
        
        skin: {
            normal: false,
            pale: false,
            yellowish: false,
            hematoma: false
        },
        
        conjunctiva: {
            normal: false,
            pale: false,
            yellowish: false
        },
        
        neck: {
            normal: false,
            neckMass: false,
            enlargedLymphNodes: false
        },
        
        breast: {
            normal: false,
            mass: false,
            nippleDischarge: false
        },
        
        abdomen: {
            normal: false,
            abdominalMass: false,
            varicosities: false
        },
        
        extremities: {
            normal: false,
            edema: false,
            varicosities: false
        },
        
        pelvicExam: {
            normal: false,
            mass: false,
            abnormalDischarge: false,
            warts: false,
            polypCyst: false,
            inflammationErosion: false,
            bloodyDischarge: false,
            cervicalConsistency: null,
            cervicalTenderness: false,
            adnexalMass: false,
            uterinePosition: null,
            uterineDepth: ''
        },
        sideB: {
    dateOfVisit: '2025-12-29',
    serviceProvider: '',
    medicalFindings: '',
    methodAccepted: '',
    followUpDate: '',
    // Pregnancy Checklist
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

function submitForm() {
    console.log('Form submitted:', formData.value);
    alert('Form submitted successfully!');
}

function resetForm() {
    if (confirm('Are you sure you want to reset the form?')) {
        // Reset logic here
        location.reload();
    }
}
</script>

<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4">
        <div class="max-w-screen mx-auto bg-white rounded-lg shadow-lg p-8">
            <!-- Header -->
            <div class="border-b-2 border-gray-800 pb-4 mb-6">
                <h1 class="text-2xl font-bold text-center mb-2">FAMILY PLANNING CLIENT ASSESSMENT RECORD</h1>
                <div class="bg-yellow-50 border border-yellow-300 rounded p-3 text-sm">
                    <p><strong>Instructions for Physician, Nurses and Midwife:</strong> Make sure that the client is not Pregnant by using the questions Listed by Side B. Completely fill out or check the required information; refer accordingly for any abnormal history or medical evaluation</p>
                </div>
            </div>

            <form @submit.prevent="submitForm" class="space-y-8">
                <!-- Client Information -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Client ID</label>
                        <input v-model="formData.clientId" type="text" class="w-full px-3 py-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
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
                    <span>NO</span>
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

                        <div class="flex gap-4 mt-8">
                            <button type="submit" class="bg-blue-600 text-white px-8 py-3 rounded font-bold hover:bg-blue-700">SUBMIT RECORD</button>
                            <button @click="resetForm" type="button" class="bg-gray-200 px-8 py-3 rounded font-bold">RESET</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
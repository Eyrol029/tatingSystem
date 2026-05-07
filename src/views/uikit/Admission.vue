<script setup>
import { ref, computed } from 'vue';

const currentStep = ref('arrival');
const patientData = ref({
    name: '',
    age: '',
    gestationalAge: '',
    readyForDelivery: null,
    isHighRisk: null,
    ward: '',
    attendingStaff: '',
    hasPhilHealth: null,
    paymentComplete: false
});

const steps = {
    arrival: { title: 'Patient Arrival', color: 'bg-blue-500' },
    assessment: { title: 'Initial Assessment', color: 'bg-purple-500' },
    notReady: { title: 'Not Ready - Discharge Home', color: 'bg-yellow-500' },
    admission: { title: 'Admission & Assignment', color: 'bg-green-500' },
    monitoring: { title: 'Patient Monitoring', color: 'bg-pink-500' },
    referral: { title: 'Referral to St. Mary Hospital', color: 'bg-red-500' },
    delivery: { title: 'Delivery Process', color: 'bg-indigo-500' },
    postpartum: { title: 'Postpartum & Newborn Care', color: 'bg-rose-500' },
    billing: { title: 'Statement of Account', color: 'bg-teal-500' },
    payment: { title: 'Payment Processing', color: 'bg-emerald-500' },
    discharge: { title: 'Patient Discharge', color: 'bg-green-600' }
};

const currentStepInfo = computed(() => steps[currentStep.value]);

const progressWidth = computed(() => {
    const progressMap = {
        arrival: '10%',
        assessment: '20%',
        notReady: '30%',
        admission: '35%',
        monitoring: '45%',
        referral: '50%',
        delivery: '60%',
        postpartum: '70%',
        billing: '80%',
        payment: '90%',
        discharge: '100%'
    };
    return progressMap[currentStep.value];
});

function handleInputChange(field, value) {
    patientData.value[field] = value;
}

function navigateToStep(step) {
    currentStep.value = step;
}

function resetProcess() {
    patientData.value = {
        name: '',
        age: '',
        gestationalAge: '',
        readyForDelivery: null,
        isHighRisk: null,
        ward: '',
        attendingStaff: '',
        hasPhilHealth: null,
        paymentComplete: false
    };
    currentStep.value = 'arrival';
}
</script>

<template>
    <div class="min-h-screen bg-gradient-to-br from-blue-50 to-pink-50 p-6">
        <div class="max-w-4xl mx-auto">
            <div class="bg-white rounded-2xl shadow-xl p-8">
                <!-- Header -->
                <div class="text-center mb-8">
                    <h1 class="text-3xl font-bold text-gray-800 mb-2">Lying-In Admission Process</h1>
                    <p class="text-gray-600">Complete patient care workflow management</p>
                </div>

                <!-- Current Step Indicator -->
                <div class="flex items-center justify-center mb-8">
                    <div :class="[currentStepInfo.color, 'p-4 rounded-full']">
                        <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                        </svg>
                    </div>
                    <div class="ml-4">
                        <h2 class="text-xl font-bold text-gray-800">{{ currentStepInfo.title }}</h2>
                        <p v-if="patientData.name" class="text-sm text-gray-600">Patient: {{ patientData.name }}</p>
                    </div>
                </div>

                <!-- Progress Bar -->
                <div class="mb-8">
                    <div class="flex justify-between text-xs text-gray-500 mb-2">
                        <span>Arrival</span>
                        <span>Assessment</span>
                        <span>Care</span>
                        <span>Billing</span>
                        <span>Discharge</span>
                    </div>
                    <div class="w-full bg-gray-200 rounded-full h-2">
                        <div 
                            :class="[currentStepInfo.color, 'h-2 rounded-full transition-all duration-500']"
                            :style="{ width: progressWidth }"
                        ></div>
                    </div>
                </div>

                <!-- Step Content -->
                <div class="bg-gray-50 rounded-xl p-6">
                    <!-- Arrival Step -->
                    <div v-if="currentStep === 'arrival'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Arrival with Labor Symptoms</h3>
                        <div class="space-y-3">
                            <input
                                v-model="patientData.name"
                                type="text"
                                placeholder="Patient Name"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                            <input
                                v-model="patientData.age"
                                type="number"
                                placeholder="Age"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                            <input
                                v-model="patientData.gestationalAge"
                                type="text"
                                placeholder="Gestational Age (weeks)"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                            <button
                                @click="navigateToStep('assessment')"
                                :disabled="!patientData.name"
                                class="w-full bg-blue-500 text-white py-3 rounded-lg hover:bg-blue-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                Proceed to Assessment
                            </button>
                        </div>
                    </div>

                    <!-- Assessment Step -->
                    <div v-if="currentStep === 'assessment'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Initial Assessment</h3>
                        <p class="text-gray-600">Evaluate if patient is ready for childbirth</p>
                        <div class="space-y-3">
                            <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
                                <p class="font-medium text-gray-700 mb-2">Patient: {{ patientData.name }}</p>
                                <p class="text-gray-600">Age: {{ patientData.age }} | GA: {{ patientData.gestationalAge }} weeks</p>
                            </div>
                            <div class="grid grid-cols-2 gap-3">
                                <button
                                    @click="handleInputChange('readyForDelivery', false); navigateToStep('notReady')"
                                    class="bg-yellow-500 text-white py-3 rounded-lg hover:bg-yellow-600 transition-colors font-semibold"
                                >
                                    Not Ready for Delivery
                                </button>
                                <button
                                    @click="handleInputChange('readyForDelivery', true); navigateToStep('admission')"
                                    class="bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition-colors font-semibold"
                                >
                                    Ready for Delivery
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Not Ready Step -->
                    <div v-if="currentStep === 'notReady'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Not Ready - Discharge Home</h3>
                        <div class="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                            <p class="text-yellow-800 font-medium mb-2">Patient advised to go home</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p>✓ Monitor contractions (frequency and intensity)</p>
                                <p>✓ Track fetal movements</p>
                                <p>✓ Watch for water breaking</p>
                                <p>✓ Return if contractions are regular (every 5 minutes)</p>
                                <p>✓ Seek immediate care if bleeding or severe pain occurs</p>
                            </div>
                        </div>
                        <button
                            @click="resetProcess"
                            class="w-full bg-gray-500 text-white py-3 rounded-lg hover:bg-gray-600 transition-colors font-semibold"
                        >
                            Complete & Return to Start
                        </button>
                    </div>

                    <!-- Admission Step -->
                    <div v-if="currentStep === 'admission'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Admission & Assignment</h3>
                        <div class="space-y-3">
                            <input
                                v-model="patientData.ward"
                                type="text"
                                placeholder="Assign Ward (e.g., Ward A, Room 101)"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                            />
                            <input
                                v-model="patientData.attendingStaff"
                                type="text"
                                placeholder="Attending Staff/Midwife Name"
                                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                            />
                            <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
                                <p class="text-green-800 font-medium">Patient Admitted Successfully</p>
                                <p class="text-sm text-gray-600 mt-1">Ward: {{ patientData.ward || 'Not assigned' }}</p>
                                <p class="text-sm text-gray-600">Staff: {{ patientData.attendingStaff || 'Not assigned' }}</p>
                            </div>
                            <button
                                @click="navigateToStep('monitoring')"
                                :disabled="!patientData.ward || !patientData.attendingStaff"
                                class="w-full bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition-colors font-semibold disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                Begin Monitoring
                            </button>
                        </div>
                    </div>

                    <!-- Monitoring Step -->
                    <div v-if="currentStep === 'monitoring'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Monitoring</h3>
                        <div class="p-4 bg-pink-50 border border-pink-200 rounded-lg">
                            <p class="text-pink-800 font-medium mb-2">Closely monitoring patient</p>
                            <div class="space-y-1 text-sm text-gray-700">
                                <p>• Vital signs monitoring</p>
                                <p>• Contraction frequency and intensity</p>
                                <p>• Fetal heart rate monitoring</p>
                                <p>• Cervical dilation progress</p>
                            </div>
                        </div>
                        <div class="space-y-3">
                            <p class="font-medium text-gray-700">Risk Assessment:</p>
                            <div class="grid grid-cols-2 gap-3">
                                <button
                                    @click="handleInputChange('isHighRisk', true); navigateToStep('referral')"
                                    class="bg-red-500 text-white py-3 rounded-lg hover:bg-red-600 transition-colors font-semibold"
                                >
                                    High Risk Identified
                                </button>
                                <button
                                    @click="handleInputChange('isHighRisk', false); navigateToStep('delivery')"
                                    class="bg-indigo-500 text-white py-3 rounded-lg hover:bg-indigo-600 transition-colors font-semibold"
                                >
                                    Continue to Delivery
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Referral Step -->
                    <div v-if="currentStep === 'referral'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Immediate Referral Required</h3>
                        <div class="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p class="text-red-800 font-bold mb-2">⚠️ High-Risk Patient Identified</p>
                            <p class="text-red-700 mb-3">Patient requires immediate referral to St. Mary Hospital</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p class="font-medium">Referral Process:</p>
                                <p>✓ Contact St. Mary Hospital emergency department</p>
                                <p>✓ Prepare patient transfer documents</p>
                                <p>✓ Arrange ambulance transport</p>
                                <p>✓ Provide complete medical records</p>
                                <p>✓ Brief receiving hospital staff on patient condition</p>
                            </div>
                        </div>
                        <button
                            @click="resetProcess"
                            class="w-full bg-red-500 text-white py-3 rounded-lg hover:bg-red-600 transition-colors font-semibold"
                        >
                            Complete Referral & Return to Start
                        </button>
                    </div>

                    <!-- Delivery Step -->
                    <div v-if="currentStep === 'delivery'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Delivery Process</h3>
                        <div class="p-4 bg-indigo-50 border border-indigo-200 rounded-lg">
                            <p class="text-indigo-800 font-medium mb-2">Midwife proceeding with delivery</p>
                            <p class="text-sm text-gray-700 mb-2">Birth aide called for assistance</p>
                            <div class="space-y-1 text-sm text-gray-700">
                                <p>• Preparing delivery room</p>
                                <p>• Monitoring maternal and fetal status</p>
                                <p>• Assisting with delivery positions</p>
                                <p>• Ready for newborn reception</p>
                            </div>
                        </div>
                        <button
                            @click="navigateToStep('postpartum')"
                            class="w-full bg-indigo-500 text-white py-3 rounded-lg hover:bg-indigo-600 transition-colors font-semibold"
                        >
                            Delivery Complete - Proceed to Postpartum Care
                        </button>
                    </div>

                    <!-- Postpartum Step -->
                    <div v-if="currentStep === 'postpartum'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Postpartum & Newborn Care</h3>
                        <div class="space-y-3">
                            <div class="p-4 bg-rose-50 border border-rose-200 rounded-lg">
                                <p class="text-rose-800 font-medium mb-2">Mother's Care:</p>
                                <div class="space-y-1 text-sm text-gray-700">
                                    <p>✓ Postpartum monitoring</p>
                                    <p>✓ Vital signs assessment</p>
                                    <p>✓ Bleeding and uterine contraction check</p>
                                    <p>✓ Breastfeeding initiation support</p>
                                    <p>✓ Pain management</p>
                                </div>
                            </div>
                            <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                                <p class="text-blue-800 font-medium mb-2">Newborn Care:</p>
                                <div class="space-y-1 text-sm text-gray-700">
                                    <p>✓ APGAR score assessment</p>
                                    <p>✓ Newborn screening</p>
                                    <p>✓ Vitamin K administration</p>
                                    <p>✓ Eye prophylaxis</p>
                                    <p>✓ Initial breastfeeding</p>
                                </div>
                            </div>
                            <div class="flex items-center space-x-2 p-3 bg-gray-50 rounded-lg">
                                <input type="checkbox" id="additionalServices" class="w-4 h-4" />
                                <label for="additionalServices" class="text-gray-700">Additional services required</label>
                            </div>
                            <button
                                @click="navigateToStep('billing')"
                                class="w-full bg-rose-500 text-white py-3 rounded-lg hover:bg-rose-600 transition-colors font-semibold"
                            >
                                Proceed to Billing
                            </button>
                        </div>
                    </div>

                    <!-- Billing Step -->
                    <div v-if="currentStep === 'billing'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Statement of Account (SOA)</h3>
                        <div class="p-4 bg-teal-50 border border-teal-200 rounded-lg">
                            <p class="text-teal-800 font-medium mb-3">Preparing Statement of Account</p>
                            <div class="space-y-2 text-sm">
                                <div class="flex justify-between"><span>Admission Fee:</span><span>₱1,500.00</span></div>
                                <div class="flex justify-between"><span>Delivery Fee:</span><span>₱8,000.00</span></div>
                                <div class="flex justify-between"><span>Postpartum Care (2 days):</span><span>₱3,000.00</span></div>
                                <div class="flex justify-between"><span>Newborn Care:</span><span>₱2,500.00</span></div>
                                <div class="flex justify-between"><span>Medications & Supplies:</span><span>₱2,000.00</span></div>
                                <hr class="my-2 border-teal-300" />
                                <div class="flex justify-between font-bold text-base"><span>Total:</span><span>₱17,000.00</span></div>
                            </div>
                        </div>
                        <div class="space-y-3">
                            <p class="font-medium text-gray-700">PhilHealth Status:</p>
                            <div class="grid grid-cols-2 gap-3">
                                <button
                                    @click="handleInputChange('hasPhilHealth', false); navigateToStep('payment')"
                                    class="bg-gray-500 text-white py-3 rounded-lg hover:bg-gray-600 transition-colors font-semibold"
                                >
                                    No PhilHealth
                                </button>
                                <button
                                    @click="handleInputChange('hasPhilHealth', true); navigateToStep('payment')"
                                    class="bg-emerald-500 text-white py-3 rounded-lg hover:bg-emerald-600 transition-colors font-semibold"
                                >
                                    Has PhilHealth
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Payment Step -->
                    <div v-if="currentStep === 'payment'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Payment Processing</h3>
                        <div v-if="patientData.hasPhilHealth" class="p-4 bg-emerald-50 border border-emerald-200 rounded-lg">
                            <p class="text-emerald-800 font-medium mb-2">PhilHealth Coverage Applied</p>
                            <div class="space-y-2 text-sm">
                                <div class="flex justify-between"><span>Total Bill:</span><span>₱17,000.00</span></div>
                                <div class="flex justify-between text-emerald-700"><span>PhilHealth Coverage:</span><span>-₱6,500.00</span></div>
                                <hr class="my-2 border-emerald-300" />
                                <div class="flex justify-between font-bold text-base"><span>Amount Due:</span><span>₱10,500.00</span></div>
                            </div>
                        </div>
                        <div v-else class="p-4 bg-gray-50 border border-gray-200 rounded-lg">
                            <p class="text-gray-800 font-medium mb-2">Assisting Patient with Payment</p>
                            <div class="space-y-2 text-sm">
                                <div class="flex justify-between font-bold text-base"><span>Amount Due:</span><span>₱17,000.00</span></div>
                                <p class="text-gray-600 mt-2">• Payment plan options available</p>
                                <p class="text-gray-600">• Social service assistance available</p>
                            </div>
                        </div>
                        <div class="space-y-3">
                            <select class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-emerald-500">
                                <option>Select Payment Method</option>
                                <option>Cash</option>
                                <option>Debit/Credit Card</option>
                                <option>GCash</option>
                                <option>Bank Transfer</option>
                                <option>Installment Plan</option>
                            </select>
                            <button
                                @click="handleInputChange('paymentComplete', true); navigateToStep('discharge')"
                                class="w-full bg-emerald-500 text-white py-3 rounded-lg hover:bg-emerald-600 transition-colors font-semibold"
                            >
                                Confirm Full Payment
                            </button>
                        </div>
                    </div>

                    <!-- Discharge Step -->
                    <div v-if="currentStep === 'discharge'" class="space-y-4">
                        <h3 class="text-xl font-semibold text-gray-800">Patient Discharge</h3>
                        <div class="p-4 bg-green-50 border border-green-200 rounded-lg">
                            <p class="text-green-800 font-bold mb-2">✓ Payment Complete - Ready for Discharge</p>
                            <p class="text-green-700 font-medium mb-3">Discharge Instructions:</p>
                            <div class="space-y-2 text-sm text-gray-700">
                                <p class="font-medium">For Mother:</p>
                                <p>• Rest adequately and avoid strenuous activities</p>
                                <p>• Monitor for excessive bleeding or fever</p>
                                <p>• Continue breastfeeding on demand</p>
                                <p>• Take prescribed medications as directed</p>
                                <p>• Return for postpartum checkup in 1 week</p>
                                <p class="font-medium mt-3">For Newborn:</p>
                                <p>• Exclusive breastfeeding for 6 months</p>
                                <p>• Follow vaccination schedule</p>
                                <p>• Watch for jaundice, difficulty breathing, or poor feeding</p>
                                <p>• Newborn screening results in 2 weeks</p>
                                <p>• Return for checkup as scheduled</p>
                            </div>
                        </div>
                        <div class="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                            <p class="text-blue-800 font-medium">Emergency Contact:</p>
                            <p class="text-sm text-gray-700">Call clinic hotline: (123) 456-7890</p>
                            <p class="text-sm text-gray-700">Available 24/7 for concerns</p>
                        </div>
                        <button
                            @click="resetProcess"
                            class="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 transition-colors font-semibold"
                        >
                            Complete Discharge & Return to Start
                        </button>
                    </div>
                </div>

                <!-- Footer -->
                <div class="mt-6 text-center text-sm text-gray-500">
                    <p>Maternal & Child Health Care System</p>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, computed } from 'vue';

const currentStep = ref(1);
const appointmentForm = ref({
    // Step 1: Personal Information
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    contactNumber: '',
    email: '',
    address: '',
    
    // Step 2: Appointment Details
    serviceType: '',
    preferredDate: '',
    preferredTime: '',
    reasonForVisit: '',
    
    // Step 3: Medical Information
    hasExistingRecord: null,
    patientId: '',
    lastVisit: '',
    currentMedications: '',
    allergies: '',
    specialNotes: ''
});

const serviceTypes = ref([
    { value: 'prenatal', label: 'Prenatal Checkup', description: 'Regular checkup during pregnancy' },
    { value: 'postnatal', label: 'Postnatal Care', description: 'Care after childbirth' },
    { value: 'family-planning', label: 'Family Planning', description: 'Contraception and family planning services' },
    { value: 'immunization', label: 'Immunization', description: 'Vaccination services' },
    { value: 'general', label: 'General Consultation', description: 'General health consultation' },
    { value: 'other', label: 'Other', description: 'Other medical services' }
]);

const timeSlots = ref([
    '08:00 AM', '08:30 AM', '09:00 AM', '09:30 AM', '10:00 AM', '10:30 AM',
    '11:00 AM', '11:30 AM', '01:00 PM', '01:30 PM', '02:00 PM', '02:30 PM',
    '03:00 PM', '03:30 PM', '04:00 PM', '04:30 PM'
]);

const isStep1Valid = computed(() => {
    return appointmentForm.value.firstName && 
           appointmentForm.value.lastName && 
           appointmentForm.value.dateOfBirth && 
           appointmentForm.value.gender && 
           appointmentForm.value.contactNumber && 
           appointmentForm.value.email;
});

const isStep2Valid = computed(() => {
    return appointmentForm.value.serviceType && 
           appointmentForm.value.preferredDate && 
           appointmentForm.value.preferredTime && 
           appointmentForm.value.reasonForVisit;
});

const isStep3Valid = computed(() => {
    return appointmentForm.value.hasExistingRecord !== null;
});

function nextStep() {
    if (currentStep.value === 1 && !isStep1Valid.value) {
        alert('Please fill in all required fields');
        return;
    }
    if (currentStep.value === 2 && !isStep2Valid.value) {
        alert('Please fill in all required fields');
        return;
    }
    if (currentStep.value < 4) {
        currentStep.value++;
    }
}

function prevStep() {
    if (currentStep.value > 1) {
        currentStep.value--;
    }
}

function submitAppointment() {
    if (!isStep3Valid.value) {
        alert('Please indicate if you have an existing clinic record');
        return;
    }
    
    alert('Appointment request submitted successfully! You will receive a confirmation via email and SMS.');
    
    // Reset form
    appointmentForm.value = {
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        gender: '',
        contactNumber: '',
        email: '',
        address: '',
        serviceType: '',
        preferredDate: '',
        preferredTime: '',
        reasonForVisit: '',
        hasExistingRecord: null,
        patientId: '',
        lastVisit: '',
        currentMedications: '',
        allergies: '',
        specialNotes: ''
    };
    currentStep.value = 1;
}


function getMinDate() {
    const today = new Date();
    return today.toISOString().split('T')[0];
}
</script>

<template>
    <div class="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50 p-6">
        <div class="max-w-4xl mx-auto">
            <!-- Header -->
            <div class="text-center mb-8">
                <h1 class="text-4xl font-bold text-gray-800 mb-2">Book an Appointment</h1>
                <p class="text-gray-600">Schedule your visit with our healthcare clinic</p>
            </div>

            <!-- Progress Steps -->
            <div class="bg-white rounded-lg shadow-md p-6 mb-6">
                <div class="flex items-center justify-between">
                    <div class="flex-1 flex items-center">
                        <div :class="['w-10 h-10 rounded-full flex items-center justify-center font-bold', currentStep >= 1 ? 'bg-purple-600 text-white' : 'bg-gray-300 text-gray-600']">
                            1
                        </div>
                        <div class="flex-1 h-1 mx-2" :class="currentStep >= 2 ? 'bg-purple-600' : 'bg-gray-300'"></div>
                    </div>
                    <div class="flex-1 flex items-center">
                        <div :class="['w-10 h-10 rounded-full flex items-center justify-center font-bold', currentStep >= 2 ? 'bg-purple-600 text-white' : 'bg-gray-300 text-gray-600']">
                            2
                        </div>
                        <div class="flex-1 h-1 mx-2" :class="currentStep >= 3 ? 'bg-purple-600' : 'bg-gray-300'"></div>
                    </div>
                    <div class="flex-1 flex items-center">
                        <div :class="['w-10 h-10 rounded-full flex items-center justify-center font-bold', currentStep >= 3 ? 'bg-purple-600 text-white' : 'bg-gray-300 text-gray-600']">
                            3
                        </div>
                        <div class="flex-1 h-1 mx-2" :class="currentStep >= 4 ? 'bg-purple-600' : 'bg-gray-300'"></div>
                    </div>
                    <div :class="['w-10 h-10 rounded-full flex items-center justify-center font-bold', currentStep >= 4 ? 'bg-purple-600 text-white' : 'bg-gray-300 text-gray-600']">
                        4
                    </div>
                </div>
                <div class="flex justify-between mt-3">
                    <span class="text-xs font-medium text-gray-600">Personal Info</span>
                    <span class="text-xs font-medium text-gray-600">Appointment</span>
                    <span class="text-xs font-medium text-gray-600">Medical Info</span>
                    <span class="text-xs font-medium text-gray-600">Review</span>
                </div>
            </div>

            <!-- Form Content -->
            <div class="bg-white rounded-lg shadow-md p-8">
                <!-- Step 1: Personal Information -->
                <div v-if="currentStep === 1" class="space-y-6">
                    <h2 class="text-2xl font-bold text-gray-800 mb-6">Personal Information</h2>
                    
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                First Name <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.firstName"
                                type="text"
                                placeholder="Enter first name"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Last Name <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.lastName"
                                type="text"
                                placeholder="Enter last name"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Date of Birth <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.dateOfBirth"
                                type="date"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Gender <span class="text-red-500">*</span>
                            </label>
                            <select
                                v-model="appointmentForm.gender"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            >
                                <option value="">Select gender</option>
                                <option value="female">Female</option>
                                <option value="male">Male</option>
                                <option value="other">Prefer not to say</option>
                            </select>
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Contact Number <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.contactNumber"
                                type="tel"
                                placeholder="09XXXXXXXXX"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Email Address <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.email"
                                type="email"
                                placeholder="your.email@example.com"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Complete Address
                        </label>
                        <textarea
                            v-model="appointmentForm.address"
                            rows="3"
                            placeholder="Street, Barangay, City, Province"
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        ></textarea>
                    </div>
                </div>

                <!-- Step 2: Appointment Details -->
                <div v-if="currentStep === 2" class="space-y-6">
                    <h2 class="text-2xl font-bold text-gray-800 mb-6">Appointment Details</h2>
                    
                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Service Type <span class="text-red-500">*</span>
                        </label>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                            <div
                                v-for="service in serviceTypes"
                                :key="service.value"
                                @click="appointmentForm.serviceType = service.value"
                                :class="[
                                    'border-2 rounded-lg p-4 cursor-pointer transition-all',
                                    appointmentForm.serviceType === service.value
                                        ? 'border-purple-600 bg-purple-50'
                                        : 'border-gray-300 hover:border-purple-300'
                                ]"
                            >
                                <h3 class="font-semibold text-gray-800">{{ service.label }}</h3>
                                <p class="text-sm text-gray-600 mt-1">{{ service.description }}</p>
                            </div>
                        </div>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Preferred Date <span class="text-red-500">*</span>
                            </label>
                            <input
                                v-model="appointmentForm.preferredDate"
                                type="date"
                                :min="getMinDate()"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Preferred Time <span class="text-red-500">*</span>
                            </label>
                            <select
                                v-model="appointmentForm.preferredTime"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            >
                                <option value="">Select time</option>
                                <option v-for="time in timeSlots" :key="time" :value="time">
                                    {{ time }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Reason for Visit <span class="text-red-500">*</span>
                        </label>
                        <textarea
                            v-model="appointmentForm.reasonForVisit"
                            rows="4"
                            placeholder="Please describe your symptoms or reason for visit..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        ></textarea>
                    </div>
                </div>

                <!-- Step 3: Medical Information -->
                <div v-if="currentStep === 3" class="space-y-6">
                    <h2 class="text-2xl font-bold text-gray-800 mb-6">Medical Information</h2>
                    
                    <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                        <p class="text-sm text-blue-800">
                            <span class="font-semibold">Note:</span> This information helps us provide better care. All medical information is kept confidential.
                        </p>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-3">
                            Do you have an existing clinic record? <span class="text-red-500">*</span>
                        </label>
                        <div class="flex gap-4">
                            <button
                                @click="appointmentForm.hasExistingRecord = true"
                                :class="[
                                    'flex-1 py-3 rounded-lg font-semibold transition-all',
                                    appointmentForm.hasExistingRecord === true
                                        ? 'bg-purple-600 text-white'
                                        : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                                ]"
                            >
                                Yes
                            </button>
                            <button
                                @click="appointmentForm.hasExistingRecord = false"
                                :class="[
                                    'flex-1 py-3 rounded-lg font-semibold transition-all',
                                    appointmentForm.hasExistingRecord === false
                                        ? 'bg-purple-600 text-white'
                                        : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                                ]"
                            >
                                No
                            </button>
                        </div>
                    </div>

                    <div v-if="appointmentForm.hasExistingRecord === true" class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Patient ID (if known)
                            </label>
                            <input
                                v-model="appointmentForm.patientId"
                                type="text"
                                placeholder="e.g., P001"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                Last Visit Date
                            </label>
                            <input
                                v-model="appointmentForm.lastVisit"
                                type="date"
                                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                            />
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Current Medications
                        </label>
                        <textarea
                            v-model="appointmentForm.currentMedications"
                            rows="3"
                            placeholder="List any medications you are currently taking..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        ></textarea>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Allergies
                        </label>
                        <textarea
                            v-model="appointmentForm.allergies"
                            rows="3"
                            placeholder="List any allergies (medications, food, etc.)..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        ></textarea>
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-700 mb-2">
                            Special Notes
                        </label>
                        <textarea
                            v-model="appointmentForm.specialNotes"
                            rows="3"
                            placeholder="Any other information we should know..."
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                        ></textarea>
                    </div>
                </div>

                <!-- Step 4: Review -->
                <div v-if="currentStep === 4" class="space-y-6">
                    <h2 class="text-2xl font-bold text-gray-800 mb-6">Review Your Appointment</h2>
                    
                    <div class="space-y-6">
                        <!-- Personal Information -->
                        <div class="bg-gray-50 rounded-lg p-6">
                            <h3 class="text-lg font-bold text-gray-800 mb-4">Personal Information</h3>
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                                <div>
                                    <span class="text-gray-600">Name:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.firstName }} {{ appointmentForm.lastName }}</span>
                                </div>
                                <div>
                                    <span class="text-gray-600">Date of Birth:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.dateOfBirth }}</span>
                                </div>
                                <div>
                                    <span class="text-gray-600">Gender:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.gender }}</span>
                                </div>
                                <div>
                                    <span class="text-gray-600">Contact:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.contactNumber }}</span>
                                </div>
                                <div class="md:col-span-2">
                                    <span class="text-gray-600">Email:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.email }}</span>
                                </div>
                                <div v-if="appointmentForm.address" class="md:col-span-2">
                                    <span class="text-gray-600">Address:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.address }}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Appointment Details -->
                        <div class="bg-gray-50 rounded-lg p-6">
                            <h3 class="text-lg font-bold text-gray-800 mb-4">Appointment Details</h3>
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                                <div>
                                    <span class="text-gray-600">Service:</span>
                                    <span class="font-semibold text-gray-900 ml-2">
                                        {{ serviceTypes.find(s => s.value === appointmentForm.serviceType)?.label }}
                                    </span>
                                </div>
                                <div>
                                    <span class="text-gray-600">Date:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.preferredDate }}</span>
                                </div>
                                <div>
                                    <span class="text-gray-600">Time:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.preferredTime }}</span>
                                </div>
                                <div class="md:col-span-2">
                                    <span class="text-gray-600">Reason:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.reasonForVisit }}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Medical Information -->
                        <div class="bg-gray-50 rounded-lg p-6">
                            <h3 class="text-lg font-bold text-gray-800 mb-4">Medical Information</h3>
                            <div class="space-y-2 text-sm">
                                <div>
                                    <span class="text-gray-600">Existing Record:</span>
                                    <span class="font-semibold text-gray-900 ml-2">
                                        {{ appointmentForm.hasExistingRecord ? 'Yes' : 'No' }}
                                    </span>
                                </div>
                                <div v-if="appointmentForm.patientId">
                                    <span class="text-gray-600">Patient ID:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.patientId }}</span>
                                </div>
                                <div v-if="appointmentForm.currentMedications">
                                    <span class="text-gray-600">Medications:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.currentMedications }}</span>
                                </div>
                                <div v-if="appointmentForm.allergies">
                                    <span class="text-gray-600">Allergies:</span>
                                    <span class="font-semibold text-gray-900 ml-2">{{ appointmentForm.allergies }}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                        <p class="text-sm text-yellow-800">
                            <span class="font-semibold">Important:</span> Please arrive 10-15 minutes before your scheduled time. Bring a valid ID and any previous medical records if available.
                        </p>
                    </div>
                </div>

                <!-- Navigation Buttons -->
                <div class="flex gap-4 mt-8">
                    <button
                        v-if="currentStep > 1"
                        @click="prevStep"
                        class="px-6 py-3 border-2 border-purple-600 text-purple-600 rounded-lg font-semibold hover:bg-purple-50 transition-colors"
                    >
                        Back
                    </button>
                    <button
                        v-if="currentStep < 4"
                        @click="nextStep"
                        class="flex-1 px-6 py-3 bg-purple-600 text-white rounded-lg font-semibold hover:bg-purple-700 transition-colors"
                    >
                        Continue
                    </button>
                    <button
                        v-if="currentStep === 4"
                        @click="submitAppointment"
                        class="flex-1 px-6 py-3 bg-green-600 text-white rounded-lg font-semibold hover:bg-green-700 transition-colors"
                    >
                        Submit Appointment
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Additional custom styles if needed */
</style>
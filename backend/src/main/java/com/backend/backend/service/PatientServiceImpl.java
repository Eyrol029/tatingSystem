package com.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.model.Patient;
import com.backend.backend.repository.AdmissionRepository;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.Billing.BillingRepository;
import com.backend.backend.repository.Billing.PaymentInstallmentRepository;
import com.backend.backend.repository.Billing.RevenueRepository;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;
import com.backend.backend.repository.Calendar.CalendarEventRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;
import com.backend.backend.repository.ReferralRepository;
import com.backend.backend.repository.UserRepository;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Autowired
    private PrenatalRecordRepository prenatalRecordRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private StatementOfAccountRepository statementOfAccountRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PaymentInstallmentRepository paymentInstallmentRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
    }

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void deletePatient(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }

        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found: " + id));

        if (patient.getNumberOfPregnancy() == null) {
            patient.setNumberOfPregnancy(0);
            patientRepository.save(patient);
        }

        admissionRepository.deleteByPatientID(id);
        appointmentRepository.deleteByPatientID(id);
        calendarEventRepository.deleteByPatientID(id);
        userRepository.deleteByPatientID(id);
        referralRepository.deleteByPatientId(id.longValue());

        List<com.backend.backend.model.PatientService> services = patientServiceRepository.findByPatientID(id);
        for (com.backend.backend.model.PatientService service : services) {
            if (service.getPatientServiceID() != null) {
                prenatalRecordRepository.deleteByServiceID(service.getPatientServiceID());
            }
        }

        statementOfAccountRepository.deleteByPatientID(id);
        paymentInstallmentRepository.deleteByPatientId(id);
        billingRepository.deleteByPatientId(id);
        revenueRepository.deleteByPatientID(id);

        patientServiceRepository.deleteByPatientID(id);
        patientRepository.deleteById(id);
    }

    @Override
    public Patient incrementPregnancy(Integer id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found: " + id));

        int currentCount = patient.getNumberOfPregnancy() != null ? patient.getNumberOfPregnancy() : 0;
        patient.setNumberOfPregnancy(currentCount + 1);
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getPatientsByLastName(String lName) {
        return patientRepository.findByLName(lName);
    }

    @Override
    public List<Patient> getPatientsByBarangay(String barangay) {
        return patientRepository.findByBarangay(barangay);
    }
}


package com.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Patient;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;

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
    public void deletePatient(Integer id) {
        // Delete appointments tied directly to the patient.
        appointmentRepository.deleteByPatientID(id);

        // Delete prenatal records tied to the patient's services.
        List<com.backend.backend.model.PatientService> services = patientServiceRepository.findByPatientID(id);
        for (com.backend.backend.model.PatientService service : services) {
            if (service.getPatientServiceID() != null) {
                prenatalRecordRepository.deleteByServiceID(service.getPatientServiceID());
            }
        }

        // Delete the patient's service entries after their dependent records are removed.
        patientServiceRepository.deleteByPatientID(id);

        patientRepository.deleteById(id);
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


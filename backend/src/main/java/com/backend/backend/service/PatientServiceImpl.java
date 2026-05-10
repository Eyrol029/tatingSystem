package com.backend.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backend.backend.model.Patient;
import com.backend.backend.repository.PatientRepository;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

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


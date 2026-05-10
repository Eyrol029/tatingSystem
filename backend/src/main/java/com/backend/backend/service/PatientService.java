package com.backend.backend.service;

import com.backend.backend.model.Patient;
import java.util.List;

public interface PatientService {
    Patient addPatient(Patient patient);
    Patient getPatientById(Integer id);
    List<Patient> getPatients();
    Patient updatePatient(Patient patient);
    void deletePatient(Integer id);
    List<Patient> getPatientsByLastName(String lName);
    List<Patient> getPatientsByBarangay(String barangay);
}
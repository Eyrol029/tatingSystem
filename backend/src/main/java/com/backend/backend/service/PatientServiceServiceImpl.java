package com.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.PatientService;
import com.backend.backend.repository.PatientServiceRepository;

@Service
public class PatientServiceServiceImpl implements PatientServiceService {

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Override
    public PatientService addPatientService(PatientService patientService) {
        return patientServiceRepository.save(patientService);
    }

    @Override
    public PatientService getPatientServiceById(Integer id) {
        return patientServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PatientService not found: " + id));
    }

    @Override
    public List<PatientService> getPatientServices() {
        return patientServiceRepository.findAll();
    }

    @Override
    public PatientService updatePatientService(PatientService patientService) {
        return patientServiceRepository.save(patientService);
    }

    @Override
    public void deletePatientService(Integer id) {
        patientServiceRepository.deleteById(id);
    }

    @Override
    public List<PatientService> getPatientServicesByPatientID(Integer patientID) {
        return patientServiceRepository.findByPatientID(patientID);
    }

    @Override
    public List<PatientService> getPatientServicesByServiceID(Integer serviceID) {
        return patientServiceRepository.findByServiceID(serviceID);
    }

    @Override
    public List<PatientService> getPatientServicesByEmployeeID(Integer employeeID) {
        return patientServiceRepository.findByEmployeeID(employeeID);
    }

    @Override
    public List<PatientService> getPatientServicesByWardID(Integer wardID) {
        return patientServiceRepository.findByWardID(wardID);
    }
}

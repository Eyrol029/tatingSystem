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
    public List<PatientService> getPatientServicesByServiceName(String serviceName) {
        return patientServiceRepository.findByServiceName(serviceName);
    }

    @Override
    public List<PatientService> getPatientServicesByEmployeeName(String employeeName) {
        return patientServiceRepository.findByEmployeeName(employeeName);
    }

    @Override
    public List<PatientService> getPatientServicesByWardName(String wardName) {
        return patientServiceRepository.findByWardName(wardName);
    }
}

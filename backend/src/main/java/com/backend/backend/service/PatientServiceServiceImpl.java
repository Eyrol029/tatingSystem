package com.backend.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;

@Service
public class PatientServiceServiceImpl implements PatientServiceService {

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientIdGeneratorService patientIdGeneratorService;

    @Override
    public PatientService addPatientService(PatientService patientService) {
        if (patientService.getDateAvailed() == null) {
            patientService.setDateAvailed(LocalDate.now());
        }

        if (patientService.getPatientID() != null) {
            Optional<Patient> patientOpt = patientRepository.findById(patientService.getPatientID());
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                // Check if this patient already has an assigned patientCode from their first service
                if (patient.getPatientCode() == null || patient.getPatientCode().trim().isEmpty()) {
                    String newId = patientIdGeneratorService.generateNextPatientId(patientService.getServiceName());
                    patient.setPatientCode(newId);
                    patientRepository.save(patient);
                    patientService.setCaseNumber(newId);
                } else if (patientService.getCaseNumber() == null || patientService.getCaseNumber().trim().isEmpty() || "---".equals(patientService.getCaseNumber().trim())) {
                    String serviceCaseNum = patientIdGeneratorService.generateNextPatientId(patientService.getServiceName());
                    patientService.setCaseNumber(serviceCaseNum);
                }
            }
        }

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

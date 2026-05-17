package com.backend.backend.service;

import java.util.List;

import com.backend.backend.model.PatientService;

public interface PatientServiceService {

    PatientService addPatientService(PatientService patientService);

    PatientService getPatientServiceById(Integer id);

    List<PatientService> getPatientServices();

    PatientService updatePatientService(PatientService patientService);

    void deletePatientService(Integer id);

    List<PatientService> getPatientServicesByPatientID(Integer patientID);

    List<PatientService> getPatientServicesByServiceName(String serviceName);

    List<PatientService> getPatientServicesByEmployeeName(String employeeName);

    List<PatientService> getPatientServicesByWardName(String wardName);
}

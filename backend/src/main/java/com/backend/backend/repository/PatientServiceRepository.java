package com.backend.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.PatientService;

public interface PatientServiceRepository extends CrudRepository<PatientService, Integer> {

    List<PatientService> findAll();

    List<PatientService> findByPatientID(Integer patientID);

    List<PatientService> findByServiceName(String serviceName);

    List<PatientService> findByEmployeeName(String employeeName);

    List<PatientService> findByWardName(String wardName);
}

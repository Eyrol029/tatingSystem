package com.backend.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Admission;

public interface AdmissionRepository extends CrudRepository<Admission, Integer> {

    List<Admission> findAll();
    List<Admission> findByServiceID(Integer serviceID);
    List<Admission> findByPatientID(Integer patientID);
    void deleteByPatientID(Integer patientID);
}
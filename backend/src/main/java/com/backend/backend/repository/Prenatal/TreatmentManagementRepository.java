package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.TreatmentManagement;

public interface TreatmentManagementRepository extends CrudRepository<TreatmentManagement, Integer> {

    List<TreatmentManagement> findAll();

    List<TreatmentManagement> findByPrenatalRecordID(Integer prenatalRecordID);
}

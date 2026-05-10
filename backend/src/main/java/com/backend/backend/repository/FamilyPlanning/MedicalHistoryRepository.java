package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.MedicalHistory;

public interface MedicalHistoryRepository extends CrudRepository<MedicalHistory, Integer> {

    List<MedicalHistory> findAll();

    List<MedicalHistory> findByClientID(Integer clientID);
}

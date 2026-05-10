package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.FpAssessmentRecord;

public interface FpAssessmentRecordRepository extends CrudRepository<FpAssessmentRecord, Integer> {

    List<FpAssessmentRecord> findAll();

    List<FpAssessmentRecord> findByClientID(Integer clientID);
}

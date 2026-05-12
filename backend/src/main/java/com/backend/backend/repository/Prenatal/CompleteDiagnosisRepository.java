package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.CompleteDiagnosis;

public interface CompleteDiagnosisRepository extends CrudRepository<CompleteDiagnosis, Integer> {

    List<CompleteDiagnosis> findAll();

    List<CompleteDiagnosis> findByClinicalHistoryID(Integer clinicalHistoryID);
}

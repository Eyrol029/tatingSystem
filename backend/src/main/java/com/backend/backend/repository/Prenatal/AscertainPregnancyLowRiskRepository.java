package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk;

public interface AscertainPregnancyLowRiskRepository
        extends CrudRepository<AscertainPregnancyLowRisk, Integer> {

    List<AscertainPregnancyLowRisk> findAll();

    List<AscertainPregnancyLowRisk> findByClinicalHistoryID(Integer clinicalHistoryID);
}

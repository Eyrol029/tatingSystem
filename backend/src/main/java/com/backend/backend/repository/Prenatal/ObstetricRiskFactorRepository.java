package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.ObstetricRiskFactor;

public interface ObstetricRiskFactorRepository extends CrudRepository<ObstetricRiskFactor, Integer> {

    List<ObstetricRiskFactor> findAll();

    List<ObstetricRiskFactor> findByClinicalHistoryID(Integer clinicalHistoryID);
}

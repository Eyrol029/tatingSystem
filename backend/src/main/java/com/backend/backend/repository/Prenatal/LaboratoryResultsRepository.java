package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.LaboratoryResults;

public interface LaboratoryResultsRepository extends CrudRepository<LaboratoryResults, Integer> {

    List<LaboratoryResults> findAll();

    List<LaboratoryResults> findByPrenatalRecordID(Integer prenatalRecordID);
}

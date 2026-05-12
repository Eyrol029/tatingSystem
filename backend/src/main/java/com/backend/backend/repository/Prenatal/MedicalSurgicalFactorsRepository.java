package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactors;

public interface MedicalSurgicalFactorsRepository extends CrudRepository<MedicalSurgicalFactors, Integer> {

    List<MedicalSurgicalFactors> findAll();

    List<MedicalSurgicalFactors> findByClinicalHistoryID(Integer clinicalHistoryID);
}

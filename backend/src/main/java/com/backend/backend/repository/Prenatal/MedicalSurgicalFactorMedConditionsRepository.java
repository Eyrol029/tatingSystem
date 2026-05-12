package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;

public interface MedicalSurgicalFactorMedConditionsRepository
        extends CrudRepository<MedicalSurgicalFactorMedConditions, Integer> {

    List<MedicalSurgicalFactorMedConditions> findAll();

    List<MedicalSurgicalFactorMedConditions> findByMedsurgID(Integer medsurgID);
}

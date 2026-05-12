package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements;

public interface MedicalSurgicalFactorMeasurementsRepository
        extends CrudRepository<MedicalSurgicalFactorMeasurements, Integer> {

    List<MedicalSurgicalFactorMeasurements> findAll();

    List<MedicalSurgicalFactorMeasurements> findByMedsurgID(Integer medsurgID);
}

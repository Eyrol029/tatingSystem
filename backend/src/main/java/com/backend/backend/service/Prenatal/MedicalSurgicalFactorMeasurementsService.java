package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements;

public interface MedicalSurgicalFactorMeasurementsService {

    MedicalSurgicalFactorMeasurements add(MedicalSurgicalFactorMeasurements data);

    MedicalSurgicalFactorMeasurements getById(Integer id);

    List<MedicalSurgicalFactorMeasurements> getAll();

    MedicalSurgicalFactorMeasurements update(MedicalSurgicalFactorMeasurements data);

    void delete(Integer id);

    List<MedicalSurgicalFactorMeasurements> getByMedsurgID(Integer medsurgID);
}

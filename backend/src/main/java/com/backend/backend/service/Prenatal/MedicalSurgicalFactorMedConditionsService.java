package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;

public interface MedicalSurgicalFactorMedConditionsService {

    MedicalSurgicalFactorMedConditions add(MedicalSurgicalFactorMedConditions data);

    MedicalSurgicalFactorMedConditions getById(Integer id);

    List<MedicalSurgicalFactorMedConditions> getAll();

    MedicalSurgicalFactorMedConditions update(MedicalSurgicalFactorMedConditions data);

    void delete(Integer id);

    List<MedicalSurgicalFactorMedConditions> getByMedsurgID(Integer medsurgID);
}

package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactors;

public interface MedicalSurgicalFactorsService {

    MedicalSurgicalFactors add(MedicalSurgicalFactors data);

    MedicalSurgicalFactors getById(Integer id);

    List<MedicalSurgicalFactors> getAll();

    MedicalSurgicalFactors update(MedicalSurgicalFactors data);

    void delete(Integer id);

    List<MedicalSurgicalFactors> getByClinicalHistoryID(Integer clinicalHistoryID);
}

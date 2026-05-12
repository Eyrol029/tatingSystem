package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.ObstetricRiskFactor;

public interface ObstetricRiskFactorService {

    ObstetricRiskFactor add(ObstetricRiskFactor data);

    ObstetricRiskFactor getById(Integer id);

    List<ObstetricRiskFactor> getAll();

    ObstetricRiskFactor update(ObstetricRiskFactor data);

    void delete(Integer id);

    List<ObstetricRiskFactor> getByClinicalHistoryID(Integer clinicalHistoryID);
}

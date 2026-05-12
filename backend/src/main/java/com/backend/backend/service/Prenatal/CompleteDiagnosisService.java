package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.CompleteDiagnosis;

public interface CompleteDiagnosisService {

    CompleteDiagnosis add(CompleteDiagnosis data);

    CompleteDiagnosis getById(Integer id);

    List<CompleteDiagnosis> getAll();

    CompleteDiagnosis update(CompleteDiagnosis data);

    void delete(Integer id);

    List<CompleteDiagnosis> getByClinicalHistoryID(Integer clinicalHistoryID);
}

package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk;

public interface AscertainPregnancyLowRiskService {

    AscertainPregnancyLowRisk addRecord(AscertainPregnancyLowRisk record);

    AscertainPregnancyLowRisk getRecordById(Integer id);

    List<AscertainPregnancyLowRisk> getRecords();

    AscertainPregnancyLowRisk updateRecord(AscertainPregnancyLowRisk record);

    void deleteRecord(Integer id);

    List<AscertainPregnancyLowRisk> getByClinicalHistoryID(Integer clinicalHistoryID);
}

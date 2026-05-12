package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.MenstrualHistory;

public interface MenstrualHistoryService {

    MenstrualHistory addRecord(MenstrualHistory record);

    MenstrualHistory getRecordById(Integer id);

    List<MenstrualHistory> getRecords();

    MenstrualHistory updateRecord(MenstrualHistory record);

    void deleteRecord(Integer id);

    List<MenstrualHistory> getByClinicalHistoryID(Integer clinicalHistoryID);
}

package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.NormalVitalSign;

public interface NormalVitalSignService {

    NormalVitalSign addRecord(NormalVitalSign record);

    NormalVitalSign getRecordById(Integer id);

    List<NormalVitalSign> getRecords();

    NormalVitalSign updateRecord(NormalVitalSign record);

    void deleteRecord(Integer id);

    List<NormalVitalSign> getByClinicalHistoryID(Integer clinicalHistoryID);
}

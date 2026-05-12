package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.PrenatalRecord;

public interface PrenatalRecordService {

    PrenatalRecord addRecord(PrenatalRecord record);

    PrenatalRecord getRecordById(Integer id);

    List<PrenatalRecord> getRecords();

    PrenatalRecord updateRecord(PrenatalRecord record);

    void deleteRecord(Integer id);

    List<PrenatalRecord> getByServiceID(Integer serviceID);
}

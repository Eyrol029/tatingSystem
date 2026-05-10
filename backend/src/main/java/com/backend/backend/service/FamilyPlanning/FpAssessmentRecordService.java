package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.FpAssessmentRecord;

public interface FpAssessmentRecordService {

    FpAssessmentRecord addRecord(FpAssessmentRecord record);

    FpAssessmentRecord getRecordById(Integer id);

    List<FpAssessmentRecord> getRecords();

    FpAssessmentRecord updateRecord(FpAssessmentRecord record);

    void deleteRecord(Integer id);

    List<FpAssessmentRecord> getByClientID(Integer clientID);
}

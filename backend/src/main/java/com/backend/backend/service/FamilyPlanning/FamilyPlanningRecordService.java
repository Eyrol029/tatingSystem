package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;

public interface FamilyPlanningRecordService {

    FamilyPlanningRecord addRecord(FamilyPlanningRecord record);

    FamilyPlanningRecord getRecordById(Integer id);

    List<FamilyPlanningRecord> getRecords();

    FamilyPlanningRecord updateRecord(FamilyPlanningRecord record);

    void deleteRecord(Integer id);

    List<FamilyPlanningRecord> getRecordsByClientID(Integer clientID);

    List<FamilyPlanningRecord> getRecordsByServiceID(Integer serviceID);
}

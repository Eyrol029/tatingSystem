package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;
import com.backend.backend.repository.FamilyPlanning.FamilyPlanningRecordRepository;

@Service
public class FamilyPlanningRecordServiceImpl implements FamilyPlanningRecordService {

    @Autowired
    private FamilyPlanningRecordRepository familyPlanningRecordRepository;

    @Override
    public FamilyPlanningRecord addRecord(FamilyPlanningRecord record) {
        return familyPlanningRecordRepository.save(record);
    }

    @Override
    public FamilyPlanningRecord getRecordById(Integer id) {
        return familyPlanningRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FamilyPlanningRecord not found: " + id));
    }

    @Override
    public List<FamilyPlanningRecord> getRecords() {
        return familyPlanningRecordRepository.findAll();
    }

    @Override
    public FamilyPlanningRecord updateRecord(FamilyPlanningRecord record) {
        return familyPlanningRecordRepository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        familyPlanningRecordRepository.deleteById(id);
    }

    @Override
    public List<FamilyPlanningRecord> getRecordsByClientID(Integer clientID) {
        return familyPlanningRecordRepository.findByClientID(clientID);
    }

    @Override
    public List<FamilyPlanningRecord> getRecordsByServiceID(Integer serviceID) {
        return familyPlanningRecordRepository.findByServiceID(serviceID);
    }
}

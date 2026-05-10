package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.FpAssessmentRecord;
import com.backend.backend.repository.FamilyPlanning.FpAssessmentRecordRepository;

@Service
public class FpAssessmentRecordServiceImpl implements FpAssessmentRecordService {

    @Autowired
    private FpAssessmentRecordRepository repository;

    @Override
    public FpAssessmentRecord addRecord(FpAssessmentRecord record) {
        return repository.save(record);
    }

    @Override
    public FpAssessmentRecord getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FP Assessment Record not found: " + id));
    }

    @Override
    public List<FpAssessmentRecord> getRecords() {
        return repository.findAll();
    }

    @Override
    public FpAssessmentRecord updateRecord(FpAssessmentRecord record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<FpAssessmentRecord> getByClientID(Integer clientID) {
        return repository.findByClientID(clientID);
    }
}

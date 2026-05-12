package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.MenstrualHistory;
import com.backend.backend.repository.Prenatal.MenstrualHistoryRepository;

@Service
public class MenstrualHistoryServiceImpl implements MenstrualHistoryService {

    @Autowired
    private MenstrualHistoryRepository repository;

    @Override
    public MenstrualHistory addRecord(MenstrualHistory record) {
        return repository.save(record);
    }

    @Override
    public MenstrualHistory getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenstrualHistory not found: " + id));
    }

    @Override
    public List<MenstrualHistory> getRecords() {
        return repository.findAll();
    }

    @Override
    public MenstrualHistory updateRecord(MenstrualHistory record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MenstrualHistory> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}

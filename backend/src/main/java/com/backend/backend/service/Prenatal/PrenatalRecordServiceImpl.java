package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;

@Service
public class PrenatalRecordServiceImpl implements PrenatalRecordService {

    @Autowired
    private PrenatalRecordRepository repository;

    @Override
    public PrenatalRecord addRecord(PrenatalRecord record) {
        return repository.save(record);
    }

    @Override
    public PrenatalRecord getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PrenatalRecord not found: " + id));
    }

    @Override
    public List<PrenatalRecord> getRecords() {
        return repository.findAll();
    }

    @Override
    public PrenatalRecord updateRecord(PrenatalRecord record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<PrenatalRecord> getByServiceID(Integer serviceID) {
        return repository.findByServiceID(serviceID);
    }
}

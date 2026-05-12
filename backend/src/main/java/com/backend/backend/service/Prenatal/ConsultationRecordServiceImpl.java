package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.ConsultationRecord;
import com.backend.backend.repository.Prenatal.ConsultationRecordRepository;

@Service
public class ConsultationRecordServiceImpl implements ConsultationRecordService {

    @Autowired
    private ConsultationRecordRepository repository;

    @Override
    public ConsultationRecord add(ConsultationRecord record) {
        return repository.save(record);
    }

    @Override
    public ConsultationRecord getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ConsultationRecord not found: " + id));
    }

    @Override
    public List<ConsultationRecord> getAll() {
        return repository.findAll();
    }

    @Override
    public ConsultationRecord update(ConsultationRecord record) {
        return repository.save(record);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ConsultationRecord> getByPrenatalRecordID(Integer prenatalRecordID) {
        return repository.findByPrenatalRecordID(prenatalRecordID);
    }
}

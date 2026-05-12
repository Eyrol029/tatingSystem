package com.backend.backend.service.Ultrasound;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Ultrasound.UltrasoundRecord;
import com.backend.backend.repository.Ultrasound.UltrasoundRecordRepository;

@Service
public class UltrasoundRecordServiceImpl implements UltrasoundRecordService {

    @Autowired
    private UltrasoundRecordRepository repository;

    @Override
    public UltrasoundRecord add(UltrasoundRecord record) {
        return repository.save(record);
    }

    @Override
    public UltrasoundRecord getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ultrasound record not found: " + id));
    }

    @Override
    public List<UltrasoundRecord> getAll() {
        return repository.findAll();
    }

    @Override
    public UltrasoundRecord update(UltrasoundRecord record) {
        return repository.save(record);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}

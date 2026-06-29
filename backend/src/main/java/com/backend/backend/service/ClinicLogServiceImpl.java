package com.backend.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.ClinicLog;
import com.backend.backend.repository.ClinicLogRepository;

@Service
public class ClinicLogServiceImpl implements ClinicLogService {

    @Autowired
    private ClinicLogRepository repository;

    @Override
    public ClinicLog add(ClinicLog log) {
        return repository.save(log);
    }

    @Override
    public ClinicLog getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClinicLog not found: " + id));
    }

    @Override
    public List<ClinicLog> getAll() {
        return repository.findAllByOrderByDateDesc();
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ClinicLog> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    @Override
    public List<ClinicLog> getByDateRange(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }

    @Override
    public List<ClinicLog> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}
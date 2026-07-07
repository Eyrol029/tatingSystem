package com.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Admission;
import com.backend.backend.repository.AdmissionRepository;

@Service
public class AdmissionServiceImpl implements AdmissionService {

    @Autowired
    private AdmissionRepository repository;

    @Override
    public Admission addAdmission(Admission admission) {
        return repository.save(admission);
    }

    @Override
    public Admission getAdmissionById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admission not found: " + id));
    }

    @Override
    public List<Admission> getAllAdmissions() {
        return repository.findAll();
    }

    @Override
    public Admission updateAdmission(Admission admission) {
        return repository.save(admission);
    }

    @Override
    public void deleteAdmission(Integer id) {
        repository.deleteById(id);
    }
}
package com.backend.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.model.LaboratoryRequest;
import com.backend.backend.repository.LaboratoryRequestRepository;

@Service
@Transactional
public class LaboratoryRequestServiceImpl implements LaboratoryRequestService {

    private final LaboratoryRequestRepository repository;

    @Autowired
    public LaboratoryRequestServiceImpl(LaboratoryRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryRequest> getAllRequests() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LaboratoryRequest> getRequestById(Long id) {
        return repository.findById(id);
    }

    @Override
    public LaboratoryRequest createRequest(LaboratoryRequest request) {
        // Business logic validation can go here before persisting
        if (request.getPatientName() == null || request.getPatientName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        return repository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaboratoryRequest> searchByPatientName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllRequests();
        }
        return repository.findByPatientNameContainingIgnoreCase(name.trim());
    }

    @Override
    public void deleteRequest(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Laboratory request not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
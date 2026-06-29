package com.backend.backend.service;

import com.backend.backend.model.ClinicalService;
import com.backend.backend.repository.ClinicalServiceRepository;
import com.backend.backend.service.ClinicalServiceServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalServiceServiceImpl implements ClinicalServiceServiceInterface {

    @Autowired
    private ClinicalServiceRepository repository;

    // ─── GET ALL ──────────────────────────────────────────────────
    @Override
    public List<ClinicalService> getAllServices() {
        return repository.findAll();
    }

    // ─── GET BY ID ────────────────────────────────────────────────
    @Override
    public Optional<ClinicalService> getServiceById(Long id) {
        return repository.findById(id);
    }

    // ─── GET BY CATEGORY ─────────────────────────────────────────
    @Override
    public List<ClinicalService> getServicesByCategory(String category) {
        return repository.findByCategory(category);
    }

    // ─── CREATE ───────────────────────────────────────────────────
    @Override
    public ClinicalService createService(ClinicalService service) {
        // Auto-generate unique service code: S001, S002, etc.
        long count = repository.count() + 1;
        String code = String.format("S%03d", count);

        while (repository.existsByServiceCode(code)) {
            count++;
            code = String.format("S%03d", count);
        }

        service.setServiceCode(code);
        return repository.save(service);
    }

    // ─── UPDATE ───────────────────────────────────────────────────
    @Override
    public Optional<ClinicalService> updateService(Long id, ClinicalService updatedService) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedService.getName());
            existing.setDescription(updatedService.getDescription());
            existing.setPrice(updatedService.getPrice());
            existing.setCategory(updatedService.getCategory());
            existing.setDuration(updatedService.getDuration());
            return repository.save(existing);
        });
    }

    // ─── DELETE ───────────────────────────────────────────────────
    @Override
    public boolean deleteService(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
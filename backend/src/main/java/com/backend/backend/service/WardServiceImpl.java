package com.backend.backend.service;

import com.backend.backend.model.Ward;
import com.backend.backend.repository.WardRepository;
import com.backend.backend.service.WardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WardServiceImpl implements WardServiceInterface {

    @Autowired
    private WardRepository repository;

    // ─── GET ALL ──────────────────────────────────────────────────
    @Override
    public List<Ward> getAllWards() {
        return repository.findAll();
    }

    // ─── GET BY ID ────────────────────────────────────────────────
    @Override
    public Optional<Ward> getWardById(Long id) {
        return repository.findById(id);
    }

    // ─── GET BY TYPE ──────────────────────────────────────────────
    @Override
    public List<Ward> getWardsByType(String type) {
        return repository.findByType(type);
    }

    // ─── GET BY STATUS ────────────────────────────────────────────
    @Override
    public List<Ward> getWardsByStatus(String status) {
        return repository.findByStatus(status);
    }

    // ─── GET BY FLOOR ─────────────────────────────────────────────
    @Override
    public List<Ward> getWardsByFloor(String floor) {
        return repository.findByFloor(floor);
    }

    // ─── GET BY TYPE AND STATUS ───────────────────────────────────
    @Override
    public List<Ward> getWardsByTypeAndStatus(String type, String status) {
        return repository.findByTypeAndStatus(type, status);
    }

    // ─── CREATE ───────────────────────────────────────────────────
    @Override
    public Ward createWard(Ward ward) {
        // Auto-generate unique ward code: W001, W002, etc.
        long count = repository.count() + 1;
        String code = String.format("W%03d", count);

        while (repository.existsByWardCode(code)) {
            count++;
            code = String.format("W%03d", count);
        }

        ward.setWardCode(code);

        // Auto-set status based on occupancy
        if (ward.getOccupied() >= ward.getCapacity()) {
            ward.setStatus("Full");
        } else if (!"Maintenance".equals(ward.getStatus())) {
            ward.setStatus("Available");
        }

        return repository.save(ward);
    }

    // ─── UPDATE ───────────────────────────────────────────────────
    @Override
    public Optional<Ward> updateWard(Long id, Ward updatedWard) {
        return repository.findById(id).map(existing -> {
            existing.setName(updatedWard.getName());
            existing.setType(updatedWard.getType());
            existing.setCapacity(updatedWard.getCapacity());
            existing.setOccupied(updatedWard.getOccupied());
            existing.setFloor(updatedWard.getFloor());
            existing.setFacilities(updatedWard.getFacilities());

            // Auto-update status based on occupancy
            if (!"Maintenance".equals(updatedWard.getStatus())) {
                if (updatedWard.getOccupied() >= updatedWard.getCapacity()) {
                    existing.setStatus("Full");
                } else {
                    existing.setStatus("Available");
                }
            } else {
                existing.setStatus("Maintenance");
            }

            return repository.save(existing);
        });
    }

    // ─── DELETE ───────────────────────────────────────────────────
    @Override
    public boolean deleteWard(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
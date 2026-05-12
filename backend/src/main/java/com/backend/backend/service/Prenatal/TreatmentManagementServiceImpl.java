package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.TreatmentManagement;
import com.backend.backend.repository.Prenatal.TreatmentManagementRepository;

@Service
public class TreatmentManagementServiceImpl implements TreatmentManagementService {

    @Autowired
    private TreatmentManagementRepository repository;

    @Override
    public TreatmentManagement add(TreatmentManagement treatment) {
        return repository.save(treatment);
    }

    @Override
    public TreatmentManagement getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TreatmentManagement not found: " + id));
    }

    @Override
    public List<TreatmentManagement> getAll() {
        return repository.findAll();
    }

    @Override
    public TreatmentManagement update(TreatmentManagement treatment) {
        return repository.save(treatment);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<TreatmentManagement> getByPrenatalRecordID(Integer prenatalRecordID) {
        return repository.findByPrenatalRecordID(prenatalRecordID);
    }
}

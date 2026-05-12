package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.VitalSigns;
import com.backend.backend.repository.Prenatal.VitalSignsRepository;

@Service
public class VitalSignsServiceImpl implements VitalSignsService {

    @Autowired
    private VitalSignsRepository repository;

    @Override
    public VitalSigns add(VitalSigns vitalSigns) {
        return repository.save(vitalSigns);
    }

    @Override
    public VitalSigns getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VitalSigns not found: " + id));
    }

    @Override
    public List<VitalSigns> getAll() {
        return repository.findAll();
    }

    @Override
    public VitalSigns update(VitalSigns vitalSigns) {
        return repository.save(vitalSigns);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<VitalSigns> getByConsultationRecordID(Integer consultationRecordID) {
        return repository.findByConsultationRecordID(consultationRecordID);
    }
}

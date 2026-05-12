package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;
import com.backend.backend.repository.Prenatal.MedicalSurgicalFactorMedConditionsRepository;

@Service
public class MedicalSurgicalFactorMedConditionsServiceImpl
        implements MedicalSurgicalFactorMedConditionsService {

    @Autowired
    private MedicalSurgicalFactorMedConditionsRepository repository;

    @Override
    public MedicalSurgicalFactorMedConditions add(MedicalSurgicalFactorMedConditions data) {
        return repository.save(data);
    }

    @Override
    public MedicalSurgicalFactorMedConditions getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedSurg Condition not found: " + id));
    }

    @Override
    public List<MedicalSurgicalFactorMedConditions> getAll() {
        return repository.findAll();
    }

    @Override
    public MedicalSurgicalFactorMedConditions update(MedicalSurgicalFactorMedConditions data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MedicalSurgicalFactorMedConditions> getByMedsurgID(Integer medsurgID) {
        return repository.findByMedsurgID(medsurgID);
    }
}

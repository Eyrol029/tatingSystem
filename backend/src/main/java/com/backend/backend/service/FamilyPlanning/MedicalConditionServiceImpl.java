package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.MedicalCondition;
import com.backend.backend.repository.FamilyPlanning.MedicalConditionRepository;

@Service
public class MedicalConditionServiceImpl implements MedicalConditionService {

    @Autowired
    private MedicalConditionRepository medicalConditionRepository;

    @Override
    public MedicalCondition addMedicalCondition(MedicalCondition medicalCondition) {
        return medicalConditionRepository.save(medicalCondition);
    }

    @Override
    public MedicalCondition getMedicalConditionById(Integer id) {
        return medicalConditionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalCondition not found: " + id));
    }

    @Override
    public List<MedicalCondition> getMedicalConditions() {
        return medicalConditionRepository.findAll();
    }

    @Override
    public MedicalCondition updateMedicalCondition(MedicalCondition medicalCondition) {
        return medicalConditionRepository.save(medicalCondition);
    }

    @Override
    public void deleteMedicalCondition(Integer id) {
        medicalConditionRepository.deleteById(id);
    }
}

package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.MedicalCondition;

public interface MedicalConditionService {

    MedicalCondition addMedicalCondition(MedicalCondition medicalCondition);

    MedicalCondition getMedicalConditionById(Integer id);

    List<MedicalCondition> getMedicalConditions();

    MedicalCondition updateMedicalCondition(MedicalCondition medicalCondition);

    void deleteMedicalCondition(Integer id);
}

package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.MedicalHistory;

public interface MedicalHistoryService {

    MedicalHistory addMedicalHistory(MedicalHistory medicalHistory);

    MedicalHistory getMedicalHistoryById(Integer id);

    List<MedicalHistory> getMedicalHistories();

    MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory);

    void deleteMedicalHistory(Integer id);

    List<MedicalHistory> getMedicalHistoryByClientID(Integer clientID);
}

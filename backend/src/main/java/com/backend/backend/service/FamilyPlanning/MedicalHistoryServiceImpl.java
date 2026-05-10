package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.MedicalHistory;
import com.backend.backend.repository.FamilyPlanning.MedicalHistoryRepository;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Override
    public MedicalHistory addMedicalHistory(MedicalHistory medicalHistory) {
        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public MedicalHistory getMedicalHistoryById(Integer id) {
        return medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalHistory not found: " + id));
    }

    @Override
    public List<MedicalHistory> getMedicalHistories() {
        return medicalHistoryRepository.findAll();
    }

    @Override
    public MedicalHistory updateMedicalHistory(MedicalHistory medicalHistory) {
        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public void deleteMedicalHistory(Integer id) {
        medicalHistoryRepository.deleteById(id);
    }

    @Override
    public List<MedicalHistory> getMedicalHistoryByClientID(Integer clientID) {
        return medicalHistoryRepository.findByClientID(clientID);
    }
}

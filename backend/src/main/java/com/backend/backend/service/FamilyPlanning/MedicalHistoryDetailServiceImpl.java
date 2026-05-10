package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail;
import com.backend.backend.repository.FamilyPlanning.MedicalHistoryDetailRepository;

@Service
public class MedicalHistoryDetailServiceImpl implements MedicalHistoryDetailService {

    @Autowired
    private MedicalHistoryDetailRepository medicalHistoryDetailRepository;

    @Override
    public MedicalHistoryDetail addMedicalHistoryDetail(MedicalHistoryDetail medicalHistoryDetail) {
        return medicalHistoryDetailRepository.save(medicalHistoryDetail);
    }

    @Override
    public MedicalHistoryDetail getMedicalHistoryDetailById(Integer id) {
        return medicalHistoryDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalHistoryDetail not found: " + id));
    }

    @Override
    public List<MedicalHistoryDetail> getMedicalHistoryDetails() {
        return medicalHistoryDetailRepository.findAll();
    }

    @Override
    public MedicalHistoryDetail updateMedicalHistoryDetail(MedicalHistoryDetail medicalHistoryDetail) {
        return medicalHistoryDetailRepository.save(medicalHistoryDetail);
    }

    @Override
    public void deleteMedicalHistoryDetail(Integer id) {
        medicalHistoryDetailRepository.deleteById(id);
    }

    @Override
    public List<MedicalHistoryDetail> getMedicalHistoryDetailsByMedicalHistoryid(Integer medicalHistoryid) {
        return medicalHistoryDetailRepository.findByMedicalHistoryid(medicalHistoryid);
    }
}

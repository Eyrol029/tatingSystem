package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail;

public interface MedicalHistoryDetailService {

    MedicalHistoryDetail addMedicalHistoryDetail(MedicalHistoryDetail medicalHistoryDetail);

    MedicalHistoryDetail getMedicalHistoryDetailById(Integer id);

    List<MedicalHistoryDetail> getMedicalHistoryDetails();

    MedicalHistoryDetail updateMedicalHistoryDetail(MedicalHistoryDetail medicalHistoryDetail);

    void deleteMedicalHistoryDetail(Integer id);

    List<MedicalHistoryDetail> getMedicalHistoryDetailsByMedicalHistoryid(Integer medicalHistoryid);
}

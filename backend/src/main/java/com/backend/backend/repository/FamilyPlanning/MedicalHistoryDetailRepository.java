package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail;

public interface MedicalHistoryDetailRepository extends CrudRepository<MedicalHistoryDetail, Integer> {

    List<MedicalHistoryDetail> findAll();

    List<MedicalHistoryDetail> findByMedicalHistoryid(Integer medicalHistoryid);
}

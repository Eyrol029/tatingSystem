package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;

public interface ClinicalHistoryAndPhysicalExaminationRepository
        extends CrudRepository<ClinicalHistoryAndPhysicalExamination, Integer> {

    List<ClinicalHistoryAndPhysicalExamination> findAll();

    List<ClinicalHistoryAndPhysicalExamination> findByPrenatalrecordID(Integer prenatalrecordID);
}

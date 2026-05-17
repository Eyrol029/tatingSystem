package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.PelvicExamination;

public interface PelvicExaminationRepository extends CrudRepository<PelvicExamination, Integer> {
    List<PelvicExamination> findAll();

    List<PelvicExamination> findBypExamID(Integer pExamID);
}
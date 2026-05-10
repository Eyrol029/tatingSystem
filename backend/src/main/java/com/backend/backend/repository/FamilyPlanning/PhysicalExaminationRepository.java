package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.PhysicalExamination;

public interface PhysicalExaminationRepository extends CrudRepository<PhysicalExamination, Integer> {

    List<PhysicalExamination> findAll();

    List<PhysicalExamination> findByClientID(Integer clientID);
}

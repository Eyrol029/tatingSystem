package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.MedicalCondition;

public interface MedicalConditionRepository extends CrudRepository<MedicalCondition, Integer> {

    List<MedicalCondition> findAll();
}

package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.ObstetricalCondition;

public interface ObstetricalConditionRepository extends CrudRepository<ObstetricalCondition, Integer> {

    List<ObstetricalCondition> findAll();
}

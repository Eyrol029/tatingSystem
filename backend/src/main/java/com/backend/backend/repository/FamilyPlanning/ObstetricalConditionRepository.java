package com.backend.backend.repository.familyplanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.familyplanning.ObstetricalCondition;

public interface ObstetricalConditionRepository extends CrudRepository<ObstetricalCondition, Integer> {

    List<ObstetricalCondition> findAll();
}

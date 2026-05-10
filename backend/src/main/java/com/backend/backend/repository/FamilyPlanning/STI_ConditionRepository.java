package com.backend.backend.repository.familyplanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.familyplanning.STI_Condition;

public interface STI_ConditionRepository extends CrudRepository<STI_Condition, Integer> {

    List<STI_Condition> findAll();
}

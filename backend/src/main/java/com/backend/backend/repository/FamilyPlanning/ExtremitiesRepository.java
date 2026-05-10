package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Extremities;

public interface ExtremitiesRepository extends CrudRepository<Extremities, Integer> {

    List<Extremities> findAll();

    List<Extremities> findByPExamID(Integer pExamID);
}

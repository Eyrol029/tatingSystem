package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Breast;

public interface BreastRepository extends CrudRepository<Breast, Integer> {
    List<Breast> findAll();
    List<Breast> findBypExamID(Integer pExamID);
}
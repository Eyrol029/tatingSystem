package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Neck;

public interface NeckRepository extends CrudRepository<Neck, Integer> {

    List<Neck> findAll();

    List<Neck> findByPExamID(Integer pExamID);
}

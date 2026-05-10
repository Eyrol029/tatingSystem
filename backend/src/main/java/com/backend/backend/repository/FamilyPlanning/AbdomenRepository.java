package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Abdomen;

public interface AbdomenRepository extends CrudRepository<Abdomen, Integer> {

    List<Abdomen> findAll();

    List<Abdomen> findByPExamID(Integer pExamID);
}

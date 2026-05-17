package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Skin;

public interface SkinRepository extends CrudRepository<Skin, Integer> {

    List<Skin> findAll();

    List<Skin> findBypExamID(Integer pExamID);
}

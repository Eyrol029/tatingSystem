package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Conjunctiva;

public interface ConjunctivaRepository extends CrudRepository<Conjunctiva, Integer> {

    List<Conjunctiva> findAll();

    List<Conjunctiva> findBypExamID(Integer pExamID);
}

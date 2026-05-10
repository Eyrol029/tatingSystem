package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed;

public interface MethodCurrentlyUsedRepository extends CrudRepository<MethodCurrentlyUsed, Integer> {

    List<MethodCurrentlyUsed> findAll();

    List<MethodCurrentlyUsed> findByTypeID(Integer typeID);
}

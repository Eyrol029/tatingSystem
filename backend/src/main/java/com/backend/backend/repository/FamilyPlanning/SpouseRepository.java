package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Spouse;

public interface SpouseRepository extends CrudRepository<Spouse, Integer> {

    List<Spouse> findAll();

    List<Spouse> findByClientID(Integer clientID);

    List<Spouse> findByLName(String lName);
}

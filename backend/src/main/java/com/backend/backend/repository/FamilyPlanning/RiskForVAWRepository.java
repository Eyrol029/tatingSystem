package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.RiskForVAW;

public interface RiskForVAWRepository extends CrudRepository<RiskForVAW, Integer> {

    List<RiskForVAW> findAll();

    List<RiskForVAW> findByClientID(Integer clientID);
}

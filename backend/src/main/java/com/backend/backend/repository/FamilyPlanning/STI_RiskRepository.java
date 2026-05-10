package com.backend.backend.repository.familyplanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.familyplanning.STI_Risk;

public interface STI_RiskRepository extends CrudRepository<STI_Risk, Integer> {

    List<STI_Risk> findAll();

    List<STI_Risk> findByClientID(Integer clientID);
}

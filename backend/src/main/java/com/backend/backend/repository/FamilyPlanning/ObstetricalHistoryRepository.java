package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.ObstetricalHistory;

public interface ObstetricalHistoryRepository extends CrudRepository<ObstetricalHistory,Integer> {

    List<ObstetricalHistory> findAll();

    List<ObstetricalHistory> findByClientID(Integer clientID);
}

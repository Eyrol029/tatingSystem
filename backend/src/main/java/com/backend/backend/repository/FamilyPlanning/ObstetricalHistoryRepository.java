package com.backend.backend.repository.familyplanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.familyplanning.ObstetricalHistory;

public interface ObstetricalHistoryRepository extends CrudRepository<ObstetricalHistory, Integer> {

    List<ObstetricalHistory> findAll();

    List<ObstetricalHistory> findByClientID(Integer clientID);
}

package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.MenstrualHistory;

public interface MenstrualHistoryRepository extends CrudRepository<MenstrualHistory, Integer> {

    List<MenstrualHistory> findAll();

    List<MenstrualHistory> findByClinicalHistoryID(Integer clinicalHistoryID);
}

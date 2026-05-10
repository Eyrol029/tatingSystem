package com.backend.backend.repository.familyplanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.familyplanning.ObstetricalConditionDetail;

public interface ObstetricalConditionDetailRepository extends CrudRepository<ObstetricalConditionDetail, Integer> {

    List<ObstetricalConditionDetail> findAll();

    List<ObstetricalConditionDetail> findByObstetricalHistoryID(Integer obstetricalHistoryID);
}

package com.backend.backend.service.familyplanning;

import java.util.List;

import com.backend.backend.model.familyplanning.ObstetricalCondition;

public interface ObstetricalConditionService {

    ObstetricalCondition addCondition(ObstetricalCondition condition);

    ObstetricalCondition getById(Integer id);

    List<ObstetricalCondition> getAll();

    ObstetricalCondition update(ObstetricalCondition condition);

    void delete(Integer id);
}

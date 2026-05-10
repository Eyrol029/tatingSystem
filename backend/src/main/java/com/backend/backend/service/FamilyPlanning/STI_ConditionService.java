package com.backend.backend.service.familyplanning;

import java.util.List;

import com.backend.backend.model.familyplanning.STI_Condition;

public interface STI_ConditionService {

    STI_Condition addCondition(STI_Condition condition);

    STI_Condition getById(Integer id);

    List<STI_Condition> getAll();

    STI_Condition update(STI_Condition condition);

    void delete(Integer id);
}

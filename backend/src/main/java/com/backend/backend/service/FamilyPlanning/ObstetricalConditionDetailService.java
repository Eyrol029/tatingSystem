package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.ObstetricalConditionDetail;

public interface ObstetricalConditionDetailService {

    ObstetricalConditionDetail addDetail(ObstetricalConditionDetail detail);

    ObstetricalConditionDetail getById(Integer id);

    List<ObstetricalConditionDetail> getAll();

    ObstetricalConditionDetail update(ObstetricalConditionDetail detail);

    void delete(Integer id);

    List<ObstetricalConditionDetail> getByHistoryID(Integer obstetricalHistoryID);
}

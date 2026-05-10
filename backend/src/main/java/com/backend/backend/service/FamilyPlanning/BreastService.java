package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Breast;

public interface BreastService {

    Breast addBreast(Breast breast);

    Breast getBreastById(Integer id);

    List<Breast> getBreasts();

    Breast updateBreast(Breast breast);

    void deleteBreast(Integer id);

    List<Breast> getByPExamID(Integer pExamID);
}

package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Extremities;

public interface ExtremitiesService {

    Extremities addExtremities(Extremities extremities);

    Extremities getExtremitiesById(Integer id);

    List<Extremities> getExtremities();

    Extremities updateExtremities(Extremities extremities);

    void deleteExtremities(Integer id);

    List<Extremities> getByPExamID(Integer pExamID);
}

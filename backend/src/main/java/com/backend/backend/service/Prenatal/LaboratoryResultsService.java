package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.LaboratoryResults;

public interface LaboratoryResultsService {

    LaboratoryResults add(LaboratoryResults result);

    LaboratoryResults getById(Integer id);

    List<LaboratoryResults> getAll();

    LaboratoryResults update(LaboratoryResults result);

    void delete(Integer id);

    List<LaboratoryResults> getByPrenatalRecordID(Integer prenatalRecordID);
}

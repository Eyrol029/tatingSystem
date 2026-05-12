package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.TreatmentManagement;

public interface TreatmentManagementService {

    TreatmentManagement add(TreatmentManagement treatment);

    TreatmentManagement getById(Integer id);

    List<TreatmentManagement> getAll();

    TreatmentManagement update(TreatmentManagement treatment);

    void delete(Integer id);

    List<TreatmentManagement> getByPrenatalRecordID(Integer prenatalRecordID);
}

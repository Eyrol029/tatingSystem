package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.VitalSigns;

public interface VitalSignsService {

    VitalSigns add(VitalSigns vitalSigns);

    VitalSigns getById(Integer id);

    List<VitalSigns> getAll();

    VitalSigns update(VitalSigns vitalSigns);

    void delete(Integer id);

    List<VitalSigns> getByConsultationRecordID(Integer consultationRecordID);
}

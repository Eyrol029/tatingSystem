package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.ConsultationVisits;

public interface ConsultationVisitsService {

    ConsultationVisits add(ConsultationVisits visit);

    ConsultationVisits getById(Integer id);

    List<ConsultationVisits> getAll();

    ConsultationVisits update(ConsultationVisits visit);

    void delete(Integer id);

    List<ConsultationVisits> getByConsultationRecordID(Integer consultationRecordID);
}

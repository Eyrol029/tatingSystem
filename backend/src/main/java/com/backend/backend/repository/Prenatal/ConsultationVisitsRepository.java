package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.ConsultationVisits;

public interface ConsultationVisitsRepository extends CrudRepository<ConsultationVisits, Integer> {

    List<ConsultationVisits> findAll();

    List<ConsultationVisits> findByConsultationRecordID(Integer consultationRecordID);
}

package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.ConsultationRecord;

public interface ConsultationRecordRepository extends CrudRepository<ConsultationRecord, Integer> {

    List<ConsultationRecord> findAll();

    List<ConsultationRecord> findByPrenatalRecordID(Integer prenatalRecordID);
}

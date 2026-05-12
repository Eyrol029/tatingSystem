package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.VitalSigns;

public interface VitalSignsRepository extends CrudRepository<VitalSigns, Integer> {

    List<VitalSigns> findAll();

    List<VitalSigns> findByConsultationRecordID(Integer consultationRecordID);
}

package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.PrenatalRecord;

public interface PrenatalRecordRepository extends CrudRepository<PrenatalRecord, Integer> {

    List<PrenatalRecord> findAll();

    List<PrenatalRecord> findByServiceID(Integer serviceID);
}

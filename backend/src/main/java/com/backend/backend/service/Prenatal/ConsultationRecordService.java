package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.ConsultationRecord;

public interface ConsultationRecordService {

    ConsultationRecord add(ConsultationRecord record);

    ConsultationRecord getById(Integer id);

    List<ConsultationRecord> getAll();

    ConsultationRecord update(ConsultationRecord record);

    void delete(Integer id);

    List<ConsultationRecord> getByPrenatalRecordID(Integer prenatalRecordID);
}

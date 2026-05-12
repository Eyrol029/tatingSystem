package com.backend.backend.service.Ultrasound;

import java.util.List;

import com.backend.backend.model.Ultrasound.UltrasoundRecord;

public interface UltrasoundRecordService {

    UltrasoundRecord add(UltrasoundRecord record);

    UltrasoundRecord getById(Integer id);

    List<UltrasoundRecord> getAll();

    UltrasoundRecord update(UltrasoundRecord record);

    void delete(Integer id);
}

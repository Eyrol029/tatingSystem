package com.backend.backend.repository.Ultrasound;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.Ultrasound.UltrasoundRecord;

public interface UltrasoundRecordRepository extends JpaRepository<UltrasoundRecord, Integer> {
}

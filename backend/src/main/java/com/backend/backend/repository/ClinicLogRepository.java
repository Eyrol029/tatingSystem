package com.backend.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.ClinicLog;

public interface ClinicLogRepository extends JpaRepository<ClinicLog, Integer> {

    List<ClinicLog> findAllByOrderByDateDesc();

    List<ClinicLog> findByDate(LocalDate date);

    List<ClinicLog> findByDateBetween(LocalDate start, LocalDate end);

    List<ClinicLog> findByNameContainingIgnoreCase(String name);
}
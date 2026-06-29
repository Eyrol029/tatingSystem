package com.backend.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.backend.backend.model.ClinicLog;

public interface ClinicLogService {

    ClinicLog add(ClinicLog log);

    ClinicLog getById(Integer id);

    List<ClinicLog> getAll();

    void delete(Integer id);

    List<ClinicLog> getByDate(LocalDate date);

    List<ClinicLog> getByDateRange(LocalDate start, LocalDate end);

    List<ClinicLog> searchByName(String name);
}
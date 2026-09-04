package com.backend.backend.repository.Billing;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.Billing.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
    List<Revenue> findByPatientID(Integer patientID);
    void deleteByPatientID(Integer patientID);

    // NEW: for dashboard financial trend
    List<Revenue> findByRevenueDateBetween(LocalDate start, LocalDate end);
}
package com.backend.backend.service;

import java.time.LocalDate;

import com.backend.backend.model.DashboardSummaryDTO;

public interface DashboardService {

    /**
     * Returns the full dashboard summary.
     * @param start optional date range start (inclusive) — null = all time
     * @param end   optional date range end   (inclusive) — null = all time
     */
    DashboardSummaryDTO getSummary(LocalDate start, LocalDate end);
}
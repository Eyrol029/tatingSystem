package com.backend.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.backend.backend.dto.PhilHealthReportDTO;

public interface ReportService {

    /**
     * Retrieves aggregated PhilHealth and Clinical report data including
     * summary KPI statistics, breakdown per service with case numbers,
     * and individual patient masterlist entries.
     */
    PhilHealthReportDTO getPhilHealthSummary(LocalDate start, LocalDate end, String serviceName, String category);

    /**
     * Gets distinct available service names for report filtering.
     */
    List<String> getAvailableServices();

    /**
     * Gets distinct categories for report filtering.
     */
    List<String> getAvailableCategories();
}

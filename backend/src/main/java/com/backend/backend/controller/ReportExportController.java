package com.backend.backend.controller;

import com.backend.backend.dto.PhilHealthReportDTO;
import com.backend.backend.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportExportController {

    private final ReportService reportService;

    public ReportExportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Returns the PhilHealth summary as JSON for in-app display.
     */
    @GetMapping(value = "/philhealth", produces = "application/json")
    public PhilHealthReportDTO getPhilHealthReport(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "category", required = false) String category) {
        return reportService.getPhilHealthSummary(start, end, service, category);
    }

    /**
     * Exports the PhilHealth summary as a CSV file. The CSV contains a row per patient record
     * with the case number and related details.
     */
    @GetMapping(value = "/philhealth/csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportPhilHealthCsv(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(value = "service", required = false) String service,
            @RequestParam(value = "category", required = false) String category) {
        PhilHealthReportDTO report = reportService.getPhilHealthSummary(start, end, service, category);
        byte[] csvBytes = generateCsv(report);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "philhealth_report.csv");
        return new ResponseEntity<byte[]>(csvBytes, headers, HttpStatus.OK);
    }

    private byte[] generateCsv(PhilHealthReportDTO report) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        // Header
        writer.println("Case Number,Patient Name,Age,Service Name,Category,Patient Type,PhilHealth,Date Availed");
        List<PhilHealthReportDTO.PatientReportItem> items = report.getPatientRecords();
        if (items != null) {
            for (PhilHealthReportDTO.PatientReportItem item : items) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                        safeCsv(item.getCaseNumber()),
                        safeCsv(item.getPatientName()),
                        item.getAge() != null ? String.valueOf(item.getAge()) : "",
                        safeCsv(item.getServiceName()),
                        safeCsv(item.getServiceCategory()),
                        safeCsv(item.getRiskStatus()),
                        item.getHasPhilHealth() != null && item.getHasPhilHealth() ? "Yes" : "No",
                        item.getDateAvailed() != null ? item.getDateAvailed().toString() : "");
            }
        }
        writer.flush();
        return out.toByteArray();
    }

    private String safeCsv(String value) {
        if (value == null) return "";
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }
}

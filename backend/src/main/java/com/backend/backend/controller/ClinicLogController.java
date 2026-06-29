package com.backend.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.ClinicLog;
import com.backend.backend.service.ClinicLogService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clinic-logs")
public class ClinicLogController {

    @Autowired
    private ClinicLogService service;

    // CREATE
    @PostMapping
    public ClinicLog add(@RequestBody ClinicLog log) {
        return service.add(log);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ClinicLog getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // GET ALL (sorted by date desc)
    @GetMapping
    public List<ClinicLog> getAll() {
        return service.getAll();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("ClinicLog deleted successfully.");
    }

    // GET BY DATE — e.g. /api/clinic-logs/by-date?date=2025-06-15
    @GetMapping("/by-date")
    public List<ClinicLog> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getByDate(date);
    }

    // GET BY DATE RANGE — e.g. /api/clinic-logs/by-range?start=2025-06-01&end=2025-06-30
    @GetMapping("/by-range")
    public List<ClinicLog> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.getByDateRange(start, end);
    }

    // SEARCH BY NAME — e.g. /api/clinic-logs/search?name=Maria
    @GetMapping("/search")
    public List<ClinicLog> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }
}
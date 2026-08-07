package com.backend.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // Autowires LaboratoryRequestServiceImpl
import org.springframework.http.HttpStatus;
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

import com.backend.backend.model.LaboratoryRequest;
import com.backend.backend.service.LaboratoryRequestService;

@RestController
@RequestMapping("/api/laboratory-requests")
@CrossOrigin(origins = "*")
public class LaboratoryRequestController {

    private final LaboratoryRequestService service;

    @Autowired
    public LaboratoryRequestController(LaboratoryRequestService service) {
        this.service = service;
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<LaboratoryRequest>> getAllRequests(
            @RequestParam(required = false) String patientName) {
        if (patientName != null && !patientName.trim().isEmpty()) {
            return ResponseEntity.ok(service.searchByPatientName(patientName));
        }
        return ResponseEntity.ok(service.getAllRequests());
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryRequest> getRequestById(@PathVariable Long id) {
        return service.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody LaboratoryRequest request) {
        try {
            LaboratoryRequest savedRequest = service.createRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        try {
            service.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
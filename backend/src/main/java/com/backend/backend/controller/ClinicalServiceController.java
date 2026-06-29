package com.backend.backend.controller;

import com.backend.backend.model.ClinicalService;
import com.backend.backend.service.ClinicalServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinical-services")
@CrossOrigin(origins = "http://localhost:5173")
public class ClinicalServiceController {

    @Autowired
    private ClinicalServiceServiceImpl service;

    // GET all services
    @GetMapping
    public ResponseEntity<List<ClinicalService>> getAllServices() {
        return ResponseEntity.ok(service.getAllServices());
    }

    // GET single service by ID
    @GetMapping("/{id}")
    public ResponseEntity<ClinicalService> getServiceById(@PathVariable Long id) {
        return service.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET services by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ClinicalService>> getServicesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getServicesByCategory(category));
    }

    // POST create new service
    @PostMapping
    public ResponseEntity<ClinicalService> createService(@RequestBody ClinicalService clinicalService) {
        ClinicalService created = service.createService(clinicalService);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update service
    @PutMapping("/{id}")
    public ResponseEntity<ClinicalService> updateService(
            @PathVariable Long id,
            @RequestBody ClinicalService clinicalService) {
        return service.updateService(id, clinicalService)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (service.deleteService(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

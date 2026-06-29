package com.backend.backend.controller;

import com.backend.backend.model.Ward;
import com.backend.backend.service.WardServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
@CrossOrigin(origins = "http://localhost:5173")
public class WardController {

    @Autowired
    private WardServiceInterface service;

    // GET all wards
    @GetMapping
    public ResponseEntity<List<Ward>> getAllWards() {
        return ResponseEntity.ok(service.getAllWards());
    }

    // GET ward by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ward> getWardById(@PathVariable Long id) {
        return service.getWardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET wards by type: /api/wards/type/Maternity
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Ward>> getWardsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getWardsByType(type));
    }

    // GET wards by status: /api/wards/status/Available
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ward>> getWardsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getWardsByStatus(status));
    }

    // GET wards by floor: /api/wards/floor/2nd Floor
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Ward>> getWardsByFloor(@PathVariable String floor) {
        return ResponseEntity.ok(service.getWardsByFloor(floor));
    }

    // GET wards by type AND status: /api/wards/filter?type=Maternity&status=Available
    @GetMapping("/filter")
    public ResponseEntity<List<Ward>> getWardsByTypeAndStatus(
            @RequestParam String type,
            @RequestParam String status) {
        return ResponseEntity.ok(service.getWardsByTypeAndStatus(type, status));
    }

    // POST create new ward
    @PostMapping
    public ResponseEntity<Ward> createWard(@RequestBody Ward ward) {
        Ward created = service.createWard(ward);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update ward
    @PutMapping("/{id}")
    public ResponseEntity<Ward> updateWard(
            @PathVariable Long id,
            @RequestBody Ward ward) {
        return service.updateWard(id, ward)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE ward
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable Long id) {
        if (service.deleteWard(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

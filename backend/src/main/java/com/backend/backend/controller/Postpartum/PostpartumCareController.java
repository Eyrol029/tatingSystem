package com.backend.backend.controller.Postpartum;

import com.backend.backend.model.PostpartumCareRecord;
import com.backend.backend.service.PostpartumCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postpartum")
@CrossOrigin(origins = "http://localhost:5173") // Matches Vue dev server port
public class PostpartumCareController {

    private final PostpartumCareService service;

    @Autowired
    public PostpartumCareController(PostpartumCareService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PostpartumCareRecord>> getAllRecords(
            @RequestParam(required = false) Long serviceID) {
        if (serviceID != null) {
            return ResponseEntity.ok(service.getRecordsByServiceID(serviceID));
        }
        return ResponseEntity.ok(service.getAllRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostpartumCareRecord> getRecordById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getRecordById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody PostpartumCareRecord record) {
        try {
            PostpartumCareRecord created = service.createRecord(record);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateRecordWithoutIdInPath(@RequestBody PostpartumCareRecord record) {
        if (record.getPostpartumRecordID() == null) {
            return ResponseEntity.badRequest().body("postpartumRecordID is required in the body for PUT requests.");
        }
        try {
            PostpartumCareRecord updated = service.updateRecord(record.getPostpartumRecordID(), record);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable Long id, @RequestBody PostpartumCareRecord record) {
        try {
            PostpartumCareRecord updated = service.updateRecord(id, record);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        try {
            service.deleteRecord(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

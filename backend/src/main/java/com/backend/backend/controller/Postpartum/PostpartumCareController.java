package com.backend.backend.controller.Postpartum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Postpartum.PostpartumCareRecord;
import com.backend.backend.service.Postpartum.PostpartumCareService;

@RestController
@RequestMapping("/api/postpartum")
@CrossOrigin(origins = "*") // Matches Vue dev server port
public class PostpartumCareController {

    private final PostpartumCareService service;

    @Autowired
    public PostpartumCareController(PostpartumCareService service) {
        this.service = service;
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<PostpartumCareRecord>> getAllRecords(
            @RequestParam(required = false) Long serviceID) {
        if (serviceID != null) {
            return ResponseEntity.ok(service.getRecordsByServiceID(serviceID));
        }
        return ResponseEntity.ok(service.getAllRecords());
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<PostpartumCareRecord> getRecordById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getRecordById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
@CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody PostpartumCareRecord record) {
        try {
            PostpartumCareRecord created = service.createRecord(record);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
@CrossOrigin(origins = "*")
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
@CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable Long id, @RequestBody PostpartumCareRecord record) {
        try {
            PostpartumCareRecord updated = service.updateRecord(id, record);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
@CrossOrigin(origins = "*")
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

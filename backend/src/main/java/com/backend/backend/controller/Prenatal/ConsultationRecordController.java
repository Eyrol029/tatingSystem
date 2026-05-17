package com.backend.backend.controller.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Prenatal.ConsultationRecord;
import com.backend.backend.service.Prenatal.ConsultationRecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/consultationrecord")
public class ConsultationRecordController {

    @Autowired
    private ConsultationRecordService service;

@CrossOrigin(origins = "*")
    // CREATE
    @PostMapping
    public ConsultationRecord add(@RequestBody ConsultationRecord record) {
        return service.add(record);
    }
@CrossOrigin(origins = "*")
    // GET BY ID
    @GetMapping("/{id}")
    public ConsultationRecord getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    // GET ALL
    @GetMapping
    public List<ConsultationRecord> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    // UPDATE
    @PutMapping
    public ConsultationRecord update(@RequestBody ConsultationRecord record) {
        return service.update(record);
    }
@CrossOrigin(origins = "*")
    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ConsultationRecord deleted successfully.";
    }
}


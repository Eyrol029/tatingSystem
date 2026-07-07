package com.backend.backend.controller;

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

import com.backend.backend.model.Admission;
import com.backend.backend.service.AdmissionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    @Autowired
    private AdmissionService service;

    @GetMapping
    public List<Admission> getAll() {
        return service.getAllAdmissions();
    }

    @GetMapping("/{id}")
    public Admission getById(@PathVariable Integer id) {
        return service.getAdmissionById(id);
    }

    @PostMapping
    public Admission add(@RequestBody Admission admission) {
        return service.addAdmission(admission);
    }

    @PutMapping
    public Admission update(@RequestBody Admission admission) {
        return service.updateAdmission(admission);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteAdmission(id);
        return "Admission deleted successfully.";
    }
}
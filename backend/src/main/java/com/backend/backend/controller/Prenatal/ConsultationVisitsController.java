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

import com.backend.backend.model.Prenatal.ConsultationVisits;
import com.backend.backend.service.Prenatal.ConsultationVisitsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/consultation-visits")
public class ConsultationVisitsController {

    @Autowired
    private ConsultationVisitsService service;

    @PostMapping
    public ConsultationVisits add(@RequestBody ConsultationVisits visit) {
        return service.add(visit);
    }

    @GetMapping("/{id}")
    public ConsultationVisits getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ConsultationVisits> getAll() {
        return service.getAll();
    }

    @PutMapping
    public ConsultationVisits update(@RequestBody ConsultationVisits visit) {
        return service.update(visit);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ConsultationVisits deleted successfully.";
    }

    @GetMapping("/consultation/{consultationRecordID}")
    public List<ConsultationVisits> getByConsultation(@PathVariable Integer consultationRecordID) {
        return service.getByConsultationRecordID(consultationRecordID);
    }
}

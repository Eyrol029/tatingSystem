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

import com.backend.backend.model.Prenatal.VitalSigns;
import com.backend.backend.service.Prenatal.VitalSignsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/vital-signs")
public class VitalSignsController {

    @Autowired
    private VitalSignsService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public VitalSigns add(@RequestBody VitalSigns vitalSigns) {
        return service.add(vitalSigns);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public VitalSigns getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<VitalSigns> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public VitalSigns update(@RequestBody VitalSigns vitalSigns) {
        return service.update(vitalSigns);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "VitalSigns deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/consultation/{consultationRecordID}")
    public List<VitalSigns> getByConsultation(@PathVariable Integer consultationRecordID) {
        return service.getByConsultationRecordID(consultationRecordID);
    }
}

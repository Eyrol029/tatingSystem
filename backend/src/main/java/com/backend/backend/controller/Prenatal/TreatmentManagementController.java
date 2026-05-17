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

import com.backend.backend.model.Prenatal.TreatmentManagement;
import com.backend.backend.service.Prenatal.TreatmentManagementService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/treatment-management")
public class TreatmentManagementController {

    @Autowired
    private TreatmentManagementService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public TreatmentManagement add(@RequestBody TreatmentManagement treatment) {
        return service.add(treatment);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public TreatmentManagement getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<TreatmentManagement> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public TreatmentManagement update(@RequestBody TreatmentManagement treatment) {
        return service.update(treatment);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "TreatmentManagement deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/prenatal/{prenatalRecordID}")
    public List<TreatmentManagement> getByPrenatalRecord(@PathVariable Integer prenatalRecordID) {
        return service.getByPrenatalRecordID(prenatalRecordID);
    }
}

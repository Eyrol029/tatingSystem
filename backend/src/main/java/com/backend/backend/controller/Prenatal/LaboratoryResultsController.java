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

import com.backend.backend.model.Prenatal.LaboratoryResults;
import com.backend.backend.service.Prenatal.LaboratoryResultsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/laboratory-results")
public class LaboratoryResultsController {

    @Autowired
    private LaboratoryResultsService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public LaboratoryResults add(@RequestBody LaboratoryResults result) {
        return service.add(result);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public LaboratoryResults getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<LaboratoryResults> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public LaboratoryResults update(@RequestBody LaboratoryResults result) {
        return service.update(result);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "LaboratoryResults deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/prenatal/{prenatalRecordID}")
    public List<LaboratoryResults> getByPrenatalRecord(@PathVariable Integer prenatalRecordID) {
        return service.getByPrenatalRecordID(prenatalRecordID);
    }
}

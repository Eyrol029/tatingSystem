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

import com.backend.backend.model.Prenatal.ObstetricRiskFactor;
import com.backend.backend.service.Prenatal.ObstetricRiskFactorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/obstetric-risk-factor")
public class ObstetricRiskFactorController {

    @Autowired
    private ObstetricRiskFactorService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public ObstetricRiskFactor add(@RequestBody ObstetricRiskFactor data) {
        return service.add(data);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ObstetricRiskFactor get(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<ObstetricRiskFactor> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public ObstetricRiskFactor update(@RequestBody ObstetricRiskFactor data) {
        return service.update(data);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/clinical/{clinicalHistoryID}")
    public List<ObstetricRiskFactor> getByClinical(@PathVariable Integer clinicalHistoryID) {
        return service.getByClinicalHistoryID(clinicalHistoryID);
    }
}

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

import com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk;
import com.backend.backend.service.Prenatal.AscertainPregnancyLowRiskService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/lowrisk")
public class AscertainPregnancyLowRiskController {

    @Autowired
    private AscertainPregnancyLowRiskService service;
@CrossOrigin(origins = "*")
    // CREATE
    @PostMapping
    public AscertainPregnancyLowRisk add(@RequestBody AscertainPregnancyLowRisk record) {
        return service.addRecord(record);
    }
@CrossOrigin(origins = "*")
    // GET BY ID
    @GetMapping("/{id}")
    public AscertainPregnancyLowRisk get(@PathVariable Integer id) {
        return service.getRecordById(id);
    }
@CrossOrigin(origins = "*")
    // GET ALL
    @GetMapping
    public List<AscertainPregnancyLowRisk> getAll() {
        return service.getRecords();
    }
@CrossOrigin(origins = "*")
    // UPDATE
    @PutMapping
    public AscertainPregnancyLowRisk update(@RequestBody AscertainPregnancyLowRisk record) {
        return service.updateRecord(record);
    }
@CrossOrigin(origins = "*")
    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "AscertainPregnancyLowRisk deleted successfully.";
    }
@CrossOrigin(origins = "*")
    // BY CLINICAL HISTORY ID
    @GetMapping("/clinical/{clinicalHistoryID}")
    public List<AscertainPregnancyLowRisk> getByClinicalHistory(@PathVariable Integer clinicalHistoryID) {
        return service.getByClinicalHistoryID(clinicalHistoryID);
    }
}

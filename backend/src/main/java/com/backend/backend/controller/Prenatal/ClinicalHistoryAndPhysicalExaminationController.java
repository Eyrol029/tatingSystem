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

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;
import com.backend.backend.service.Prenatal.ClinicalHistoryAndPhysicalExaminationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/clinicalhistory")
public class ClinicalHistoryAndPhysicalExaminationController {

    @Autowired
    private ClinicalHistoryAndPhysicalExaminationService service;

    // CREATE
    @PostMapping
    public ClinicalHistoryAndPhysicalExamination addRecord(
            @RequestBody ClinicalHistoryAndPhysicalExamination record) {
        return service.addRecord(record);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ClinicalHistoryAndPhysicalExamination getRecord(
            @PathVariable Integer id) {
        return service.getRecordById(id);
    }

    // GET ALL
    @GetMapping
    public List<ClinicalHistoryAndPhysicalExamination> getRecords() {
        return service.getRecords();
    }

    // UPDATE
    @PutMapping
    public ClinicalHistoryAndPhysicalExamination updateRecord(
            @RequestBody ClinicalHistoryAndPhysicalExamination record) {
        return service.updateRecord(record);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "ClinicalHistoryAndPhysicalExamination deleted successfully.";
    }

    // GET BY PRENATAL RECORD ID
    @GetMapping("/prenatal/{prenatalrecordID}")
    public List<ClinicalHistoryAndPhysicalExamination> getByPrenatalrecordID(
            @PathVariable Integer prenatalrecordID) {
        return service.getByPrenatalrecordID(prenatalrecordID);
    }
}

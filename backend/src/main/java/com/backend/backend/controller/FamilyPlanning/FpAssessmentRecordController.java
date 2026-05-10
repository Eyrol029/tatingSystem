package com.backend.backend.controller.FamilyPlanning;

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

import com.backend.backend.model.FamilyPlanning.FpAssessmentRecord;
import com.backend.backend.service.FamilyPlanning.FpAssessmentRecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/fpassessment")
public class FpAssessmentRecordController {

    @Autowired
    private FpAssessmentRecordService service;

    // CREATE
    @PostMapping
    public FpAssessmentRecord add(@RequestBody FpAssessmentRecord record) {
        return service.addRecord(record);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public FpAssessmentRecord get(@PathVariable Integer id) {
        return service.getRecordById(id);
    }

    // GET ALL
    @GetMapping
    public List<FpAssessmentRecord> getAll() {
        return service.getRecords();
    }

    // UPDATE
    @PutMapping
    public FpAssessmentRecord update(@RequestBody FpAssessmentRecord record) {
        return service.updateRecord(record);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "FpAssessmentRecord deleted successfully.";
    }

    // GET BY CLIENT ID
    @GetMapping("/client/{clientID}")
    public List<FpAssessmentRecord> getByClient(@PathVariable Integer clientID) {
        return service.getByClientID(clientID);
    }
}

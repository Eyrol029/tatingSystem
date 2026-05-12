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

import com.backend.backend.model.Prenatal.MenstrualHistory;
import com.backend.backend.service.Prenatal.MenstrualHistoryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/menstrual")
public class MenstrualHistoryController {

    @Autowired
    private MenstrualHistoryService service;

    // CREATE
    @PostMapping
    public MenstrualHistory add(@RequestBody MenstrualHistory record) {
        return service.addRecord(record);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public MenstrualHistory get(@PathVariable Integer id) {
        return service.getRecordById(id);
    }

    // GET ALL
    @GetMapping
    public List<MenstrualHistory> getAll() {
        return service.getRecords();
    }

    // UPDATE
    @PutMapping
    public MenstrualHistory update(@RequestBody MenstrualHistory record) {
        return service.updateRecord(record);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "MenstrualHistory deleted successfully.";
    }

    // BY CLINICAL HISTORY ID
    @GetMapping("/clinical/{clinicalHistoryID}")
    public List<MenstrualHistory> getByClinicalHistory(@PathVariable Integer clinicalHistoryID) {
        return service.getByClinicalHistoryID(clinicalHistoryID);
    }
}

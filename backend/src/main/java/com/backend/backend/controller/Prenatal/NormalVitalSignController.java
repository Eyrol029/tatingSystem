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

import com.backend.backend.model.Prenatal.NormalVitalSign;
import com.backend.backend.service.Prenatal.NormalVitalSignService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/normalsign")
public class NormalVitalSignController {

    @Autowired
    private NormalVitalSignService service;

    // CREATE
    @PostMapping
    public NormalVitalSign add(@RequestBody NormalVitalSign record) {
        return service.addRecord(record);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public NormalVitalSign get(@PathVariable Integer id) {
        return service.getRecordById(id);
    }

    // GET ALL
    @GetMapping
    public List<NormalVitalSign> getAll() {
        return service.getRecords();
    }

    // UPDATE
    @PutMapping
    public NormalVitalSign update(@RequestBody NormalVitalSign record) {
        return service.updateRecord(record);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "NormalVitalSign deleted successfully.";
    }

    // GET BY CLINICAL HISTORY ID
    @GetMapping("/clinical/{clinicalHistoryID}")
    public List<NormalVitalSign> getByClinicalHistory(@PathVariable Integer clinicalHistoryID) {
        return service.getByClinicalHistoryID(clinicalHistoryID);
    }
}

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

import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.service.Prenatal.PrenatalRecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/records")
public class PrenatalRecordController {

    @Autowired
    private PrenatalRecordService service;
@CrossOrigin(origins = "*")
    // CREATE
    @PostMapping
    public PrenatalRecord addRecord(@RequestBody PrenatalRecord record) {
        return service.addRecord(record);
    }
@CrossOrigin(origins = "*")
    // GET BY ID
    @GetMapping("/{id}")
    public PrenatalRecord getRecord(@PathVariable Integer id) {
        return service.getRecordById(id);
    }
@CrossOrigin(origins = "*")
    // GET ALL
    @GetMapping
    public List<PrenatalRecord> getRecords() {
        return service.getRecords();
    }
@CrossOrigin(origins = "*")
    // UPDATE
    @PutMapping
    public PrenatalRecord updateRecord(@RequestBody PrenatalRecord record) {
        return service.updateRecord(record);
    }
@CrossOrigin(origins = "*")
    // DELETE
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        service.deleteRecord(id);
        return "PrenatalRecord deleted successfully.";
    }
@CrossOrigin(origins = "*")
    // GET BY SERVICE ID
    @GetMapping("/service/{serviceID}")
    public List<PrenatalRecord> getByServiceID(@PathVariable Integer serviceID) {
        return service.getByServiceID(serviceID);
    }
}

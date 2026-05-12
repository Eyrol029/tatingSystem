package com.backend.backend.controller.Ultrasound;

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

import com.backend.backend.model.Ultrasound.UltrasoundRecord;
import com.backend.backend.service.Ultrasound.UltrasoundRecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ultrasound")
public class UltrasoundRecordController {

    @Autowired
    private UltrasoundRecordService service;

    // CREATE
    @PostMapping
    public UltrasoundRecord add(@RequestBody UltrasoundRecord record) {
        return service.add(record);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public UltrasoundRecord getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<UltrasoundRecord> getAll() {
        return service.getAll();
    }

    // UPDATE
    @PutMapping
    public UltrasoundRecord update(@RequestBody UltrasoundRecord record) {
        return service.update(record);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Ultrasound record deleted successfully.";
    }
}

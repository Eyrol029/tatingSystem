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

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements;
import com.backend.backend.service.Prenatal.MedicalSurgicalFactorMeasurementsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/medsurg-measurements")
public class MedicalSurgicalFactorMeasurementsController {

    @Autowired
    private MedicalSurgicalFactorMeasurementsService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public MedicalSurgicalFactorMeasurements add(@RequestBody MedicalSurgicalFactorMeasurements data) {
        return service.add(data);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public MedicalSurgicalFactorMeasurements get(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<MedicalSurgicalFactorMeasurements> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public MedicalSurgicalFactorMeasurements update(@RequestBody MedicalSurgicalFactorMeasurements data) {
        return service.update(data);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/medsurg/{medsurgID}")
    public List<MedicalSurgicalFactorMeasurements> getByMedsurg(@PathVariable Integer medsurgID) {
        return service.getByMedsurgID(medsurgID);
    }
}

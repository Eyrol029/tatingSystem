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

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;
import com.backend.backend.service.Prenatal.MedicalSurgicalFactorMedConditionsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/medsurg-conditions")
public class MedicalSurgicalFactorMedConditionsController {

    @Autowired
    private MedicalSurgicalFactorMedConditionsService service;

    @PostMapping
    public MedicalSurgicalFactorMedConditions add(@RequestBody MedicalSurgicalFactorMedConditions data) {
        return service.add(data);
    }

    @GetMapping("/{id}")
    public MedicalSurgicalFactorMedConditions get(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<MedicalSurgicalFactorMedConditions> getAll() {
        return service.getAll();
    }

    @PutMapping
    public MedicalSurgicalFactorMedConditions update(@RequestBody MedicalSurgicalFactorMedConditions data) {
        return service.update(data);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Deleted successfully.";
    }

    @GetMapping("/medsurg/{medsurgID}")
    public List<MedicalSurgicalFactorMedConditions> getByMedsurg(@PathVariable Integer medsurgID) {
        return service.getByMedsurgID(medsurgID);
    }
}

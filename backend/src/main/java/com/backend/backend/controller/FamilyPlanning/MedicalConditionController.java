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

import com.backend.backend.model.FamilyPlanning.MedicalCondition;
import com.backend.backend.service.FamilyPlanning.MedicalConditionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/medicalconditions")
public class MedicalConditionController {

    @Autowired
    private MedicalConditionService medicalConditionService;

    @PostMapping
    public MedicalCondition addMedicalCondition(@RequestBody MedicalCondition medicalCondition) {
        return medicalConditionService.addMedicalCondition(medicalCondition);
    }

    @GetMapping("/{id}")
    public MedicalCondition getMedicalCondition(@PathVariable Integer id) {
        return medicalConditionService.getMedicalConditionById(id);
    }

    @GetMapping
    public List<MedicalCondition> getMedicalConditions() {
        return medicalConditionService.getMedicalConditions();
    }

    @PutMapping
    public MedicalCondition updateMedicalCondition(@RequestBody MedicalCondition medicalCondition) {
        return medicalConditionService.updateMedicalCondition(medicalCondition);
    }

    @DeleteMapping("/{id}")
    public String deleteMedicalCondition(@PathVariable Integer id) {
        medicalConditionService.deleteMedicalCondition(id);
        return "MedicalCondition deleted.";
    }
}

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

import com.backend.backend.model.FamilyPlanning.MedicalHistory;
import com.backend.backend.service.FamilyPlanning.MedicalHistoryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/medicalhistory")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public MedicalHistory addMedicalHistory(@RequestBody MedicalHistory medicalHistory) {
        return medicalHistoryService.addMedicalHistory(medicalHistory);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public MedicalHistory getMedicalHistory(@PathVariable Integer id) {
        return medicalHistoryService.getMedicalHistoryById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<MedicalHistory> getMedicalHistories() {
        return medicalHistoryService.getMedicalHistories();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public MedicalHistory updateMedicalHistory(@RequestBody MedicalHistory medicalHistory) {
        return medicalHistoryService.updateMedicalHistory(medicalHistory);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteMedicalHistory(@PathVariable Integer id) {
        medicalHistoryService.deleteMedicalHistory(id);
        return "MedicalHistory deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<MedicalHistory> getByClientID(@PathVariable Integer clientID) {
        return medicalHistoryService.getMedicalHistoryByClientID(clientID);
    }
}

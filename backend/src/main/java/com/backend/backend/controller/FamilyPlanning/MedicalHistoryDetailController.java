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

import com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail;
import com.backend.backend.service.FamilyPlanning.MedicalHistoryDetailService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/medicalhistory/detail")
public class MedicalHistoryDetailController {

    @Autowired
    private MedicalHistoryDetailService medicalHistoryDetailService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public MedicalHistoryDetail addMedicalHistoryDetail(@RequestBody MedicalHistoryDetail medicalHistoryDetail) {
        return medicalHistoryDetailService.addMedicalHistoryDetail(medicalHistoryDetail);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public MedicalHistoryDetail getMedicalHistoryDetail(@PathVariable Integer id) {
        return medicalHistoryDetailService.getMedicalHistoryDetailById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public List<MedicalHistoryDetail> getMedicalHistoryDetails() {
        return medicalHistoryDetailService.getMedicalHistoryDetails();
    }

    @CrossOrigin(origins = "*")
    @PutMapping
    public MedicalHistoryDetail updateMedicalHistoryDetail(@RequestBody MedicalHistoryDetail medicalHistoryDetail) {
        return medicalHistoryDetailService.updateMedicalHistoryDetail(medicalHistoryDetail);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteMedicalHistoryDetail(@PathVariable Integer id) {
        medicalHistoryDetailService.deleteMedicalHistoryDetail(id);
        return "MedicalHistoryDetail deleted.";
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/medicalHistory/{medicalHistoryid}")
    public List<MedicalHistoryDetail> getByMedicalHistoryid(@PathVariable Integer medicalHistoryid) {
        return medicalHistoryDetailService.getMedicalHistoryDetailsByMedicalHistoryid(medicalHistoryid);
    }
}

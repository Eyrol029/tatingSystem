package com.backend.backend.controller.familyplanning;

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

import com.backend.backend.model.FamilyPlanning.Breast;
import com.backend.backend.service.FamilyPlanning.BreastService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/breast")
public class BreastController {

    @Autowired
    private BreastService breastService;

    @PostMapping
    public Breast addBreast(@RequestBody Breast breast) {
        return breastService.addBreast(breast);
    }

    @GetMapping("/{id}")
    public Breast getBreast(@PathVariable Integer id) {
        return breastService.getBreastById(id);
    }

    @GetMapping
    public List<Breast> getBreasts() {
        return breastService.getBreasts();
    }

    @PutMapping
    public Breast updateBreast(@RequestBody Breast breast) {
        return breastService.updateBreast(breast);
    }

    @DeleteMapping("/{id}")
    public String deleteBreast(@PathVariable Integer id) {
        breastService.deleteBreast(id);
        return "Breast deleted.";
    }

    @GetMapping("/pExam/{pExamID}")
    public List<Breast> getByPExam(@PathVariable Integer pExamID) {
        return breastService.getByPExamID(pExamID);
    }
}

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

import com.backend.backend.model.FamilyPlanning.Extremities;
import com.backend.backend.service.FamilyPlanning.ExtremitiesService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/extremities")
public class ExtremitiesController {

    @Autowired
    private ExtremitiesService extremitiesService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public Extremities addExtremities(@RequestBody Extremities extremities) {
        return extremitiesService.addExtremities(extremities);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Extremities getExtremities(@PathVariable Integer id) {
        return extremitiesService.getExtremitiesById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Extremities> getExtremities() {
        return extremitiesService.getExtremities();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public Extremities updateExtremities(@RequestBody Extremities extremities) {
        return extremitiesService.updateExtremities(extremities);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteExtremities(@PathVariable Integer id) {
        extremitiesService.deleteExtremities(id);
        return "Extremities deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/pExam/{pExamID}")
    public List<Extremities> getByPExam(@PathVariable Integer pExamID) {
        return extremitiesService.getByPExamID(pExamID);
    }
}

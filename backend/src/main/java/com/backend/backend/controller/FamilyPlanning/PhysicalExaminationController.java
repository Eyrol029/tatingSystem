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

import com.backend.backend.model.FamilyPlanning.PhysicalExamination;
import com.backend.backend.service.FamilyPlanning.PhysicalExaminationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/physical-exam")
public class PhysicalExaminationController {

    @Autowired
    private PhysicalExaminationService physicalExaminationService;

    @PostMapping
    public PhysicalExamination addExam(@RequestBody PhysicalExamination exam) {
        return physicalExaminationService.addExam(exam);
    }

    @GetMapping("/{id}")
    public PhysicalExamination getExam(@PathVariable Integer id) {
        return physicalExaminationService.getExamById(id);
    }

    @GetMapping
    public List<PhysicalExamination> getAll() {
        return physicalExaminationService.getAllExams();
    }

    @PutMapping
    public PhysicalExamination update(@RequestBody PhysicalExamination exam) {
        return physicalExaminationService.updateExam(exam);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        physicalExaminationService.deleteExam(id);
        return "PhysicalExamination deleted.";
    }

    @GetMapping("/client/{clientID}")
    public List<PhysicalExamination> getByClient(@PathVariable Integer clientID) {
        return physicalExaminationService.getByClientID(clientID);
    }
}

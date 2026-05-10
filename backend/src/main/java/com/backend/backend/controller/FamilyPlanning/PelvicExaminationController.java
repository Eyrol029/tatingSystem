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

import com.backend.backend.model.FamilyPlanning.PelvicExamination;
import com.backend.backend.service.FamilyPlanning.PelvicExaminationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/pelvic-examination")
public class PelvicExaminationController {

    @Autowired
    private PelvicExaminationService pelvicExaminationService;

    @PostMapping
    public PelvicExamination addPelvic(@RequestBody PelvicExamination pelvic) {
        return pelvicExaminationService.addPelvicExamination(pelvic);
    }

    @GetMapping("/{id}")
    public PelvicExamination getPelvic(@PathVariable Integer id) {
        return pelvicExaminationService.getPelvicExaminationById(id);
    }

    @GetMapping
    public List<PelvicExamination> getAll() {
        return pelvicExaminationService.getPelvicExaminations();
    }

    @PutMapping
    public PelvicExamination update(@RequestBody PelvicExamination pelvic) {
        return pelvicExaminationService.updatePelvicExamination(pelvic);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        pelvicExaminationService.deletePelvicExamination(id);
        return "PelvicExamination deleted.";
    }

    @GetMapping("/pExam/{pExamID}")
    public List<PelvicExamination> getByPExam(@PathVariable Integer pExamID) {
        return pelvicExaminationService.getByPExamID(pExamID);
    }
}

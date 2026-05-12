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

import com.backend.backend.model.Prenatal.CompleteDiagnosis;
import com.backend.backend.service.Prenatal.CompleteDiagnosisService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/complete-diagnosis")
public class CompleteDiagnosisController {

    @Autowired
    private CompleteDiagnosisService service;

    @PostMapping
    public CompleteDiagnosis add(@RequestBody CompleteDiagnosis data) {
        return service.add(data);
    }

    @GetMapping("/{id}")
    public CompleteDiagnosis get(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CompleteDiagnosis> getAll() {
        return service.getAll();
    }

    @PutMapping
    public CompleteDiagnosis update(@RequestBody CompleteDiagnosis data) {
        return service.update(data);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Deleted successfully.";
    }

    @GetMapping("/clinical/{clinicalHistoryID}")
    public List<CompleteDiagnosis> getByClinical(@PathVariable Integer clinicalHistoryID) {
        return service.getByClinicalHistoryID(clinicalHistoryID);
    }
}

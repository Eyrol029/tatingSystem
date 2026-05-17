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

import com.backend.backend.model.FamilyPlanning.Conjunctiva;
import com.backend.backend.service.FamilyPlanning.ConjunctivaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/conjunctiva")
public class ConjunctivaController {

    @Autowired
    private ConjunctivaService conjunctivaService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public Conjunctiva add(@RequestBody Conjunctiva conjunctiva) {
        return conjunctivaService.addConjunctiva(conjunctiva);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Conjunctiva get(@PathVariable Integer id) {
        return conjunctivaService.getConjunctivaById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Conjunctiva> getAll() {
        return conjunctivaService.getAllConjunctiva();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public Conjunctiva update(@RequestBody Conjunctiva conjunctiva) {
        return conjunctivaService.updateConjunctiva(conjunctiva);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        conjunctivaService.deleteConjunctiva(id);
        return "Conjunctiva record deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/exam/{pExamID}")
    public List<Conjunctiva> getByExam(@PathVariable Integer pExamID) {
        return conjunctivaService.getByPExamID(pExamID);
    }
}

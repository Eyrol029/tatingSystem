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

import com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist;
import com.backend.backend.service.FamilyPlanning.PregnancyExclusionChecklistService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/pregnancychecklist")
public class PregnancyExclusionChecklistController {

    @Autowired
    private PregnancyExclusionChecklistService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public PregnancyExclusionChecklist add(@RequestBody PregnancyExclusionChecklist checklist) {
        return service.add(checklist);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public PregnancyExclusionChecklist get(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<PregnancyExclusionChecklist> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public PregnancyExclusionChecklist update(@RequestBody PregnancyExclusionChecklist checklist) {
        return service.update(checklist);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Checklist deleted successfully.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<PregnancyExclusionChecklist> getByClient(@PathVariable Integer clientID) {
        return service.getByClientID(clientID);
    }
}

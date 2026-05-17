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

import com.backend.backend.model.FamilyPlanning.ObstetricalCondition;
import com.backend.backend.service.FamilyPlanning.ObstetricalConditionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/obstetrical-condition")
public class ObstetricalConditionController {

    @Autowired
    private ObstetricalConditionService service;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ObstetricalCondition add(@RequestBody ObstetricalCondition condition) {
        return service.addCondition(condition);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ObstetricalCondition getById(@PathVariable Integer id) {
        return service.getById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<ObstetricalCondition> getAll() {
        return service.getAll();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public ObstetricalCondition update(@RequestBody ObstetricalCondition condition) {
        return service.update(condition);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ObstetricalCondition deleted.";
    }
}

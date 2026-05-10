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

import com.backend.backend.model.familyplanning.ObstetricalCondition;
import com.backend.backend.service.familyplanning.ObstetricalConditionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/obstetrical-condition")
public class ObstetricalConditionController {

    @Autowired
    private ObstetricalConditionService service;

    @PostMapping
    public ObstetricalCondition add(@RequestBody ObstetricalCondition condition) {
        return service.addCondition(condition);
    }

    @GetMapping("/{id}")
    public ObstetricalCondition getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ObstetricalCondition> getAll() {
        return service.getAll();
    }

    @PutMapping
    public ObstetricalCondition update(@RequestBody ObstetricalCondition condition) {
        return service.update(condition);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ObstetricalCondition deleted.";
    }
}

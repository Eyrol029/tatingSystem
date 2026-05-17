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

import com.backend.backend.model.FamilyPlanning.STI_Condition;
import com.backend.backend.service.FamilyPlanning.STI_ConditionService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/sti-condition")
public class STI_ConditionController {

    @Autowired
    private STI_ConditionService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public STI_Condition add(@RequestBody STI_Condition condition) {
        return service.addCondition(condition);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public STI_Condition getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<STI_Condition> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public STI_Condition update(@RequestBody STI_Condition condition) {
        return service.update(condition);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "STI_Condition deleted.";
    }
}

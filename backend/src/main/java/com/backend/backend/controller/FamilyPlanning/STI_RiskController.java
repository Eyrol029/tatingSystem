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

import com.backend.backend.model.FamilyPlanning.STI_Risk;
import com.backend.backend.service.FamilyPlanning.STI_RiskService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/sti-risk")
public class STI_RiskController {

    @Autowired
    private STI_RiskService service;
@CrossOrigin(origins = "*")
    @PostMapping
    public STI_Risk add(@RequestBody STI_Risk risk) {
        return service.addRisk(risk);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public STI_Risk getById(@PathVariable Integer id) {
        return service.getById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<STI_Risk> getAll() {
        return service.getAll();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public STI_Risk update(@RequestBody STI_Risk risk) {
        return service.update(risk);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "STI_Risk deleted.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<STI_Risk> getByClient(@PathVariable Integer clientID) {
        return service.getByClientID(clientID);
    }
}

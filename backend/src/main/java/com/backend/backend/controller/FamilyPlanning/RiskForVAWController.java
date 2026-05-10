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

import com.backend.backend.model.FamilyPlanning.RiskForVAW;
import com.backend.backend.service.FamilyPlanning.RiskForVAWService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/risk-vaw")
public class RiskForVAWController {

    @Autowired
    private RiskForVAWService riskForVAWService;

    @PostMapping
    public RiskForVAW addRisk(@RequestBody RiskForVAW risk) {
        return riskForVAWService.addRisk(risk);
    }

    @GetMapping("/{id}")
    public RiskForVAW getRisk(@PathVariable Integer id) {
        return riskForVAWService.getRiskById(id);
    }

    @GetMapping
    public List<RiskForVAW> getAll() {
        return riskForVAWService.getAllRisks();
    }

    @PutMapping
    public RiskForVAW update(@RequestBody RiskForVAW risk) {
        return riskForVAWService.updateRisk(risk);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        riskForVAWService.deleteRisk(id);
        return "RiskForVAW deleted.";
    }

    @GetMapping("/client/{clientID}")
    public List<RiskForVAW> getByClient(@PathVariable Integer clientID) {
        return riskForVAWService.getByClientID(clientID);
    }
}

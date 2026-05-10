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

import com.backend.backend.model.familyplanning.STI_RiskDetail;
import com.backend.backend.service.familyplanning.STI_RiskDetailService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/sti-risk-detail")
public class STI_RiskDetailController {

    @Autowired
    private STI_RiskDetailService service;

    @PostMapping
    public STI_RiskDetail add(@RequestBody STI_RiskDetail detail) {
        return service.addDetail(detail);
    }

    @GetMapping("/{id}")
    public STI_RiskDetail getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<STI_RiskDetail> getAll() {
        return service.getAll();
    }

    @PutMapping
    public STI_RiskDetail update(@RequestBody STI_RiskDetail detail) {
        return service.update(detail);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "STI_RiskDetail deleted.";
    }

    @GetMapping("/risk/{stiRiskID}")
    public List<STI_RiskDetail> getByRisk(@PathVariable Integer stiRiskID) {
        return service.getByRiskID(stiRiskID);
    }

    @GetMapping("/condition/{stiConditionID}")
    public List<STI_RiskDetail> getByCondition(@PathVariable Integer stiConditionID) {
        return service.getByConditionID(stiConditionID);
    }
}

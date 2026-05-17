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

import com.backend.backend.model.FamilyPlanning.ObstetricalConditionDetail;
import com.backend.backend.service.FamilyPlanning.ObstetricalConditionDetailService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/obstetrical-condition-detail")
public class ObstetricalConditionDetailController {

    @Autowired
    private ObstetricalConditionDetailService service;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ObstetricalConditionDetail add(@RequestBody ObstetricalConditionDetail detail) {
        return service.addDetail(detail);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ObstetricalConditionDetail getById(@PathVariable Integer id) {
        return service.getById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<ObstetricalConditionDetail> getAll() {
        return service.getAll();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public ObstetricalConditionDetail update(@RequestBody ObstetricalConditionDetail detail) {
        return service.update(detail);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ObstetricalConditionDetail deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/history/{obstetricalHistoryID}")
    public List<ObstetricalConditionDetail> getByHistory(@PathVariable Integer obstetricalHistoryID) {
        return service.getByHistoryID(obstetricalHistoryID);
    }
}

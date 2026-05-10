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

import com.backend.backend.model.familyplanning.ObstetricalConditionDetail;
import com.backend.backend.service.familyplanning.ObstetricalConditionDetailService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/obstetrical-condition-detail")
public class ObstetricalConditionDetailController {

    @Autowired
    private ObstetricalConditionDetailService service;

    @PostMapping
    public ObstetricalConditionDetail add(@RequestBody ObstetricalConditionDetail detail) {
        return service.addDetail(detail);
    }

    @GetMapping("/{id}")
    public ObstetricalConditionDetail getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ObstetricalConditionDetail> getAll() {
        return service.getAll();
    }

    @PutMapping
    public ObstetricalConditionDetail update(@RequestBody ObstetricalConditionDetail detail) {
        return service.update(detail);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ObstetricalConditionDetail deleted.";
    }

    @GetMapping("/history/{obstetricalHistoryID}")
    public List<ObstetricalConditionDetail> getByHistory(@PathVariable Integer obstetricalHistoryID) {
        return service.getByHistoryID(obstetricalHistoryID);
    }
}

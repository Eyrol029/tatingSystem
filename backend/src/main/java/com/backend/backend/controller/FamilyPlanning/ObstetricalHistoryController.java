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

import com.backend.backend.model.FamilyPlanning.ObstetricalHistory;
import com.backend.backend.service.FamilyPlanning.ObstetricalHistoryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/obstetrical-history")
public class ObstetricalHistoryController {

    @Autowired
    private ObstetricalHistoryService service;
    @CrossOrigin(origins = "*")
    @PostMapping
    public ObstetricalHistory add(@RequestBody ObstetricalHistory history) {
        return service.addObstetricalHistory(history);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ObstetricalHistory getById(@PathVariable Integer id) {
        return service.getById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<ObstetricalHistory> getAll() {
        return service.getAll();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public ObstetricalHistory update(@RequestBody ObstetricalHistory history) {
        return service.update(history);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "ObstetricalHistory deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<ObstetricalHistory> getByClient(@PathVariable Integer clientID) {
        return service.getByClientID(clientID);
    }
}

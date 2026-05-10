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

import com.backend.backend.model.FamilyPlanning.Spouse;
import com.backend.backend.service.FamilyPlanning.SpouseService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/spouses")
public class SpouseController {

    @Autowired
    private SpouseService spouseService;

    @PostMapping
    public Spouse addSpouse(@RequestBody Spouse spouse) {
        return spouseService.addSpouse(spouse);
    }

    @GetMapping("/{id}")
    public Spouse getSpouse(@PathVariable Integer id) {
        return spouseService.getSpouseById(id);
    }

    @GetMapping
    public List<Spouse> getSpouses() {
        return spouseService.getSpouses();
    }

    @PutMapping
    public Spouse updateSpouse(@RequestBody Spouse spouse) {
        return spouseService.updateSpouse(spouse);
    }

    @DeleteMapping("/{id}")
    public String deleteSpouse(@PathVariable Integer id) {
        spouseService.deleteSpouse(id);
        return "Spouse deleted.";
    }

    @GetMapping("/client/{clientID}")
    public List<Spouse> getByClientID(@PathVariable Integer clientID) {
        return spouseService.getSpousesByClientID(clientID);
    }

    @GetMapping("/search/lastname/{lName}")
    public List<Spouse> getByLName(@PathVariable String lName) {
        return spouseService.getSpousesByLName(lName);
    }
}

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
@CrossOrigin(origins = "*")
    @PostMapping
    public Spouse addSpouse(@RequestBody Spouse spouse) {
        return spouseService.addSpouse(spouse);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Spouse getSpouse(@PathVariable Integer id) {
        return spouseService.getSpouseById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<Spouse> getSpouses() {
        return spouseService.getSpouses();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public Spouse updateSpouse(@RequestBody Spouse spouse) {
        return spouseService.updateSpouse(spouse);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteSpouse(@PathVariable Integer id) {
        spouseService.deleteSpouse(id);
        return "Spouse deleted.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<Spouse> getByClientID(@PathVariable Integer clientID) {
        return spouseService.getSpousesByClientID(clientID);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/search/lastname/{lName}")
    public List<Spouse> getByLName(@PathVariable String lName) {
        return spouseService.getSpousesByLName(lName);
    }
}

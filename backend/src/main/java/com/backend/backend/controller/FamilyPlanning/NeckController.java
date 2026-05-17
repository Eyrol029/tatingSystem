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

import com.backend.backend.model.FamilyPlanning.Neck;
import com.backend.backend.service.FamilyPlanning.NeckService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/neck")
public class NeckController {

    @Autowired
    private NeckService neckService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public Neck addNeck(@RequestBody Neck neck) {
        return neckService.addNeck(neck);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Neck getNeck(@PathVariable Integer id) {
        return neckService.getNeckById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Neck> getNecks() {
        return neckService.getNecks();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public Neck updateNeck(@RequestBody Neck neck) {
        return neckService.updateNeck(neck);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteNeck(@PathVariable Integer id) {
        neckService.deleteNeck(id);
        return "Neck deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/pExam/{pExamID}")
    public List<Neck> getByPExam(@PathVariable Integer pExamID) {
        return neckService.getByPExamID(pExamID);
    }
}

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

import com.backend.backend.model.FamilyPlanning.Abdomen;
import com.backend.backend.service.FamilyPlanning.AbdomenService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/abdomen")
public class AbdomenController {

    @Autowired
    private AbdomenService abdomenService;

    @PostMapping
    public Abdomen addAbdomen(@RequestBody Abdomen abdomen) {
        return abdomenService.addAbdomen(abdomen);
    }

    @GetMapping("/{id}")
    public Abdomen getAbdomen(@PathVariable Integer id) {
        return abdomenService.getAbdomenById(id);
    }

    @GetMapping
    public List<Abdomen> getAbdomens() {
        return abdomenService.getAbdomens();
    }

    @PutMapping
    public Abdomen updateAbdomen(@RequestBody Abdomen abdomen) {
        return abdomenService.updateAbdomen(abdomen);
    }

    @DeleteMapping("/{id}")
    public String deleteAbdomen(@PathVariable Integer id) {
        abdomenService.deleteAbdomen(id);
        return "Abdomen deleted.";
    }

    @GetMapping("/pExam/{pExamID}")
    public List<Abdomen> getByPExam(@PathVariable Integer pExamID) {
        return abdomenService.getByPExamID(pExamID);
    }
}

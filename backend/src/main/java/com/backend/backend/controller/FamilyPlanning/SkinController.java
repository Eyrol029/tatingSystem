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

import com.backend.backend.model.FamilyPlanning.Skin;
import com.backend.backend.service.FamilyPlanning.SkinService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/skin")
public class SkinController {

    @Autowired
    private SkinService skinService;

    @PostMapping
    public Skin addSkin(@RequestBody Skin skin) {
        return skinService.addSkin(skin);
    }

    @GetMapping("/{id}")
    public Skin getSkin(@PathVariable Integer id) {
        return skinService.getSkinById(id);
    }

    @GetMapping
    public List<Skin> getAll() {
        return skinService.getAllSkin();
    }

    @PutMapping
    public Skin update(@RequestBody Skin skin) {
        return skinService.updateSkin(skin);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        skinService.deleteSkin(id);
        return "Skin record deleted.";
    }

    @GetMapping("/exam/{pExamID}")
    public List<Skin> getByExam(@PathVariable Integer pExamID) {
        return skinService.getByPExamID(pExamID);
    }
}

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

import com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed;
import com.backend.backend.service.FamilyPlanning.MethodCurrentlyUsedService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/methods")
public class MethodCurrentlyUsedController {

    @Autowired
    private MethodCurrentlyUsedService methodCurrentlyUsedService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public MethodCurrentlyUsed addMethod(@RequestBody MethodCurrentlyUsed method) {
        return methodCurrentlyUsedService.addMethod(method);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public MethodCurrentlyUsed getMethod(@PathVariable Integer id) {
        return methodCurrentlyUsedService.getMethodById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<MethodCurrentlyUsed> getMethods() {
        return methodCurrentlyUsedService.getMethods();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public MethodCurrentlyUsed updateMethod(@RequestBody MethodCurrentlyUsed method) {
        return methodCurrentlyUsedService.updateMethod(method);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteMethod(@PathVariable Integer id) {
        methodCurrentlyUsedService.deleteMethod(id);
        return "MethodCurrentlyUsed deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/type/{typeID}")
    public List<MethodCurrentlyUsed> getByTypeID(@PathVariable Integer typeID) {
        return methodCurrentlyUsedService.getMethodsByTypeID(typeID);
    }
}

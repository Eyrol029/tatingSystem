package com.backend.backend.controller.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backend.backend.model.Inventory.ClinicalToolInventory;
import com.backend.backend.service.Inventory.ClinicalToolInventoryService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/inventory")
public class ClinicalToolInventoryController {

    @Autowired
    private ClinicalToolInventoryService clinicalToolInventoryService;

    @CrossOrigin(origins = "*")
    @PostMapping
    public ClinicalToolInventory addTool(@RequestBody ClinicalToolInventory tool) {
        return clinicalToolInventoryService.addTool(tool);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ClinicalToolInventory getTool(@PathVariable Integer id) {
        return clinicalToolInventoryService.getToolById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    public List<ClinicalToolInventory> getAllTools() {
        return clinicalToolInventoryService.getAllTools();
    }

    @CrossOrigin(origins = "*")
    @PutMapping
    public ClinicalToolInventory updateTool(@RequestBody ClinicalToolInventory tool) {
        return clinicalToolInventoryService.updateTool(tool);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteTool(@PathVariable Integer id) {
        clinicalToolInventoryService.deleteTool(id);
        return "Tool deleted.";
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/search/{toolName}")
    public List<ClinicalToolInventory> getToolByName(@PathVariable String toolName) {
        return clinicalToolInventoryService.getToolsByName(toolName);
    }
}
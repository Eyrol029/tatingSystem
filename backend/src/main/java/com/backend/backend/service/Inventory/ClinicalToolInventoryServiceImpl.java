package com.backend.backend.service.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Inventory.ClinicalToolInventory;
import com.backend.backend.repository.Inventory.ClinicalToolInventoryRepository;

@Service
public class ClinicalToolInventoryServiceImpl implements ClinicalToolInventoryService {

    @Autowired
    private ClinicalToolInventoryRepository clinicalToolInventoryRepository;

    @Override
    public ClinicalToolInventory addTool(ClinicalToolInventory tool) {
        return clinicalToolInventoryRepository.save(tool);
    }

    @Override
    public ClinicalToolInventory getToolById(Integer id) {
        return clinicalToolInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found: " + id));
    }

    @Override
    public List<ClinicalToolInventory> getAllTools() {
        return clinicalToolInventoryRepository.findAll();
    }

    @Override
    public ClinicalToolInventory updateTool(ClinicalToolInventory tool) {
        return clinicalToolInventoryRepository.save(tool);
    }

    @Override
    public void deleteTool(Integer id) {
        clinicalToolInventoryRepository.deleteById(id);
    }

    @Override
    public List<ClinicalToolInventory> getToolsByName(String toolName) {
        return clinicalToolInventoryRepository.findByToolName(toolName);
    }
}
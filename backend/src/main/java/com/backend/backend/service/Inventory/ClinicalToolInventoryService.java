package com.backend.backend.service.Inventory;

import java.util.List;

import com.backend.backend.model.Inventory.ClinicalToolInventory;

public interface ClinicalToolInventoryService {

    ClinicalToolInventory addTool(ClinicalToolInventory tool);

    ClinicalToolInventory getToolById(Integer id);

    List<ClinicalToolInventory> getAllTools();

    ClinicalToolInventory updateTool(ClinicalToolInventory tool);

    void deleteTool(Integer id);

    List<ClinicalToolInventory> getToolsByName(String toolName);
}
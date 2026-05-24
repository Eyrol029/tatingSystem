package com.backend.backend.repository.Inventory;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Inventory.ClinicalToolInventory;

public interface ClinicalToolInventoryRepository extends CrudRepository<ClinicalToolInventory, Integer> {

    List<ClinicalToolInventory> findAll();

    List<ClinicalToolInventory> findByToolName(String toolName);
}
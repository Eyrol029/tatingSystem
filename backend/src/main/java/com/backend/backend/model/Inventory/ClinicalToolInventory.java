package com.backend.backend.model.Inventory;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinical_tools_inventory")
public class ClinicalToolInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tool_id")
    private Integer toolId;

    @Column(name = "tool_name", nullable = false)
    private String toolName;

    @Column(name = "stock_units", nullable = false)
    private Integer stockUnits;

    @Column(name = "min_stock_unit")
    private Integer minStockUnit;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;

    // Getters and Setters

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Integer getStockUnits() {
        return stockUnits;
    }

    public void setStockUnits(Integer stockUnits) {
        this.stockUnits = stockUnits;
    }

    public Integer getMinStockUnit() {
        return minStockUnit;
    }

    public void setMinStockUnit(Integer minStockUnit) {
        this.minStockUnit = minStockUnit;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }
}
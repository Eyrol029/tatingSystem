package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STI_Condition")
public class STI_Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stiConditionID")
    private Integer stiConditionID;

    @Column(name = "ConditionName", nullable = false, length = 150)
    private String conditionName;

    // Getters and Setters
    public Integer getStiConditionID() {
        return stiConditionID;
    }

    public void setStiConditionID(Integer stiConditionID) {
        this.stiConditionID = stiConditionID;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }
}

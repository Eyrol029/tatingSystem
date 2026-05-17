package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ObstetricalCondition")
public class ObstetricalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obstetricConditionID")
    private Integer obstetricConditionID;

    @Column(name = "conditionName", nullable = false, length = 100)
    private String conditionName;

    // Getters and Setters
    public Integer getObstetricConditionID() {
        return obstetricConditionID;
    }

    public void setObstetricConditionID(Integer obstetricConditionID) {
        this.obstetricConditionID = obstetricConditionID;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }
}

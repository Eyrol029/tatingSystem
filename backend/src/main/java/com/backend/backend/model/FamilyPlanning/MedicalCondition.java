package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalCondition")
public class MedicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicalConditionID")
    private Integer medicalConditionID;

    @Column(name = "ConditionName", nullable = false, length = 100)
    private String conditionName;

    public Integer getMedicalConditionID() {
        return medicalConditionID;
    }

    public void setMedicalConditionID(Integer medicalConditionID) {
        this.medicalConditionID = medicalConditionID;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }
}

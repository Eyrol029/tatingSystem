package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Breast")
public class Breast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breastID")
    private Integer breastID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "Condition")
    private String condition;

    public Integer getBreastID() {
        return breastID;
    }

    public void setBreastID(Integer breastID) {
        this.breastID = breastID;
    }

    public Integer getPExamID() {
        return pExamID;
    }

    public void setPExamID(Integer pExamID) {
        this.pExamID = pExamID;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

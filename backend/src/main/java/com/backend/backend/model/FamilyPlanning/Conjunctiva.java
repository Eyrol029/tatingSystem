package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Conjunctiva")
public class Conjunctiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conjunctivaID")
    private Integer conjunctivaID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "Condition")
    private String condition;

    // Getters and Setters
    public Integer getConjunctivaID() {
        return conjunctivaID;
    }

    public void setConjunctivaID(Integer conjunctivaID) {
        this.conjunctivaID = conjunctivaID;
    }

    // Conjunctiva.java
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

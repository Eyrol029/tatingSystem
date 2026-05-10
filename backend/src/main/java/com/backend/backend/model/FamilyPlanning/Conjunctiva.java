package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "Condition")
    private ConjunctivaCondition condition;

    // ENUM values based on SQL
    public enum ConjunctivaCondition {
        normal,
        pale,
        yellowish
    }

    // Getters and Setters
    public Integer getConjunctivaID() {
        return conjunctivaID;
    }

    public void setConjunctivaID(Integer conjunctivaID) {
        this.conjunctivaID = conjunctivaID;
    }

    public Integer getpExamID() {
        return pExamID;
    }

    public void setpExamID(Integer pExamID) {
        this.pExamID = pExamID;
    }

    public ConjunctivaCondition getCondition() {
        return condition;
    }

    public void setCondition(ConjunctivaCondition condition) {
        this.condition = condition;
    }
}

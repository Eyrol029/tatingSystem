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
@Table(name = "Skin")
public class Skin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skinID")
    private Integer skinID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Condition")
    private SkinCondition condition;

    // ENUM mapping
    public enum SkinCondition {
        normal,
        pale,
        yellowish,
        hematoma
    }

    // Getters and Setters
    public Integer getSkinID() {
        return skinID;
    }

    public void setSkinID(Integer skinID) {
        this.skinID = skinID;
    }

    public Integer getpExamID() {
        return pExamID;
    }

    public void setpExamID(Integer pExamID) {
        this.pExamID = pExamID;
    }

    public SkinCondition getCondition() {
        return condition;
    }

    public void setCondition(SkinCondition condition) {
        this.condition = condition;
    }
}

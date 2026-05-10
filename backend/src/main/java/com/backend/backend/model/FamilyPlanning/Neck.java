package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Neck")
public class Neck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neckID")
    private Integer neckID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "Condition")
    private String condition;

    public Integer getNeckID() {
        return neckID;
    }

    public void setNeckID(Integer neckID) {
        this.neckID = neckID;
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

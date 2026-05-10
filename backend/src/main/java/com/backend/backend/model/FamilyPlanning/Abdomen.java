package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Abdomen")
public class Abdomen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abdomenID")
    private Integer abdomenID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "Condition")
    private String condition;

    public Integer getAbdomenID() {
        return abdomenID;
    }

    public void setAbdomenID(Integer abdomenID) {
        this.abdomenID = abdomenID;
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

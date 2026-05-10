package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalHistoryDetail")
public class MedicalHistoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyDetailID")
    private Integer historyDetailID;

    @Column(name = "medicalHistoryid")
    private Integer medicalHistoryid;

    @Column(name = "medicalConditionID")
    private Integer medicalConditionID;

    public Integer getHistoryDetailID() {
        return historyDetailID;
    }

    public void setHistoryDetailID(Integer historyDetailID) {
        this.historyDetailID = historyDetailID;
    }

    public Integer getMedicalHistoryid() {
        return medicalHistoryid;
    }

    public void setMedicalHistoryid(Integer medicalHistoryid) {
        this.medicalHistoryid = medicalHistoryid;
    }

    public Integer getMedicalConditionID() {
        return medicalConditionID;
    }

    public void setMedicalConditionID(Integer medicalConditionID) {
        this.medicalConditionID = medicalConditionID;
    }
}

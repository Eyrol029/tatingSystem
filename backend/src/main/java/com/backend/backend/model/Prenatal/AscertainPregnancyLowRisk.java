package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AscertainPregnancyLowRisk")
public class AscertainPregnancyLowRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firstPregnancyRiskID")
    private Integer firstPregnancyRiskID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "isLowRisk")
    private Boolean isLowRisk;

    // Getters and Setters
    public Integer getFirstPregnancyRiskID() {
        return firstPregnancyRiskID;
    }

    public void setFirstPregnancyRiskID(Integer firstPregnancyRiskID) {
        this.firstPregnancyRiskID = firstPregnancyRiskID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public Boolean getIsLowRisk() {
        return isLowRisk;
    }

    public void setIsLowRisk(Boolean isLowRisk) {
        this.isLowRisk = isLowRisk;
    }
}

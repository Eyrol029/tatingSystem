package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STI_RiskDetail")
public class STI_RiskDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stiRiskDetailID")
    private Integer stiRiskDetailID;

    @Column(name = "stiRiskID")
    private Integer stiRiskID;

    @Column(name = "stiConditionID")
    private Integer stiConditionID;

    @Column(name = "Answer")
    private Boolean answer;

    @Column(name = "DischargeFrom", length = 20)
    private String dischargeFrom;

    // Getters and Setters
    public Integer getStiRiskDetailID() {
        return stiRiskDetailID;
    }

    public void setStiRiskDetailID(Integer stiRiskDetailID) {
        this.stiRiskDetailID = stiRiskDetailID;
    }

    public Integer getStiRiskID() {
        return stiRiskID;
    }

    public void setStiRiskID(Integer stiRiskID) {
        this.stiRiskID = stiRiskID;
    }

    public Integer getStiConditionID() {
        return stiConditionID;
    }

    public void setStiConditionID(Integer stiConditionID) {
        this.stiConditionID = stiConditionID;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getDischargeFrom() {
        return dischargeFrom;
    }

    public void setDischargeFrom(String dischargeFrom) {
        this.dischargeFrom = dischargeFrom;
    }
}

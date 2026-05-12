package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NormalVitalSign")
public class NormalVitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "normalVitalSignID")
    private Integer normalVitalSignID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "vitalSign")
    private String vitalSign;

    // Getters and Setters
    public Integer getNormalVitalSignID() {
        return normalVitalSignID;
    }

    public void setNormalVitalSignID(Integer normalVitalSignID) {
        this.normalVitalSignID = normalVitalSignID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public String getVitalSign() {
        return vitalSign;
    }

    public void setVitalSign(String vitalSign) {
        this.vitalSign = vitalSign;
    }
}

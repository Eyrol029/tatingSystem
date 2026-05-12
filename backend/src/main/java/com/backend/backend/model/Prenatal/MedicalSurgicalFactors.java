package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalSurgicalFactors")
public class MedicalSurgicalFactors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medsurgID")
    private Integer medsurgID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    // Getters and Setters
    public Integer getMedsurgID() {
        return medsurgID;
    }

    public void setMedsurgID(Integer medsurgID) {
        this.medsurgID = medsurgID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }
}

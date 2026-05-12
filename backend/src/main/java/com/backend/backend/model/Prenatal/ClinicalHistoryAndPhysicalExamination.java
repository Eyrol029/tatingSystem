package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ClinicalHistoryAndPhysicalExamination")
public class ClinicalHistoryAndPhysicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "prenatalrecordID")
    private Integer prenatalrecordID;

    // Getters and Setters
    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public Integer getPrenatalrecordID() {
        return prenatalrecordID;
    }

    public void setPrenatalrecordID(Integer prenatalrecordID) {
        this.prenatalrecordID = prenatalrecordID;
    }
}

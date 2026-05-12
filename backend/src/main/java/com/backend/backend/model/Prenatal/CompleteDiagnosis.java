package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CompleteDiagnosis")
public class CompleteDiagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "completeDiagnosisID")
    private Integer completeDiagnosisID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    // Getters and Setters
    public Integer getCompleteDiagnosisID() {
        return completeDiagnosisID;
    }

    public void setCompleteDiagnosisID(Integer completeDiagnosisID) {
        this.completeDiagnosisID = completeDiagnosisID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}

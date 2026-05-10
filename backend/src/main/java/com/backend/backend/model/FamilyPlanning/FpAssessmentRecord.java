package com.backend.backend.model.FamilyPlanning;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fpAssessmentRecord")
public class FpAssessmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fpAssessmentRecordID")
    private Integer fpAssessmentRecordID;

    @Column(name = "clientID", nullable = false)
    private Integer clientID;

    @Column(name = "dateOfVisit")
    private Date dateOfVisit;

    @Column(name = "medicalFindings", columnDefinition = "TEXT")
    private String medicalFindings;

    @Column(name = "methodAccepted")
    private String methodAccepted;

    @Column(name = "dateOfFollowUpVisit")
    private Date dateOfFollowUpVisit;

    public Integer getFpAssessmentRecordID() {
        return fpAssessmentRecordID;
    }

    public void setFpAssessmentRecordID(Integer fpAssessmentRecordID) {
        this.fpAssessmentRecordID = fpAssessmentRecordID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getMedicalFindings() {
        return medicalFindings;
    }

    public void setMedicalFindings(String medicalFindings) {
        this.medicalFindings = medicalFindings;
    }

    public String getMethodAccepted() {
        return methodAccepted;
    }

    public void setMethodAccepted(String methodAccepted) {
        this.methodAccepted = methodAccepted;
    }

    public Date getDateOfFollowUpVisit() {
        return dateOfFollowUpVisit;
    }

    public void setDateOfFollowUpVisit(Date dateOfFollowUpVisit) {
        this.dateOfFollowUpVisit = dateOfFollowUpVisit;
    }
}

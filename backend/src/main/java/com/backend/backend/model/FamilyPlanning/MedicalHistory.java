package com.backend.backend.model.FamilyPlanning;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalHistory")
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicalHistoryid")
    private Integer medicalHistoryid;

    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "HasDisability")
    private Boolean hasDisability;

    @Column(name = "DisabilityDescription")
    private String disabilityDescription;

    @Column(name = "DateRecorded")
    private Date dateRecorded;

    public Integer getMedicalHistoryid() {
        return medicalHistoryid;
    }

    public void setMedicalHistoryid(Integer medicalHistoryid) {
        this.medicalHistoryid = medicalHistoryid;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Boolean getHasDisability() {
        return hasDisability;
    }

    public void setHasDisability(Boolean hasDisability) {
        this.hasDisability = hasDisability;
    }

    public String getDisabilityDescription() {
        return disabilityDescription;
    }

    public void setDisabilityDescription(String disabilityDescription) {
        this.disabilityDescription = disabilityDescription;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }
}

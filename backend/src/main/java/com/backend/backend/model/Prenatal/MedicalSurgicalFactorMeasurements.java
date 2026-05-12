package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalSurgicalFactor_measurements")
public class MedicalSurgicalFactorMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medSurgMeasurementID")
    private Integer medSurgMeasurementID;

    @Column(name = "medsurgID")
    private Integer medsurgID;

    @Column(name = "normoActiveBowelSounds")
    private Boolean normoActiveBowelSounds;

    @Column(name = "nonTender")
    private Boolean nonTender;

    @Column(name = "activeFetalMovement")
    private Boolean activeFetalMovement;

    @Column(name = "fundalWeight")
    private Double fundalWeight;

    @Column(name = "estimatedFetalWeight")
    private Double estimatedFetalWeight;

    @Column(name = "fetalHeartTone")
    private Integer fetalHeartTone;

    // Getters and Setters
    public Integer getMedSurgMeasurementID() {
        return medSurgMeasurementID;
    }

    public void setMedSurgMeasurementID(Integer medSurgMeasurementID) {
        this.medSurgMeasurementID = medSurgMeasurementID;
    }

    public Integer getMedsurgID() {
        return medsurgID;
    }

    public void setMedsurgID(Integer medsurgID) {
        this.medsurgID = medsurgID;
    }

    public Boolean getNormoActiveBowelSounds() {
        return normoActiveBowelSounds;
    }

    public void setNormoActiveBowelSounds(Boolean normoActiveBowelSounds) {
        this.normoActiveBowelSounds = normoActiveBowelSounds;
    }

    public Boolean getNonTender() {
        return nonTender;
    }

    public void setNonTender(Boolean nonTender) {
        this.nonTender = nonTender;
    }

    public Boolean getActiveFetalMovement() {
        return activeFetalMovement;
    }

    public void setActiveFetalMovement(Boolean activeFetalMovement) {
        this.activeFetalMovement = activeFetalMovement;
    }

    public Double getFundalWeight() {
        return fundalWeight;
    }

    public void setFundalWeight(Double fundalWeight) {
        this.fundalWeight = fundalWeight;
    }

    public Double getEstimatedFetalWeight() {
        return estimatedFetalWeight;
    }

    public void setEstimatedFetalWeight(Double estimatedFetalWeight) {
        this.estimatedFetalWeight = estimatedFetalWeight;
    }

    public Integer getFetalHeartTone() {
        return fetalHeartTone;
    }

    public void setFetalHeartTone(Integer fetalHeartTone) {
        this.fetalHeartTone = fetalHeartTone;
    }
}

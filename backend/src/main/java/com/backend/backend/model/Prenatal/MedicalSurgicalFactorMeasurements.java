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

    @Column(name = "parousVagina")
    private Boolean parousVagina;

    @Column(name = "cervixSmoothClosed")
    private Boolean cervixSmoothClosed;

    @Column(name = "fundalWeight")
    private Double fundalWeight;

    @Column(name = "estimatedFetalWeight")
    private Double estimatedFetalWeight;

    @Column(name = "leopoldL1")
    private String leopoldL1;

    @Column(name = "leopoldL2")
    private String leopoldL2;

    @Column(name = "leopoldL3")
    private String leopoldL3;

    @Column(name = "leopoldL4")
    private String leopoldL4;

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

    public Boolean getParousVagina() {
        return parousVagina;
    }

    public void setParousVagina(Boolean parousVagina) {
        this.parousVagina = parousVagina;
    }

    public Boolean getCervixSmoothClosed() {
        return cervixSmoothClosed;
    }

    public void setCervixSmoothClosed(Boolean cervixSmoothClosed) {
        this.cervixSmoothClosed = cervixSmoothClosed;
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

    public String getLeopoldL1() {
        return leopoldL1;
    }

    public void setLeopoldL1(String leopoldL1) {
        this.leopoldL1 = leopoldL1;
    }

    public String getLeopoldL2() {
        return leopoldL2;
    }

    public void setLeopoldL2(String leopoldL2) {
        this.leopoldL2 = leopoldL2;
    }

    public String getLeopoldL3() {
        return leopoldL3;
    }

    public void setLeopoldL3(String leopoldL3) {
        this.leopoldL3 = leopoldL3;
    }

    public String getLeopoldL4() {
        return leopoldL4;
    }

    public void setLeopoldL4(String leopoldL4) {
        this.leopoldL4 = leopoldL4;
    }

    public Integer getFetalHeartTone() {
        return fetalHeartTone;
    }

    public void setFetalHeartTone(Integer fetalHeartTone) {
        this.fetalHeartTone = fetalHeartTone;
    }
}

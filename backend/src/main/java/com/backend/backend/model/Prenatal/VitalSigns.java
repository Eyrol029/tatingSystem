package com.backend.backend.model.Prenatal;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "VitalSigns")
public class VitalSigns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vitalSignID")
    private Integer vitalSignID;

    @Column(name = "consultationRecordID")
    private Integer consultationRecordID;

    @Column(name = "fetalHeartTone")
    private String fetalHeartTone;

    @Column(name = "fundalHeight")
    private String fundalHeight;

    @Column(name = "maternalPosition")
    private String maternalPosition;

    @Column(name = "fetalPresentation")
    private String fetalPresentation;

    @Column(name = "maternalWeight")
    private BigDecimal maternalWeight;

    @Column(name = "respiratoryRate")
    private Integer respiratoryRate;

    @Column(name = "heartRate")
    private Integer heartRate;

    @Column(name = "bloodPressure")
    private String bloodPressure;

    @Column(name = "bodyTemperature")
    private BigDecimal bodyTemperature;

    @Column(name = "nextVisitDate")
    @Temporal(TemporalType.DATE)
    private Date nextVisitDate;

    // getters and setters
    public Integer getVitalSignID() {
        return vitalSignID;
    }

    public void setVitalSignID(Integer vitalSignID) {
        this.vitalSignID = vitalSignID;
    }

    public Integer getConsultationRecordID() {
        return consultationRecordID;
    }

    public void setConsultationRecordID(Integer consultationRecordID) {
        this.consultationRecordID = consultationRecordID;
    }

    public String getFetalHeartTone() {
        return fetalHeartTone;
    }

    public void setFetalHeartTone(String fetalHeartTone) {
        this.fetalHeartTone = fetalHeartTone;
    }

    public String getFundalHeight() {
        return fundalHeight;
    }

    public void setFundalHeight(String fundalHeight) {
        this.fundalHeight = fundalHeight;
    }

    public String getMaternalPosition() {
        return maternalPosition;
    }

    public void setMaternalPosition(String maternalPosition) {
        this.maternalPosition = maternalPosition;
    }

    public String getFetalPresentation() {
        return fetalPresentation;
    }

    public void setFetalPresentation(String fetalPresentation) {
        this.fetalPresentation = fetalPresentation;
    }

    public BigDecimal getMaternalWeight() {
        return maternalWeight;
    }

    public void setMaternalWeight(BigDecimal maternalWeight) {
        this.maternalWeight = maternalWeight;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public BigDecimal getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(BigDecimal bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public Date getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(Date nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }
}

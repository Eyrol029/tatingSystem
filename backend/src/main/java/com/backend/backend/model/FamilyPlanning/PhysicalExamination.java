package com.backend.backend.model.FamilyPlanning;

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
@Table(name = "PhysicalExamination")
public class PhysicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "height")
    private BigDecimal height;

    @Column(name = "blood_Pressure", length = 15)
    private String bloodPressure;

    @Column(name = "pulse_Rate")
    private Integer pulseRate;

    @Column(name = "date_Examined")
    @Temporal(TemporalType.DATE)
    private Date dateExamined;

    @Column(name = "examiner_Name", length = 100)
    private String examinerName;

    // Getters and Setters
    public Integer getPExamID() {  // ✅ Fixed: capital P
        return pExamID;
    }

    public void setPExamID(Integer pExamID) {  // ✅ Fixed: capital P
        this.pExamID = pExamID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(Integer pulseRate) {
        this.pulseRate = pulseRate;
    }

    public Date getDateExamined() {
        return dateExamined;
    }

    public void setDateExamined(Date dateExamined) {
        this.dateExamined = dateExamined;
    }

    public String getExaminerName() {
        return examinerName;
    }

    public void setExaminerName(String examinerName) {
        this.examinerName = examinerName;
    }
}
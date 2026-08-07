package com.backend.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_referrals")
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link or reference to Patient
    private Long patientId;
    private String patientName;
    private Integer age;
    private String contact;
    private String gestationalWeek;

    // Vitals Snapshot
    private Integer systolicBp;
    private Integer diastolicBp;
    private Double glucose;
    private Integer fetalHeartRate;
    private Double fetalGrowthPercentile;

    // Risk Factors (Stored as comma-separated or text block)
    @Column(columnDefinition = "TEXT")
    private String riskFactors;

    // Referral Details Form Fields
    private String hospitalName;
    private String obGyneName;
    private String obGyneContact;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    public Referral() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGestationalWeek() {
        return gestationalWeek;
    }

    public void setGestationalWeek(String gestationalWeek) {
        this.gestationalWeek = gestationalWeek;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(Integer diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public Double getGlucose() {
        return glucose;
    }

    public void setGlucose(Double glucose) {
        this.glucose = glucose;
    }

    public Integer getFetalHeartRate() {
        return fetalHeartRate;
    }

    public void setFetalHeartRate(Integer fetalHeartRate) {
        this.fetalHeartRate = fetalHeartRate;
    }

    public Double getFetalGrowthPercentile() {
        return fetalGrowthPercentile;
    }

    public void setFetalGrowthPercentile(Double fetalGrowthPercentile) {
        this.fetalGrowthPercentile = fetalGrowthPercentile;
    }

    public String getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(String riskFactors) {
        this.riskFactors = riskFactors;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getObGyneName() {
        return obGyneName;
    }

    public void setObGyneName(String obGyneName) {
        this.obGyneName = obGyneName;
    }

    public String getObGyneContact() {
        return obGyneContact;
    }

    public void setObGyneContact(String obGyneContact) {
        this.obGyneContact = obGyneContact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

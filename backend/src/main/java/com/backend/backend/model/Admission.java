package com.backend.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admission")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer admissionID;

    @Column(name = "patientID")
    private Integer patientID;

    // Cached for display without an extra lookup — not the source of truth
    private String patientName;

    private Integer age;
    private String gestationalAge;

    private Boolean readyForDelivery;
    private Boolean isHighRisk;

    @Column(name = "wardID")
    private Integer wardID;
    private String wardName;

    @Column(name = "attendingStaffID")
    private Integer attendingStaffID;
    private String attendingStaffName;

    private Boolean hasPhilHealth;
    private Boolean paymentComplete = false;

    // Links this admission to the real Statement of Account created during Billing
    @Column(name = "soaID")
    private Integer soaID;

    // Tracks where the patient currently is in the workflow, so the process
    // can be resumed later instead of always starting at "arrival"
    private String currentStep = "arrival";

    private LocalDateTime admissionDate;

    public Integer getAdmissionID() {
        return admissionID;
    }

    public void setAdmissionID(Integer admissionID) {
        this.admissionID = admissionID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
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

    public String getGestationalAge() {
        return gestationalAge;
    }

    public void setGestationalAge(String gestationalAge) {
        this.gestationalAge = gestationalAge;
    }

    public Boolean getReadyForDelivery() {
        return readyForDelivery;
    }

    public void setReadyForDelivery(Boolean readyForDelivery) {
        this.readyForDelivery = readyForDelivery;
    }

    public Boolean getIsHighRisk() {
        return isHighRisk;
    }

    public void setIsHighRisk(Boolean isHighRisk) {
        this.isHighRisk = isHighRisk;
    }

    public Integer getWardID() {
        return wardID;
    }

    public void setWardID(Integer wardID) {
        this.wardID = wardID;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public Integer getAttendingStaffID() {
        return attendingStaffID;
    }

    public void setAttendingStaffID(Integer attendingStaffID) {
        this.attendingStaffID = attendingStaffID;
    }

    public String getAttendingStaffName() {
        return attendingStaffName;
    }

    public void setAttendingStaffName(String attendingStaffName) {
        this.attendingStaffName = attendingStaffName;
    }

    public Boolean getHasPhilHealth() {
        return hasPhilHealth;
    }

    public void setHasPhilHealth(Boolean hasPhilHealth) {
        this.hasPhilHealth = hasPhilHealth;
    }

    public Boolean getPaymentComplete() {
        return paymentComplete;
    }

    public void setPaymentComplete(Boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
    }

    public Integer getSoaID() {
        return soaID;
    }

    public void setSoaID(Integer soaID) {
        this.soaID = soaID;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }
}
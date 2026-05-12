package com.backend.backend.model.Billing;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billingID;

    private Integer patientServiceID;

    private LocalDate billingDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Lob
    private String remarks;

    public enum PaymentStatus {
        Pending,
        Paid,
        Partial
    }

    public Integer getBillingID() {
        return billingID;
    }

    public void setBillingID(Integer billingID) {
        this.billingID = billingID;
    }

    public Integer getPatientServiceID() {
        return patientServiceID;
    }

    public void setPatientServiceID(Integer patientServiceID) {
        this.patientServiceID = patientServiceID;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

package com.backend.backend.model.Billing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billingID;

    @Column(name = "soaId")
    private Integer soaId;

    @Column(name = "patientServiceId")
    private Integer patientServiceId;

    @Column(name = "patientId")
    private Integer patientId;

    @Column(name = "amountPaid")
    private Double amountPaid;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "paymentMethod", length = 50)
    private String paymentMethod;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "installmentNumber")
    private Integer installmentNumber;

    // NEW: name of the discount applied on this payment, if any (e.g. "Senior Citizen")
    @Column(name = "discountName")
    private String discountName;

    // NEW: peso amount of the discount applied on this payment, if any
    @Column(name = "discountAmount")
    private Double discountAmount;

    // NEW: JSON array of the availed services / discounts shown in the frontend breakdown,
    // e.g. [{"name":"Consultation","amount":500,"isDiscount":false},{"name":"Senior Citizen","amount":-200,"isDiscount":true}]
    // Stored as raw JSON text so the frontend can reconstruct the exact breakdown when reopening.
    @Column(name = "serviceBreakdown", columnDefinition = "TEXT")
    private String serviceBreakdown;

    public Integer getbillingID() {
        return billingID;
    }

    public void setbillingID(Integer billingID) {
        this.billingID = billingID;
    }

    public Integer getSoaId() {
        return soaId;
    }

    public void setSoaId(Integer soaId) {
        this.soaId = soaId;
    }

    public Integer getPatientServiceId() {
        return patientServiceId;
    }

    public void setPatientServiceId(Integer patientServiceId) {
        this.patientServiceId = patientServiceId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getServiceBreakdown() {
        return serviceBreakdown;
    }

    public void setServiceBreakdown(String serviceBreakdown) {
        this.serviceBreakdown = serviceBreakdown;
    }
}
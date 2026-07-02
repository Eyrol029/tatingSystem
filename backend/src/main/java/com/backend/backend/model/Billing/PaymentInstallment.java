package com.backend.backend.model.Billing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PaymentInstallment")
public class PaymentInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer installmentId;

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

    public Integer getInstallmentId() {
        return installmentId;
    }

    public void setInstallmentId(Integer installmentId) {
        this.installmentId = installmentId;
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
}

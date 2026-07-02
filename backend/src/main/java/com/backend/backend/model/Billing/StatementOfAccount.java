package com.backend.backend.model.Billing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "StatementOfAccount")
public class StatementOfAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer soaID;

    @Column(name = "patientServiceID")
    private Integer patientServiceID;

    @Column(name = "patientID")
    private Integer patientID;

    private Integer billingID;

    private String invoiceReceiptNumber;

    private LocalDateTime dateTimeAdmission;

    private LocalDateTime dateTimeDischarge;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.Pending;

    @Column(name = "summaryFees")
    private Double summaryFees = 0.0;

    @Column(name = "totalAmount")
    private Double totalAmount = 0.0;

    @Column(name = "amountPaid")
    private Double amountPaid = 0.0;

    @Column(name = "balanceAmount")
    private Double balanceAmount = 0.0;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @Column(name = "installmentCount")
    private Integer installmentCount = 0;

    public enum PaymentStatus {
        Pending,
        Partial,
        Paid
    }

    public Integer getSoaID() {
        return soaID;
    }

    public void setSoaID(Integer soaID) {
        this.soaID = soaID;
    }

    public Integer getPatientServiceID() {
        return patientServiceID;
    }

    public void setPatientServiceID(Integer patientServiceID) {
        this.patientServiceID = patientServiceID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getBillingID() {
        return billingID;
    }

    public void setBillingID(Integer billingID) {
        this.billingID = billingID;
    }

    public String getInvoiceReceiptNumber() {
        return invoiceReceiptNumber;
    }

    public void setInvoiceReceiptNumber(String invoiceReceiptNumber) {
        this.invoiceReceiptNumber = invoiceReceiptNumber;
    }

    public LocalDateTime getDateTimeAdmission() {
        return dateTimeAdmission;
    }

    public void setDateTimeAdmission(LocalDateTime dateTimeAdmission) {
        this.dateTimeAdmission = dateTimeAdmission;
    }

    public LocalDateTime getDateTimeDischarge() {
        return dateTimeDischarge;
    }

    public void setDateTimeDischarge(LocalDateTime dateTimeDischarge) {
        this.dateTimeDischarge = dateTimeDischarge;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getSummaryFees() {
        return summaryFees != null ? summaryFees : totalAmount;
    }

    public void setSummaryFees(Double summaryFees) {
        this.summaryFees = summaryFees;
        this.totalAmount = summaryFees;
    }

    public Double getTotalAmount() {
        return totalAmount != null ? totalAmount : summaryFees;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        this.summaryFees = totalAmount;
    }

    public Double getAmountPaid() {
        return amountPaid != null ? amountPaid : 0.0;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getBalanceAmount() {
        return balanceAmount != null ? balanceAmount : Math.max(0.0, getTotalAmount() - getAmountPaid());
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getInstallmentCount() {
        return installmentCount != null ? installmentCount : 0;
    }

    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }
}

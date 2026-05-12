package com.backend.backend.model.Billing;

import java.time.LocalDateTime;

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

    private Integer billingID;

    private String invoiceReceiptNumber;

    private LocalDateTime dateTimeAdmission;

    private LocalDateTime dateTimeDischarge;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Double summaryFees;

    public enum PaymentStatus {
        Pending,
        Paid
    }

    public Integer getSoaID() {
        return soaID;
    }

    public void setSoaID(Integer soaID) {
        this.soaID = soaID;
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
        return summaryFees;
    }

    public void setSummaryFees(Double summaryFees) {
        this.summaryFees = summaryFees;
    }
}

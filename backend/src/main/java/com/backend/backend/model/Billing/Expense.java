package com.backend.backend.model.Billing;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer billingID;

    private LocalDate expenseDate;

    @Column(nullable = false)
    private String category;

    @Lob
    private String description;

    private String payee;

    @Column(nullable = false)
    private Double amount;

    private Integer invoiceReceiptNumber;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillingID() {
        return billingID;
    }

    public void setBillingID(Integer billingID) {
        this.billingID = billingID;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getInvoiceReceiptNumber() {
        return invoiceReceiptNumber;
    }

    public void setInvoiceReceiptNumber(Integer invoiceReceiptNumber) {
        this.invoiceReceiptNumber = invoiceReceiptNumber;
    }
}

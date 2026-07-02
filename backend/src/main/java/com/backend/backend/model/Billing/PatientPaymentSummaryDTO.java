package com.backend.backend.model.Billing;

import java.time.LocalDateTime;

public class PatientPaymentSummaryDTO {
    private Integer soaId;
    private Integer patientServiceId;
    private Integer patientId;
    private String patientName;
    private String email;
    private String phone;
    private String serviceName;
    private Double totalAmount;
    private Double amountPaid;
    private Double balanceAmount;
    private String paymentStatus;
    private String invoiceReceiptNumber;
    private LocalDateTime dueDate;

    public Integer getSoaId() { return soaId; }
    public void setSoaId(Integer soaId) { this.soaId = soaId; }

    public Integer getPatientServiceId() { return patientServiceId; }
    public void setPatientServiceId(Integer patientServiceId) { this.patientServiceId = patientServiceId; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public Double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getInvoiceReceiptNumber() { return invoiceReceiptNumber; }
    public void setInvoiceReceiptNumber(String invoiceReceiptNumber) { this.invoiceReceiptNumber = invoiceReceiptNumber; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
}

package com.backend.backend.controller.Billing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Billing.PaymentInstallment;
import com.backend.backend.repository.Billing.PaymentInstallmentRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/billing/installments")
public class PaymentInstallmentController {

    @Autowired
    private PaymentInstallmentRepository repository;

    @GetMapping("/soa/{soaId}")
    public List<PaymentInstallment> getBySoa(@PathVariable Integer soaId) {
        return repository.findBySoaId(soaId);
    }

    @PostMapping
    public PaymentInstallment create(@RequestBody PaymentInstallmentRequest request) {
        PaymentInstallment installment = new PaymentInstallment();
        installment.setSoaId(request.getSoaId());
        installment.setPatientServiceId(request.getPatientServiceId());
        installment.setPatientId(request.getPatientId());
        installment.setAmountPaid(request.getAmountPaid());
        installment.setPaymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDateTime.now());
        installment.setPaymentMethod(request.getPaymentMethod());
        installment.setNotes(request.getNotes());
        installment.setInstallmentNumber(request.getInstallmentNumber());
        return repository.save(installment);
    }

    public static class PaymentInstallmentRequest {
        private Integer soaId;
        private Integer patientServiceId;
        private Integer patientId;
        private Double amountPaid;
        private LocalDateTime paymentDate;
        private String paymentMethod;
        private String notes;
        private Integer installmentNumber;

        public Integer getSoaId() { return soaId; }
        public void setSoaId(Integer soaId) { this.soaId = soaId; }
        public Integer getPatientServiceId() { return patientServiceId; }
        public void setPatientServiceId(Integer patientServiceId) { this.patientServiceId = patientServiceId; }
        public Integer getPatientId() { return patientId; }
        public void setPatientId(Integer patientId) { this.patientId = patientId; }
        public Double getAmountPaid() { return amountPaid; }
        public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }
        public LocalDateTime getPaymentDate() { return paymentDate; }
        public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public Integer getInstallmentNumber() { return installmentNumber; }
        public void setInstallmentNumber(Integer installmentNumber) { this.installmentNumber = installmentNumber; }
    }
}

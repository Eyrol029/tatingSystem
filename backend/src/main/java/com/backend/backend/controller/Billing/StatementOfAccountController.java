package com.backend.backend.controller.Billing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Billing.PatientPaymentSummaryDTO;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.model.Billing.StatementOfAccountDetailsDTO;
import com.backend.backend.service.Billing.StatementOfAccountService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/billing/soa")
public class StatementOfAccountController {

    @Autowired
    private StatementOfAccountService service;

    @PostMapping
    public StatementOfAccount add(@RequestBody StatementOfAccount soa) {
        return service.addSoa(soa);
    }

    @GetMapping("/{id}")
    public StatementOfAccount get(@PathVariable Integer id) {
        return service.getSoaById(id);
    }

    @GetMapping
    public List<StatementOfAccount> getAll() {
        return service.getAllSoa();
    }

    @GetMapping("/dashboard")
    public List<PatientPaymentSummaryDTO> getDashboard() {
        return service.getDashboardSummaries();
    }

    @GetMapping("/patient/{patientId}")
    public StatementOfAccountDetailsDTO getPatientSoa(@PathVariable Integer patientId) {
        return service.getPatientSoaDetails(patientId);
    }

    @PutMapping
    public StatementOfAccount update(@RequestBody StatementOfAccount soa) {
        return service.updateSoa(soa);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteSoa(id);
        return "Statement of Account deleted successfully.";
    }

    @PostMapping("/{id}/payments")
    public StatementOfAccount applyPayment(@PathVariable Integer id, @RequestBody PaymentRequest request) {
        StatementOfAccount soa = service.getSoaById(id);
        if (request.getTotalAmount() != null) {
            soa.setTotalAmount(request.getTotalAmount());
        }
        if (request.getPaymentDate() != null) {
            soa.setDueDate(request.getPaymentDate());
        }
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            soa.setDescription(request.getNotes());
        }
        service.applyPayment(soa, request.getAmount());
        return soa;
    }

    public static class PaymentRequest {
        private Double amount;
        private Double totalAmount;
        private LocalDateTime paymentDate;
        private String paymentMethod;
        private String notes;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
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
    }
}

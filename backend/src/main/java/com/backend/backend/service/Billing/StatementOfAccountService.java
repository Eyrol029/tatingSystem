package com.backend.backend.service.Billing;

import java.util.List;

import com.backend.backend.model.Billing.PatientPaymentSummaryDTO;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.model.Billing.StatementOfAccountDetailsDTO;

public interface StatementOfAccountService {

    StatementOfAccount addSoa(StatementOfAccount soa);

    StatementOfAccount getSoaById(Integer id);

    List<StatementOfAccount> getAllSoa();

    StatementOfAccount updateSoa(StatementOfAccount soa);

    void deleteSoa(Integer id);

    // CHANGED: now accepts the payment notes plus the structured discount / service
    // breakdown so they can be persisted on the PaymentInstallment record, not just
    // used to overwrite the SOA's description.
    void applyPayment(
            StatementOfAccount soa,
            Double amount,
            String notes,
            String discountName,
            Double discountAmount,
            String serviceBreakdown
    );

    List<PatientPaymentSummaryDTO> getDashboardSummaries();

    StatementOfAccountDetailsDTO getPatientSoaDetails(Integer patientId);
}
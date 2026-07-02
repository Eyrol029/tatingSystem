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

    void applyPayment(StatementOfAccount soa, Double amount);

    List<PatientPaymentSummaryDTO> getDashboardSummaries();

    StatementOfAccountDetailsDTO getPatientSoaDetails(Integer patientId);
}

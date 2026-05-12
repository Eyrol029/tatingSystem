package com.backend.backend.service.Billing;

import java.util.List;

import com.backend.backend.model.Billing.StatementOfAccount;

public interface StatementOfAccountService {

    StatementOfAccount addSoa(StatementOfAccount soa);

    StatementOfAccount getSoaById(Integer id);

    List<StatementOfAccount> getAllSoa();

    StatementOfAccount updateSoa(StatementOfAccount soa);

    void deleteSoa(Integer id);
}

package com.backend.backend.service.Billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;

@Service
public class StatementOfAccountServiceImpl implements StatementOfAccountService {

    @Autowired
    private StatementOfAccountRepository repository;

    @Override
    public StatementOfAccount addSoa(StatementOfAccount soa) {
        return repository.save(soa);
    }

    @Override
    public StatementOfAccount getSoaById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SOA not found: " + id));
    }

    @Override
    public List<StatementOfAccount> getAllSoa() {
        return repository.findAll();
    }

    @Override
    public StatementOfAccount updateSoa(StatementOfAccount soa) {
        return repository.save(soa);
    }

    @Override
    public void deleteSoa(Integer id) {
        repository.deleteById(id);
    }
}

package com.backend.backend.service.Billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Billing.Billing;
import com.backend.backend.repository.Billing.BillingRepository;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository repository;

    @Override
    public Billing addBilling(Billing billing) {
        return repository.save(billing);
    }

    @Override
    public Billing getBillingById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing not found: " + id));
    }

    @Override
    public List<Billing> getAllBilling() {
        return repository.findAll();
    }

    @Override
    public Billing updateBilling(Billing billing) {
        return repository.save(billing);
    }

    @Override
    public void deleteBilling(Integer id) {
        repository.deleteById(id);
    }
}

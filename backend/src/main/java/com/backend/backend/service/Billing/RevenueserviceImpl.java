package com.backend.backend.service.Billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Billing.Revenue;
import com.backend.backend.repository.Billing.RevenueRepository;

@Service
public class RevenueserviceImpl implements Revenueservice {

    @Autowired
    private RevenueRepository repository;

    @Override
    public Revenue addRevenue(Revenue revenue) {
        return repository.save(revenue);
    }

    @Override
    public Revenue getRevenueById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revenue not found: " + id));
    }

    @Override
    public List<Revenue> getAllRevenue() {
        return repository.findAll();
    }

    @Override
    public Revenue updateRevenue(Revenue revenue) {
        return repository.save(revenue);
    }

    @Override
    public void deleteRevenue(Integer id) {
        repository.deleteById(id);
    }
}
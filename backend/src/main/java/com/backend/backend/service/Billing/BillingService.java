package com.backend.backend.service.Billing;

import java.util.List;

import com.backend.backend.model.Billing.Billing;

public interface BillingService {

    Billing addBilling(Billing billing);

    Billing getBillingById(Integer id);

    List<Billing> getAllBilling();

    Billing updateBilling(Billing billing);

    void deleteBilling(Integer id);
}

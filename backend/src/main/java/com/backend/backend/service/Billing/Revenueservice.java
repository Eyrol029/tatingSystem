package com.backend.backend.service.Billing;

import java.util.List;

import com.backend.backend.model.Billing.Revenue;

public interface Revenueservice {
    
    Revenue addRevenue(Revenue revenue);
 
    Revenue getRevenueById(Integer id);
 
    List<Revenue> getAllRevenue();
 
    Revenue updateRevenue(Revenue revenue);
 
    void deleteRevenue(Integer id);
}

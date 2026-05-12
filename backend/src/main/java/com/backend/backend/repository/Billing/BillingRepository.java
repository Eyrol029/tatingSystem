package com.backend.backend.repository.Billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Billing.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {
}

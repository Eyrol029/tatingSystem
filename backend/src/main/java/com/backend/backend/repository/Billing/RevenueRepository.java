package com.backend.backend.repository.Billing;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.Billing.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
}
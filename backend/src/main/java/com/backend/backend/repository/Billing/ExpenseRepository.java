package com.backend.backend.repository.Billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Billing.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}

package com.backend.backend.repository.Billing;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Billing.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // NEW: for dashboard financial trend
    List<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end);
}
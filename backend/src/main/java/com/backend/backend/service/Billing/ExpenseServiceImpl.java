package com.backend.backend.service.Billing;

import com.backend.backend.model.Billing.Expense;
import com.backend.backend.repository.Billing.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    @Override
    public Expense addExpense(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public Expense getExpenseById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));
    }

    @Override
    public List<Expense> getAllExpense() {
        return repository.findAll();
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public void deleteExpense(Integer id) {
        repository.deleteById(id);
    }
}

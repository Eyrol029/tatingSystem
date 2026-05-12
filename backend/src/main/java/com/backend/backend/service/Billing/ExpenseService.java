package com.backend.backend.service.Billing;

import java.util.List;

import com.backend.backend.model.Billing.Expense;

public interface ExpenseService {

    Expense addExpense(Expense expense);

    Expense getExpenseById(Integer id);

    List<Expense> getAllExpense();

    Expense updateExpense(Expense expense);

    void deleteExpense(Integer id);
}

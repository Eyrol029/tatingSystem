package com.backend.backend.model.Billing;

public class FinancialPoint {
    private String date;       // "yyyy-MM-dd"
    private Double revenue;
    private Double expenses;
    private Double netIncome;

    public FinancialPoint() {}

    public FinancialPoint(String date, Double revenue, Double expenses, Double netIncome) {
        this.date = date;
        this.revenue = revenue;
        this.expenses = expenses;
        this.netIncome = netIncome;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Double getRevenue() { return revenue; }
    public void setRevenue(Double revenue) { this.revenue = revenue; }

    public Double getExpenses() { return expenses; }
    public void setExpenses(Double expenses) { this.expenses = expenses; }

    public Double getNetIncome() { return netIncome; }
    public void setNetIncome(Double netIncome) { this.netIncome = netIncome; }
}
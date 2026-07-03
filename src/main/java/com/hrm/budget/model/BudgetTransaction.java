package com.hrm.budget.model;

public class BudgetTransaction {
    private String transactionId;
    private String department;
    private double amount;
    private String type; // "DEDUCT" hoặc "REFUND"
    private String description;

    public BudgetTransaction(String id, String dept, double amt, String t, String desc) {
        this.transactionId = id;
        this.department = dept;
        this.amount = amt;
        this.type = t;
        this.description = desc;
    }
}

package com.hrm.budget.model;

public class Budget {
    private String department;
    private double allocatedAmount;
    private double currentAmount;
    private String status; // "DRAFT", "SUBMITTED", "APPROVED", "REJECTED"
    private String note;

    public Budget(String dept, double alloc) {
        this.department = dept;
        this.allocatedAmount = alloc;
        this.currentAmount = alloc;
        this.status = "DRAFT";
        this.note = "";
    }

    public void deduct(double amt) {
        if (this.currentAmount >= amt) {
            this.currentAmount -= amt;
        } else {
            throw new IllegalArgumentException("Ngân sách phòng ban " + department + " không đủ để thực hiện chi tiêu!");
        }
    }

    public void refund(double amt) {
        this.currentAmount += amt;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public String getDepartment() {
        return department;
    }

    public void submitForApproval(String note) {
        this.status = "SUBMITTED";
        this.note = note;
    }

    public void approve() {
        this.status = "APPROVED";
    }

    public void reject(String reason) {
        this.status = "REJECTED";
        this.note = reason;
    }
}

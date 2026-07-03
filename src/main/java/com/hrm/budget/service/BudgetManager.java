package com.hrm.budget.service;

import com.hrm.budget.model.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private List<Budget> budgets = new ArrayList<>();

    public void deductBudget(String dept, double amount, String desc) {
        for (Budget b : budgets) {
            if (b.getDepartment().equals(dept)) {
                if (b.getCurrentAmount() >= amount) {
                    b.deduct(amount);
                    System.out.println("✅ Đã trừ " + amount + " từ ngân sách: " + dept);
                } else {
                    System.out.println("❌ Ngân sách phòng " + dept + " không đủ!");
                }
                return;
            }
        }
    }
}

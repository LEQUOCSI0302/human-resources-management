package com.hrm.payroll.model;

public class SalaryComponent {
    private String componentName;
    private double amount;
    private boolean isDeduction; // Phụ cấp hay Khấu trừ

    public SalaryComponent(String name, double amt, boolean isDed) {
        this.componentName = name;
        this.amount = amt;
        this.isDeduction = isDed;
    }

    public String getComponentName() {
        return componentName;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isDeduction() {
        return isDeduction;
    }
}

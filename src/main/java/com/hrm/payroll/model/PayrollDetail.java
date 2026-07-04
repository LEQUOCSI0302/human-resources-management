package com.hrm.payroll.model;

public class PayrollDetail {
    private String employeeId;
    private String employeeName;
    private double basicSalary;
    private int workingDays;
    private double allowance;
    private double insurance;
    private double tax;
    private double netSalary;

    public PayrollDetail(String employeeId, String employeeName, double basicSalary, int workingDays, double allowance, double insurance, double tax, double netSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.basicSalary = basicSalary;
        this.workingDays = workingDays;
        this.allowance = allowance;
        this.insurance = insurance;
        this.tax = tax;
        this.netSalary = netSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    @Override
    public String toString() {
        return "=========================\n" + "Mã NV : " + employeeId + "\nTên : " + employeeName + "\nLương CB : " + basicSalary + "\nNgày công : " + workingDays + "\nPhụ cấp : " + allowance + "\nBHXH : " + insurance + "\nThuế : " + tax + "\nThực lĩnh : " + netSalary;
    }

}
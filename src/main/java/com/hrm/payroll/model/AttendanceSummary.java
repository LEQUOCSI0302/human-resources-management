package com.hrm.payroll.model;

public class AttendanceSummary {
    private String employeeId;
    private int workingDays;

    public AttendanceSummary(String employeeId, int workingDays) {
        this.employeeId = employeeId;
        this.workingDays = workingDays;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public int getWorkingDays() {
        return workingDays;
    }
}
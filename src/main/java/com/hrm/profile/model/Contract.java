package com.hrm.profile.model;

public class Contract {
    private String contractId;
    private double baseSalary;
    private double allowance;
    private String startDate;
    private String endDate;
    private String status;
    public Contract(String cId, double base, double allow, String start, String end) {
        this.contractId = cId;
        this.baseSalary = base;
        this.allowance = allow;
        this.startDate = start;
        this.endDate = end;
        this.status = "ACTIVE";
    }

    public double getBaseSalary() { return baseSalary; }
    public double getAllowance() { return allowance; }
    public String getStatus() { return status; }
}

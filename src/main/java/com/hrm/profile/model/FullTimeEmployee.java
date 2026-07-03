package com.hrm.profile.model;

public class FullTimeEmployee extends EmployeeProfile{
    private String insuranceId;
    private double socialInsuranceRate;

    public FullTimeEmployee(String id, String name, String dept, String pos, String insId) {
        super(id, name, dept, pos);
        this.insuranceId = insId;
        this.socialInsuranceRate = 0.08; // 8% mặc định
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public double getSocialInsuranceRate() {
        return socialInsuranceRate;
    }
}

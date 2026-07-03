package com.hrm.profile.model.contract;

import com.hrm.profile.model.Contract;

public class SalesSalaryStrategy implements ISalaryStrategy{
    private double monthlyRevenue;
    private double commissionRate;

    public SalesSalaryStrategy(double rev, double rate) {
        this.monthlyRevenue = rev;
        this.commissionRate = rate;
    }
    @Override
    public double calculateBaseSalary(Contract c) {
        if (c == null) return 0;
        // Lương kinh doanh = lương cơ bản hợp đồng + phụ cấp + (doanh thu * % hoa hồng)
        return c.getBaseSalary() + c.getAllowance() + (this.monthlyRevenue * this.commissionRate);
    }
}

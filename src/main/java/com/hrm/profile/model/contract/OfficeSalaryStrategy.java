package com.hrm.profile.model.contract;

import com.hrm.profile.model.Contract;

public class OfficeSalaryStrategy implements ISalaryStrategy{
    @Override
    public double calculateBaseSalary(Contract c) {
        if (c == null) return 0;
        // Lương cơ bản văn phòng = lương cơ bản trong hợp đồng + phụ cấp
        return c.getBaseSalary() + c.getAllowance();
    }
}

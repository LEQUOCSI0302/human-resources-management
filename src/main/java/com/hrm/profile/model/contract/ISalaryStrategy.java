package com.hrm.profile.model.contract;

import com.hrm.profile.model.Contract;

public interface ISalaryStrategy {
    double calculateBaseSalary(Contract c);
}

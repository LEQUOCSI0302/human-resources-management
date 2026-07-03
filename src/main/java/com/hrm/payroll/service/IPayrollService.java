package com.hrm.payroll.service;

import com.hrm.payroll.model.PaySlip;
import com.hrm.profile.model.EmployeeProfile;

import java.util.List;

public interface IPayrollService {
    void approveAndPayroll(List<PaySlip> slips, String dept);
    PaySlip processPayroll(EmployeeProfile emp, String month);
}

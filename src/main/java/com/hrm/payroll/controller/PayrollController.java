package com.hrm.payroll.controller;

import com.hrm.payroll.model.PayrollDetail;
import com.hrm.payroll.repository.PayrollRepository;
import com.hrm.payroll.service.PayrollService_tl;
import com.hrm.profile.model.EmployeeProfile;

import java.util.List;

public class PayrollController {
    private PayrollService_tl service;
    public PayrollController() {
        service = new PayrollService_tl(new PayrollRepository());
    }


    // 5.1.1
    // View -> Controller
    // Yêu cầu tính lương
    public void calculatePayroll(List<EmployeeProfile> employees, String month) {
        service.calculatePayroll(employees, month);
    }


    // 5.1.4 Hiển thị bảng lương tạm tính
    public void previewPayroll() {
        service.previewPayroll();

    }


    // 5.1.5 Lưu bảng lương
    public void savePayroll(String payrollId, String month) {
        service.savePayroll(payrollId, month);

    }


    // Xem tất cả bảng lương
    public void showAllPayroll() {
        service.showAllPayroll();

    }

}
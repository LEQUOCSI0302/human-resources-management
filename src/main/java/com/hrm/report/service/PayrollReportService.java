package com.hrm.report.service;

import com.hrm.payroll.service.PayrollService;

import java.util.ArrayList;
import java.util.List;

public class PayrollReportService implements ReportService{
    private PayrollService payrollService;

    public PayrollReportService(PayrollService ps) {
        this.payrollService = ps;
    }

    @Override
    public List<String> generateReportData(String month) {
        List<String> report = new ArrayList<>();
        report.add("=== BÁO CÁO LƯƠNG THÁNG: " + month + " ===");
        // Logic lấy danh sách PaySlip và định dạng thành danh sách String
        report.add("Tổng chi lương: [Dữ liệu từ PayrollService]");
        return report;
    }
}

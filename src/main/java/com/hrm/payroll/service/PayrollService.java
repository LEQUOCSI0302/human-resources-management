package com.hrm.payroll.service;

import com.hrm.payroll.model.AttendanceSummary;
import com.hrm.payroll.model.Payroll;
import com.hrm.payroll.model.PayrollDetail;
import com.hrm.payroll.repository.PayrollRepository;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class PayrollService {
    // Database
    private PayrollRepository repo;

    // Lưu bảng lương đang tính (chưa lưu DB)
    private List<PayrollDetail> draftPayroll = new ArrayList<>();
    public PayrollService(PayrollRepository repo) {
        this.repo = repo;
    }


     // 5.1.0 -> 5.1.4 TÍNH LƯƠNG (Chỉ tính và xem trước)
    public List<PayrollDetail> calculatePayroll(List<EmployeeProfile> employees, String month) {
        draftPayroll.clear();


         // 5.1.2 Validation
        if (employees == null || employees.isEmpty()) {
            System.out.println("Không có nhân viên để tính lương.");
            return draftPayroll;
        }


         //loop từng nhân viên
        for (EmployeeProfile emp : employees) {
            // ==Employee
            // Demo lương cơ bản
            double basicSalary = 10000000;

            // Demo phụ cấp
            double allowance = 1000000;


            // == AttendanceSummary
            // Demo số ngày công
            AttendanceSummary attendance = new AttendanceSummary(emp.getId(), 26);
            int workingDays = attendance.getWorkingDays();

            // 5.1.3 Tính lương
            double salaryByDay = basicSalary / 26;
            double grossSalary = salaryByDay * workingDays;
            double insurance = grossSalary * 0.08;
            double tax = grossSalary * 0.05;
            double netSalary = grossSalary + allowance - insurance - tax;

            // tạo new PayrollDetail
            PayrollDetail detail = new PayrollDetail(emp.getId(), emp.getName(), basicSalary, workingDays, allowance, insurance, tax, netSalary);
            draftPayroll.add(detail);

        }

//        5.1.4
        System.out.println("--------------------------------------");
        System.out.println("Tính lương hoàn tất.");
        System.out.println("Tổng nhân viên: " + draftPayroll.size());

        return draftPayroll;
    }


     // 5.1.5 Xem bảng lương tạm
    public void previewPayroll() {
        if (draftPayroll.isEmpty()) {
            System.out.println("Chưa có bảng lương.");
            return;
        }

        for (PayrollDetail detail : draftPayroll) {
            System.out.println(detail);
            System.out.println("--------------------------------");
        }

    }


     // 5.1.5 -> 5.1.6 Lưu bảng lương
    public void savePayroll(String payrollId, String month) {
        if (draftPayroll.isEmpty()) {
            System.out.println("Chưa tính lương.");
            return;
        }
        Payroll payroll = new Payroll(payrollId, month);

    //gắn PayrollDetail
        for (PayrollDetail detail : draftPayroll) {
            payroll.addDetail(detail);

        }


    //insert db
        repo.save(payroll);
        System.out.println("Lưu bảng lương thành công.");

    }

//xem all bảng lương
    public void showAllPayroll() {
        List<Payroll> list = repo.findAll();
        if (list.isEmpty()) {
            System.out.println("Chưa có bảng lương.");
            return;
        }
        for (Payroll payroll : list) {
            System.out.println(payroll);
            System.out.println("==============================");
        }
    }
}
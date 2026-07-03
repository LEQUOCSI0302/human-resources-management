package com.hrm.payroll.service;

import com.hrm.attendance.service.AttendanceService;
import com.hrm.discipline.service.DisciplineService;
import com.hrm.payroll.model.PaySlip;
import com.hrm.payroll.model.SalaryComponent;
import com.hrm.profile.model.Contract;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.training.observer.TrainingService;

public abstract class BasePayrollProcessor {
    protected TrainingService trainingService;
    protected DisciplineService disciplineService;
    protected AttendanceService attendanceService;

    public BasePayrollProcessor(TrainingService ts, DisciplineService ds, AttendanceService as) {
        this.trainingService = ts;
        this.disciplineService = ds;
        this.attendanceService = as;
    }

    // Khung quy trình tính toán lương chuẩn (Template Method)
    public PaySlip processPayroll(EmployeeProfile emp, String month) {
        String slipId = "SLIP_" + emp.getId() + "_" + month;
        PaySlip slip = new PaySlip(slipId, emp, month);

        double baseSalary = 0.0;
        double allowance = 0.0;

        // Lấy thông tin từ hợp đồng đang kích hoạt (Active) của nhân viên
        if (emp.getContracts() != null && !emp.getContracts().isEmpty()) {
            Contract activeContract = emp.getContracts().stream()
                    .filter(c -> "Active".equalsIgnoreCase(c.getStatus()))
                    .findFirst()
                    .orElse(emp.getContracts().get(0));

            allowance = activeContract.getAllowance();
            // Áp dụng Pattern Strategy để tính toán lương cơ bản linh hoạt
            if (emp.getSalaryStrategy() != null) {
                baseSalary = emp.getSalaryStrategy().calculateBaseSalary(activeContract);
            } else {
                baseSalary = activeContract.getBaseSalary();
            }
        }

        // Thêm các thành phần cộng lương
        slip.addComponent(new SalaryComponent("Lương cơ bản", baseSalary, false));
        if (allowance > 0) slip.addComponent(new SalaryComponent("Phụ cấp hợp đồng", allowance, false));

        // Gọi Hook tính thưởng KPI (Được triển khai ở PayrollService)
        double bonus = calculateBonus(emp, month);
        if (bonus > 0) slip.addComponent(new SalaryComponent("Thưởng KPI hiệu suất", bonus, false));

        // Lấy các khoản phạt khấu trừ từ dịch vụ kỷ luật (DisciplineService)
        double penalties = disciplineService.getPenaltyDeductions(emp, month);
        if (penalties > 0) slip.addComponent(new SalaryComponent("Khấu trừ vi phạm kỷ luật", penalties, true));

        // Gọi Hook tính thuế thu nhập cá nhân (Được triển khai ở PayrollService)
        double incomeBeforeTax = baseSalary + allowance + bonus - penalties;
        double tax = deductTaxes(incomeBeforeTax > 0 ? incomeBeforeTax : 0);
        if (tax > 0) slip.addComponent(new SalaryComponent("Thuế thu nhập cá nhân (PIT)", tax, true));

        // Kích hoạt tính tổng lương thực lĩnh cuối cùng
        slip.calculateNetSalary();
        return slip;
    }

    // Các bước sơ khai (Primitive Operations) để lớp con hoàn thiện logic
    protected abstract double calculateBonus(EmployeeProfile emp, String month);
    protected abstract double deductTaxes(double totalIncomeBeforeTax);
}

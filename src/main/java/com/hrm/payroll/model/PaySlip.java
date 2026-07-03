package com.hrm.payroll.model;

import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class PaySlip {
    private String slipId;
    private EmployeeProfile employee;
    private String month;
    private double netSalary; // Đây sẽ là kết quả cuối cùng
    private List<SalaryComponent> components; // Danh sách phụ cấp & khấu trừ

    public PaySlip(String id, EmployeeProfile emp, String m) {
        this.slipId = id;
        this.employee = emp;
        this.month = m;
        // Khởi tạo danh sách components để tránh NullPointerException
        this.components = new java.util.ArrayList<>();
    }

    public void addComponent(SalaryComponent comp) {
        this.components.add(comp);
    }

    /**
     * Logic xử lý tính lương thực nhận:
     * Lương thực nhận = Lương cơ bản (từ hợp đồng) + Tổng phụ cấp - Tổng khấu trừ
     */
    public double calculateNetSalary() {
        // 1. Lấy lương cơ bản từ hợp đồng của nhân viên
        // (Giả định lấy hợp đồng đang active ở vị trí đầu tiên hoặc hợp đồng mới nhất)
        double baseSalary = 0;
        if (employee.getContracts() != null && !employee.getContracts().isEmpty()) {
            baseSalary = employee.getContracts().get(0).getBaseSalary();
        }

        double totalAllowance = 0;
        double totalDeduction = 0;

        // 2. Duyệt qua danh sách các thành phần lương để cộng/trừ
        for (SalaryComponent comp : components) {
            if (comp.isDeduction()) {
                totalDeduction += comp.getAmount();
            } else {
                totalAllowance += comp.getAmount();
            }
        }

        // 3. Tính toán công thức cuối cùng
        this.netSalary = baseSalary + totalAllowance - totalDeduction;

        return this.netSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public String getSlipId() {
        return slipId;
    }

    public EmployeeProfile getEmployee() {
        return employee;
    }

    public String getMonth() {
        return month;
    }

    public List<SalaryComponent> getComponents() {
        return components;
    }
}

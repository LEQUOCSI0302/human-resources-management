package com.hrm.discipline.service;

import com.hrm.discipline.model.Violation;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class DisciplineService {
    private List<Violation> violationRepo = new ArrayList<>();

    public void recordViolation(Violation v) {
        violationRepo.add(v);
    }

    // Phương thức này cực kỳ quan trọng cho Module Payroll
    public double getPenaltyDeductions(EmployeeProfile emp, String month) {
        double totalPenalty = 0;
        for (Violation v : violationRepo) {
            // Kiểm tra vi phạm của nhân viên trong tháng cần tính lương
            if (v.getEmployee().getId().equals(emp.getId())) {
                totalPenalty += v.getPenaltyAmount();
            }
        }
        return totalPenalty;
    }
}

package com.hrm.training.observer;

import com.hrm.profile.model.EmployeeProfile;

public class HrNotificationObserver implements KpiObserver{
    @Override
    public void onKpiChanged(EmployeeProfile emp, String month, double oldScore, double newScore) {
        System.out.println("\n--- [THÔNG BÁO HỆ THỐNG HR] ---");
        System.out.println("Điểm KPI của nhân viên: " + emp.getName() + " (ID: " + emp.getId() + ")");
        System.out.println("Kỳ đánh giá: Tháng " + month);
        System.out.println("Thay đổi: Từ " + oldScore + " -> Thành " + newScore);
        System.out.println("--------------------------------\n");
    }
}

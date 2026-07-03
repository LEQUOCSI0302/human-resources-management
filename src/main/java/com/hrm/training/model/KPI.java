package com.hrm.training.model;

import com.hrm.profile.model.EmployeeProfile;

public class KPI {
    private EmployeeProfile employee;
    private String month;
    private double score;           // Điểm số hiện tại/mặc định
    private double selfScore;       // Nhân viên tự chấm
    private double validatedScore;  // Quản lý chốt điểm
    private boolean isValidated;    // Trạng thái đã duyệt chưa

    public KPI(EmployeeProfile emp, String month, double score) {
        this.employee = emp;
        this.month = month;
        this.score = score;
        this.selfScore = 0.0;
        this.validatedScore = 0.0;
        this.isValidated = false;
    }

    public double getKpiScore() {
        // Nếu đã được duyệt thì lấy điểm chính thức từ quản lý, ngược lại lấy điểm hiện tại
        return isValidated ? validatedScore : score;
    }

    public EmployeeProfile getEmployee() {
        return employee;
    }

    public String getMonth() {
        return month;
    }

    // Nhân viên tự đánh giá
    public void selfEvaluate(double score) {
        this.selfScore = score;
        this.score = score; // Tạm thời cập nhật điểm bằng điểm tự đánh giá
        System.out.println("Nhân viên " + employee.getName() + " tự đánh giá KPI tháng " + month + ": " + score);
    }

    // Quản lý phê duyệt và chốt điểm số cuối cùng
    public void validate(double score) {
        this.validatedScore = score;
        this.isValidated = true;
        this.score = score; // Điểm chốt cuối cùng dùng để tính lương/thưởng
        System.out.println("Quản lý đã phê duyệt KPI tháng " + month + " cho " + employee.getName() + ": " + score);
    }
}

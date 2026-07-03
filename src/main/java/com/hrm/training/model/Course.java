package com.hrm.training.model;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private double budgetCost;
    private String status; // Ví dụ: "DRAFT", "OPEN", "COMPLETED"

    // Constructor đúng theo signature trong sơ đồ lớp
    public Course(String id, String name, String start, String end, double cost) {
        this.courseId = id;
        this.name = name;
        this.description = ""; // Mặc định để trống, có thể cập nhật sau
        this.startDate = start;
        this.endDate = end;
        this.budgetCost = cost;
        this.status = "DRAFT"; // Trạng thái ban đầu khi khởi tạo
    }

    // Mở lớp học/khóa học cho nhân viên đăng ký
    public void open() {
        this.status = "OPEN";
        System.out.println("Khóa học '" + name + "' (ID: " + courseId + ") đã chính thức mở đăng ký.");
    }

    // Kết thúc khóa học sau khi đào tạo xong
    public void complete() {
        this.status = "COMPLETED";
        System.out.println("Khóa học '" + name + "' (ID: " + courseId + ") đã kết thúc giảng dạy.");
    }

    // Kiểm tra xem khóa học đã hoàn thành hay chưa
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(this.status);
    }

    // Các Getter/Setter bổ trợ cần thiết để các class khác kết nối dữ liệu
    public String getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public double getBudgetCost() {
        return budgetCost;
    }

    public String getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

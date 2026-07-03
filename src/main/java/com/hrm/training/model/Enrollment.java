package com.hrm.training.model;

import com.hrm.profile.model.EmployeeProfile;

public class Enrollment {
    private String enrollmentId;
    private EmployeeProfile employee;
    private Course course;
    private double score;
    private boolean isCertified;
    private String status; // Ví dụ: "ENROLLED", "PASSED", "FAILED"

    // Constructor đúng theo signature trong sơ đồ lớp
    public Enrollment(String id, EmployeeProfile emp, Course c) {
        this.enrollmentId = id;
        this.employee = emp;
        this.course = c;
        this.score = 0.0;          // Mặc định ban đầu chưa có điểm
        this.isCertified = false;  // Chưa được cấp chứng chỉ
        this.status = "ENROLLED";  // Trạng thái mới đăng ký học
    }

    // Nhập điểm thi/đánh giá cuối khóa cho nhân viên
    public void inputScore(double score) {
        this.score = score;
        System.out.println("Đã cập nhật điểm cho học viên " + employee.getName()
                + " tại khóa [" + course.getName() + "]: " + score + " điểm.");
    }

    // Xét duyệt cấp chứng chỉ dựa trên điểm số và điểm chuẩn (threshold) đầu ra
    public void issueCertificateIfPassed(double threshold) {
        if (this.score >= threshold) {
            this.isCertified = true;
            this.status = "PASSED";
            System.out.println("Hệ thống cấp chứng chỉ: Nhân viên " + employee.getName()
                    + " đã VƯỢT QUA khóa học " + course.getName() + ".");
        } else {
            this.isCertified = false;
            this.status = "FAILED";
            System.out.println("Hệ thống thông báo: Nhân viên " + employee.getName()
                    + " KHÔNG ĐẠT chuẩn đầu ra khóa học " + course.getName() + ".");
        }
    }

    // Các Getter bổ trợ cần thiết để TrainingService hoặc TrainingMenu truy vấn
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public EmployeeProfile getEmployee() {
        return employee;
    }

    public Course getCourse() {
        return course;
    }

    public double getScore() {
        return score;
    }

    public boolean isCertified() {
        return isCertified;
    }

    public String getStatus() {
        return status;
    }
}

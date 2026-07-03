package com.hrm.console;

import com.hrm.profile.model.EmployeeProfile;
import com.hrm.training.model.Course;
import com.hrm.training.model.Enrollment;
import com.hrm.training.model.KPI;
import com.hrm.training.model.TrainingPlan;
import com.hrm.training.observer.TrainingService;

import java.util.List;
import java.util.Scanner;

public class TrainingMenu {
    private TrainingService trainingService;
    private List<EmployeeProfile> employees;
    private Scanner scanner;

    // Constructor đúng theo sơ đồ lớp
    public TrainingMenu(TrainingService service, List<EmployeeProfile> employees) {
        this.trainingService = service;
        this.employees = employees;
        this.scanner = new Scanner(System.in);
    }

    // Hiển thị menu và điều hướng lựa chọn
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ ĐÀO TẠO & KPI =====");
            System.out.println("1. Nhân viên tự đánh giá KPI");
            System.out.println("2. Quản lý phê duyệt (Validate) KPI");
            System.out.println("3. Cập nhật KPI hệ thống (Update KPI)");
            System.out.println("4. Xem điểm KPI nhân viên");
            System.out.println("5. Thêm kế hoạch đào tạo mới");
            System.out.println("6. Mở một khóa học mới");
            System.out.println("7. Đăng ký nhân viên vào khóa học");
            System.out.println("8. Nhập điểm kết thúc khóa học");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng (0-8): ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: handleSelfEvaluateKpi(); break;
                    case 2: handleValidateKpi(); break;
                    case 3: handleUpdateKpi(); break;
                    case 4: handleViewKpi(); break;
                    case 5: handleAddTrainingPlan(); break;
                    case 6: handleOpenCourse(); break;
                    case 7: handleEnroll(); break;
                    case 8: handleInputScore(); break;
                    case 0: System.out.println("Đang quay lại Main Menu..."); break;
                    default: System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng chỉ nhập số từ 0 đến 8!");
                choice = -1;
            }
        } while (choice != 0);
    }

    // Hàm hỗ trợ tìm kiếm nhân viên theo ID (đã có trong sơ đồ lớp)
    public EmployeeProfile findEmployee() {
        System.out.print("Nhập mã số nhân viên (ID): ");
        String id = scanner.nextLine();
        for (EmployeeProfile emp : employees) {
            if (emp.getId().equalsIgnoreCase(id)) {
                return emp;
            }
        }
        System.out.println("Không tìm thấy nhân viên có ID: " + id);
        return null;
    }

    // 1. Xử lý Nhân viên tự chấm điểm KPI
    public void handleSelfEvaluateKpi() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập tháng đánh giá (Ví dụ: 07/2026): ");
        String month = scanner.nextLine();
        System.out.print("Nhập số điểm tự đánh giá (0-10): ");
        double score = Double.parseDouble(scanner.nextLine());

        // Mô phỏng tạo ra thực thể KPI và gọi tự chấm điểm
        KPI kpi = new KPI(emp, month, 0);
        kpi.selfEvaluate(score);
    }

    // 2. Xử lý Quản lý duyệt/chốt điểm KPI
    public void handleValidateKpi() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập tháng phê duyệt (Ví dụ: 07/2026): ");
        String month = scanner.nextLine();
        System.out.print("Nhập điểm chốt cuối cùng: ");
        double score = Double.parseDouble(scanner.nextLine());

        // Gọi đồng bộ qua TrainingService để vừa chốt vừa kích hoạt bắn thông báo (Observer)
        trainingService.updateKPI(emp, month, score);
    }

    // 3. Xử lý Cập nhật KPI trực tiếp từ hệ thống
    public void handleUpdateKpi() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập tháng cập nhật: ");
        String month = scanner.nextLine();
        System.out.print("Nhập điểm KPI mới: ");
        double score = Double.parseDouble(scanner.nextLine());

        trainingService.updateKPI(emp, month, score);
    }

    // 4. Xử lý Xem điểm KPI của nhân viên
    public void handleViewKpi() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập tháng muốn xem: ");
        String month = scanner.nextLine();

        double score = trainingService.getKpiScore(emp, month);
        System.out.println("--> Điểm KPI tháng " + month + " của " + emp.getName() + " là: " + score);
    }

    // 5. Xử lý Thêm kế hoạch đào tạo
    public void handleAddTrainingPlan() {
        System.out.print("Nhập mã kế hoạch (Plan ID): ");
        String id = scanner.nextLine();
        System.out.print("Nhập chủ đề đào tạo (Topics): ");
        String topics = scanner.nextLine();
        System.out.print("Phòng ban mục tiêu (Target Department): ");
        String dept = scanner.nextLine();
        System.out.print("Dự toán chi phí (Budget Cost): ");
        double cost = Double.parseDouble(scanner.nextLine());

        TrainingPlan plan = new TrainingPlan(id, topics, dept, cost);
        trainingService.addTrainingPlan(plan);
    }

    // 6. Xử lý Mở khóa học mới thuộc một kế hoạch nào đó
    public void handleOpenCourse() {
        System.out.print("Nhập mã khóa học (Course ID): ");
        String id = scanner.nextLine();
        System.out.print("Nhập tên khóa học: ");
        String name = scanner.nextLine();
        System.out.print("Ngày bắt đầu (dd/mm/yyyy): ");
        String start = scanner.nextLine();
        System.out.print("Ngày kết thúc (dd/mm/yyyy): ");
        String end = scanner.nextLine();
        System.out.print("Chi phí khóa học: ");
        double cost = Double.parseDouble(scanner.nextLine());

        Course course = new Course(id, name, start, end, cost);
        course.open(); // Chuyển trạng thái khóa học sang OPEN
    }

    // 7. Xử lý Đăng ký khóa học cho một nhân viên cụ thể
    public void handleEnroll() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập mã khóa học muốn đăng ký: ");
        String courseId = scanner.nextLine();
        System.out.print("Nhập tên khóa học đó: ");
        String courseName = scanner.nextLine();

        // Giả lập lấy thực thể Course sang để tạo Enrollment
        Course course = new Course(courseId, courseName, "N/A", "N/A", 0);

        String enrollmentId = "ENROL_" + emp.getId() + "_" + courseId;
        Enrollment enrollment = new Enrollment(enrollmentId, emp, course);
        System.out.println("--> Đăng ký thành công! Mã lượt học: " + enrollment.getEnrollmentId());
    }

    // 8. Xử lý Nhập điểm thi và xét tốt nghiệp khóa học cho nhân viên
    public void handleInputScore() {
        EmployeeProfile emp = findEmployee();
        if (emp == null) return;

        System.out.print("Nhập mã khóa học: ");
        String courseId = scanner.nextLine();
        System.out.print("Nhập tên khóa học: ");
        String courseName = scanner.nextLine();

        Course course = new Course(courseId, courseName, "N/A", "N/A", 0);
        String enrollmentId = "ENROL_" + emp.getId() + "_" + courseId;

        Enrollment enrollment = new Enrollment(enrollmentId, emp, course);

        System.out.print("Nhập điểm số cuối khóa đạt được: ");
        double score = Double.parseDouble(scanner.nextLine());

        enrollment.inputScore(score);

        // Xét điểm chuẩn là 5.0 để cấp chứng chỉ
        enrollment.issueCertificateIfPassed(5.0);
    }
}

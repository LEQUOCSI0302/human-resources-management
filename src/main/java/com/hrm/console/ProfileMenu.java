package com.hrm.console;

import com.hrm.profile.model.Contract;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.hrm.profile.model.PartTimeEmployee;

import java.util.List;
import java.util.Scanner;

public class ProfileMenu {
    private List<EmployeeProfile> employees;
    private Scanner scanner;

    public ProfileMenu(List<EmployeeProfile> employees) {
        this.employees = employees;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== PROFILE MENU (QUẢN LÝ HỒ SƠ) ===");
            System.out.println("1. Xem toàn bộ danh sách nhân viên (handleListAll)");
            System.out.println("2. Xem chi tiết hồ sơ nhân viên (handleViewDetail)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                handleListAll();
            } else if (choice == 2) {
                handleViewDetail();
            } else if (choice == 0) {
                break;
            }
        }
    }

    public void handleListAll() {
        System.out.println("\n--- DANH SÁCH NHÂN VIÊN ---");
        if (employees.isEmpty()) {
            System.out.println("(Trống)");
            return;
        }
        for (EmployeeProfile emp : employees) {
            String type = (emp instanceof FullTimeEmployee) ? "Full-Time" : "Part-Time";
            System.out.printf("ID: %s | Tên: %s | Phòng: %s | Chức vụ: %s | Loại: %s\n",
                    emp.getId(), emp.getName(), emp.getDepartment(), emp.getDepartment(), type);
        }
    }

    public void handleViewDetail() {
        System.out.print("Nhập mã nhân viên cần xem: ");
        String id = scanner.nextLine();
        EmployeeProfile found = null;
        for (EmployeeProfile emp : employees) {
            if (emp.getId().equalsIgnoreCase(id)) {
                found = emp;
                break;
            }
        }

        if (found == null) {
            System.out.println("❌ Không tìm thấy nhân viên!");
            return;
        }

        System.out.println("\n--- CHI TIẾT HỒ SƠ ---");
        System.out.println("Mã NV: " + found.getId());
        System.out.println("Họ tên: " + found.getName());
        System.out.println("Phòng ban: " + found.getDepartment());
        if (found instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) found;
            System.out.println("Loại: Full-Time");
            System.out.println("Mã số bảo hiểm: " + ft.getInsuranceId());
        } else if (found instanceof PartTimeEmployee) {
            PartTimeEmployee pt = (PartTimeEmployee) found;
            System.out.println("Loại: Part-Time");
            System.out.println("Mức lương/giờ: " + pt.getHourlyRate());
            System.out.println("Số giờ đã làm: " + pt.getTotalHoursWorked());
        }

        System.out.println("Danh sách hợp đồng: " + found.getContracts().size() + " hợp đồng.");
        for (Contract c : found.getContracts()) {
            System.out.printf("  -> Lương gốc: %.2f | Phụ cấp: %.2f | Trạng thái: %s\n",
                    c.getBaseSalary(), c.getAllowance(), c.getStatus());
        }
    }
}

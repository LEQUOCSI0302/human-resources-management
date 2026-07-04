package com.hrm.console;

import com.hrm.attendance.controller.AttendanceController;
import com.hrm.attendance.model.Shift;
import com.hrm.profile.model.EmployeeProfile;

import java.util.List;
import java.util.Scanner;

public class AttendanceMenu {

    private Scanner scanner;
    private AttendanceController controller;
    private List<EmployeeProfile> employees;

    public AttendanceMenu(List<EmployeeProfile> employees) {
        this.scanner = new Scanner(System.in);
        this.controller = new AttendanceController();
        this.employees = employees;
    }

    public void displayMenu() {
        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("        QUẢN LÝ CHẤM CÔNG");
            System.out.println("====================================");
            System.out.println("1. Chấm công");
            System.out.println("2. Hướng dẫn");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    attendance();
                    break;
                case 2:
                    showGuide();
                    break;
                case 0:
                    System.out.println("Quay lại Menu chính...");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");

            }

        } while (choice != 0);
    }


    private void attendance() {

        System.out.println("\n========== CHẤM CÔNG ==========");

        // 3.1.1 Nhập mã nhân viên
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();

        EmployeeProfile employee = null;

        // Tìm nhân viên trong danh sách
        for (EmployeeProfile e : employees) {
            if (e.getId().equalsIgnoreCase(id)) {
                employee = e;
                break;

            }
        }

        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên.");
            return;
        }

        // Demo ca làm
        Shift shift = new Shift("S01", "Ca sáng", "08:00:00", "17:00:00", 8);

        System.out.println("-----------------------------");
        System.out.println("Nhân viên : " + employee.getName());
        System.out.println("Phòng ban : " + employee.getDepartment());
        System.out.println("Ca làm    : " + shift.getShiftName());
        System.out.println("-----------------------------");

        // check vị trí
        System.out.print("Bạn đang ở đúng vị trí? (true/false): ");
        boolean location = Boolean.parseBoolean(scanner.nextLine());

        // 3.1.1
        controller.attendance(employee, shift, location);
        System.out.println("--------------------------------");
        System.out.println("Hoàn thành xử lý chấm công.");
    }

    private void showGuide() {
        System.out.println("\n=========== HƯỚNG DẪN ===========");
        System.out.println("1. Nhập đúng mã nhân viên.");
        System.out.println("2. Đứng đúng vị trí chấm công.");
        System.out.println("3. Lần đầu sẽ CHECK IN.");
        System.out.println("4. Lần thứ hai sẽ CHECK OUT.");
        System.out.println("5. Sau 08:00 sẽ báo Đi muộn.");
        System.out.println("6. Trước 17:00 sẽ báo Về sớm.");
    }

}
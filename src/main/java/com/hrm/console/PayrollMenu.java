package com.hrm.console;

import com.hrm.payroll.controller.PayrollController;
import com.hrm.profile.model.EmployeeProfile;

import java.util.List;
import java.util.Scanner;

public class PayrollMenu {
    private Scanner scanner;
    private PayrollController controller;
    private List<EmployeeProfile> employees;

    public PayrollMenu(List<EmployeeProfile> employees) {
        scanner = new Scanner(System.in);
        controller = new PayrollController();
        this.employees = employees;

    }

    public void displayMenu() {
        int choice;

        do {

            System.out.println("\n====================================");
            System.out.println("        QUẢN LÝ TIỀN LƯƠNG");
            System.out.println("====================================");
            System.out.println("1. Tính lương");
            System.out.println("2. Xem bảng lương tạm");
            System.out.println("3. Lưu bảng lương");
            System.out.println("4. Xem bảng lương đã lưu");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    calculatePayroll();
                    break;
                case 2:
                    controller.previewPayroll();
                    break;
                case 3:
                    savePayroll();
                    break;
                case 4:
                    controller.showAllPayroll();
                    break;
                case 0:
                    System.out.println("Quay lại Menu chính...");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }

        } while (choice != 0);

    }



    // 5.1.0 Người dùng nhập kỳ lương
    private void calculatePayroll() {
        System.out.println("\n========== TÍNH LƯƠNG ==========");
        System.out.print("Nhập kỳ lương (VD: 2026-07): ");
        String month = scanner.nextLine();
        controller.calculatePayroll(employees, month);

    }


    // 5.1.5 Lưu bảng lương
    private void savePayroll() {
        System.out.println("\n========== LƯU BẢNG LƯƠNG ==========");
        System.out.print("Mã bảng lương: ");
        String payrollId = scanner.nextLine();
        System.out.print("Kỳ lương: ");
        String month = scanner.nextLine();
        controller.savePayroll(payrollId, month);

    }

}
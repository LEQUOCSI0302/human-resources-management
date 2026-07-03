package com.hrm.console;

import com.hrm.report.factory.ReportFactory;
import com.hrm.report.service.ReportService;

import java.util.List;
import java.util.Scanner;

public class ReportMenu {
    private ReportFactory reportFactory;
    private Scanner scanner = new Scanner(System.in);

    public ReportMenu(ReportFactory factory) {
        this.reportFactory = factory;
    }

    public void displayMenu() {
        System.out.println("\n=== MENU BÁO CÁO ===");
        System.out.println("1. In Báo cáo Lương");
        System.out.println("2. In Báo cáo Chấm công");
        System.out.println("0. Quay lại");
        System.out.print("Chọn: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) handlePrintPayrollReport();
    }

    public void handlePrintPayrollReport() {
        System.out.print("Nhập tháng (MM-YYYY): ");
        String month = scanner.nextLine();
        ReportService service = reportFactory.getReportService("PAYROLL");
        List<String> data = service.generateReportData(month);

        System.out.println("\n--- DỮ LIỆU BÁO CÁO ---");
        for (String line : data) System.out.println(line);
    }
}

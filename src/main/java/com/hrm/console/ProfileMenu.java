package com.hrm.console;

import com.hrm.profile.model.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
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
            System.out.println("\n=== PROFILE MENU (QUẢN LÝ NHÂN VIÊN) ===");
            System.out.println("1. Xem toàn bộ danh sách nhân viên (handleListAll)");
            System.out.println("2. Xem chi tiết hồ sơ nhân viên (handleViewDetail)");
            System.out.println("3. Tạo hồ sơ nhân viên ");
            System.out.println("4. Cập nhật hồ sơ nhân viên");
            System.out.println("5. Xóa hồ sơ nhân viên");
            System.out.println("6. Thêm bằng cấp/chứng chỉ");
            System.out.println("7. Sửa bằng cấp/chứng chỉ");
            System.out.println("8. Xóa bằng cấp/chứng chỉ");
            System.out.println("9. Xem bằng cấp/chứng chỉ");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                handleListAll();
            } else if (choice == 2) {
                handleViewDetail();
            }else if(choice == 3){
                createEmployee();
            }else if(choice == 4){
                updateEmployee();
            }else if(choice == 5){
                deleteEmployee();
            }else if(choice == 6){
                addCertificate();
            }else if(choice == 7){
                updateCertificate();
            }else if(choice == 8){
                deleteCertificate();
            }else if(choice == 9){
                viewCertificates();
            }else if(choice == 0){
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

    public void addCertificate() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if (emp == null) {
            System.out.println("Không tìm thấy nhân viên");
            return;
        }
        System.out.print("Tên bằng cấp/chứng chỉ: ");
        String ten = scanner.nextLine();
        System.out.print("Ngày cấp (yyyy-MM-dd): ");
        LocalDate ngayCap = LocalDate.parse(scanner.nextLine());
        System.out.print("Ngày hết hạn (yyyy-MM-dd): ");
        LocalDate ngayHetHan = LocalDate.parse(scanner.nextLine());
        System.out.print("File đính kèm: ");
        String file = scanner.nextLine();
        int certificateId = emp.getCertificates().size() + 1;
        Certificate certificate = new Certificate(certificateId, ten,ngayCap,ngayHetHan, file);
        emp.addCertificate(certificate);
        System.out.println("Thêm bằng cấp thành công!");
    }
    private EmployeeProfile findEmployee(String id) {
        for (EmployeeProfile emp : employees) {
            if (emp.getId().equalsIgnoreCase(id)) {
                return emp;
            }
        }
        return null;
    }
    public void viewCertificates() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if (emp == null) {
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        if (emp.getCertificates().isEmpty()) {
            System.out.println("Nhân viên chưa có bằng cấp/chứng chỉ.");
            return;
        }
        for (Certificate c : emp.getCertificates()) {
            System.out.println(c.displayCertificate());
        }
    }
    public void updateCertificate() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if(emp == null){
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        if(emp.getCertificates().isEmpty()){
            System.out.println("Nhân viên chưa có bằng cấp!");
            return;
        }
        System.out.println("\n===== DANH SÁCH BẰNG CẤP =====");
        for(Certificate c : emp.getCertificates()){
            System.out.println(c.displayCertificate());
        }
        System.out.print("Nhập ID bằng cấp cần sửa: ");
        int certId = Integer.parseInt(scanner.nextLine());
        Certificate certificate = null;
        for(Certificate c : emp.getCertificates()){
            if(c.getId() == certId){
                certificate = c;
                break;
            }
        }
        if(certificate == null){
            System.out.println("Không tìm thấy bằng cấp!");
            return;
        }
        System.out.print("Tên mới: ");
        certificate.setTenBangCap(scanner.nextLine());
        System.out.print("Ngày cấp mới (yyyy-M-d): ");
        certificate.setNgayCap(
                java.time.LocalDate.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-M-d")
                )
        );
        System.out.print("Ngày hết hạn mới (yyyy-M-d): ");
        certificate.setNgayHetHan(
                java.time.LocalDate.parse(
                        scanner.nextLine(),
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-M-d")
                )
        );
        System.out.print("File mới: ");
        certificate.setFileDinhKem(scanner.nextLine());
        System.out.println("Cập nhật thành công!");
    }
    public void deleteCertificate() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if(emp == null){
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        if(emp.getCertificates().isEmpty()){
            System.out.println("Nhân viên chưa có bằng cấp!");
            return;
        }
        System.out.println("\n===== DANH SÁCH BẰNG CẤP =====");
        for(Certificate c : emp.getCertificates()){
            System.out.println(c.displayCertificate());
        }
        System.out.print("Nhập ID bằng cấp cần xóa: ");
        int certId = Integer.parseInt(scanner.nextLine());
        boolean removed = emp.getCertificates().removeIf(c -> c.getId() == certId);
        if(removed){
            System.out.println("Xóa thành công!");
        }else{
            System.out.println("Không tìm thấy bằng cấp!");
        }
    }
    public void createEmployee() {
        System.out.print("Mã nhân viên: ");
        String id = scanner.nextLine();
        if(findEmployee(id) != null){
            System.out.println("Mã nhân viên đã tồn tại!");
            return;
        }
        System.out.print("Họ tên: ");
        String name = scanner.nextLine();
        System.out.print("Phòng ban: ");
        String department = scanner.nextLine();
        System.out.print("Chức vụ: ");
        String position = scanner.nextLine();
        System.out.println("Loại nhân viên");
        System.out.println("1. Full Time");
        System.out.println("2. Part Time");
        int type = Integer.parseInt(scanner.nextLine());
        EmployeeProfile emp;
        if(type == 1){
            System.out.print("Mã bảo hiểm: ");
            String insurance = scanner.nextLine();
            emp = new FullTimeEmployee(
                    id,
                    name,
                    department,
                    position,
                    insurance
            );
        }else{
            System.out.print("Lương/giờ: ");
            double rate = Double.parseDouble(scanner.nextLine());
            emp = new PartTimeEmployee(
                    id,
                    name,
                    department,
                    position,
                    rate
            );
        }
        employees.add(emp);
        System.out.println("Tạo hồ sơ thành công!");
    }
    public void updateEmployee() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if(emp == null){
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        System.out.print("Họ tên mới: ");
        emp.setName(scanner.nextLine());
        System.out.print("Phòng ban mới: ");
        emp.setDepartment(scanner.nextLine());
        System.out.print("Chức vụ mới: ");
        emp.setPosition(scanner.nextLine());
        System.out.println("Cập nhật thành công!");
    }
    public void deleteEmployee() {
        System.out.print("Nhập mã nhân viên: ");
        String id = scanner.nextLine();
        EmployeeProfile emp = findEmployee(id);
        if(emp == null){
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        employees.remove(emp);
        System.out.println("Xóa hồ sơ thành công!");
    }
}

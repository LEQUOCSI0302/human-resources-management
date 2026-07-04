package com.hrm.console;

import com.hrm.authorization.model.LichSuPhanQuyen;
import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.model.Quyen;
import com.hrm.authorization.model.VaiTro;
import com.hrm.authorization.service.PhanQuyenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menu console cho module Phân quyền (UC-PQ-01).
 * actorId = id của Admin đang đăng nhập (giả lập, không làm lại UC đăng nhập).
 */
public class PhanQuyenMenu {
    private final PhanQuyenService service;
    private final Scanner scanner;
    private final int actorId;

    public PhanQuyenMenu(PhanQuyenService service, Scanner scanner, int actorId) {
        this.service = service;
        this.scanner = scanner;
        this.actorId = actorId;
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n=== MENU PHÂN QUYỀN (RBAC) ===");
            System.out.println("1. Danh sách vai trò");
            System.out.println("2. Tạo vai trò mới");
            System.out.println("3. Sửa vai trò");
            System.out.println("4. Xóa vai trò");
            System.out.println("5. Gán quyền cho vai trò");
            System.out.println("6. Thu hồi 1 quyền khỏi vai trò");
            System.out.println("7. Thu hồi tất cả quyền của vai trò");
            System.out.println("8. Danh sách quyền (catalog)");
            System.out.println("9. Danh sách người dùng");
            System.out.println("10. Gán user vào role");
            System.out.println("11. Gỡ user khỏi role");
            System.out.println("12. Kiểm tra quyền truy cập của 1 user");
            System.out.println("13. Xem lịch sử (audit log)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = readInt();

            try {
                switch (choice) {
                    case 1 -> inDanhSachVaiTro();
                    case 2 -> taoVaiTro();
                    case 3 -> suaVaiTro();
                    case 4 -> xoaVaiTro();
                    case 5 -> ganQuyen();
                    case 6 -> thuHoiMotQuyen();
                    case 7 -> thuHoiTatCaQuyen();
                    case 8 -> inDanhSachQuyen();
                    case 9 -> inDanhSachNguoiDung();
                    case 10 -> ganUserVaoRole();
                    case 11 -> goUserKhoiRole();
                    case 12 -> kiemTraQuyen();
                    case 13 -> inLichSu();
                    case 0 -> System.out.println("Quay lại menu chính...");
                    default -> System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (IllegalArgumentException | IllegalStateException | SecurityException ex) {
                // E1, E2, E3 ... : hiển thị lỗi nghiệp vụ, không crash chương trình
                System.out.println("❌ Lỗi: " + ex.getMessage());
            }
        } while (choice != 0);
    }

    private void inDanhSachVaiTro() {
        System.out.println("\n--- DANH SÁCH VAI TRÒ ---");
        for (VaiTro vt : service.danhSachVaiTro()) {
            System.out.println(vt);
            for (Quyen q : vt.getDanhSachQuyen()) {
                System.out.println("     - " + q);
            }
        }
    }

    private void taoVaiTro() {
        System.out.print("Mã vai trò: ");
        String ma = scanner.nextLine();
        System.out.print("Tên vai trò: ");
        String ten = scanner.nextLine();
        System.out.print("Mô tả: ");
        String moTa = scanner.nextLine();
        VaiTro vt = service.taoVaiTro(ma, ten, moTa, actorId);
        System.out.println("✅ Đã tạo vai trò: " + vt);
    }

    private void suaVaiTro() {
        System.out.print("ID vai trò cần sửa: ");
        int id = readInt();
        System.out.print("Tên vai trò mới: ");
        String ten = scanner.nextLine();
        System.out.print("Mô tả mới: ");
        String moTa = scanner.nextLine();
        VaiTro vt = service.suaVaiTro(id, ten, moTa, actorId);
        System.out.println("✅ Đã cập nhật: " + vt);
    }

    private void xoaVaiTro() {
        System.out.print("ID vai trò cần xóa: ");
        int id = readInt();
        boolean ok = service.xoaVaiTro(id, actorId);
        System.out.println(ok ? "✅ Đã xóa vai trò." : "❌ Không thể xóa.");
    }

    private void ganQuyen() {
        System.out.print("ID vai trò: ");
        int vaiTroId = readInt();
        inDanhSachQuyen();
        System.out.print("Nhập danh sách ID quyền muốn gán (cách nhau bằng dấu phẩy): ");
        String line = scanner.nextLine();
        List<Integer> ids = new ArrayList<>();
        for (String part : line.split(",")) {
            part = part.trim();
            if (!part.isEmpty()) ids.add(Integer.parseInt(part));
        }
        service.ganQuyenChoVaiTro(vaiTroId, ids, actorId);
        System.out.println("✅ Đã cập nhật quyền cho vai trò #" + vaiTroId);
    }

    private void thuHoiMotQuyen() {
        System.out.print("ID vai trò: ");
        int vaiTroId = readInt();
        System.out.print("ID quyền cần thu hồi: ");
        int quyenId = readInt();
        boolean ok = service.thuHoiQuyen(vaiTroId, quyenId, actorId);
        System.out.println(ok ? "✅ Đã thu hồi quyền." : "❌ Vai trò không có quyền này.");
    }

    private void thuHoiTatCaQuyen() {
        System.out.print("ID vai trò: ");
        int vaiTroId = readInt();
        service.thuHoiTatCaQuyen(vaiTroId, actorId);
        System.out.println("✅ Đã thu hồi toàn bộ quyền của vai trò #" + vaiTroId);
    }

    private void inDanhSachQuyen() {
        System.out.println("\n--- DANH SÁCH QUYỀN (CATALOG) ---");
        for (Quyen q : service.danhSachQuyen()) {
            System.out.println(q);
        }
    }

    private void inDanhSachNguoiDung() {
        System.out.println("\n--- DANH SÁCH NGƯỜI DÙNG ---");
        for (NguoiDung nd : service.danhSachNguoiDung()) {
            System.out.println(nd + " | Vai trò: " + nd.getDanhSachVaiTro());
        }
    }

    private void ganUserVaoRole() {
        System.out.print("ID người dùng: ");
        int userId = readInt();
        System.out.print("ID vai trò: ");
        int roleId = readInt();
        boolean ok = service.ganUserVaoRole(userId, roleId, actorId);
        System.out.println(ok ? "✅ Đã gán vai trò cho người dùng." : "ℹ️ Người dùng đã có vai trò này.");
    }

    private void goUserKhoiRole() {
        System.out.print("ID người dùng: ");
        int userId = readInt();
        System.out.print("ID vai trò: ");
        int roleId = readInt();
        boolean ok = service.goUserKhoiRole(userId, roleId, actorId);
        System.out.println(ok ? "✅ Đã gỡ vai trò khỏi người dùng." : "❌ Người dùng không có vai trò này.");
    }

    private void kiemTraQuyen() {
        System.out.print("ID người dùng: ");
        int userId = readInt();
        System.out.print("Mã quyền cần kiểm tra: ");
        String maQuyen = scanner.nextLine();
        boolean coQuyen = service.kiemTraQuyen(userId, maQuyen);
        System.out.println(coQuyen ? "✅ CHO PHÉP truy cập." : "❌ 403 - KHÔNG có quyền truy cập.");
    }

    private void inLichSu() {
        System.out.println("\n--- LỊCH SỬ PHÂN QUYỀN (AUDIT LOG) ---");
        for (LichSuPhanQuyen log : service.xemLichSu()) {
            System.out.println(log);
        }
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số hợp lệ: ");
            }
        }
    }
}
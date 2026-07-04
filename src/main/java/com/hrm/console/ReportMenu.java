package com.hrm.console;

import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.repository.NguoiDungRepository;
import com.hrm.report.factory.BaoCaoServiceFactory;
import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;
import com.hrm.report.service.IBaoCaoService;
import com.hrm.report.service.XuatBaoCaoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * ReportMenu - Menu console cho module Báo cáo (UC-BC-01).
 * Luồng: chọn người dùng hiện tại (mô phỏng đăng nhập RBAC) -> chọn loại báo cáo ->
 * nhập tiêu chí lọc -> hệ thống kiểm tra quyền (qua BaoCaoServiceProxy) -> hiển thị bảng ->
 * tùy chọn xuất file (PDF/EXCEL).
 */
public class ReportMenu {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BaoCaoServiceFactory baoCaoServiceFactory;
    private final NguoiDungRepository nguoiDungRepo;
    private final XuatBaoCaoService xuatBaoCaoService;
    private final Scanner scanner;

    private KetQuaBaoCao ketQuaGanNhat; // lưu kết quả lần xem gần nhất để xuất file

    public ReportMenu(BaoCaoServiceFactory baoCaoServiceFactory,
                      NguoiDungRepository nguoiDungRepo,
                      XuatBaoCaoService xuatBaoCaoService,
                      Scanner scanner) {
        this.baoCaoServiceFactory = baoCaoServiceFactory;
        this.nguoiDungRepo = nguoiDungRepo;
        this.xuatBaoCaoService = xuatBaoCaoService;
        this.scanner = scanner;
    }

    public void displayMenu() {
        NguoiDung actor = chonNguoiDungHienTai();
        if (actor == null) {
            System.out.println("❌ Không có người dùng hợp lệ để thao tác. Quay lại menu chính.");
            return;
        }

        int choice;
        do {
            System.out.println("\n=== MENU BÁO CÁO (đang thao tác với: " + actor.getTenDangNhap()
                    + " - " + actor.getPhongBan() + ") ===");
            System.out.println("1. Xem báo cáo Nhân sự");
            System.out.println("2. Xem báo cáo Lương");
            System.out.println("3. Xem báo cáo Chấm công");
            System.out.println("4. Xuất file báo cáo vừa xem (PDF/EXCEL)");
            System.out.println("5. Đổi người dùng đang thao tác");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            choice = readInt();

            try {
                switch (choice) {
                    case 1 -> xemBaoCao(LoaiBaoCao.NHAN_SU, actor);
                    case 2 -> xemBaoCao(LoaiBaoCao.LUONG, actor);
                    case 3 -> xemBaoCao(LoaiBaoCao.CHAM_CONG, actor);
                    case 4 -> xuatFile();
                    case 5 -> {
                        NguoiDung moi = chonNguoiDungHienTai();
                        if (moi != null) actor = moi;
                    }
                    case 0 -> System.out.println("Quay lại menu chính...");
                    default -> System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (SecurityException ex) {
                // E1 - không có quyền truy cập (403)
                System.out.println("⛔ " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                // E2 - tiêu chí lọc không hợp lệ
                System.out.println("❌ Lỗi: " + ex.getMessage());
            }
        } while (choice != 0);
    }

    private void xemBaoCao(LoaiBaoCao loai, NguoiDung actor) {
        TieuChiLoc tieuChi = nhapTieuChiLoc(loai);
        IBaoCaoService service = baoCaoServiceFactory.getReportService(loai, actor);
        KetQuaBaoCao ketQua = service.xemBaoCao(tieuChi);
        ketQua.inBang();
        ketQuaGanNhat = ketQua;
    }

    private TieuChiLoc nhapTieuChiLoc(LoaiBaoCao loai) {
        LocalDate tuNgay = nhapNgay("Từ ngày (yyyy-MM-dd, Enter = 2026-01-01): ", LocalDate.of(2026, 1, 1));
        LocalDate denNgay = nhapNgay("Đến ngày (yyyy-MM-dd, Enter = 2026-12-31): ", LocalDate.of(2026, 12, 31));

        System.out.print("Lọc theo phòng ban (Enter = tất cả): ");
        String phongBan = scanner.nextLine().trim();

        System.out.print("Lọc theo mã nhân viên (Enter = tất cả): ");
        String maNhanVien = scanner.nextLine().trim();

        return new TieuChiLoc(loai, tuNgay, denNgay,
                phongBan.isBlank() ? null : phongBan,
                maNhanVien.isBlank() ? null : maNhanVien);
    }

    private LocalDate nhapNgay(String prompt, LocalDate macDinh) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return macDinh;
            try {
                return LocalDate.parse(line, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Định dạng ngày không hợp lệ, vui lòng nhập theo yyyy-MM-dd.");
            }
        }
    }

    private void xuatFile() {
        if (ketQuaGanNhat == null) {
            System.out.println("ℹ️ Bạn cần xem một báo cáo trước khi xuất file.");
            return;
        }
        System.out.print("Định dạng xuất (PDF/EXCEL): ");
        String dinhDang = scanner.nextLine().trim();
        var file = xuatBaoCaoService.xuatFile(ketQuaGanNhat, dinhDang);
        System.out.println("✅ Đã xuất file: " + file);
    }

    private NguoiDung chonNguoiDungHienTai() {
        List<NguoiDung> danhSach = nguoiDungRepo.findAll();
        if (danhSach.isEmpty()) {
            return null;
        }
        System.out.println("\n--- CHỌN NGƯỜI DÙNG ĐANG THAO TÁC (mô phỏng đăng nhập) ---");
        for (NguoiDung nd : danhSach) {
            System.out.println(nd + " | Vai trò: " + nd.getDanhSachVaiTro());
        }
        System.out.print("Nhập ID người dùng: ");
        int id = readInt();
        return nguoiDungRepo.findById(id).orElse(null);
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
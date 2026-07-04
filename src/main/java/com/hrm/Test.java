package com.hrm;

import com.hrm.auth.model.Account;
import com.hrm.auth.model.Role;
import com.hrm.auth.pattern.SessionManager;
import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.model.Quyen;
import com.hrm.authorization.model.VaiTro;
import com.hrm.authorization.repository.*;
import com.hrm.authorization.service.PhanQuyenService;
import com.hrm.console.*;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.hrm.profile.model.PartTimeEmployee;
import com.hrm.report.data.ReportSampleData;
import com.hrm.report.factory.BaoCaoServiceFactory;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.service.BaoCaoChamCongService;
import com.hrm.report.service.BaoCaoLuongService;
import com.hrm.report.service.BaoCaoNhanSuService;
import com.hrm.report.service.XuatBaoCaoService;
import com.hrm.training.model.TrainingPlan;
import com.hrm.training.observer.HrNotificationObserver;
import com.hrm.training.service.TrainingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sharedScanner = new Scanner(System.in);
        List<EmployeeProfile> employees = new ArrayList<>();
        // tạo vài nhân viên để test
        FullTimeEmployee emp1 = new FullTimeEmployee("NV001","Nguyễn Văn A","IT","Developer","BH001" );
        employees.add(emp1);
        FullTimeEmployee emp2 = new FullTimeEmployee("NV002","Trần Văn B","HR","Manager","BH002");
        employees.add(emp2);
        FullTimeEmployee emp3 = new FullTimeEmployee("NV003","Lê Thị C","IT","Tester","BH003");
        employees.add(emp3);
        PartTimeEmployee emp4 = new PartTimeEmployee("NV004","Phạm Văn D","Sales","Nhân viên bán hàng", 50000);
        employees.add(emp4);

        SessionManager session = SessionManager.getInstance();
        List<Account> accounts = new ArrayList<>();
        Role admin = new Role("ADMIN");
        accounts.add(new Account("admin","123456", admin));
        Role hrRole = new Role("HR");
        accounts.add(new Account("hr01", "123456", hrRole));
        Role managerRole = new Role("MANAGER");
        accounts.add(new Account("manager01", "123456", managerRole));
        Role employeeRole = new Role("EMPLOYEE");
        accounts.add(new Account("nv01", "123456", employeeRole));
        LoginMenu loginMenu = new LoginMenu(accounts, session, sharedScanner);

        TrainingService trainingService = new TrainingService();
        trainingService.attach(new HrNotificationObserver());


        trainingService.addTrainingPlan(new TrainingPlan("KH001", "Java nâng cao", "IT", 20000000));
        trainingService.addTrainingPlan(new TrainingPlan("KH002", "Kỹ năng quản lý", "HR", 15000000));
        trainingService.addTrainingPlan(new TrainingPlan("KH003", "Kỹ năng bán hàng", "Sales", 8000000));
        trainingService.updateKPI(emp1, "05/2026", 8.0);
        trainingService.updateKPI(emp1, "06/2026", 8.5);
        trainingService.updateKPI(emp2, "05/2026", 9.0);
        trainingService.updateKPI(emp2, "06/2026", 8.8);
        trainingService.updateKPI(emp3, "06/2026", 7.5);
        trainingService.updateKPI(emp4, "06/2026", 6.5);
        TrainingMenu trainingMenu = new TrainingMenu(trainingService, employees, sharedScanner);

        // ==== Module PHÂN QUYỀN (RBAC) - bổ sung ====
        NguoiDungRepository nguoiDungRepo = new NguoiDungRepository();
        VaiTroRepository vaiTroRepo = new VaiTroRepository();
        QuyenRepository quyenRepo = new QuyenRepository();
        NguoiDungVaiTroRepository userRoleRepo = new NguoiDungVaiTroRepository();
        LichSuPhanQuyenRepository logRepo = new LichSuPhanQuyenRepository();

        PhanQuyenService phanQuyenService = new PhanQuyenService(
                nguoiDungRepo, vaiTroRepo, quyenRepo, userRoleRepo, logRepo);

        seedPhanQuyen(phanQuyenService);

        int actorAdminId = 1; // NguoiDung #1 (admin) - thực hiện các thao tác CRUD phân quyền
        PhanQuyenMenu phanQuyenMenu = new PhanQuyenMenu(phanQuyenService, sharedScanner, actorAdminId);

        // ==== Module BÁO CÁO (Report) - bổ sung ====
        ReportSampleData reportData = new ReportSampleData();

        BaoCaoServiceFactory baoCaoServiceFactory = new BaoCaoServiceFactory(phanQuyenService);
        baoCaoServiceFactory.register(new BaoCaoNhanSuService(reportData));
        baoCaoServiceFactory.register(new BaoCaoLuongService(reportData));
        baoCaoServiceFactory.register(new BaoCaoChamCongService(reportData));

        XuatBaoCaoService xuatBaoCaoService = new XuatBaoCaoService();

        ReportMenu reportMenu = new ReportMenu(baoCaoServiceFactory, nguoiDungRepo, xuatBaoCaoService, sharedScanner);

        MainMenu mainMenu = new MainMenu(loginMenu, new ProfileMenu(employees,sharedScanner), null, null, null, trainingMenu, null, null, reportMenu, phanQuyenMenu, session);
        mainMenu.displayMenu();


    }

    // Khởi tạo dữ liệu mẫu cho module Phân quyền (RBAC): quyền báo cáo, vai trò, người dùng.
    private static void seedPhanQuyen(PhanQuyenService service) {
        //Catalog quyền (Quyen)
        Quyen qNhanSu = service.taoQuyen(LoaiBaoCao.NHAN_SU.getMaQuyenYeuCau(), "Xem báo cáo nhân sự", "XEM", "BAOCAO_NHANSU", "Cho phép xem báo cáo nhân sự");
        Quyen qLuong = service.taoQuyen(LoaiBaoCao.LUONG.getMaQuyenYeuCau(), "Xem báo cáo lương", "XEM", "BAOCAO_LUONG", "Cho phép xem báo cáo lương");
        Quyen qChamCong = service.taoQuyen(LoaiBaoCao.CHAM_CONG.getMaQuyenYeuCau(), "Xem báo cáo chấm công", "XEM", "BAOCAO_CHAMCONG", "Cho phép xem báo cáo chấm công");
        Quyen qQuanLyPhanQuyen = service.taoQuyen("QUANLY_PHANQUYEN", "Quản lý phân quyền", "QUANLY", "PHANQUYEN", "Cho phép quản trị vai trò & quyền");

        //Vai trò (VaiTro)
        int actorHeThong = 0;
        VaiTro admin = service.taoVaiTro("ADMIN", "Quản trị viên", "Toàn quyền hệ thống", actorHeThong);
        VaiTro truongPhong = service.taoVaiTro("TRUONGPHONG", "Trưởng phòng", "Xem báo cáo trong phạm vi phòng ban quản lý", actorHeThong);
        VaiTro nhanVien = service.taoVaiTro("NHANVIEN", "Nhân viên", "Không có quyền xem báo cáo (dùng để test 403)", actorHeThong);

        service.ganQuyenChoVaiTro(admin.getId(),
                Arrays.asList(qNhanSu.getId(), qLuong.getId(), qChamCong.getId(), qQuanLyPhanQuyen.getId()), actorHeThong);
        service.ganQuyenChoVaiTro(truongPhong.getId(),
                Arrays.asList(qNhanSu.getId(), qLuong.getId(), qChamCong.getId()), actorHeThong);

        //Người dùng (NguoiDung)
        NguoiDung ndAdmin = service.taoNguoiDung("ND001", "admin", "123456",
                "admin@hrm.com", "Quản trị hệ thống", "BAN_GIAM_DOC");
        NguoiDung ndTruongPhongIT = service.taoNguoiDung("ND002", "truongphong.it", "123456",
                "tp.it@hrm.com", "Trần Thị Bích", "IT");
        NguoiDung ndNhanVienHR = service.taoNguoiDung("ND003", "nhanvien.hr", "123456",
                "nv.hr@hrm.com", "Lê Văn Cường", "HR");

        service.ganUserVaoRole(ndAdmin.getId(), admin.getId(), actorHeThong);
        service.ganUserVaoRole(ndTruongPhongIT.getId(), truongPhong.getId(), actorHeThong);
        service.ganUserVaoRole(ndNhanVienHR.getId(), nhanVien.getId(), actorHeThong);

        System.out.println("✅ Đã khởi tạo dữ liệu mẫu Phân quyền: 3 quyền báo cáo + 1 quyền quản trị, "
                + "3 vai trò (ADMIN/TRUONGPHONG/NHANVIEN), 3 người dùng (ND001 admin, ND002 truongphong.it [IT], "
                + "ND003 nhanvien.hr [không có quyền báo cáo]).");
    }
}
package com.hrm.report.data;

import com.hrm.attendance.model.AttendanceRecord;
import com.hrm.attendance.model.Shift;
import com.hrm.payroll.model.PaySlip;
import com.hrm.payroll.model.SalaryComponent;
import com.hrm.profile.model.Contract;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.hrm.profile.model.PartTimeEmployee;

import java.util.ArrayList;
import java.util.List;

/**
 * ReportSampleData - Nguồn dữ liệu mẫu (đóng vai trò "CSDL" giả lập) để module Báo cáo
 * có thể truy vấn & tổng hợp mà không cần kết nối cơ sở dữ liệu thật.
 * Tái sử dụng các model đã có sẵn trong hệ thống: EmployeeProfile, Contract, PaySlip, AttendanceRecord.
 */
public class ReportSampleData {
    private final List<EmployeeProfile> nhanVien = new ArrayList<>();
    private final List<PaySlip> phieuLuong = new ArrayList<>();
    private final List<AttendanceRecord> chamCong = new ArrayList<>();

    public ReportSampleData() {
        seedNhanVien();
        seedPhieuLuong();
        seedChamCong();
    }

    private void seedNhanVien() {
        FullTimeEmployee nv1 = new FullTimeEmployee("NV001", "Nguyễn Văn An", "IT", "Developer", "BH001");
        nv1.addContract(new Contract("HD001", 15000000, 1500000, "2025-01-01", "2027-01-01"));

        FullTimeEmployee nv2 = new FullTimeEmployee("NV002", "Trần Thị Bích", "IT", "Team Lead", "BH002");
        nv2.addContract(new Contract("HD002", 22000000, 2000000, "2024-06-01", "2026-06-01"));

        FullTimeEmployee nv3 = new FullTimeEmployee("NV003", "Lê Văn Cường", "HR", "Nhân viên HR", "BH003");
        nv3.addContract(new Contract("HD003", 12000000, 1000000, "2025-03-01", "2027-03-01"));

        FullTimeEmployee nv4 = new FullTimeEmployee("NV004", "Phạm Thị Dung", "HR", "Trưởng phòng HR", "BH004");
        nv4.addContract(new Contract("HD004", 25000000, 2500000, "2023-01-01", "2028-01-01"));

        PartTimeEmployee nv5 = new PartTimeEmployee("NV005", "Hoàng Văn Em", "KT", "Kế toán viên", 150000);
        nv5.addContract(new Contract("HD005", 10000000, 500000, "2025-05-01", "2026-11-01"));

        nhanVien.add(nv1);
        nhanVien.add(nv2);
        nhanVien.add(nv3);
        nhanVien.add(nv4);
        nhanVien.add(nv5);
    }

    private void seedPhieuLuong() {
        String[] cacThang = {"2026-05", "2026-06"};
        for (String thang : cacThang) {
            for (EmployeeProfile nv : nhanVien) {
                PaySlip slip = new PaySlip("SLIP_" + nv.getId() + "_" + thang, nv, thang);
                double baseSalary = nv.getContracts().isEmpty() ? 0 : nv.getContracts().get(0).getBaseSalary();
                double allowance = nv.getContracts().isEmpty() ? 0 : nv.getContracts().get(0).getAllowance();
                slip.addComponent(new SalaryComponent("Lương cơ bản", baseSalary, false));
                if (allowance > 0) slip.addComponent(new SalaryComponent("Phụ cấp", allowance, false));
                slip.calculateNetSalary();
                phieuLuong.add(slip);
            }
        }
    }

    private void seedChamCong() {
        Shift caHanhChinh = new Shift("CA01", "Hành chính", 8);
        String[] ngayThangSau = {
                "2026-06-01", "2026-06-02", "2026-06-03", "2026-06-04", "2026-06-05"
        };
        int idx = 0;
        for (EmployeeProfile nv : nhanVien) {
            for (String ngay : ngayThangSau) {
                // Mô phỏng: 1 nhân viên nghỉ 1 ngày ngẫu nhiên (idx chẵn -> nghỉ ngày đầu tuần)
                boolean present = !(idx % 7 == 0);
                AttendanceRecord record = new AttendanceRecord(nv, ngay, caHanhChinh, present);
                if (present) {
                    record.checkIn("08:00");
                    record.checkOut("17:00");
                }
                chamCong.add(record);
                idx++;
            }
        }
    }

    public List<EmployeeProfile> getNhanVien() { return nhanVien; }
    public List<PaySlip> getPhieuLuong() { return phieuLuong; }
    public List<AttendanceRecord> getChamCong() { return chamCong; }
}
package com.hrm.report.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * TieuChiLoc - Sơ đồ lớp: -loaiBaoCaoId, -tuNgay, -denNgay, -phongBanId, -maNhanVien
 * + validate(): boolean
 * + taoQuery(): String
 */
public class TieuChiLoc {
    private LoaiBaoCao loaiBaoCao;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String phongBan;   // null/"" = tất cả phòng ban
    private String maNhanVien; // null/"" = tất cả nhân viên

    public TieuChiLoc(LoaiBaoCao loaiBaoCao, LocalDate tuNgay, LocalDate denNgay,
                      String phongBan, String maNhanVien) {
        this.loaiBaoCao = loaiBaoCao;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.phongBan = phongBan;
        this.maNhanVien = maNhanVien;
    }

    public LoaiBaoCao getLoaiBaoCao() { return loaiBaoCao; }
    public LocalDate getTuNgay() { return tuNgay; }
    public LocalDate getDenNgay() { return denNgay; }
    public String getPhongBan() { return phongBan; }
    public void setPhongBan(String phongBan) { this.phongBan = phongBan; }
    public String getMaNhanVien() { return maNhanVien; }

    /**
     * E2 - Tiêu chí không hợp lệ: Ngày bắt đầu > ngày kết thúc.
     * BR2 - Khoảng thời gian lọc không vượt quá 12 tháng trong một lần truy vấn.
     */
    public boolean validate() {
        if (tuNgay == null || denNgay == null) return false;
        if (tuNgay.isAfter(denNgay)) return false;
        Period khoangCach = Period.between(tuNgay, denNgay);
        int soThang = khoangCach.getYears() * 12 + khoangCach.getMonths();
        return soThang <= 12;
    }

    public String taoQuery() {
        return String.format("SELECT * FROM %s WHERE ngay BETWEEN '%s' AND '%s'%s%s",
                loaiBaoCao, tuNgay, denNgay,
                (phongBan == null || phongBan.isBlank()) ? "" : " AND phongBan='" + phongBan + "'",
                (maNhanVien == null || maNhanVien.isBlank()) ? "" : " AND maNhanVien='" + maNhanVien + "'");
    }

    @Override
    public String toString() {
        return String.format("TieuChiLoc{loai=%s, tu=%s, den=%s, phongBan=%s, maNV=%s}",
                loaiBaoCao, tuNgay, denNgay, phongBan, maNhanVien);
    }
}
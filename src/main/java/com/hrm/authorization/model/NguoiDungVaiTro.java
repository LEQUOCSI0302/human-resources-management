package com.hrm.authorization.model;

import java.time.LocalDateTime;

/**
 * NguoiDungVaiTro - Bảng liên kết Nhiều-Nhiều giữa NguoiDung và VaiTro.
 * Sơ đồ lớp: -nguoiDungId, -vaiTroId, -ngayGan, -nguoiGanId, -trangThai
 */
public class NguoiDungVaiTro {
    public static final String TRANG_THAI_ACTIVE = "ACTIVE";
    public static final String TRANG_THAI_REVOKED = "REVOKED";

    private int nguoiDungId;
    private int vaiTroId;
    private LocalDateTime ngayGan;
    private int nguoiGanId;
    private String trangThai;

    public NguoiDungVaiTro(int nguoiDungId, int vaiTroId, int nguoiGanId) {
        this.nguoiDungId = nguoiDungId;
        this.vaiTroId = vaiTroId;
        this.nguoiGanId = nguoiGanId;
        this.ngayGan = LocalDateTime.now();
        this.trangThai = TRANG_THAI_ACTIVE;
    }

    public int getNguoiDungId() { return nguoiDungId; }
    public int getVaiTroId() { return vaiTroId; }
    public LocalDateTime getNgayGan() { return ngayGan; }
    public int getNguoiGanId() { return nguoiGanId; }
    public String getTrangThai() { return trangThai; }

    public boolean isActive() {
        return TRANG_THAI_ACTIVE.equalsIgnoreCase(trangThai);
    }

    public boolean ganVaiTro() {
        this.trangThai = TRANG_THAI_ACTIVE;
        this.ngayGan = LocalDateTime.now();
        return true;
    }

    public boolean goVaiTro() {
        this.trangThai = TRANG_THAI_REVOKED;
        return true;
    }

    @Override
    public String toString() {
        return String.format("User#%d - Role#%d [%s]", nguoiDungId, vaiTroId, trangThai);
    }
}
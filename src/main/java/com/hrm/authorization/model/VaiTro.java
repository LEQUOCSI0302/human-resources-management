package com.hrm.authorization.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * VaiTro (Role) - Sơ đồ lớp: VaiTro(-id, -maVaiTro, -tenVaiTro, -moTa, -ngayTao, -nguoiTaoId)
 * + getDanhSachQuyen(): List
 * + themQuyen(q: Quyen): boolean
 * + xoaQuyen(q: Quyen): boolean
 * + kiemTraTrungTen(ten: String): boolean
 */
public class VaiTro {
    private int id;
    private String maVaiTro;
    private String tenVaiTro;
    private String moTa;
    private LocalDateTime ngayTao;
    private int nguoiTaoId;
    private final List<Quyen> danhSachQuyen = new ArrayList<>();

    public VaiTro(int id, String maVaiTro, String tenVaiTro, String moTa, int nguoiTaoId) {
        this.id = id;
        this.maVaiTro = maVaiTro;
        this.tenVaiTro = tenVaiTro;
        this.moTa = moTa;
        this.nguoiTaoId = nguoiTaoId;
        this.ngayTao = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getMaVaiTro() { return maVaiTro; }
    public String getTenVaiTro() { return tenVaiTro; }
    public void setTenVaiTro(String tenVaiTro) { this.tenVaiTro = tenVaiTro; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public int getNguoiTaoId() { return nguoiTaoId; }

    public List<Quyen> getDanhSachQuyen() {
        return danhSachQuyen;
    }

    /** BR3: Không được gán quyền "super_admin" cho vai trò thông thường. */
    public boolean themQuyen(Quyen q) {
        if (q == null) return false;
        if (q.laSuperAdmin()) {
            throw new SecurityException("Không thể gán quyền SUPER_ADMIN cho vai trò thông thường (BR3).");
        }
        if (danhSachQuyen.contains(q)) return false;
        return danhSachQuyen.add(q);
    }

    public boolean xoaQuyen(Quyen q) {
        return danhSachQuyen.remove(q);
    }

    public void xoaTatCaQuyen() {
        danhSachQuyen.clear();
    }

    public boolean coQuyen(String maQuyen) {
        for (Quyen q : danhSachQuyen) {
            if (q.getMaQuyen().equalsIgnoreCase(maQuyen)) return true;
        }
        return false;
    }

    public boolean kiemTraTrungTen(String ten) {
        return this.tenVaiTro != null && this.tenVaiTro.equalsIgnoreCase(ten);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%d quyền)", id, maVaiTro, tenVaiTro, danhSachQuyen.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VaiTro)) return false;
        return this.id == ((VaiTro) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}
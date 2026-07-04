package com.hrm.authorization.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NguoiDung (User) - Sơ đồ lớp: NguoiDung(-id, -maNguoiDung, -tenDangNhap, -matKhau,
 * -email, -hoTen, -trangThai, -ngayTao)
 * + getDanhSachVaiTro(): List
 * + getDanhSachQuyen(): List
 * + kiemTraTrangThai(): boolean
 *
 * Ghi chú mở rộng: thêm thuộc tính "phongBan" (không có trong sơ đồ gốc) để hỗ trợ
 * BR1 của UC Báo cáo: "Trưởng phòng chỉ xem được dữ liệu của phòng ban mình quản lý".
 */
public class NguoiDung {
    public static final String TRANG_THAI_ACTIVE = "ACTIVE";
    public static final String TRANG_THAI_LOCKED = "LOCKED";

    private int id;
    private String maNguoiDung;
    private String tenDangNhap;
    private String matKhau;
    private String email;
    private String hoTen;
    private String trangThai;
    private LocalDateTime ngayTao;
    private String phongBan; // mở rộng: phòng ban quản lý (dùng cho vai trò Trưởng phòng)

    private final List<VaiTro> danhSachVaiTro = new ArrayList<>();

    public NguoiDung(int id, String maNguoiDung, String tenDangNhap, String matKhau,
                     String email, String hoTen, String phongBan) {
        this.id = id;
        this.maNguoiDung = maNguoiDung;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.email = email;
        this.hoTen = hoTen;
        this.phongBan = phongBan;
        this.trangThai = TRANG_THAI_ACTIVE;
        this.ngayTao = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getMaNguoiDung() { return maNguoiDung; }
    public String getTenDangNhap() { return tenDangNhap; }
    public boolean checkPassword(String raw) { return this.matKhau.equals(raw); }
    public String getEmail() { return email; }
    public String getHoTen() { return hoTen; }
    public String getTrangThai() { return trangThai; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public String getPhongBan() { return phongBan; }

    public void khoa() { this.trangThai = TRANG_THAI_LOCKED; }
    public void moKhoa() { this.trangThai = TRANG_THAI_ACTIVE; }

    /** BR5: Tài khoản bị khóa bị từ chối mọi truy cập dù có quyền hợp lệ. */
    public boolean kiemTraTrangThai() {
        return TRANG_THAI_ACTIVE.equalsIgnoreCase(this.trangThai);
    }

    public List<VaiTro> getDanhSachVaiTro() {
        return danhSachVaiTro;
    }

    /** Chỉ nên được gọi từ PhanQuyenService để đảm bảo tính nhất quán với NguoiDungVaiTroRepository. */
    public void themVaiTroNoiBo(VaiTro vt) {
        if (!danhSachVaiTro.contains(vt)) danhSachVaiTro.add(vt);
    }

    /** Chỉ nên được gọi từ PhanQuyenService để đảm bảo tính nhất quán với NguoiDungVaiTroRepository. */
    public void goVaiTroNoiBo(VaiTro vt) {
        danhSachVaiTro.remove(vt);
    }

    public boolean coVaiTro(String maVaiTro) {
        for (VaiTro vt : danhSachVaiTro) {
            if (vt.getMaVaiTro().equalsIgnoreCase(maVaiTro)) return true;
        }
        return false;
    }

    /** BR4: Nếu user có nhiều vai trò, quyền được hợp nhất (union) từ tất cả vai trò. */
    public List<Quyen> getDanhSachQuyen() {
        List<Quyen> ketQua = new ArrayList<>();
        for (VaiTro vt : danhSachVaiTro) {
            for (Quyen q : vt.getDanhSachQuyen()) {
                if (!ketQua.contains(q)) ketQua.add(q);
            }
        }
        return ketQua;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s - %s", id, tenDangNhap, hoTen, phongBan, trangThai);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NguoiDung)) return false;
        return this.id == ((NguoiDung) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}
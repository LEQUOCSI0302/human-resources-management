package com.hrm.authorization.model;

/**
 * Quyen (Permission) - Đại diện cho một quyền hạn cụ thể trong hệ thống.
 * Sơ đồ lớp: Quyen(-id, -maQuyen, -tenQuyen, -hanhDong, -taiNguyen, -moTa)
 */
public class Quyen {
    /** Mã quyền dành riêng cho vai trò super_admin - không được gán cho vai trò thường (BR3). */
    public static final String MA_SUPER_ADMIN = "SUPER_ADMIN";

    private int id;
    private String maQuyen;
    private String tenQuyen;
    private String hanhDong;   // VD: XEM, TAO, SUA, XOA, XUAT, IN...
    private String taiNguyen;  // VD: BAOCAO_NHANSU, BAOCAO_LUONG, PHANQUYEN...
    private String moTa;

    public Quyen(int id, String maQuyen, String tenQuyen, String hanhDong, String taiNguyen, String moTa) {
        this.id = id;
        this.maQuyen = maQuyen;
        this.tenQuyen = tenQuyen;
        this.hanhDong = hanhDong;
        this.taiNguyen = taiNguyen;
        this.moTa = moTa;
    }

    public int getId() { return id; }
    public String getMaQuyen() { return maQuyen; }
    public String getTenQuyen() { return tenQuyen; }
    public String getHanhDong() { return hanhDong; }
    public String getTaiNguyen() { return taiNguyen; }
    public String getMoTa() { return moTa; }

    public boolean laSuperAdmin() {
        return MA_SUPER_ADMIN.equalsIgnoreCase(this.maQuyen);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s/%s)", id, maQuyen, tenQuyen, hanhDong, taiNguyen);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quyen)) return false;
        return this.id == ((Quyen) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}
package com.hrm.authorization.model;

import java.time.LocalDateTime;

/**
 * LichSuPhanQuyen (Audit log) - Sơ đồ lớp:
 * -id, -nguoiThucHienId, -hanhDong, -doiTuongId, -loaiDoiTuong, -giaTriCu, -giaTriMoi, -thoiGian
 * BR6: Mọi thay đổi trong module phân quyền đều phải ghi audit log.
 */
public class LichSuPhanQuyen {
    private int id;
    private int nguoiThucHienId;
    private String hanhDong;     // TAO_VAI_TRO, SUA_VAI_TRO, XOA_VAI_TRO, GAN_QUYEN, THU_HOI_QUYEN,
    // GAN_USER_ROLE, GO_USER_ROLE, TU_CHOI_TRUY_CAP, CHO_PHEP_TRUY_CAP...
    private int doiTuongId;
    private String loaiDoiTuong; // VAI_TRO, QUYEN, NGUOI_DUNG, TRUY_CAP...
    private String giaTriCu;
    private String giaTriMoi;
    private LocalDateTime thoiGian;

    public LichSuPhanQuyen(int id, int nguoiThucHienId, String hanhDong, int doiTuongId,
                           String loaiDoiTuong, String giaTriCu, String giaTriMoi) {
        this.id = id;
        this.nguoiThucHienId = nguoiThucHienId;
        this.hanhDong = hanhDong;
        this.doiTuongId = doiTuongId;
        this.loaiDoiTuong = loaiDoiTuong;
        this.giaTriCu = giaTriCu;
        this.giaTriMoi = giaTriMoi;
        this.thoiGian = LocalDateTime.now();
    }

    public int getId() { return id; }
    public int getNguoiThucHienId() { return nguoiThucHienId; }
    public String getHanhDong() { return hanhDong; }
    public int getDoiTuongId() { return doiTuongId; }
    public String getLoaiDoiTuong() { return loaiDoiTuong; }
    public String getGiaTriCu() { return giaTriCu; }
    public String getGiaTriMoi() { return giaTriMoi; }
    public LocalDateTime getThoiGian() { return thoiGian; }

    @Override
    public String toString() {
        return String.format("[%s] User#%d thực hiện '%s' trên %s#%d (cũ='%s' -> mới='%s')",
                thoiGian, nguoiThucHienId, hanhDong, loaiDoiTuong, doiTuongId, giaTriCu, giaTriMoi);
    }
}
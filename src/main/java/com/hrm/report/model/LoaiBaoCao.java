package com.hrm.report.model;

/**
 * LoaiBaoCao - 3 loại báo cáo theo đặc tả UC-BC-01: Nhân sự / Lương / Chấm công.
 * Mỗi loại gắn với 1 mã quyền tương ứng cần có để được xem báo cáo.
 */
public enum LoaiBaoCao {
    NHAN_SU("Báo cáo nhân sự", "XEM_BAOCAO_NHANSU"),
    LUONG("Báo cáo lương", "XEM_BAOCAO_LUONG"),
    CHAM_CONG("Báo cáo chấm công", "XEM_BAOCAO_CHAMCONG");

    private final String tenHienThi;
    private final String maQuyenYeuCau;

    LoaiBaoCao(String tenHienThi, String maQuyenYeuCau) {
        this.tenHienThi = tenHienThi;
        this.maQuyenYeuCau = maQuyenYeuCau;
    }

    public String getTenHienThi() { return tenHienThi; }
    public String getMaQuyenYeuCau() { return maQuyenYeuCau; }
}
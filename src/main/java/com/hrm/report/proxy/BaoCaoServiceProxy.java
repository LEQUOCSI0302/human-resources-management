package com.hrm.report.proxy;

import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.service.PhanQuyenService;
import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;
import com.hrm.report.service.IBaoCaoService;

/**
 * BaoCaoServiceProxy - Áp dụng Proxy Pattern (giống PayrollServiceProxy) để kiểm tra quyền
 * TRƯỚC khi cho phép truy vấn báo cáo, đúng như Activity Diagram Báo cáo:
 * bước "Kiểm tra quyền truy cập (UC-PQ-04)" gọi sang module Phân quyền.
 *
 * BR1: Nếu actor có vai trò TRUONGPHONG, tự động giới hạn dữ liệu theo phòng ban của actor,
 * bỏ qua tuỳ chọn phòng ban khác mà người dùng nhập (A3 - Trưởng phòng xem báo cáo).
 */
public class BaoCaoServiceProxy implements IBaoCaoService {
    private final IBaoCaoService realService;
    private final PhanQuyenService phanQuyenService;
    private final NguoiDung actor;

    public BaoCaoServiceProxy(IBaoCaoService realService, PhanQuyenService phanQuyenService, NguoiDung actor) {
        this.realService = realService;
        this.phanQuyenService = phanQuyenService;
        this.actor = actor;
    }

    @Override
    public LoaiBaoCao getLoaiBaoCao() {
        return realService.getLoaiBaoCao();
    }

    @Override
    public KetQuaBaoCao xemBaoCao(TieuChiLoc tieuChi) {
        String maQuyenCanCo = realService.getLoaiBaoCao().getMaQuyenYeuCau();

        // E1 - Không có quyền: hệ thống phát hiện Actor không đủ quyền -> từ chối 403.
        boolean coQuyen = phanQuyenService.kiemTraQuyen(actor.getId(), maQuyenCanCo);
        if (!coQuyen) {
            throw new SecurityException("403 Forbidden: Tài khoản '" + actor.getTenDangNhap()
                    + "' không có quyền '" + maQuyenCanCo + "' để xem " + realService.getLoaiBaoCao().getTenHienThi());
        }

        // A3 - Trưởng phòng: tự động giới hạn phạm vi dữ liệu theo phòng ban quản lý.
        if (actor.coVaiTro("TRUONGPHONG")) {
            tieuChi.setPhongBan(actor.getPhongBan());
        }

        return realService.xemBaoCao(tieuChi);
    }
}
package com.hrm.report.service;

import com.hrm.profile.model.EmployeeProfile;
import com.hrm.report.data.ReportSampleData;
import com.hrm.report.model.DongBaoCao;
import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * BaoCaoNhanSuService - Báo cáo tổng hợp thông tin nhân sự, có thể lọc theo phòng ban / mã NV.
 */
public class BaoCaoNhanSuService implements IBaoCaoService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private final ReportSampleData data;

    public BaoCaoNhanSuService(ReportSampleData data) {
        this.data = data;
    }

    @Override
    public LoaiBaoCao getLoaiBaoCao() { return LoaiBaoCao.NHAN_SU; }

    @Override
    public KetQuaBaoCao xemBaoCao(TieuChiLoc tieuChi) {
        if (!tieuChi.validate()) {
            throw new IllegalArgumentException("Tiêu chí lọc không hợp lệ (ngày bắt đầu > ngày kết thúc, "
                    + "hoặc khoảng thời gian vượt quá 12 tháng - BR2).");
        }

        KetQuaBaoCao ketQua = new KetQuaBaoCao(ID_GENERATOR.getAndIncrement(), LoaiBaoCao.NHAN_SU);
        for (EmployeeProfile nv : data.getNhanVien()) {
            if (!phuHop(nv, tieuChi)) continue;
            DongBaoCao dong = new DongBaoCao()
                    .them("Mã NV", nv.getId())
                    .them("Họ tên", nv.getName())
                    .them("Phòng ban", nv.getDepartment())
                    .them("Số hợp đồng", nv.getContracts().size())
                    .them("Số chứng chỉ", nv.getCertificates().size());
            ketQua.themDong(dong);
        }
        ketQua.tongHopDuLieu();
        return ketQua;
    }

    private boolean phuHop(EmployeeProfile nv, TieuChiLoc tc) {
        if (tc.getPhongBan() != null && !tc.getPhongBan().isBlank()
                && !tc.getPhongBan().equalsIgnoreCase(nv.getDepartment())) {
            return false;
        }
        if (tc.getMaNhanVien() != null && !tc.getMaNhanVien().isBlank()
                && !tc.getMaNhanVien().equalsIgnoreCase(nv.getId())) {
            return false;
        }
        return true;
    }
}
package com.hrm.report.service;

import com.hrm.payroll.model.PaySlip;
import com.hrm.report.data.ReportSampleData;
import com.hrm.report.model.DongBaoCao;
import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BaoCaoLuongService - Báo cáo lương, lọc theo khoảng thời gian (tháng) và phòng ban.
 * BR4: Báo cáo lương yêu cầu kỳ lương đã được tính toán (netSalary đã tính) trước khi hiển thị.
 */
public class BaoCaoLuongService implements IBaoCaoService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private final ReportSampleData data;

    public BaoCaoLuongService(ReportSampleData data) {
        this.data = data;
    }

    @Override
    public LoaiBaoCao getLoaiBaoCao() { return LoaiBaoCao.LUONG; }

    @Override
    public KetQuaBaoCao xemBaoCao(TieuChiLoc tieuChi) {
        if (!tieuChi.validate()) {
            throw new IllegalArgumentException("Tiêu chí lọc không hợp lệ (ngày bắt đầu > ngày kết thúc, "
                    + "hoặc khoảng thời gian vượt quá 12 tháng - BR2).");
        }

        KetQuaBaoCao ketQua = new KetQuaBaoCao(ID_GENERATOR.getAndIncrement(), LoaiBaoCao.LUONG);
        for (PaySlip slip : data.getPhieuLuong()) {
            if (!phuHop(slip, tieuChi)) continue;
            if (slip.getNetSalary() <= 0) continue; // BR4: kỳ lương chưa tính -> bỏ qua
            DongBaoCao dong = new DongBaoCao()
                    .them("Mã NV", slip.getEmployee().getId())
                    .them("Họ tên", slip.getEmployee().getName())
                    .them("Phòng ban", slip.getEmployee().getDepartment())
                    .them("Kỳ lương", slip.getMonth())
                    .them("Lương thực nhận", slip.getNetSalary());
            ketQua.themDong(dong);
        }
        ketQua.tongHopDuLieu();
        return ketQua;
    }

    private boolean phuHop(PaySlip slip, TieuChiLoc tc) {
        YearMonth ky = YearMonth.parse(slip.getMonth());
        LocalDate dauKy = ky.atDay(1);
        LocalDate cuoiKy = ky.atEndOfMonth();
        // Kỳ lương phải giao với khoảng [tuNgay, denNgay]
        if (cuoiKy.isBefore(tc.getTuNgay()) || dauKy.isAfter(tc.getDenNgay())) return false;

        if (tc.getPhongBan() != null && !tc.getPhongBan().isBlank()
                && !tc.getPhongBan().equalsIgnoreCase(slip.getEmployee().getDepartment())) {
            return false;
        }
        if (tc.getMaNhanVien() != null && !tc.getMaNhanVien().isBlank()
                && !tc.getMaNhanVien().equalsIgnoreCase(slip.getEmployee().getId())) {
            return false;
        }
        return true;
    }
}
package com.hrm.report.service;

import com.hrm.attendance.model.AttendanceRecord;
import com.hrm.report.data.ReportSampleData;
import com.hrm.report.model.DongBaoCao;
import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BaoCaoChamCongService - Báo cáo chấm công, lọc theo khoảng ngày và phòng ban.
 */
public class BaoCaoChamCongService implements IBaoCaoService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private final ReportSampleData data;

    public BaoCaoChamCongService(ReportSampleData data) {
        this.data = data;
    }

    @Override
    public LoaiBaoCao getLoaiBaoCao() { return LoaiBaoCao.CHAM_CONG; }

    @Override
    public KetQuaBaoCao xemBaoCao(TieuChiLoc tieuChi) {
        if (!tieuChi.validate()) {
            throw new IllegalArgumentException("Tiêu chí lọc không hợp lệ (ngày bắt đầu > ngày kết thúc, "
                    + "hoặc khoảng thời gian vượt quá 12 tháng - BR2).");
        }

        KetQuaBaoCao ketQua = new KetQuaBaoCao(ID_GENERATOR.getAndIncrement(), LoaiBaoCao.CHAM_CONG);
        for (AttendanceRecord record : data.getChamCong()) {
            if (!phuHop(record, tieuChi)) continue;
            DongBaoCao dong = new DongBaoCao()
                    .them("Mã NV", record.getEmployee().getId())
                    .them("Họ tên", record.getEmployee().getName())
                    .them("Phòng ban", record.getEmployee().getDepartment())
                    .them("Ngày", record.getDate())
                    .them("Ca làm", record.getShift().getShiftName())
                    .them("Có mặt", record.isPresent() ? "Có" : "Vắng");
            ketQua.themDong(dong);
        }
        ketQua.tongHopDuLieu();
        return ketQua;
    }

    private boolean phuHop(AttendanceRecord record, TieuChiLoc tc) {
        LocalDate ngay = LocalDate.parse(record.getDate());
        if (ngay.isBefore(tc.getTuNgay()) || ngay.isAfter(tc.getDenNgay())) return false;

        if (tc.getPhongBan() != null && !tc.getPhongBan().isBlank()
                && !tc.getPhongBan().equalsIgnoreCase(record.getEmployee().getDepartment())) {
            return false;
        }
        if (tc.getMaNhanVien() != null && !tc.getMaNhanVien().isBlank()
                && !tc.getMaNhanVien().equalsIgnoreCase(record.getEmployee().getId())) {
            return false;
        }
        return true;
    }
}
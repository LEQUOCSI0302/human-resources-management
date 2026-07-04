package com.hrm.report.service;

import com.hrm.report.model.KetQuaBaoCao;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.model.TieuChiLoc;

/**
 * IBaoCaoService - Hợp đồng chung cho các dịch vụ báo cáo (Nhân sự / Lương / Chấm công).
 * Bước 6-7 Basic Flow: "Hệ thống ... truy vấn CSDL theo tiêu chí đã chọn,
 * tổng hợp dữ liệu và hiển thị báo cáo dạng bảng và biểu đồ."
 */
public interface IBaoCaoService {
    LoaiBaoCao getLoaiBaoCao();

    /**
     * @throws IllegalArgumentException E2 - tiêu chí lọc không hợp lệ (BR2 vượt quá 12 tháng).
     */
    KetQuaBaoCao xemBaoCao(TieuChiLoc tieuChi);
}
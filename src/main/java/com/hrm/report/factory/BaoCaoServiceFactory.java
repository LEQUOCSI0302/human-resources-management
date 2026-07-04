package com.hrm.report.factory;

import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.service.PhanQuyenService;
import com.hrm.report.model.LoaiBaoCao;
import com.hrm.report.proxy.BaoCaoServiceProxy;
import com.hrm.report.service.IBaoCaoService;

import java.util.EnumMap;
import java.util.Map;

/**
 * BaoCaoServiceFactory - Factory Pattern: đăng ký & cấp phát dịch vụ báo cáo theo LoaiBaoCao.
 * Mỗi lần lấy dịch vụ cho 1 actor cụ thể, factory sẽ bọc dịch vụ thật trong BaoCaoServiceProxy
 * để đảm bảo mọi truy vấn đều đi qua bước kiểm tra quyền (NFR2).
 */
public class BaoCaoServiceFactory {
    private final Map<LoaiBaoCao, IBaoCaoService> registry = new EnumMap<>(LoaiBaoCao.class);
    private final PhanQuyenService phanQuyenService;

    public BaoCaoServiceFactory(PhanQuyenService phanQuyenService) {
        this.phanQuyenService = phanQuyenService;
    }

    public void register(IBaoCaoService service) {
        registry.put(service.getLoaiBaoCao(), service);
    }

    /** Trả về dịch vụ báo cáo đã được bọc Proxy kiểm tra quyền cho actor hiện tại. */
    public IBaoCaoService getReportService(LoaiBaoCao loai, NguoiDung actor) {
        IBaoCaoService real = registry.get(loai);
        if (real == null) {
            throw new IllegalArgumentException("Không tìm thấy dịch vụ báo cáo cho loại: " + loai);
        }
        return new BaoCaoServiceProxy(real, phanQuyenService, actor);
    }
}
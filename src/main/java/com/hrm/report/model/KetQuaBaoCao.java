package com.hrm.report.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * KetQuaBaoCao - Sơ đồ lớp:
 * -baoCaoId, -danhSachDong, -tongSoNguoi, -tongLuong, -duLieuBieuDo
 * + tongHopDuLieu() + tinhToan() + getChanData(): Map
 */
public class KetQuaBaoCao {
    private final int baoCaoId;
    private final LoaiBaoCao loaiBaoCao;
    private final List<DongBaoCao> danhSachDong = new ArrayList<>();
    private int tongSoNguoi;
    private double tongLuong;
    private final Map<String, Double> duLieuBieuDo = new LinkedHashMap<>();

    public KetQuaBaoCao(int baoCaoId, LoaiBaoCao loaiBaoCao) {
        this.baoCaoId = baoCaoId;
        this.loaiBaoCao = loaiBaoCao;
    }

    public void themDong(DongBaoCao dong) {
        danhSachDong.add(dong);
    }

    public int getBaoCaoId() { return baoCaoId; }
    public LoaiBaoCao getLoaiBaoCao() { return loaiBaoCao; }
    public List<DongBaoCao> getDanhSachDong() { return danhSachDong; }
    public int getTongSoNguoi() { return tongSoNguoi; }
    public double getTongLuong() { return tongLuong; }
    public Map<String, Double> getDuLieuBieuDo() { return duLieuBieuDo; }

    public boolean coDuLieu() {
        return !danhSachDong.isEmpty();
    }

    /**
     * Tổng hợp dữ liệu (bước 17 sequence): tính tongSoNguoi và tongLuong (nếu có cột "Lương thực nhận"),
     * đồng thời dựng dữ liệu biểu đồ theo phòng ban.
     */
    public void tongHopDuLieu() {
        tinhToan();
        duLieuBieuDo.clear();
        for (DongBaoCao dong : danhSachDong) {
            Object pb = dong.get("Phòng ban");
            if (pb != null) {
                double luong = toDouble(dong.get("Lương thực nhận"));
                duLieuBieuDo.merge(String.valueOf(pb), luong > 0 ? luong : 1.0, Double::sum);
            }
        }
    }

    public void tinhToan() {
        tongSoNguoi = danhSachDong.size();
        tongLuong = 0;
        for (DongBaoCao dong : danhSachDong) {
            tongLuong += toDouble(dong.get("Lương thực nhận"));
        }
    }

    public Map<String, Object> getChanData() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("tongSoNguoi", tongSoNguoi);
        map.put("tongLuong", tongLuong);
        map.put("duLieuBieuDo", duLieuBieuDo);
        return map;
    }

    private double toDouble(Object val) {
        if (val instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    public void inBang() {
        System.out.println("\n===== " + loaiBaoCao.getTenHienThi() + " (Mã BC#" + baoCaoId + ") =====");
        if (!coDuLieu()) {
            System.out.println("(Không có dữ liệu phù hợp)");
            return;
        }
        int stt = 1;
        for (DongBaoCao dong : danhSachDong) {
            System.out.println(stt++ + ". " + dong);
        }
        System.out.println("---------------------------------------------");
        System.out.println("Tổng số dòng: " + tongSoNguoi);
        if (tongLuong > 0) System.out.println("Tổng lương: " + String.format("%,.0f", tongLuong) + " đ");
        if (!duLieuBieuDo.isEmpty()) System.out.println("Dữ liệu biểu đồ theo phòng ban: " + dinhDangBieuDo());
    }

    /** Định dạng duLieuBieuDo tránh ký hiệu khoa học (VD: 1.55E8) khi in/xuất file. */
    public String dinhDangBieuDo() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Double> e : duLieuBieuDo.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(e.getKey()).append("=").append(String.format("%,.0f", e.getValue()));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}
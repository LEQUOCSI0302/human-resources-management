package com.hrm.report.service;

import com.hrm.report.model.DongBaoCao;
import com.hrm.report.model.FileBaoCao;
import com.hrm.report.model.KetQuaBaoCao;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * XuatBaoCaoService - Sinh file báo cáo (PDF/EXCEL) ra đĩa từ KetQuaBaoCao.
 * Đây là bước cuối trong Basic Flow của UC Báo cáo: "Người dùng chọn xuất file -> Hệ thống
 * sinh file và trả về đường dẫn tải xuống".
 *
 * Vì đây là ứng dụng console không có thư viện PDF/Excel thật, service mô phỏng:
 *  - EXCEL -> sinh file .csv (mở được bằng Excel)
 *  - PDF   -> sinh file .txt định dạng như một bản in báo cáo
 */
public class XuatBaoCaoService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private static final String OUTPUT_DIR = "output/reports";
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * @param ketQua   kết quả báo cáo đã được tổng hợp (KetQuaBaoCao.tongHopDuLieu() nên được gọi trước)
     * @param dinhDang "PDF" hoặc "EXCEL"
     * @throws IllegalArgumentException nếu định dạng không hỗ trợ hoặc báo cáo không có dữ liệu
     */
    public FileBaoCao xuatFile(KetQuaBaoCao ketQua, String dinhDang) {
        if (ketQua == null || !ketQua.coDuLieu()) {
            throw new IllegalArgumentException("Không thể xuất file: báo cáo không có dữ liệu.");
        }
        String dinhDangChuan = chuanHoaDinhDang(dinhDang);

        try {
            Files.createDirectories(Path.of(OUTPUT_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục xuất báo cáo: " + e.getMessage());
        }

        String ext = "EXCEL".equals(dinhDangChuan) ? "csv" : "txt";
        String timestamp = java.time.LocalDateTime.now().format(TS_FORMAT);
        String tenFile = String.format("BaoCao_%s_%d_%s.%s",
                ketQua.getLoaiBaoCao(), ketQua.getBaoCaoId(), timestamp, ext);
        String duongDan = OUTPUT_DIR + "/" + tenFile;

        if ("EXCEL".equals(dinhDangChuan)) {
            ghiFileCsv(ketQua, duongDan);
        } else {
            ghiFileVanBan(ketQua, duongDan);
        }

        return new FileBaoCao(ID_GENERATOR.getAndIncrement(), ketQua.getBaoCaoId(), tenFile, dinhDangChuan, duongDan);
    }

    private String chuanHoaDinhDang(String dinhDang) {
        if (dinhDang == null) throw new IllegalArgumentException("Định dạng xuất không được để trống.");
        String d = dinhDang.trim().toUpperCase();
        if (!d.equals("PDF") && !d.equals("EXCEL")) {
            throw new IllegalArgumentException("Định dạng không hỗ trợ: " + dinhDang + " (chỉ hỗ trợ PDF hoặc EXCEL).");
        }
        return d;
    }

    private void ghiFileVanBan(KetQuaBaoCao ketQua, String duongDan) {
        try (PrintWriter writer = new PrintWriter(duongDan, StandardCharsets.UTF_8)) {
            writer.println("=================================================");
            writer.println("  " + ketQua.getLoaiBaoCao().getTenHienThi().toUpperCase());
            writer.println("  Mã báo cáo: #" + ketQua.getBaoCaoId());
            writer.println("=================================================");
            int stt = 1;
            for (DongBaoCao dong : ketQua.getDanhSachDong()) {
                writer.println(stt++ + ". " + dong);
            }
            writer.println("-------------------------------------------------");
            writer.println("Tổng số dòng : " + ketQua.getTongSoNguoi());
            if (ketQua.getTongLuong() > 0) {
                writer.println(String.format("Tổng lương   : %,.0f đ", ketQua.getTongLuong()));
            }
            if (!ketQua.getDuLieuBieuDo().isEmpty()) {
                writer.println("Biểu đồ theo phòng ban: " + ketQua.dinhDangBieuDo());
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi ghi file PDF (mô phỏng .txt): " + e.getMessage());
        }
    }

    private void ghiFileCsv(KetQuaBaoCao ketQua, String duongDan) {
        try (PrintWriter writer = new PrintWriter(duongDan, StandardCharsets.UTF_8)) {
            List<DongBaoCao> danhSach = ketQua.getDanhSachDong();
            if (!danhSach.isEmpty()) {
                Map<String, Object> cotDauTien = danhSach.get(0).getCacCot();
                writer.println(String.join(",", cotDauTien.keySet()));
                for (DongBaoCao dong : danhSach) {
                    StringBuilder line = new StringBuilder();
                    boolean first = true;
                    for (Object gt : dong.getCacCot().values()) {
                        if (!first) line.append(",");
                        line.append(csvEscape(gt));
                        first = false;
                    }
                    writer.println(line);
                }
            }
            writer.println();
            writer.println("Tổng số dòng," + ketQua.getTongSoNguoi());
            if (ketQua.getTongLuong() > 0) {
                writer.println("Tổng lương," + String.format("%.0f", ketQua.getTongLuong()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi ghi file EXCEL (mô phỏng .csv): " + e.getMessage());
        }
    }

    private String csvEscape(Object val) {
        String s = String.valueOf(val);
        if (s.contains(",") || s.contains("\"")) {
            s = "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}

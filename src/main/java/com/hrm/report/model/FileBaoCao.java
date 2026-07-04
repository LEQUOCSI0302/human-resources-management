package com.hrm.report.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * FileBaoCao - Sơ đồ lớp: -id, -baoCaoId, -tenFile, -dinhDang, -duongDan, -dungLuong, -ngayXuat
 * + layFile(): Byte[]  + xoaFile(): boolean
 */
public class FileBaoCao {
    private final int id;
    private final int baoCaoId;
    private final String tenFile;
    private final String dinhDang; // PDF | EXCEL
    private final String duongDan;
    private long dungLuong;
    private final LocalDateTime ngayXuat;

    public FileBaoCao(int id, int baoCaoId, String tenFile, String dinhDang, String duongDan) {
        this.id = id;
        this.baoCaoId = baoCaoId;
        this.tenFile = tenFile;
        this.dinhDang = dinhDang;
        this.duongDan = duongDan;
        this.ngayXuat = LocalDateTime.now();
        this.dungLuong = tinhDungLuong();
    }

    private long tinhDungLuong() {
        try {
            return Files.size(Path.of(duongDan));
        } catch (IOException e) {
            return 0L;
        }
    }

    public int getId() { return id; }
    public int getBaoCaoId() { return baoCaoId; }
    public String getTenFile() { return tenFile; }
    public String getDinhDang() { return dinhDang; }
    public String getDuongDan() { return duongDan; }
    public long getDungLuong() { return dungLuong; }
    public LocalDateTime getNgayXuat() { return ngayXuat; }

    public byte[] layFile() throws IOException {
        return Files.readAllBytes(Path.of(duongDan));
    }

    public boolean xoaFile() {
        try {
            return Files.deleteIfExists(Path.of(duongDan));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("FileBaoCao#%d [%s] %s (%d bytes) -> %s", id, dinhDang, tenFile, dungLuong, duongDan);
    }
}
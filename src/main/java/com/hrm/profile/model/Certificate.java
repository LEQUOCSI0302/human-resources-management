package com.hrm.profile.model;

import java.time.LocalDate;

public class Certificate {
    private int id;
    private String tenBangCap;
    private LocalDate ngayCap;
    private LocalDate ngayHetHan;
    private String fileDinhKem;

    public Certificate(int id, String tenBangCap, LocalDate ngayCap, LocalDate ngayHetHan, String fileDinhKem) {
        this.id = id;
        this.tenBangCap = tenBangCap;
        this.ngayCap = ngayCap;
        this.ngayHetHan = ngayHetHan;
        this.fileDinhKem = fileDinhKem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenBangCap() {
        return tenBangCap;
    }

    public void setTenBangCap(String tenBangCap) {
        this.tenBangCap = tenBangCap;
    }

    public LocalDate getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(LocalDate ngayCap) {
        this.ngayCap = ngayCap;
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getFileDinhKem() {
        return fileDinhKem;
    }

    public void setFileDinhKem(String fileDinhKem) {
        this.fileDinhKem = fileDinhKem;
    }

    public String displayCertificate() {
        return "Certificate{" +
                "id=" + id +
                ", tenBangCap='" + tenBangCap + '\'' +
                ", ngayCap=" + ngayCap +
                ", ngayHetHan=" + ngayHetHan +
                ", fileDinhKem='" + fileDinhKem + '\'' +
                '}';
    }
}

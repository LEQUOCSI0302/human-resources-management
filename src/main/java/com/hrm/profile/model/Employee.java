package com.hrm.profile.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Employee {
    private String maNhanVien;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String phongBan;
    private String chucVu;
    private ArrayList<Certificate> listCertificate;

    public Employee(String maNhanVien, String hoTen, LocalDate ngaySinh, String gioiTinh, String soDienThoai, String phongBan, String chucVu, ArrayList<Certificate> listCertificate) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.phongBan = phongBan;
        this.chucVu = chucVu;
        this.listCertificate = listCertificate;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(String phongBan) {
        this.phongBan = phongBan;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public ArrayList<Certificate> getListCertificate() {
        return listCertificate;
    }

    public void setListCertificate(ArrayList<Certificate> listCertificate) {
        this.listCertificate = listCertificate;
    }
}

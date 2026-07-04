package com.hrm.authorization.repository;

import com.hrm.authorization.model.NguoiDung;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NguoiDungRepository {
    private final List<NguoiDung> data = new ArrayList<>();
    private int nextId = 1;

    public NguoiDung create(String maNguoiDung, String tenDangNhap, String matKhau,
                            String email, String hoTen, String phongBan) {
        if (findByTenDangNhap(tenDangNhap).isPresent()) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại: " + tenDangNhap);
        }
        NguoiDung nd = new NguoiDung(nextId++, maNguoiDung, tenDangNhap, matKhau, email, hoTen, phongBan);
        data.add(nd);
        return nd;
    }

    public List<NguoiDung> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<NguoiDung> findById(int id) {
        return data.stream().filter(u -> u.getId() == id).findFirst();
    }

    public Optional<NguoiDung> findByTenDangNhap(String tenDangNhap) {
        return data.stream().filter(u -> u.getTenDangNhap().equalsIgnoreCase(tenDangNhap)).findFirst();
    }

    public boolean delete(int id) {
        return data.removeIf(u -> u.getId() == id);
    }
}
package com.hrm.authorization.repository;

import com.hrm.authorization.model.VaiTro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VaiTroRepository {
    private final List<VaiTro> data = new ArrayList<>();
    private int nextId = 1;

    /** BR1: Tên vai trò phải duy nhất trong toàn hệ thống. */
    public VaiTro create(String maVaiTro, String tenVaiTro, String moTa, int nguoiTaoId) {
        if (existsByTen(tenVaiTro)) {
            throw new IllegalArgumentException("Tên vai trò đã tồn tại: " + tenVaiTro);
        }
        VaiTro vt = new VaiTro(nextId++, maVaiTro, tenVaiTro, moTa, nguoiTaoId);
        data.add(vt);
        return vt;
    }

    public List<VaiTro> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<VaiTro> findById(int id) {
        return data.stream().filter(v -> v.getId() == id).findFirst();
    }

    public boolean existsByTen(String ten) {
        return data.stream().anyMatch(v -> v.kiemTraTrungTen(ten));
    }

    public boolean existsByTenExcludingId(String ten, int excludeId) {
        return data.stream().anyMatch(v -> v.getId() != excludeId && v.kiemTraTrungTen(ten));
    }

    public boolean delete(int id) {
        return data.removeIf(v -> v.getId() == id);
    }
}
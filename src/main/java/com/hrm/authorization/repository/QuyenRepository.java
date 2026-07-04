package com.hrm.authorization.repository;

import com.hrm.authorization.model.Quyen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuyenRepository {
    private final List<Quyen> data = new ArrayList<>();
    private int nextId = 1;

    public Quyen create(String maQuyen, String tenQuyen, String hanhDong, String taiNguyen, String moTa) {
        Quyen q = new Quyen(nextId++, maQuyen, tenQuyen, hanhDong, taiNguyen, moTa);
        data.add(q);
        return q;
    }

    public List<Quyen> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<Quyen> findById(int id) {
        return data.stream().filter(q -> q.getId() == id).findFirst();
    }

    public Optional<Quyen> findByMa(String maQuyen) {
        return data.stream().filter(q -> q.getMaQuyen().equalsIgnoreCase(maQuyen)).findFirst();
    }

    public boolean delete(int id) {
        return data.removeIf(q -> q.getId() == id);
    }
}
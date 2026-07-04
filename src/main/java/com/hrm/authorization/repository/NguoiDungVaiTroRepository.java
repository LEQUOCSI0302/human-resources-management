package com.hrm.authorization.repository;

import com.hrm.authorization.model.NguoiDungVaiTro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NguoiDungVaiTroRepository {
    private final List<NguoiDungVaiTro> data = new ArrayList<>();

    public NguoiDungVaiTro create(int nguoiDungId, int vaiTroId, int nguoiGanId) {
        NguoiDungVaiTro link = new NguoiDungVaiTro(nguoiDungId, vaiTroId, nguoiGanId);
        data.add(link);
        return link;
    }

    public List<NguoiDungVaiTro> findAll() {
        return new ArrayList<>(data);
    }

    public Optional<NguoiDungVaiTro> findActive(int nguoiDungId, int vaiTroId) {
        return data.stream()
                .filter(l -> l.getNguoiDungId() == nguoiDungId && l.getVaiTroId() == vaiTroId && l.isActive())
                .findFirst();
    }

    public List<NguoiDungVaiTro> findByNguoiDung(int nguoiDungId) {
        return data.stream().filter(l -> l.getNguoiDungId() == nguoiDungId).toList();
    }

    public List<NguoiDungVaiTro> findByVaiTro(int vaiTroId) {
        return data.stream().filter(l -> l.getVaiTroId() == vaiTroId).toList();
    }

    public boolean coLienKetDangHoatDong(int vaiTroId) {
        return data.stream().anyMatch(l -> l.getVaiTroId() == vaiTroId && l.isActive());
    }
}
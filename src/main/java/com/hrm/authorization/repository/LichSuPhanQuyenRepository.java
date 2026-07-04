package com.hrm.authorization.repository;

import com.hrm.authorization.model.LichSuPhanQuyen;

import java.util.ArrayList;
import java.util.List;

public class LichSuPhanQuyenRepository {
    private final List<LichSuPhanQuyen> data = new ArrayList<>();
    private int nextId = 1;

    public LichSuPhanQuyen ghiLog(int nguoiThucHienId, String hanhDong, int doiTuongId,
                                  String loaiDoiTuong, String giaTriCu, String giaTriMoi) {
        LichSuPhanQuyen log = new LichSuPhanQuyen(nextId++, nguoiThucHienId, hanhDong, doiTuongId,
                loaiDoiTuong, giaTriCu, giaTriMoi);
        data.add(log);
        return log;
    }

    public List<LichSuPhanQuyen> getDanhSachLog() {
        return new ArrayList<>(data);
    }

    public List<LichSuPhanQuyen> getDanhSachLog(int doiTuongId) {
        return data.stream().filter(l -> l.getDoiTuongId() == doiTuongId).toList();
    }
}
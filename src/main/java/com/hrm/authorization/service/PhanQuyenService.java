package com.hrm.authorization.service;

import com.hrm.authorization.model.LichSuPhanQuyen;
import com.hrm.authorization.model.NguoiDung;
import com.hrm.authorization.model.Quyen;
import com.hrm.authorization.model.VaiTro;
import com.hrm.authorization.repository.*;

import java.util.List;

/**
 * PhanQuyenService - Cài đặt UC-PQ-01 (Phân Quyền - RBAC).
 * Bao gồm 2 luồng chính theo đặc tả Use Case:
 *  - Luồng A: Quản lý vai trò & gán quyền (CRUD Vai trò, gán/thu hồi Quyền)
 *  - Luồng B: Gán user vào role & Kiểm tra quyền truy cập (UC-PQ-04)
 */
public class PhanQuyenService {
    private final NguoiDungRepository nguoiDungRepo;
    private final VaiTroRepository vaiTroRepo;
    private final QuyenRepository quyenRepo;
    private final NguoiDungVaiTroRepository userRoleRepo;
    private final LichSuPhanQuyenRepository logRepo;

    public PhanQuyenService(NguoiDungRepository nguoiDungRepo, VaiTroRepository vaiTroRepo,
                            QuyenRepository quyenRepo, NguoiDungVaiTroRepository userRoleRepo,
                            LichSuPhanQuyenRepository logRepo) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.vaiTroRepo = vaiTroRepo;
        this.quyenRepo = quyenRepo;
        this.userRoleRepo = userRoleRepo;
        this.logRepo = logRepo;
    }

    // ==================== QUẢN LÝ QUYỀN (Catalog) ====================

    public Quyen taoQuyen(String maQuyen, String tenQuyen, String hanhDong, String taiNguyen, String moTa) {
        if (quyenRepo.findByMa(maQuyen).isPresent()) {
            throw new IllegalArgumentException("Mã quyền đã tồn tại: " + maQuyen);
        }
        return quyenRepo.create(maQuyen, tenQuyen, hanhDong, taiNguyen, moTa);
    }

    public List<Quyen> danhSachQuyen() {
        return quyenRepo.findAll();
    }

    // ==================== LUỒNG A: QUẢN LÝ VAI TRÒ & GÁN QUYỀN ====================

    /** Bước 3-4 Luồng A: Tạo vai trò mới. E1 nếu tên trùng (ném IllegalArgumentException). */
    public VaiTro taoVaiTro(String maVaiTro, String tenVaiTro, String moTa, int actorId) {
        VaiTro vt = vaiTroRepo.create(maVaiTro, tenVaiTro, moTa, actorId);
        logRepo.ghiLog(actorId, "TAO_VAI_TRO", vt.getId(), "VAI_TRO", "", vt.getTenVaiTro());
        return vt;
    }

    /** A1 - Sửa vai trò. */
    public VaiTro suaVaiTro(int vaiTroId, String tenVaiTroMoi, String moTaMoi, int actorId) {
        VaiTro vt = timVaiTro(vaiTroId);
        if (vaiTroRepo.existsByTenExcludingId(tenVaiTroMoi, vaiTroId)) {
            throw new IllegalArgumentException("Tên vai trò đã tồn tại: " + tenVaiTroMoi);
        }
        String tenCu = vt.getTenVaiTro();
        vt.setTenVaiTro(tenVaiTroMoi);
        vt.setMoTa(moTaMoi);
        logRepo.ghiLog(actorId, "SUA_VAI_TRO", vt.getId(), "VAI_TRO", tenCu, tenVaiTroMoi);
        return vt;
    }

    /** A2 - Xóa vai trò. E2/BR2: không thể xóa vai trò đang được gán cho người dùng. */
    public boolean xoaVaiTro(int vaiTroId, int actorId) {
        VaiTro vt = timVaiTro(vaiTroId);
        if (userRoleRepo.coLienKetDangHoatDong(vaiTroId)) {
            throw new IllegalStateException("Không thể xóa vai trò đang được gán cho người dùng.");
        }
        boolean ok = vaiTroRepo.delete(vaiTroId);
        if (ok) logRepo.ghiLog(actorId, "XOA_VAI_TRO", vaiTroId, "VAI_TRO", vt.getTenVaiTro(), "");
        return ok;
    }

    public List<VaiTro> danhSachVaiTro() {
        return vaiTroRepo.findAll();
    }

    public VaiTro timVaiTro(int vaiTroId) {
        return vaiTroRepo.findById(vaiTroId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vai trò #" + vaiTroId));
    }

    /** Bước 5-8 Luồng A: Gán danh sách quyền cho vai trò. BR3 được kiểm tra bên trong VaiTro.themQuyen(). */
    public void ganQuyenChoVaiTro(int vaiTroId, List<Integer> maQuyenIds, int actorId) {
        VaiTro vt = timVaiTro(vaiTroId);
        for (Integer quyenId : maQuyenIds) {
            Quyen q = quyenRepo.findById(quyenId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy quyền #" + quyenId));
            boolean added = vt.themQuyen(q);
            if (added) {
                logRepo.ghiLog(actorId, "GAN_QUYEN", vaiTroId, "VAI_TRO", "", q.getMaQuyen());
            }
        }
    }

    /** A3 - Thu hồi 1 quyền khỏi vai trò. */
    public boolean thuHoiQuyen(int vaiTroId, int quyenId, int actorId) {
        VaiTro vt = timVaiTro(vaiTroId);
        Quyen q = quyenRepo.findById(quyenId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy quyền #" + quyenId));
        boolean removed = vt.xoaQuyen(q);
        if (removed) {
            logRepo.ghiLog(actorId, "THU_HOI_QUYEN", vaiTroId, "VAI_TRO", q.getMaQuyen(), "");
        }
        return removed;
    }

    /** A3 - Thu hồi toàn bộ quyền của vai trò. */
    public void thuHoiTatCaQuyen(int vaiTroId, int actorId) {
        VaiTro vt = timVaiTro(vaiTroId);
        vt.xoaTatCaQuyen();
        logRepo.ghiLog(actorId, "THU_HOI_TAT_CA_QUYEN", vaiTroId, "VAI_TRO", "*", "");
    }

    // ==================== LUỒNG B: GÁN USER VÀO ROLE & KIỂM TRA QUYỀN ====================

    public NguoiDung taoNguoiDung(String maNguoiDung, String tenDangNhap, String matKhau,
                                  String email, String hoTen, String phongBan) {
        return nguoiDungRepo.create(maNguoiDung, tenDangNhap, matKhau, email, hoTen, phongBan);
    }

    public List<NguoiDung> danhSachNguoiDung() {
        return nguoiDungRepo.findAll();
    }

    public NguoiDung timNguoiDung(int nguoiDungId) {
        return nguoiDungRepo.findById(nguoiDungId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng #" + nguoiDungId));
    }

    /** +Gán user vào role: bước 1-4. Nếu đã có role -> bỏ qua (idempotent), khớp Sequence "Đã có role này?". */
    public boolean ganUserVaoRole(int nguoiDungId, int vaiTroId, int actorId) {
        NguoiDung nd = timNguoiDung(nguoiDungId);
        VaiTro vt = timVaiTro(vaiTroId);

        var existing = userRoleRepo.findActive(nguoiDungId, vaiTroId);
        if (existing.isPresent()) {
            return false; // "Đã có role này" - bỏ qua, không tạo trùng
        }
        userRoleRepo.create(nguoiDungId, vaiTroId, actorId);
        nd.themVaiTroNoiBo(vt);
        logRepo.ghiLog(actorId, "GAN_USER_ROLE", nguoiDungId, "NGUOI_DUNG", "", vt.getMaVaiTro());
        return true;
    }

    /** A4 - Gỡ user khỏi role. */
    public boolean goUserKhoiRole(int nguoiDungId, int vaiTroId, int actorId) {
        NguoiDung nd = timNguoiDung(nguoiDungId);
        VaiTro vt = timVaiTro(vaiTroId);
        var link = userRoleRepo.findActive(nguoiDungId, vaiTroId);
        if (link.isEmpty()) return false;
        link.get().goVaiTro();
        nd.goVaiTroNoiBo(vt);
        logRepo.ghiLog(actorId, "GO_USER_ROLE", nguoiDungId, "NGUOI_DUNG", vt.getMaVaiTro(), "");
        return true;
    }

    /**
     * +Kiểm tra quyền (Luồng B, bước 1-4 / UC-PQ-04):
     * 1. Đọc "token" (giả lập bằng nguoiDungId) → tìm User → Roles → Permissions.
     * 2. Hợp nhất (union) tất cả quyền từ các vai trò (BR4).
     * 3. Fail-safe: user không tồn tại / bị khóa (BR5) / không có quyền → từ chối (mặc định deny).
     * 4. Ghi audit log (BR6) cho cả trường hợp thành công và thất bại.
     */
    public boolean kiemTraQuyen(int nguoiDungId, String maQuyenYeuCau) {
        try {
            NguoiDung nd = timNguoiDung(nguoiDungId);
            if (!nd.kiemTraTrangThai()) {
                logRepo.ghiLog(nguoiDungId, "TU_CHOI_TRUY_CAP", nguoiDungId, "TRUY_CAP", maQuyenYeuCau, "TAI_KHOAN_BI_KHOA");
                return false;
            }
            List<Quyen> quyenHopNhat = nd.getDanhSachQuyen();
            boolean coQuyen = quyenHopNhat.stream()
                    .anyMatch(q -> q.getMaQuyen().equalsIgnoreCase(maQuyenYeuCau));

            if (coQuyen) {
                logRepo.ghiLog(nguoiDungId, "CHO_PHEP_TRUY_CAP", nguoiDungId, "TRUY_CAP", "", maQuyenYeuCau);
            } else {
                logRepo.ghiLog(nguoiDungId, "TU_CHOI_TRUY_CAP", nguoiDungId, "TRUY_CAP", maQuyenYeuCau, "KHONG_CO_QUYEN");
            }
            return coQuyen;
        } catch (Exception ex) {
            // NFR2: fail-safe - từ chối mặc định nếu RBAC gặp lỗi
            logRepo.ghiLog(nguoiDungId, "TU_CHOI_TRUY_CAP", nguoiDungId, "TRUY_CAP", maQuyenYeuCau, "LOI_HE_THONG: " + ex.getMessage());
            return false;
        }
    }

    public List<LichSuPhanQuyen> xemLichSu() {
        return logRepo.getDanhSachLog();
    }

    public List<LichSuPhanQuyen> xemLichSu(int doiTuongId) {
        return logRepo.getDanhSachLog(doiTuongId);
    }
}
package com.hrm.training.observer;

import com.hrm.profile.model.EmployeeProfile;
import com.hrm.training.model.KPI;
import com.hrm.training.model.TrainingPlan;

import java.util.ArrayList;
import java.util.List;

public class TrainingService {
    private List<KpiObserver> observers;
    private List<KPI> kpiRepo;
    private List<TrainingPlan> planRepo;

    public TrainingService() {
        this.observers = new ArrayList<>();
        this.kpiRepo = new ArrayList<>();
        this.planRepo = new ArrayList<>();
    }

    // Đăng ký Observer (Ví dụ: Thêm HrNotificationObserver vào danh sách nhận tin)
    public void attach(KpiObserver obs) {
        if (obs != null && !observers.contains(obs)) {
            observers.add(obs);
        }
    }

    // Hủy đăng ký Observer
    public void detach(KpiObserver obs) {
        if (obs != null) {
            observers.remove(obs);
        }
    }

    // Cập nhật điểm KPI và tự động kích hoạt thông báo tới các Observer
    public void updateKPI(EmployeeProfile emp, String month, double score) {
        KPI targetKpi = null;
        double oldScore = 0.0;

        // Tìm xem nhân viên này đã có bản ghi KPI cho tháng đó chưa
        for (KPI k : kpiRepo) {
            if (k.getEmployee().getId().equals(emp.getId()) && k.getMonth().equalsIgnoreCase(month)) {
                targetKpi = k;
                break;
            }
        }

        if (targetKpi != null) {
            oldScore = targetKpi.getKpiScore();
            targetKpi.validate(score); // Cập nhật và duyệt điểm số mới
        } else {
            // Nếu chưa có thì tạo mới bản ghi KPI cho nhân viên
            targetKpi = new KPI(emp, month, score);
            targetKpi.validate(score);
            kpiRepo.add(targetKpi);
        }

        // BẮT ĐẦU PHÁT THÔNG BÁO (Notify cho tất cả Observers đang lắng nghe)
        for (KpiObserver obs : observers) {
            obs.onKpiChanged(emp, month, oldScore, score);
        }
    }

    // Lấy điểm KPI (Sẽ được gọi bởi PayrollService để tính toán tiền thưởng)
    public double getKpiScore(EmployeeProfile emp, String month) {
        for (KPI k : kpiRepo) {
            if (k.getEmployee().getId().equals(emp.getId()) && k.getMonth().equalsIgnoreCase(month)) {
                return k.getKpiScore();
            }
        }
        return 0.0; // Trả về 0 nếu chưa có dữ liệu KPI tháng này
    }

    // Thêm kế hoạch đào tạo
    public void addTrainingPlan(TrainingPlan plan) {
        if (plan != null) {
            planRepo.add(plan);
            System.out.println("Đã thêm kế hoạch đào tạo mã: " + plan.getTargetDepartment());
        }
    }
}

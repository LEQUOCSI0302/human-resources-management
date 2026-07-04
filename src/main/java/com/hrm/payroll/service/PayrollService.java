package com.hrm.payroll.service;

import com.hrm.attendance.service.AttendanceService;
import com.hrm.budget.service.BudgetManager;
import com.hrm.discipline.service.DisciplineService;
import com.hrm.payroll.model.PaySlip;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.training.service.TrainingService;

import java.util.List;

public class PayrollService extends BasePayrollProcessor implements IPayrollService{
    // Thuộc tính quản lý ngân sách được cấu hình riêng cho PayrollService
    private BudgetManager budgetManager;

    // Constructor nhận các service phụ thuộc từ lớp cha và gán budgetManager
    public PayrollService(TrainingService ts, DisciplineService ds, AttendanceService as, BudgetManager bm) {
        super(ts, ds, as); // Gọi constructor của BasePayrollProcessor
        this.budgetManager = bm;
    }

    /**
     * Hoàn thiện phương thức Duyệt và Chi Lương cho Phòng Ban
     */
    @Override
    public void approveAndPayroll(List<PaySlip> slips, String dept) {
        if (slips == null || slips.isEmpty()) {
            System.out.println("Không có danh sách phiếu lương để duyệt.");
            return;
        }

        double totalPayrollAmount = 0.0;
        String payrollMonth = "";

        // 1. Lọc và tính tổng tiền lương thực nhận (Net Salary) của phòng ban chỉ định
        for (PaySlip slip : slips) {
            if (slip.getEmployee() != null && slip.getEmployee().getDepartment().equalsIgnoreCase(dept)) {
                // Đảm bảo phiếu lương đã được tính toán lương ròng chính xác
                totalPayrollAmount += slip.calculateNetSalary();

                // Lấy thông tin tháng từ phiếu lương hợp lệ đầu tiên để lưu vết lịch sử ngân sách
                if (payrollMonth.isEmpty() && slip.getMonth() != null) {
                    payrollMonth = slip.getMonth();
                }
            }
        }

        // 2. Thực hiện trừ quỹ ngân sách nếu tổng số tiền chi lương lớn hơn 0
        if (totalPayrollAmount > 0) {
            String transactionDesc = "Chi trả lương cho phòng ban: " + dept
                    + (payrollMonth.isEmpty() ? "" : " - Tháng: " + payrollMonth);
            try {
                // Gọi BudgetManager thực hiện trừ tiền quỹ của phòng ban tương ứng
                budgetManager.deductBudget(dept, totalPayrollAmount, transactionDesc);

                System.out.println("[PayrollService] Đã phê duyệt thành công bảng lương phòng ban: " + dept);
                System.out.println("Tổng số tiền đã khấu trừ từ ngân sách phòng ban: " + totalPayrollAmount);
            } catch (Exception e) {
                System.err.println("Lỗi hệ thống khi khấu trừ ngân sách phòng ban: " + e.getMessage());
            }
        } else {
            System.out.println("Không tìm thấy phiếu lương nào thuộc phòng ban '" + dept + "' để thực hiện chi trả.");
        }
    }

    /**
     * Override hàm tính thưởng KPI từ lớp trừu tượng BasePayrollProcessor
     */
    @Override
    protected double calculateBonus(EmployeeProfile emp, String month) {
        // Lấy điểm KPI của nhân viên từ TrainingService
        double kpiScore = trainingService.getKpiScore(emp, month);

        // Logic thưởng mẫu: KPI >= 9.0 thưởng 2,000,000; KPI >= 7.0 thưởng 1,000,000
        if (kpiScore >= 9.0) {
            return 2000000.0;
        } else if (kpiScore >= 7.0) {
            return 1000000.0;
        }
        return 0.0;
    }

    /**
     * Override hàm tính thuế thu nhập từ lớp trừu tượng BasePayrollProcessor
     */
    @Override
    protected double deductTaxes(double totalIncomeBeforeTax) {
        // Logic tính thuế mẫu đơn giản: Khấu trừ 10% cho phần thu nhập vượt quá 11,000,000
        if (totalIncomeBeforeTax > 11000000.0) {
            return (totalIncomeBeforeTax - 11000000.0) * 0.1;
        }
        return 0.0;
    }
}

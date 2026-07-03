package com.hrm.payroll.proxy;

import com.hrm.auth.model.Account;
import com.hrm.auth.model.Role;
import com.hrm.auth.pattern.SessionManager;
import com.hrm.payroll.model.PaySlip;
import com.hrm.payroll.service.IPayrollService;
import com.hrm.profile.model.EmployeeProfile;

import java.util.List;

public class PayrollServiceProxy implements IPayrollService{
    private IPayrollService realService;
    private SessionManager session;

    public PayrollServiceProxy(IPayrollService realService, SessionManager session) {
        this.realService = realService;
        this.session = session;
    }

    // Kiểm tra quyền từ tài khoản đang đăng nhập trong Session
    private boolean checkAuth(String permissionCode) {
        Account currentAcc = session.getCurrentAccount();
        if (currentAcc == null || currentAcc.isLocked()) {
            return false;
        }
        Role userRole = currentAcc.getRole();
        return userRole != null && userRole.hasPermission(permissionCode);
    }

    @Override
    public void approveAndPayroll(List<PaySlip> slips, String dept) {
        // Chỉ cấp quản trị viên có quyền APPROVE_PAYROLL mới được gọi xuống RealService
        if (checkAuth("APPROVE_PAYROLL")) {
            realService.approveAndPayroll(slips, dept);
        } else {
            System.err.println("[Bảo mật] Từ chối thao tác: Tài khoản không có quyền phê duyệt lương!");
            throw new SecurityException("Unauthorized access: Missing APPROVE_PAYROLL permission.");
        }
    }

    @Override
    public PaySlip processPayroll(EmployeeProfile emp, String month) {
        if (checkAuth("PROCESS_PAYROLL")) {
            return realService.processPayroll(emp, month);
        } else {
            System.err.println("[Bảo mật] Từ chối thao tác: Không có quyền tính toán bảng lương!");
            throw new SecurityException("Unauthorized access: Missing PROCESS_PAYROLL permission.");
        }
    }

}

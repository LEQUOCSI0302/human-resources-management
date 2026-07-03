package com.hrm.attendance.service;

import com.hrm.attendance.model.AttendanceRecord;
import com.hrm.attendance.model.LeaveRequest;
import com.hrm.attendance.model.Shift;
import com.hrm.attendance.repository.AttendanceRepository;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class AttendanceService {
    private AttendanceRepository repo;
    private List<LeaveRequest> leaveRequestRepo = new ArrayList<>();

    public AttendanceService(AttendanceRepository repo) {
        this.repo = repo;
    }

    public void checkIn(EmployeeProfile emp, String date, Shift shift) {
        AttendanceRecord record = new AttendanceRecord(emp, date, shift, true);
        repo.save(record);
    }

    public double getTotalHoursByEmployee(EmployeeProfile emp, String month) {
        double total = 0;
        for (AttendanceRecord r : repo.findByEmployee(emp)) {
            // Giả định date lưu theo định dạng YYYY-MM
            if (r.getDate().startsWith(month)) {
                total += r.getShift().getWorkHours();
            }
        }
        return total;
    }

    // Nghiệp vụ nghỉ phép
    // Phương thức gửi yêu cầu nghỉ phép
    public void submitLeaveRequest(LeaveRequest request) {
        leaveRequestRepo.add(request);
        System.out.println("📄 Đã gửi yêu cầu nghỉ phép: " + request.getRequestId());
    }

    // Phương thức phê duyệt nghỉ phép
    public void approveLeave(String requestId) {
        for (LeaveRequest req : leaveRequestRepo) {
            if (req.getRequestId().equals(requestId)) {
                req.approve();
                System.out.println("✅ Yêu cầu " + requestId + " đã được phê duyệt.");
                return;
            }
        }
    }
}

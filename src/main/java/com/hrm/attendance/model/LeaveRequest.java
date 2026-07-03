package com.hrm.attendance.model;

import com.hrm.profile.model.EmployeeProfile;

public class LeaveRequest {
    private String requestId;
    private EmployeeProfile employee;
    private String fromDate;
    private String toDate;
    private String reason;
    private String status; // Ví dụ: "PENDING", "APPROVED", "REJECTED"

    public LeaveRequest(String id, EmployeeProfile emp, String from, String to, String reason) {
        this.requestId = id;
        this.employee = emp;
        this.fromDate = from;
        this.toDate = to;
        this.reason = reason;
        this.status = "PENDING";
    }

    public void approve() {
        this.status = "APPROVED";
    }

    public void reject() {
        this.status = "REJECTED";
    }

    public String getStatus() {
        return status;
    }

    // Các Getter khác nếu cần
    public String getRequestId() { return requestId; }
    public EmployeeProfile getEmployee() { return employee; }
}

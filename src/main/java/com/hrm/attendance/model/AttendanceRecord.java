package com.hrm.attendance.model;

import com.hrm.profile.model.EmployeeProfile;

public class AttendanceRecord {
    private EmployeeProfile employee;
    private String date; // Lưu ý: theo sơ đồ là String
    private Shift shift;
    private boolean isPresent;
    private String checkInTime;
    private String checkOutTime;
    private String status;

    public AttendanceRecord(EmployeeProfile emp, String d, Shift s, boolean present) {
        this.employee = emp;
        this.date = d;
        this.shift = s;
        this.isPresent = present;
        this.status = "PENDING";
    }

    public void checkIn(String time) { this.checkInTime = time; }
    public void checkOut(String time) { this.checkOutTime = time; }

    public EmployeeProfile getEmployee() { return employee; }
    public Shift getShift() { return shift; }
    public boolean isPresent() { return isPresent; }

    public String getDate() {
        return date;
    }
}

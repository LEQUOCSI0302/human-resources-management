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

    public void setEmployee(EmployeeProfile employee) {
        this.employee = employee;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void checkIn(String time) { this.checkInTime = time; }
    public void checkOut(String time) { this.checkOutTime = time; }

    public EmployeeProfile getEmployee() { return employee; }
    public Shift getShift() { return shift; }
    public boolean isPresent() { return isPresent; }

    public String getDate() {
        return date;
    }
    @Override
    public String toString(){
        return "Nhân viên: " + employee.getName() + "\nNgày: " + date + "\nCa: " + shift.getShiftName() + "\nCheck In: " + checkInTime + "\nCheck Out: " + checkOutTime + "\nTrạng thái: " + status;
    }
}

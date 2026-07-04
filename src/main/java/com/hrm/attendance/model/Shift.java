package com.hrm.attendance.model;

public class Shift {
    private String shiftId;
    private String shiftName;
    private String startTime;
    private String endTime;
    private double workHours;

    public Shift(String shiftId, String shiftName, String startTime, String endTime, double workHours) {
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workHours = workHours;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getWorkHours() { return workHours; }
    public String getShiftName() { return shiftName; }
}

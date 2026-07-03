package com.hrm.attendance.model;

public class Shift {
    private String shiftId;
    private String shiftName;
    private double workHours;

    public Shift(String id, String name, double hours) {
        this.shiftId = id;
        this.shiftName = name;
        this.workHours = hours;
    }

    public double getWorkHours() { return workHours; }
    public String getShiftName() { return shiftName; }
}

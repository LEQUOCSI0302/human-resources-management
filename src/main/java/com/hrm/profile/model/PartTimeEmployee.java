package com.hrm.profile.model;

public class PartTimeEmployee extends EmployeeProfile{
    private double hourlyRate;
    private double totalHoursWorked;

    public PartTimeEmployee(String id, String name, String dept, String pos, double rate) {
        super(id, name, dept, pos);
        this.hourlyRate = rate;
        this.totalHoursWorked = 0.0;
    }
    public double getHourlyRate() { return hourlyRate; }
    public double getTotalHoursWorked() { return totalHoursWorked; }

    public void addHours(double hours) {
        this.totalHoursWorked += hours;
    }
}

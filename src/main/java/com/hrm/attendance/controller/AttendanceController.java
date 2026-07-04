package com.hrm.attendance.controller;

import com.hrm.attendance.model.Shift;
import com.hrm.attendance.repository.AttendanceRepository;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.profile.model.EmployeeProfile;

public class AttendanceController {
    private AttendanceService service;

    public AttendanceController(){
        service= new AttendanceService(new AttendanceRepository());
    }

    public void attendance(EmployeeProfile emp, Shift shift, boolean location){
        service.processAttendance(emp, shift, location);
    }
}

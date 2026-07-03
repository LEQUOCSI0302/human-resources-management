package com.hrm.attendance.repository;

import com.hrm.attendance.model.AttendanceRecord;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    private List<AttendanceRecord> records = new ArrayList<>();

    public void save(AttendanceRecord r) {
        records.add(r);
    }

    public List<AttendanceRecord> findByEmployee(EmployeeProfile emp) {
        List<AttendanceRecord> result = new ArrayList<>();
        for (AttendanceRecord r : records) {
            if (r.getEmployee().getId().equals(emp.getId())) {
                result.add(r);
            }
        }
        return result;
    }

    public List<AttendanceRecord> findAll() {
        return records;
    }
}

package com.hrm.attendance.repository;

import com.hrm.attendance.model.AttendanceRecord;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    private static List<AttendanceRecord> records = new ArrayList<>();

    public void save(AttendanceRecord r) {

        records.add(r);
        System.out.println("DATABASE INSERT Attendance");
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

    //findToday
    public AttendanceRecord findToday(EmployeeProfile emp, String date){
        for(AttendanceRecord r:records){
            if(r.getEmployee().getId().equals(emp.getId()) && r.getDate().equals(date)){
                return r;
            }
        }
        return null;
    }
//update
    public void update(AttendanceRecord record) {
        for (int i = 0; i < records.size(); i++) {
            AttendanceRecord current = records.get(i);
            if (current.getEmployee().getId().equals(record.getEmployee().getId()) && current.getDate().equals(record.getDate())) {
                records.set(i, record);
                System.out.println(" Cập nhật chấm công thành công.");
                return;
            }
        }
        System.out.println(" Không tìm thấy bản ghi để cập nhật.");
    }
}

package com.hrm.profile.factory;

import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.hrm.profile.model.PartTimeEmployee;

public class EmployeeFactory {
    public EmployeeProfile createEmployee(String type, String id, String name, String dept, String pos) {
        if (type.equalsIgnoreCase("FULLTIME")) {
            // Mặc định tạo insuranceId giả lập ban đầu cho FullTime là "INS-" + id
            return new FullTimeEmployee(id, name, dept, pos, "INS-" + id);
        } else if (type.equalsIgnoreCase("PARTTIME")) {
            // Mặc định hourlyRate ban đầu là 50000 cho PartTime
            return new PartTimeEmployee(id, name, dept, pos, 50000.0);
        }
        throw new IllegalArgumentException("❌ Loại nhân viên không hợp lệ!");
    }
}

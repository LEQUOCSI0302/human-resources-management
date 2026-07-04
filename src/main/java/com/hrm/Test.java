package com.hrm;

import com.hrm.auth.model.Account;
import com.hrm.auth.model.Role;
import com.hrm.auth.pattern.SessionManager;
import com.hrm.console.*;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<EmployeeProfile> employees = new ArrayList<>();
        // tạo vài nhân viên để test
        FullTimeEmployee emp1 = new FullTimeEmployee("NV001","Nguyễn Văn A","IT","Developer","BH001" );
        employees.add(emp1);
        FullTimeEmployee emp2 = new FullTimeEmployee("NV002","Trần Văn B","HR","Manager","BH002");
        employees.add(emp2);

        SessionManager session = SessionManager.getInstance();
        List<Account> accounts = new ArrayList<>();
        Role admin = new Role("ADMIN");
        accounts.add(new Account("admin","123456", admin));
        LoginMenu loginMenu = new LoginMenu(accounts, session);
        AttendanceMenu attendanceMenu = new AttendanceMenu(employees);
        PayrollMenu payrollMenu = new PayrollMenu(employees);
        MainMenu mainMenu = new MainMenu(loginMenu, new ProfileMenu(employees), null, attendanceMenu, payrollMenu, null, null, null, null, session);
        mainMenu.displayMenu();
    }
}

package com.hrm;

import com.hrm.auth.model.Account;
import com.hrm.auth.model.Role;
import com.hrm.auth.pattern.SessionManager;
import com.hrm.console.*;
import com.hrm.profile.model.EmployeeProfile;
import com.hrm.profile.model.FullTimeEmployee;
import com.hrm.profile.model.PartTimeEmployee;
import com.hrm.training.model.TrainingPlan;
import com.hrm.training.observer.HrNotificationObserver;
import com.hrm.training.service.TrainingService;
import com.sun.tools.javac.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sharedScanner = new Scanner(System.in);
        List<EmployeeProfile> employees = new ArrayList<>();
        // tạo vài nhân viên để test
        FullTimeEmployee emp1 = new FullTimeEmployee("NV001","Nguyễn Văn A","IT","Developer","BH001" );
        employees.add(emp1);
        FullTimeEmployee emp2 = new FullTimeEmployee("NV002","Trần Văn B","HR","Manager","BH002");
        employees.add(emp2);
        FullTimeEmployee emp3 = new FullTimeEmployee("NV003","Lê Thị C","IT","Tester","BH003");
        employees.add(emp3);
        PartTimeEmployee emp4 = new PartTimeEmployee("NV004","Phạm Văn D","Sales","Nhân viên bán hàng", 50000);
        employees.add(emp4);

        SessionManager session = SessionManager.getInstance();
        List<Account> accounts = new ArrayList<>();
        Role admin = new Role("ADMIN");
        accounts.add(new Account("admin","123456", admin));
        Role hrRole = new Role("HR");
        accounts.add(new Account("hr01", "123456", hrRole));
        Role managerRole = new Role("MANAGER");
        accounts.add(new Account("manager01", "123456", managerRole));
        Role employeeRole = new Role("EMPLOYEE");
        accounts.add(new Account("nv01", "123456", employeeRole));
        LoginMenu loginMenu = new LoginMenu(accounts, session, sharedScanner)

        TrainingService trainingService = new TrainingService();
        trainingService.attach(new HrNotificationObserver());


        trainingService.addTrainingPlan(new TrainingPlan("KH001", "Java nâng cao", "IT", 20000000));
        trainingService.addTrainingPlan(new TrainingPlan("KH002", "Kỹ năng quản lý", "HR", 15000000));
        trainingService.addTrainingPlan(new TrainingPlan("KH003", "Kỹ năng bán hàng", "Sales", 8000000));
        trainingService.updateKPI(emp1, "05/2026", 8.0);
        trainingService.updateKPI(emp1, "06/2026", 8.5);
        trainingService.updateKPI(emp2, "05/2026", 9.0);
        trainingService.updateKPI(emp2, "06/2026", 8.8);
        trainingService.updateKPI(emp3, "06/2026", 7.5);
        trainingService.updateKPI(emp4, "06/2026", 6.5);
        TrainingMenu trainingMenu = new TrainingMenu(trainingService, employees, sharedScanner);


        MainMenu mainMenu = new MainMenu(loginMenu, new ProfileMenu(employees,sharedScanner), null, null, null, trainingMenu, null, null, null, session);
        mainMenu.displayMenu();


    }
}

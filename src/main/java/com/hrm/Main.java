package com.hrm;

import com.hrm.auth.model.Account;
import com.hrm.auth.model.Permission;
import com.hrm.auth.model.Role;
import com.hrm.auth.pattern.SessionManager;
import com.hrm.console.LoginMenu;
import com.hrm.console.ProfileMenu;
import com.hrm.profile.model.EmployeeProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Mock Data
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account("admin", "123456", new Role("Admin")));

        List<EmployeeProfile> employeeList = new ArrayList<>();

        // Khởi tạo
        SessionManager session = SessionManager.getInstance();
        LoginMenu loginMenu = new LoginMenu(accountList, session);
        ProfileMenu profileMenu = new ProfileMenu(employeeList);

        Scanner mainScanner = new Scanner(System.in);

        while (true) {
            if (!session.isLoggedIn()) {
                loginMenu.displayMenu();
            } else {
                System.out.println("\n===== MENU CHÍNH =====");
                System.out.println("1. Quản lý hồ sơ (Profile)");
                System.out.println("2. Đăng xuất");
                System.out.print("Chọn: ");

                int choice = Integer.parseInt(mainScanner.nextLine());
                if (choice == 1) {
                    profileMenu.displayMenu();
                } else if (choice == 2) {
                    loginMenu.handleLogout();
                }
            }
        }
    }
}

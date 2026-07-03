package com.hrm.console;

import com.hrm.auth.model.Account;
import com.hrm.auth.pattern.SessionManager;

import java.util.List;
import java.util.Scanner;

public class LoginMenu {
    private List<Account> accounts;
    private SessionManager session;
    private Scanner scanner;

    public LoginMenu(List<Account> accounts, SessionManager session) {
        this.accounts = accounts;
        this.session = session;
        this.scanner = new Scanner(System.in);
    }
    public void displayMenu() {
        System.out.println("\n=== HỆ THỐNG QUẢN LÝ NHÂN SỰ ===");
        System.out.println("Vui lòng đăng nhập để tiếp tục.");
        System.out.print("Tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Mật khẩu: ");
        String password = scanner.nextLine();

        handleLogin(username, password);
    }
    public void handleLogin(String username, String password) {
        for (Account acc : accounts) {
            if (acc.getUsername().equals(username)) {
                if (acc.isLocked()) {
                    System.out.println("❌ Tài khoản của bạn đã bị khóa do đăng nhập sai nhiều lần!");
                    return;
                }
                if (acc.checkPassword(password)) {
                    acc.resetFailedLogin();
                    session.setCurrentAccount(acc);
                    System.out.println("✅ Đăng nhập thành công! Xin chào, " + acc.getUsername());
                    return;
                } else {
                    acc.incrementFailedLogin();
                    System.out.println("❌ Sai mật khẩu!");
                    return;
                }
            }
        }
        System.out.println("❌ Không tìm thấy tài khoản này!");
    }

    public void handleLogout() {
        session.clearSession();
        System.out.println("👋 Đã đăng xuất thành công!");
    }
}

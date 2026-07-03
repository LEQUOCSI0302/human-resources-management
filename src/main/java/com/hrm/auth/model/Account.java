package com.hrm.auth.model;

public class Account {
    private String username;
    private String password;
    private Role role;
    private int failedLoginCount;
    private boolean isLocked;

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isLocked() {
        return isLocked;
    }
    public boolean checkPassword(String pwd) {
        return this.password.equals(pwd);
    }

    public void incrementFailedLogin() {
        this.failedLoginCount++;
        if (this.failedLoginCount >= 5) {
            lock();
        }
    }
    public void resetFailedLogin() { this.failedLoginCount = 0; }
    public void lock() { this.isLocked = true; }
    public void unlock() {
        this.isLocked = false;
        this.failedLoginCount = 0;
    }
}

package com.hrm.auth.pattern;

import com.hrm.auth.model.Account;

public class SessionManager {
    private static SessionManager instance;
    private Account currentAccount;
    private SessionManager() {}
    // Method lấy instance an toàn với luồng (Thread-safe)
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }
    public void clearSession() {
        this.currentAccount = null;
    }

    public boolean isLoggedIn() {
        return this.currentAccount != null;
    }
}

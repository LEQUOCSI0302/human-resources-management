package com.hrm.auth.model;

import java.util.List;

public class Role {
    private String roleName;
    private List<Permission> permissions;

    public Role(String roleName) {
        this.roleName = roleName;
    }
    public void addPermission(Permission p) {
        this.permissions.add(p);
    }

    public boolean hasPermission(String code) {
        for (Permission p : permissions) {
            if (p.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public String getRoleName() {
        return roleName;
    }
}

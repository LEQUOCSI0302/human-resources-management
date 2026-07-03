package com.hrm.auth.model;

public class Permission {
    private String actionName;
    private String code;

    public Permission(String actionName, String code) {
        this.actionName = actionName;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getActionName() {
        return actionName;
    }
}

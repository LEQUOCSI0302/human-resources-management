package com.hrm.discipline.model;

public class ViolationCatalog {
    private String errorCode;
    private String errorName;
    private int defaultSeverity;
    private double defaultPenalty;

    public ViolationCatalog(String code, String name, int sev, double penalty) {
        this.errorCode = code;
        this.errorName = name;
        this.defaultSeverity = sev;
        this.defaultPenalty = penalty;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public double getDefaultPenalty() {
        return defaultPenalty;
    }

    public int getDefaultSeverity() {
        return defaultSeverity;
    }
}

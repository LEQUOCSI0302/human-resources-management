package com.hrm.discipline.model;

import com.hrm.profile.model.EmployeeProfile;

public class Violation {
    private String violationId;
    private EmployeeProfile employee;
    private int severityLevel;
    private double penaltyAmount;
    private String description;
    private String status; // "PENDING", "EXPLANATION_REQUESTED", "SUBMITTED", "FINALIZED"
    private String explanationContent;
    private ViolationCatalog catalogRef;

    public Violation(String id, EmployeeProfile emp, int level, double amt, String desc) {
        this.violationId = id;
        this.employee = emp;
        this.severityLevel = level;
        this.penaltyAmount = amt;
        this.description = desc;
        this.status = "PENDING";
        this.explanationContent = "";
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public EmployeeProfile getEmployee() {
        return employee;
    }

    public int getSeverityLevel() {
        return severityLevel;
    }

    public void requestExplanation() {
        this.status = "EXPLANATION_REQUESTED";
        System.out.println("Yêu cầu nhân viên " + employee.getName() + " giải trình cho vi phạm " + violationId);
    }

    public void submitExplanation(String content) {
        this.explanationContent = content;
        this.status = "SUBMITTED";
        System.out.println("Nhân viên đã nộp giải trình cho vi phạm " + violationId);
    }

    public void finalizeCase() {
        this.status = "FINALIZED";
        System.out.println("Hồ sơ vi phạm " + violationId + " đã chính thức khép lại và áp dụng phạt.");
    }

    public void setCatalogRef(ViolationCatalog catalog) {
        this.catalogRef = catalog;
    }
}

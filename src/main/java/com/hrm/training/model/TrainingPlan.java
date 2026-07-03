package com.hrm.training.model;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlan {
    private String planId;
    private String topics;
    private String targetDepartment;
    private double budgetCost;
    private List<Course> courseRepo;       // Liên kết 1-* với Course
    private List<Enrollment> enrollmentRepo; // Liên kết 1-* với Enrollment

    public TrainingPlan(String id, String tp, String dept, double cost) {
        this.planId = id;
        this.topics = tp;
        this.targetDepartment = dept;
        this.budgetCost = cost;
        this.courseRepo = new ArrayList<>();
        this.enrollmentRepo = new ArrayList<>();
    }

    public double getBudgetCost() {
        return budgetCost;
    }

    public String getTargetDepartment() {
        return targetDepartment;
    }
}

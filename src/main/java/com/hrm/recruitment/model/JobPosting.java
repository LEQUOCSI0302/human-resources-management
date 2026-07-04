package com.hrm.recruitment.model;

import java.time.LocalDate;
import java.util.Objects;

public class JobPosting {
    private final String jobId;
    private String title;
    private String description;
    private String requirements;
    private String department;
    private String position;
    private int quantity;
    private LocalDate deadline;
    private JobPostingStatus status;

    public JobPosting(
            String jobId,
            String title,
            String description,
            String requirements,
            String department,
            String position,
            int quantity,
            LocalDate deadline,
            JobPostingStatus status
    ) {
        this.jobId = Objects.requireNonNull(jobId, "Mã tin tuyển dụng không được để trống.");
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.department = department;
        this.position = position;
        this.quantity = quantity;
        this.deadline = deadline;
        this.status = status == null ? JobPostingStatus.DANG_HIEN_THI : status;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public void update(
            String title,
            String description,
            String requirements,
            String department,
            String position,
            int quantity,
            LocalDate deadline
    ) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.department = department;
        this.position = position;
        this.quantity = quantity;
        this.deadline = deadline;
    }

    public void changeStatus(JobPostingStatus status) {
        this.status = Objects.requireNonNull(
                status,
                "Trạng thái tin tuyển dụng không được để trống."
        );
    }

    public boolean isActive() {
        return status == JobPostingStatus.DANG_HIEN_THI
                && deadline != null
                && !deadline.isBefore(LocalDate.now());
    }
}
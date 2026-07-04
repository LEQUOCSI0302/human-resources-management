package com.hrm.recruitment.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Application {
    private final String applicationId;
    private final Candidate candidate;
    private final JobPosting jobPosting;
    private final LocalDateTime submittedAt;

    private ApplicationStatus status;
    private String screeningNote;
    private String hiredEmployeeId;

    public Application(
            String applicationId,
            Candidate candidate,
            JobPosting jobPosting
    ) {
        this.applicationId = Objects.requireNonNull(
                applicationId,
                "Mã hồ sơ không được để trống."
        );

        this.candidate = Objects.requireNonNull(
                candidate,
                "Ứng viên không được để trống."
        );

        this.jobPosting = Objects.requireNonNull(
                jobPosting,
                "Tin tuyển dụng không được để trống."
        );

        this.submittedAt = LocalDateTime.now();
        this.status = ApplicationStatus.MOI_TIEP_NHAN;
        this.screeningNote = "";
        this.hiredEmployeeId = "";
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getScreeningNote() {
        return screeningNote;
    }

    public String getHiredEmployeeId() {
        return hiredEmployeeId;
    }

    public void changeStatus(ApplicationStatus status) {
        this.status = Objects.requireNonNull(
                status,
                "Trạng thái hồ sơ không được để trống."
        );
    }

    public void setScreeningNote(String screeningNote) {
        this.screeningNote = screeningNote == null ? "" : screeningNote;
    }

    public void markHiredEmployee(String employeeId) {
        this.hiredEmployeeId = employeeId == null ? "" : employeeId;
        this.status = ApplicationStatus.DA_TAO_NHAN_VIEN;
    }

    public boolean hasBeenConvertedToEmployee() {
        return hiredEmployeeId != null && !hiredEmployeeId.isBlank();
    }
}
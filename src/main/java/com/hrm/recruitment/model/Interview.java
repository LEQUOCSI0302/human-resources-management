package com.hrm.recruitment.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Interview {
    private final String interviewId;
    private final Application application;
    private final LocalDateTime scheduledAt;
    private final String location;
    private final String interviewer;

    private String result;
    private String notes;

    public Interview(
            String interviewId,
            Application application,
            LocalDateTime scheduledAt,
            String location,
            String interviewer
    ) {
        this.interviewId = Objects.requireNonNull(
                interviewId,
                "Mã phỏng vấn không được để trống."
        );

        this.application = Objects.requireNonNull(
                application,
                "Hồ sơ ứng tuyển không được để trống."
        );

        this.scheduledAt = Objects.requireNonNull(
                scheduledAt,
                "Thời gian phỏng vấn không được để trống."
        );

        this.location = location == null ? "" : location;
        this.interviewer = interviewer == null ? "" : interviewer;
        this.result = "";
        this.notes = "";
    }

    public String getInterviewId() {
        return interviewId;
    }

    public Application getApplication() {
        return application;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getLocation() {
        return location;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public String getResult() {
        return result;
    }

    public String getNotes() {
        return notes;
    }

    public boolean hasResult() {
        return result != null && !result.isBlank();
    }

    public void recordResult(String result, String notes) {
        this.result = result == null ? "" : result;
        this.notes = notes == null ? "" : notes;
    }
}
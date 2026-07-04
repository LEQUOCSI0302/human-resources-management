package com.hrm.recruitment.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class RecruitmentNotification {
    private final String notificationId;
    private final Application application;
    private final String type;
    private final String content;
    private final LocalDateTime createdAt;
    private String status;

    public RecruitmentNotification(
            String notificationId,
            Application application,
            String type,
            String content
    ) {
        this.notificationId = Objects.requireNonNull(
                notificationId,
                "Mã thông báo không được để trống."
        );

        this.application = Objects.requireNonNull(
                application,
                "Hồ sơ ứng tuyển không được để trống."
        );

        this.type = type == null ? "" : type;
        this.content = content == null ? "" : content;
        this.createdAt = LocalDateTime.now();
        this.status = "DA_GUI";
    }

    public String getNotificationId() {
        return notificationId;
    }

    public Application getApplication() {
        return application;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void markSent() {
        this.status = "DA_GUI";
    }
}
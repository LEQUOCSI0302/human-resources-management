package com.hrm.recruitment.repository;

import com.hrm.recruitment.model.RecruitmentNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private final List<RecruitmentNotification> notifications = new ArrayList<>();

    public void save(RecruitmentNotification notification) {
        notifications.add(notification);
    }

    public List<RecruitmentNotification> findByApplicationId(String applicationId) {
        List<RecruitmentNotification> result = new ArrayList<>();

        if (applicationId == null) {
            return result;
        }

        for (RecruitmentNotification notification : notifications) {
            if (applicationId.equalsIgnoreCase(
                    notification.getApplication().getApplicationId()
            )) {
                result.add(notification);
            }
        }

        return result;
    }

    public List<RecruitmentNotification> findAll() {
        return new ArrayList<>(notifications);
    }
}

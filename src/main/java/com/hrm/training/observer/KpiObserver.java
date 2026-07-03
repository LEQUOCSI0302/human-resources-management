package com.hrm.training.observer;

import com.hrm.profile.model.EmployeeProfile;

public interface KpiObserver {
    void onKpiChanged(EmployeeProfile emp, String month, double oldScore, double newScore);
}

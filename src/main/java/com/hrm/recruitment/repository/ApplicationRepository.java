package com.hrm.recruitment.repository;

import com.hrm.recruitment.model.Application;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {
    private final List<Application> applications = new ArrayList<>();

    public void save(Application application) {
        applications.add(application);
    }

    public Application findById(String applicationId) {
        if (applicationId == null) {
            return null;
        }

        for (Application application : applications) {
            if (applicationId.equalsIgnoreCase(application.getApplicationId())) {
                return application;
            }
        }

        return null;
    }

    public boolean existsByCandidateAndJob(String candidateId, String jobId) {
        if (candidateId == null || jobId == null) {
            return false;
        }

        for (Application application : applications) {
            boolean sameCandidate = candidateId.equalsIgnoreCase(
                    application.getCandidate().getId()
            );

            boolean sameJob = jobId.equalsIgnoreCase(
                    application.getJobPosting().getJobId()
            );

            if (sameCandidate && sameJob) {
                return true;
            }
        }

        return false;
    }

    public List<Application> findByJobId(String jobId) {
        List<Application> result = new ArrayList<>();

        if (jobId == null) {
            return result;
        }

        for (Application application : applications) {
            if (jobId.equalsIgnoreCase(application.getJobPosting().getJobId())) {
                result.add(application);
            }
        }

        return result;
    }

    public List<Application> findAll() {
        return new ArrayList<>(applications);
    }
}
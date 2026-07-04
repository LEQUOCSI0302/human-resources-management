package com.hrm.recruitment.repository;

import com.hrm.recruitment.model.JobPosting;
import com.hrm.recruitment.model.JobPostingStatus;

import java.util.ArrayList;
import java.util.List;

public class JobPostingRepository {
    private final List<JobPosting> jobPostings = new ArrayList<>();

    public void save(JobPosting jobPosting) {
        jobPostings.add(jobPosting);
    }

    public JobPosting findById(String jobId) {
        if (jobId == null) {
            return null;
        }

        for (JobPosting jobPosting : jobPostings) {
            if (jobPosting.getJobId().equalsIgnoreCase(jobId)) {
                return jobPosting;
            }
        }

        return null;
    }

    public List<JobPosting> findAll() {
        return new ArrayList<>(jobPostings);
    }

    public List<JobPosting> findAllActive() {
        List<JobPosting> result = new ArrayList<>();

        for (JobPosting jobPosting : jobPostings) {
            if (jobPosting.isActive()) {
                result.add(jobPosting);
            }
        }

        return result;
    }

    public boolean updateStatus(String jobId, JobPostingStatus status) {
        JobPosting jobPosting = findById(jobId);

        if (jobPosting == null) {
            return false;
        }

        jobPosting.changeStatus(status);
        return true;
    }
}
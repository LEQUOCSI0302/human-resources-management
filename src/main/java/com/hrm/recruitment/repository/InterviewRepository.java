package com.hrm.recruitment.repository;

import com.hrm.recruitment.model.Interview;

import java.util.ArrayList;
import java.util.List;

public class InterviewRepository {
    private final List<Interview> interviews = new ArrayList<>();

    public void save(Interview interview) {
        interviews.add(interview);
    }

    public Interview findById(String interviewId) {
        if (interviewId == null) {
            return null;
        }

        for (Interview interview : interviews) {
            if (interviewId.equalsIgnoreCase(interview.getInterviewId())) {
                return interview;
            }
        }

        return null;
    }

    public List<Interview> findByApplicationId(String applicationId) {
        List<Interview> result = new ArrayList<>();

        if (applicationId == null) {
            return result;
        }

        for (Interview interview : interviews) {
            if (applicationId.equalsIgnoreCase(
                    interview.getApplication().getApplicationId()
            )) {
                result.add(interview);
            }
        }

        return result;
    }

    public List<Interview> findAll() {
        return new ArrayList<>(interviews);
    }
}
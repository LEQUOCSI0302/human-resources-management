package com.hrm.recruitment.repository;

import com.hrm.recruitment.model.Candidate;

import java.util.ArrayList;
import java.util.List;

public class CandidateRepository {
    private final List<Candidate> candidates = new ArrayList<>();

    public void save(Candidate candidate) {
        Candidate existing = findById(candidate.getId());

        if (existing == null) {
            candidates.add(candidate);
        }
    }

    public Candidate findById(String candidateId) {
        if (candidateId == null) {
            return null;
        }

        for (Candidate candidate : candidates) {
            if (candidateId.equalsIgnoreCase(candidate.getId())) {
                return candidate;
            }
        }

        return null;
    }

    public Candidate findByEmail(String email) {
        if (email == null) {
            return null;
        }

        for (Candidate candidate : candidates) {
            if (email.equalsIgnoreCase(candidate.getEmail())) {
                return candidate;
            }
        }

        return null;
    }

    public List<Candidate> findAll() {
        return new ArrayList<>(candidates);
    }
}
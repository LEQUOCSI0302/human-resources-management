package com.hrm.recruitment.builder;

import com.hrm.recruitment.model.Candidate;

public class CandidateBuilder {
    private String id, name, cvPath;
    private Candidate candidate = new Candidate();

    public CandidateBuilder setId(String id) { this.id = id; return this; }
    public CandidateBuilder setName(String name) { this.name = name; return this; }
    public CandidateBuilder setCv(String path) { this.cvPath = path; return this; }

    public Candidate build() {
        return candidate;
    }
}

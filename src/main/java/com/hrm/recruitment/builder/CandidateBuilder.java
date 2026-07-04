package com.hrm.recruitment.builder;

import com.hrm.recruitment.model.Candidate;

public class CandidateBuilder {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String cvPath;

    public CandidateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CandidateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CandidateBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public CandidateBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CandidateBuilder setCv(String path) {
        this.cvPath = path;
        return this;
    }

    public Candidate build() {
        return new Candidate(id, name, email, phone, cvPath);
    }
}
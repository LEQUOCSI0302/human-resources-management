package com.hrm.recruitment.model;

public class Candidate {
    private String id, name, cvPath, status;

    public Candidate(String id, String name, String cvPath, String status) {
        this.id = id;
        this.name = name;
        this.cvPath = cvPath;
        this.status = status;
    }

    public Candidate() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCvPath() {
        return cvPath;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

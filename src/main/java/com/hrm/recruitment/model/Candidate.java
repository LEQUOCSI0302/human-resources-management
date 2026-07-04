package com.hrm.recruitment.model;

public class Candidate {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String cvPath;

    private String status;

    public Candidate() {
        this.status = "ACTIVE";
    }

    public Candidate(String id, String name, String cvPath, String status) {
        this(id, name, "", "", cvPath);
        this.status = status == null || status.isBlank() ? "ACTIVE" : status;
    }

    public Candidate(String id, String name, String email, String phone, String cvPath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cvPath = cvPath;
        this.status = "ACTIVE";
    }

    public String getId() {
        return id;
    }

    public String getCandidateId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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

    public void setCandidateId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
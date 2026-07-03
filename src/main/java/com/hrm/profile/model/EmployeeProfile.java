package com.hrm.profile.model;

import com.hrm.profile.model.contract.ISalaryStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class EmployeeProfile {
    private String id;
    private String name;
    private String department;
    private String position;
    private List<Contract> contracts; // Sơ đồ: getContracts: List<Contracts>
    private List<Certificate> certificates;
    private ISalaryStrategy salaryStrategy;

    public EmployeeProfile(String id, String name, String dept, String pos) {
        this.id = id;
        this.name = name;
        this.department = dept;
        this.position = pos;
        this.contracts = new ArrayList<>();
        this.certificates = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Contract> getContracts() { return contracts; }
    public void addContract(Contract c) { this.contracts.add(c); }

    public void setSalaryStrategy(ISalaryStrategy st) { this.salaryStrategy = st; }
    public ISalaryStrategy getSalaryStrategy() { return salaryStrategy; }
    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void addCertificate(Certificate c){
        this.certificates.add(c);
    }
}

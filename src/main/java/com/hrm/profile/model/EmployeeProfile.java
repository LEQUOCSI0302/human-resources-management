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
    private ISalaryStrategy salaryStrategy;

    public EmployeeProfile(String id, String name, String dept, String pos) {
        this.id = id;
        this.name = name;
        this.department = dept;
        this.position = pos;
        this.contracts = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }

    public List<Contract> getContracts() { return contracts; }
    public void addContract(Contract c) { this.contracts.add(c); }

    public void setSalaryStrategy(ISalaryStrategy st) { this.salaryStrategy = st; }
    public ISalaryStrategy getSalaryStrategy() { return salaryStrategy; }
}

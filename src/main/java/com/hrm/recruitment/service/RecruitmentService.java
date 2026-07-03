package com.hrm.recruitment.service;

import com.hrm.profile.factory.EmployeeFactory;
import com.hrm.recruitment.model.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecruitmentService {
    private List<Candidate> recruitmentRepo = new ArrayList<>();
    private EmployeeFactory employeeFactory; // Dùng để chuyển Candidate -> Employee

    public RecruitmentService(EmployeeFactory ef) { this.employeeFactory = ef; }

//    public void hireCandidate(Candidate c, String dept, String pos) {
//        // Chuyển đổi ứng viên thành nhân viên chính thức
//        var emp = employeeFactory.createEmployee(c.getName(), dept, pos);
//        System.out.println("✅ Đã tuyển dụng thành công: " + emp.getName());
//    }

//    public Interview scheduleInterview(String candidateId, String date, String interviewer) {
//        Candidate c = findById(candidateId);
//        Interview i = new Interview(UUID.randomUUID().toString(), c, date, interviewer);
//        return i;
//    }
}

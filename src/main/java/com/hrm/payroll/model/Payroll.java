package com.hrm.payroll.model;

import java.util.ArrayList;
import java.util.List;

public class Payroll {
    private String payrollId;
    private String month;
    private List<PayrollDetail> details;

    public Payroll(String payrollId,String month){
        this.payrollId=payrollId;
        this.month=month;
        details=new ArrayList<>();

    }

    public void addDetail(PayrollDetail detail){
        details.add(detail);

    }

    public List<PayrollDetail> getDetails(){
        return details;

    }

}
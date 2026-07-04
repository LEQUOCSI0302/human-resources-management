package com.hrm.payroll.repository;

import com.hrm.payroll.model.Payroll;

import java.util.ArrayList;
import java.util.List;

public class PayrollRepository {
    private List<Payroll> payrolls=new ArrayList<>();
    public void save(Payroll payroll){
        payrolls.add(payroll);
        System.out.println("DATABASE INSERT PAYROLL");
    }

    public List<Payroll> findAll(){
        return payrolls;

    }

}
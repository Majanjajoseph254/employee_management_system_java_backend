package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Duty;

import java.util.List;

public interface DutyService {
    public Duty assignDutyByAdminToEmployee(Duty duty,Long empid,int adminid);
    public Duty assignDutyByAdminToManager(Duty duty,Long managerid,int adminid);
    public void assignDutyByManagerToEmployee(Duty duty, Long empid, Long managerid);

    public List<Duty>viewAllDutiesofEmployee(Long eid);
    public List<Duty>viewAllDutiesAssignedByManager(Long managerid);
    public List<Duty>viewAllDutiesofAssignedByAdmin(int aid);


}

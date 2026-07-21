package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Leave;

import java.util.List;

public interface LeaveService {
    public Leave applyLeaveByEmployee(Leave leave,Long empid);
    public List<Leave>viewLeavesByEmployee(Long empid);
    public Leave applyLeaveByManager(Leave leave, Long managerid);
    public List<Leave> viewLeavesByManager(Long managerid);
    public String updateLeaveStatus(int leaveid, String status);

    Leave applyLeave(Leave leave);


}

package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Leave;
import com.jcoder.emsbp.model.Manager;
import com.jcoder.emsbp.repository.EmployeeRepository;
import com.jcoder.emsbp.repository.LeaveRepository;
import com.jcoder.emsbp.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Leave applyLeaveByEmployee(Leave leave, Long empid) {
        Employee emp = employeeRepository.findById(empid).orElse(null);
        if(emp != null){
            leave.setEmployee(emp);
            leave.setStatus("Pending");
            return leaveRepository.save(leave);
        }
        return null;
    }

    @Override
    public List<Leave> viewLeavesByEmployee(Long empid) {
        return leaveRepository.findByEmployeeId(empid);
    }

    @Override
    public Leave applyLeaveByManager(Leave leave, Long managerid) {
        Manager manager = managerRepository.findById(managerid).orElse(null);
        if(manager != null){
            leave.setManager(manager);
            leave.setStatus("Pending");
            return leaveRepository.save(leave);
        }
        return null;
    }

    @Override
    public List<Leave> viewLeavesByManager(Long managerid) {
        return leaveRepository.findByManagerId(managerid);
    }

    @Override
    public String updateLeaveStatus(int leaveid, String status) {
        Leave leave = leaveRepository.findById(leaveid).orElse(null);
        if(leave != null){
            leave.setStatus(status.toUpperCase());
            leaveRepository.save(leave);
            return "Leave Status updated to:" + status;
        }
        return "Leave ID not found";
    }

    @Override
    public Leave applyLeave(Leave leave) {
        // Generic apply: persist and mark pending if not set
        if (leave.getStatus() == null || leave.getStatus().isEmpty()) {
            leave.setStatus("Pending");
        }
        return leaveRepository.save(leave);
    }
}

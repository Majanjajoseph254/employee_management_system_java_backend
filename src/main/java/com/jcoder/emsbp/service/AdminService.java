package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.*;

import java.util.List;

public interface AdminService {

    public Admin checkadminlogin(String username, String password);
    public void addManager(Manager manager);
    public List<Manager> viewAllManagers();
    public String deleteManager(Long mid);
    public List<Employee>viewAllEmployees();
    public String deleteEmployee(Long eid);


    Long managercount();

    Long employeecount();

    public String assigndutyToManager(Duty duty, Long managerId);
    public String assigndutyToEmployee(Duty duty, Long employeeId);
    public List<Leave> viewAllLeaveApplications();


    void updateAccountStatus(Long empid, String status);


}

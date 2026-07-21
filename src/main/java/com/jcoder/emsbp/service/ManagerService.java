package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Manager;

import java.util.List;

public interface ManagerService {
    public Manager checkmanagerlogin(String username, String password);
    public Manager findManagerById(Long id);
    public Manager findManagerByUsername(String username);
    public Manager findManagerByEmail(String email);
    public List<Manager> viewAllManagers();
    public List<Employee> viewAllEmployees();
    public void updateEmployeeAccountStatus(Long employeeid, String status);

    public String generateResetToken(String email);
    public boolean validateResetToken(String token);
    public boolean changePassword(Manager manager,String oldPassword,String newPassword);
    public void updatePassword(String token,String newPassword);
    public void deleteResetToken(String token);
    public boolean isTokenExpired(String token);
}

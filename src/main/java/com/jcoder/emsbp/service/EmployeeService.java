package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;

import java.util.List;

public interface EmployeeService {
    public Employee checkemplogin(String username,String password);
    public String registerEmployee(Employee emp);
    public String updateEmployeeProfile(Employee emp);
    public Employee findEmployeeById(Long id);
    public Employee findEmployeeByUsername(String username);
    public Employee findEmployeeByEmail(String email);

    public List<Employee> viewAllEmployees();
    public String updateAccountStatus(Long empid, String status);
    public List<Duty> viewAssignedDuties(Long empid);

    public String generateResetToken(String email);
    public boolean validateResetToken(String token);
    public boolean changePassword(Employee employee, String oldPassword, String newPassword);
    public void updatePassword(String token,String newPassword);
    public void deleteResetToken(String token);
    public boolean isTokenExpired(String token);


    Employee checkemployeelogin(String identifier, String password);
}

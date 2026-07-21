package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.ResetToken;
import com.jcoder.emsbp.repository.DutyRepository;
import com.jcoder.emsbp.repository.EmployeeRepository;
import com.jcoder.emsbp.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DutyRepository dutyRepository;
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Override
    public Employee checkemplogin(String username, String password) {
        return employeeRepository.findEmployeeByUsernameAndPassword(username, password);
    }

    @Override
    public Employee checkemployeelogin(String identifier, String password) {
        // Try to find by username + password
        Employee emp = employeeRepository.findEmployeeByUsernameAndPassword(identifier, password);
        if (emp != null) return emp;

        // Try to find by email and password
        Optional<Employee> byEmail = employeeRepository.findByEmail(identifier);
        if (byEmail.isPresent() && byEmail.get().getPassword().equals(password)) {
            return byEmail.get();
        }
        return null;
    }

    @Override
    public String registerEmployee(Employee emp) {
        Long eid = generateRandomEmployeeId();
        emp.setId(eid);
        String randomPassword = generatedRandomPassword();
        emp.setPassword(randomPassword);
        emp.setAccountstatus("Pending");
        // persist the new employee
        employeeRepository.save(emp);
        return "Employee registered successfully with id: " + emp.getID();
    }

    @Override
    public String updateEmployeeProfile(Employee emp) {
        employeeRepository.save(emp);
        return "Employee profile updated successfully!!...";
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee findEmployeeByUsername(String username) {
        return employeeRepository.findEmployeeByUsername(username);
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public List<Employee> viewAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String updateAccountStatus(Long empid, String status) {
        Optional<Employee> emp = employeeRepository.findById(empid);
        if(emp.isPresent()){
            Employee e = emp.get();
            e.setAccountstatus(status);
            employeeRepository.save(e);
            return "Account status updated successfully!!...";
        }
        return "ID Not Found!";
    }

    @Override
    public List<Duty> viewAssignedDuties(Long empid) {
        Employee emp = employeeRepository.findById(empid).orElse(null);
        if(emp != null) {
            return dutyRepository.findDutiesByEmployee(emp);
        }
        return Collections.emptyList();
    }



    @Override
    public String generateResetToken(String email) {
        Optional<Employee> manager = employeeRepository.findByEmail(email);
        if(manager.isPresent()){
            String token= UUID.randomUUID().toString();
            ResetToken rt = new ResetToken();

            rt.setToken(token);
            rt.setEmail(email);
            rt.setCreatedAt(LocalDateTime.now());
            rt.setExpiresAt(LocalDateTime.now().plusMinutes(15));

            resetTokenRepository.save(rt);
            return token;

        }
        return null;
    }

    @Override
    public boolean validateResetToken(String token) {
        Optional<ResetToken> rt = resetTokenRepository.findByToken(token);
        return rt.isPresent() && !isTokenExpired(token);
    }

    @Override
    public boolean changePassword(Employee employee, String oldPassword, String newPassword) {
        if(employee.getPassword().equals(oldPassword)){
            employee.setPassword(newPassword);
            employeeRepository.save(employee);
            return true;
        }
        return false;
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        if(resetToken.isPresent() && !isTokenExpired(token)){
            String email = resetToken.get().getEmail();
            Optional<Employee> employee = employeeRepository.findByEmail(email);
            if(employee.isPresent()){
                Employee e = employee.get();
                e.setPassword(newPassword);
                employeeRepository.save(e);
                deleteResetToken(token);
            }

        }

    }

    @Override
    public void deleteResetToken(String token) {
        resetTokenRepository.deleteByToken(token);

    }

    @Override
    public boolean isTokenExpired(String token) {
        Optional<ResetToken> rt = resetTokenRepository.findByToken(token);
        return rt.map(resetToken -> resetToken.getExpiresAt().isBefore(LocalDateTime.now())).orElse(true);
    }
    private Long generateRandomEmployeeId(){
        Random random = new Random();

        return (long) (1000 + random.nextInt(9000));
    }
    private String generatedRandomPassword(){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        String combined = upper + lower + digits + special;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(special.charAt(random.nextInt(special.length())));

        for(int i = 4; i< 8; i++){
            sb.append(combined.charAt(random.nextInt(combined.length())));

        }
        return sb.toString();
    }

}

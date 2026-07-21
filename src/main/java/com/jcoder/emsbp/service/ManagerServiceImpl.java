package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Manager;
import com.jcoder.emsbp.model.ResetToken;
import com.jcoder.emsbp.repository.EmployeeRepository;
import com.jcoder.emsbp.repository.ManagerRepository;
import com.jcoder.emsbp.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Override
    public Manager checkmanagerlogin(String username, String password) {
        return managerRepository.findManagerByUsernameAndPassword(username,password);
    }

    @Override
    public Manager findManagerById(Long id) {
        return managerRepository.findManagerById(id);
    }

    @Override
    public Manager findManagerByUsername(String username) {
        return managerRepository.findManagerByUsername(username);
    }

    @Override
    public Manager findManagerByEmail(String email) {
        return managerRepository.findManagerByEmail(email);
    }

    @Override
    public List<Manager> viewAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public List<Employee> viewAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void updateEmployeeAccountStatus(Long employeeid, String status) {
        Optional<Employee> emp = employeeRepository.findById(employeeid);
        if (emp.isPresent()) {
            Employee e = emp.get();
            e.setAccountstatus(status);
            employeeRepository.save(e);
        }
    }

    @Override
    public String generateResetToken(String email) {
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isPresent()) {
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
    public boolean changePassword(Manager manager, String oldPassword, String newPassword) {
        if(manager.getPassword().equals(oldPassword)){
            manager.setPassword(newPassword);
            managerRepository.save(manager);
            return true;
        }
        return false;
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        if (resetToken.isPresent() && !isTokenExpired(token)) {
            String email = resetToken.get().getEmail();
            Optional<Manager> manager = managerRepository.findByEmail(email);
            if (manager.isPresent()) {
                Manager m = manager.get();
                m.setPassword(newPassword);
                managerRepository.save(m);
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
}

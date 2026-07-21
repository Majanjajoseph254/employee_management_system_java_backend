package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.*;
import com.jcoder.emsbp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public Admin checkadminlogin(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public void addManager(Manager manager) {
        Long manager_id=generateRandomManagerId();
        String password=generatedRandomPassword();

        manager.setId(manager_id);
        manager.setPassword(password);
        Manager savedManager = managerRepository.save(manager);

        Email e = new Email();
        e.setRecipient(manager.getEmail());
        e.setSubject("Welcome Manager to EMS!");
        e.setMessage("Hi!" + manager.getName() + "\n\nYour account has been created successfully.\n\n"+"Your Id is:"+manager.getId()+"\n\nYour Password is:"+manager.getPassword()+"\n\nPlease keep this information secure and do not share it with anyone.\n\nThank you.");
        emailRepository.save(e);
        emailService.sendEmail(manager.getEmail(), e.getSubject(), e.getMessage());

    }

    @Override
    public List<Manager> viewAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public String deleteManager(Long mid) {
        Optional<Manager> manager = managerRepository.findById((long) mid);
        if(manager.isPresent()){
            managerRepository.deleteById(mid);
            return "Manager deleted successfully...";
        }
        else{
            return "Manager Not Found...";
        }
    }

    @Override
    public List<Employee> viewAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String deleteEmployee(Long eid) {
        Optional<Employee> employee = employeeRepository.findById((long) eid);
        if(employee.isPresent()){
            employeeRepository.deleteById(eid);
            return "Employee deleted successfully...";
        }
        else{
            return "Employee Not Found...";
        }
    }

    @Override
    public Long managercount() {
        return managerRepository.count();
    }

    @Override
    public Long employeecount() {
        return employeeRepository.count();
    }

    @Autowired
    private DutyService dutyService;

    @Override
    public String assigndutyToManager(Duty duty, Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        Optional<Admin> admin = adminRepository.findById(1);
        if (manager.isPresent() && admin.isPresent()) {
            Duty saved = dutyService.assignDutyByAdminToManager(duty, managerId, admin.get().getId());
            return saved != null ? "Duty assigned to manager successfully" : "Failed to assign duty";
        }
        return "Manager or Admin not found";
    }

    @Override
    public String assigndutyToEmployee(Duty duty, Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Optional<Admin> admin = adminRepository.findById(1);
        if (employee.isPresent() && admin.isPresent()) {
            Duty saved = dutyService.assignDutyByAdminToEmployee(duty, employeeId, admin.get().getId());
            return saved != null ? "Duty assigned to employee successfully" : "Failed to assign duty";
        }
        return "Employee or Admin not found";
    }

    @Override
    public List<Leave> viewAllLeaveApplications() {
        return leaveRepository.findAll();
    }

    @Override
    public void updateAccountStatus(Long empid, String status) {
        Optional<Employee> empOpt = employeeRepository.findById(empid);
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            emp.setAccountstatus(status);
            employeeRepository.save(emp);
        }
    }

    private Long generateRandomManagerId(){
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

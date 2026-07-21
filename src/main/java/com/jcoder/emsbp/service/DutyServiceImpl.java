package com.jcoder.emsbp.service;

import com.jcoder.emsbp.model.Admin;
import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Manager;
import com.jcoder.emsbp.repository.AdminRepository;
import com.jcoder.emsbp.repository.DutyRepository;
import com.jcoder.emsbp.repository.EmployeeRepository;
import com.jcoder.emsbp.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DutyServiceImpl implements DutyService {
    @Autowired
    private DutyRepository dutyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Override
    public Duty assignDutyByAdminToEmployee(Duty duty, Long empid, int adminid) {
        Employee emp = employeeRepository.findEmployeeById(empid);
        Admin admin = adminRepository.findAdminById(adminid);
        if(emp !=null && admin !=null){
            duty.setEmployee(emp);
            duty.setAssignedByAdmin(admin);

            return dutyRepository.save(duty);
        }

        return null;
    }

    @Override
    public Duty assignDutyByAdminToManager(Duty duty, Long managerid, int adminid) {
        Manager manager = managerRepository.findManagerById(managerid);
        Admin admin = adminRepository.findAdminById(adminid);
        if(manager !=null && admin !=null){
            duty.setManager(manager);
            duty.setAssignedByAdmin(admin);

            return dutyRepository.save(duty);
        }

        return null;
    }

    @Override
    public void assignDutyByManagerToEmployee(Duty duty, Long empid, Long managerid) {
        Employee emp = employeeRepository.findEmployeeById(empid);
        Manager manager = managerRepository.findManagerById(managerid);
        if(emp !=null && manager !=null){
            duty.setEmployee(emp);
            duty.setManager(manager);

            dutyRepository.save(duty);
        }

    }

    @Override
    public List<Duty> viewAllDutiesofEmployee(Long eid) {
        return dutyRepository.findByEmployeeId(eid);
    }

    @Override
    public List<Duty> viewAllDutiesAssignedByManager(Long managerid) {
        return dutyRepository.findAssignedByManagerId(managerid);
    }



    @Override
    public List<Duty> viewAllDutiesofAssignedByAdmin(int aid) {
        return dutyRepository.findAssignedByAdmin(aid);
    }
}

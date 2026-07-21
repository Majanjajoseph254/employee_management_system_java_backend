package com.jcoder.emsbp.controller;

import com.jcoder.emsbp.dto.LoginRequest;
import com.jcoder.emsbp.model.Admin;
import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Manager;
import com.jcoder.emsbp.security.JWTUtilizer;
import com.jcoder.emsbp.service.AdminService;
import com.jcoder.emsbp.service.EmployeeService;
import com.jcoder.emsbp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/checkapi")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JWTUtilizer jwtService;

    @GetMapping("/")
    public String home(){
        return "Employee Management System Backend Project is Running...";
    }

    @PostMapping("/checklogin")
    public ResponseEntity<?>login(@RequestBody LoginRequest loginRequest){
        String identifier = loginRequest.getIdentifier();
        String password = loginRequest.getPassword();

        Admin admin = adminService.checkadminlogin(identifier, password);
        Manager manager = managerService.checkmanagerlogin(identifier, password);
        Employee employee = employeeService.checkemployeelogin(identifier, password);


        if (admin != null) {
            String token = jwtService.generateJWTToken(admin.getUsername(), "admin");
            Map<String, Object>res= new HashMap<String,Object>();
            res.put("role","admin");
            res.put("message","Login successfully");
            res.put("data","admin");
            res.put("token", token);

            return ResponseEntity.ok(res);

        }

        if(manager!=null){
            String token = jwtService.generateJWTToken(manager.getUsername(), "manager");
            Map<String, Object>res= new HashMap<String,Object>();
            res.put("role","manager");
            res.put("message","Login successfully");
            res.put("data","manager");
            res.put("token", token);

            return ResponseEntity.ok(res);
        }
        if(employee!=null){
            if(employee.getAccountstatus().equalsIgnoreCase("Accepted")){
                String token = jwtService.generateJWTToken(employee.getUsername(), "employee");
                Map<String, Object>res= new HashMap<String,Object>();
                res.put("role","employee");
                res.put("message","Login successfully");
                res.put("data","employee");
                res.put("token", token);

                return ResponseEntity.ok(res);
            }
            else {
                return ResponseEntity.status(403).body("Your account is not accepted yet. Please contact the administrator.");
            }
        }
    return ResponseEntity.status(401).body("Invalid credentials. Please check your username and password.");
    }

}

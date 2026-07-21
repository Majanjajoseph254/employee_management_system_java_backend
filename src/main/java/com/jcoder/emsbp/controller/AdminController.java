package com.jcoder.emsbp.controller;

import com.jcoder.emsbp.model.*;
import com.jcoder.emsbp.security.JWTUtilizer;
import com.jcoder.emsbp.service.AdminService;
import com.jcoder.emsbp.service.DutyService;
import com.jcoder.emsbp.service.EmployeeService;
import com.jcoder.emsbp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DutyService dutyService;

    @Autowired
    private JWTUtilizer jwtService;

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @PostMapping("/addManager")
    public ResponseEntity<String> addManager(@RequestBody Manager manager, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        adminService.addManager(manager);
        return ResponseEntity.ok("Manager added successfully" + manager.getId());
    }

    @GetMapping("/viewallmanagers")
    public ResponseEntity<String> viewAllManagers(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(adminService.viewAllManagers().toString());
    }

    @GetMapping("/viewallemployees")
    public ResponseEntity<String> viewAllEmployees(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(adminService.viewAllEmployees().toString());
    }

    @PutMapping("/assigndutytomanager")
    public ResponseEntity<String> assignDutyToManager(@RequestBody Duty duty,
                                                      @RequestParam Long managerId,
                                                      @RequestParam int adminId,
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access Denied, Admin privileges required!");
        }

        Duty saved = dutyService.assignDutyByAdminToManager(duty, managerId, adminId);
        if (saved == null) {
            return ResponseEntity.status(400).body("Failed to assign duty: invalid manager or admin id");
        }

        return ResponseEntity.ok("Duty assigned successfully to manager id " + managerId + " with duty id: " + saved.getId());
    }

    @PutMapping("/updateAccountStatus")
    public ResponseEntity<String> updateAccountStatus(@RequestParam Long empid, @RequestParam String status, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access Denied, Admin privileges required!");
        }
        adminService.updateAccountStatus(empid, status);
        return ResponseEntity.ok("Account status updated successfully");
    }

    @PutMapping("/assigndutytoemployee")
    public ResponseEntity<String> assignDutyToEmployee(@RequestBody Duty duty,
                                                       @RequestParam Long employeeId,
                                                       @RequestParam int adminId,
                                                       @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid authorization header");
        }

        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access Denied, Admin privileges required!");
        }

        Duty saved = dutyService.assignDutyByAdminToEmployee(duty, employeeId, adminId);
        if (saved == null) {
            return ResponseEntity.status(400).body("Failed to assign duty: invalid employee or admin id");
        }

        return ResponseEntity.ok("Duty assigned successfully to employee id " + employeeId + " with duty id: " + saved.getId());
    }

    @GetMapping("/viewallapplications")
    public ResponseEntity<List<Leave>> viewAllLeaveApplications(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Leave> leaves = adminService.viewAllLeaveApplications();
        return ResponseEntity.ok(leaves);
    }

    @DeleteMapping("/deleteemployee")
    public ResponseEntity<String> deleteEmployee(@RequestParam Long empid, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access Denied, Admin privileges required!");
        }

        String message = adminService.deleteEmployee(empid);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/deleteemanager")
    public ResponseEntity<String> deleteManager(@RequestParam Long mid, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body("Access Denied, Admin privileges required!");
        }

        String message = adminService.deleteManager(mid);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/viewemployeeduties")
    public ResponseEntity<List<Duty>> viewEmployeeAssignedDuties(@RequestParam Long eid, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Duty> duties = dutyService.viewAllDutiesofEmployee(eid);
        return ResponseEntity.ok(duties);
    }

    @GetMapping("/viewmanagerduties")
    public ResponseEntity<List<Duty>> viewDutiesAssignedByManager(@RequestParam Long mid, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Duty> duties = dutyService.viewAllDutiesAssignedByManager(mid);
        return ResponseEntity.ok(duties);
    }

    @GetMapping("/viewadminduties")
    public ResponseEntity<List<Duty>> viewDutiesAssignedByAdmin(@RequestParam int aid, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Duty> duties = dutyService.viewAllDutiesofAssignedByAdmin(aid);
        return ResponseEntity.ok(duties);
    }

    @GetMapping("/getmanagercount")
    public ResponseEntity<Long> getManagerCount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        Long count = adminService.managercount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/getemployeecount")
    public ResponseEntity<Long> getEmployeeCount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("admin")) {
            return ResponseEntity.status(403).body(null);
        }
        Long count = adminService.employeecount();
        return ResponseEntity.ok(count);

    }
}
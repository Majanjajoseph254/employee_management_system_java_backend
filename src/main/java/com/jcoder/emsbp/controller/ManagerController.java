package com.jcoder.emsbp.controller;

import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Leave;
import com.jcoder.emsbp.security.JWTUtilizer;
import com.jcoder.emsbp.service.DutyService;
import com.jcoder.emsbp.service.LeaveService;
import com.jcoder.emsbp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@CrossOrigin("*")
public class ManagerController {
    @Autowired
    private JWTUtilizer jwtService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private DutyService dutyService;

    private boolean isAuthorized(String authHeader){
        try {
            String token = authHeader.substring(7);
            String role = jwtService.validateToken(token).get("role");
            return !role.equals("manager");
        } catch (Exception e) {
            return true;
        }
    }

    @GetMapping("/viewallemployees")
    public ResponseEntity<List<Employee>>viewAllEmployees(@RequestHeader("Authorization") String authHeader){
        if(isAuthorized(authHeader)){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok().body(managerService.viewAllEmployees());
    }

    @PutMapping("/updateAccountStatus")
    public ResponseEntity<String> updateEmployeeAccountStatus(@RequestParam Long empid, @RequestParam String status, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("manager")) {
            return ResponseEntity.status(403).body("Access Denied, Manager privileges required!");
        }
        managerService.updateEmployeeAccountStatus(empid, status);
        return ResponseEntity.ok("Account status updated successfully");
    }
    @PostMapping("/applyleave")
    public ResponseEntity<Leave>applyLeave(@RequestBody Leave leave,@RequestParam Long managerid, @RequestHeader("Authorization") String authHeader){
        if(isAuthorized(authHeader)){
            return ResponseEntity.status(403).body(null);
        }

        Leave appliedLeave = leaveService.applyLeaveByManager(leave, managerid);
        return ResponseEntity.ok(appliedLeave);
    }

    @GetMapping("/viewownleaves")
    public ResponseEntity<List<Leave>>viewLeavesByManager(@RequestHeader("Authorization") String authHeader, @RequestParam Long managerid){
        if(isAuthorized(authHeader)){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok().body(leaveService.viewLeavesByManager(managerid));
    }
    @PutMapping("/updateleavestatus")
    public ResponseEntity<String> updateLeaveStatus(@RequestParam int leaveid, @RequestParam String status, @RequestHeader("Authorization") String authHeader){
        if(isAuthorized(authHeader)){
            return ResponseEntity.status(403).body(null);
        }
        leaveService.updateLeaveStatus(leaveid, status);
        return ResponseEntity.ok("Leave status updated successfully");
    }
    @PostMapping("/assigndutytoemployee")
    public ResponseEntity<String> assignDutyToEmployee(@RequestBody Duty duty,@RequestParam Long managerid,@RequestParam Long empid,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if (!jwtService.validateToken(token).get("role").equals("manager")) {
            return ResponseEntity.status(403).body("Access Denied, Manager privileges required!");
        }
        dutyService.assignDutyByManagerToEmployee(duty, empid, managerid);
        return ResponseEntity.ok("Duty assigned successfully");
    }
    @GetMapping("/viewalldutiesassignedtomanager")
    public ResponseEntity<List<Duty>>viewAllDutiesAssignedToManager(@RequestHeader("Authorization") String authHeader, @RequestParam Long managerid){
        if(isAuthorized(authHeader)){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok().body(dutyService.viewAllDutiesAssignedByManager(managerid));
    }
}


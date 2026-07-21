
package com.jcoder.emsbp.controller;

import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;
import com.jcoder.emsbp.model.Leave;
import com.jcoder.emsbp.security.JWTUtilizer;
import com.jcoder.emsbp.service.EmployeeService;
import com.jcoder.emsbp.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class EmployeeController {
    @Autowired
    private JWTUtilizer jwtService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveService leaveService;

    private boolean isAuthorized(String authHeader, String expectedRole) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;
            String token = authHeader.substring(7);
            String role = jwtService.validateToken(token).get("role");
            return expectedRole.equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping(value = "/addemployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerEmployee(
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam int age,
            @RequestParam String designation,
            @RequestParam String department,
            @RequestParam double salary,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String contact,
            @RequestParam MultipartFile photo,
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ) {
        try {
            // If registration should be restricted, uncomment and adjust role check
            // if (!isAuthorized(authHeader, "admin")) {
            //     return ResponseEntity.status(403).body("Access denied");
            // }

            Employee emp = new Employee();
            emp.setName(name);
            emp.setGender(gender);
            emp.setAge(age);
            emp.setDesignation(designation);
            emp.setDepartment(department);
            emp.setSalary(salary);
            emp.setUsername(username);
            emp.setEmail(email);
            emp.setContact(contact);
            if (photo != null) {
                emp.setPhoto(photo.getBytes());
            }

            String result = employeeService.registerEmployee(emp);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering employee: " + e.getMessage());
        }
    }

    @GetMapping("/viewprofile")
    public ResponseEntity<Employee>viewEmpProfile(@RequestParam Long empid,@RequestHeader(name = "Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "employee"))  {
            return ResponseEntity.status(403).body(null);
        }
        Employee emp = employeeService.findEmployeeById(empid);
        if (emp != null) {
            return ResponseEntity.ok(emp);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/viewassignedduties")
    public ResponseEntity<List<Duty>> viewAssignedDuties(@RequestParam Long empid, @RequestHeader(name = "Authorization") String authHeader) {
        if (!isAuthorized(authHeader, "employee")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Duty> duties = employeeService.viewAssignedDuties(empid);
        return ResponseEntity.ok(duties);
    }

    @PostMapping("/applyleave")
    public ResponseEntity<Leave>applyLeave(@RequestBody Leave leave, @RequestParam Long empid, @RequestHeader("Authorization") String authHeader){
        if(!isAuthorized(authHeader,"employee")){
            return ResponseEntity.status(403).body(null);
        }

        Leave appliedLeave = leaveService.applyLeaveByEmployee(leave, empid);
        return ResponseEntity.ok(appliedLeave);
    }

    @GetMapping("/viewownleaves")
    public ResponseEntity<List<Leave>>viewLeavesByEmployee(@RequestHeader("Authorization") String authHeader, @RequestParam Long empid){
        if(!isAuthorized(authHeader,"employee")){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok().body(leaveService.viewLeavesByEmployee(empid));
    }

}

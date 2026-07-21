package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public List<Employee> findByNameContainingIgnoreCase(String name);

    Employee findEmployeeByUsernameAndPassword(String username, String password);
    Employee findEmployeeById(Long id);
    Employee findEmployeeByUsername(String username);
    Employee findEmployeeByEmail(String email);

    Optional<Employee> findByEmail(String email);
}

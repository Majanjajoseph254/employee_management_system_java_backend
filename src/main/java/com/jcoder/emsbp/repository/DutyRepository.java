package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.Duty;
import com.jcoder.emsbp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Integer> {
    public List<Duty> findDutiesByEmployee(Employee emp);
    public List<Duty> findByEmployeeId(Long id);
    public List<Duty> findAssignedByManagerId(Long managerid);



    @Query("SELECT d FROM Duty d WHERE d.assignedByAdmin = :adminId") // Use your actual entity field name here
    List<Duty> findAssignedByAdmin(@Param("adminId") int adminId);
}

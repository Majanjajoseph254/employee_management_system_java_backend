package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeaveRepository extends JpaRepository<Leave,Integer> {
public List<Leave> findByEmployeeId(Long empid);
public List<Leave> findByStatus(String status);
public List<Leave> findByManagerId(Long managerid);
}

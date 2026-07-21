package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    public Admin findByUsernameAndPassword(String username,String password);

    Admin findAdminById(int adminid);
}

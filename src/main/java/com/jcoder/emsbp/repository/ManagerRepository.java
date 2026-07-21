package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    public Manager findManagerByUsernameAndPassword(String username, String password);
    public Manager findManagerById(Long id);
    public Manager findManagerByUsername(String username);
    public Manager findManagerByEmail(String email);

    public Optional<Manager> findByEmail(String email);

}

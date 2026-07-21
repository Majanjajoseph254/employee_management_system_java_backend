package com.jcoder.emsbp.repository;

import com.jcoder.emsbp.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    @Query("select r from ResetToken r where r.token = ?1" )
    Optional<ResetToken> findByToken(String token);

    Optional<ResetToken> findByEmail(String email);

    void deleteByToken(String token);
}

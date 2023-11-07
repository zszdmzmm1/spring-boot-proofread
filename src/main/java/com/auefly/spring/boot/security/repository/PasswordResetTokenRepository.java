package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenOrderByIdIdDesc(String token);
}

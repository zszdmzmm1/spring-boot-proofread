package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenOrderByIdDesc(String token);

    PasswordResetToken findByToken(String token);

    @Modifying
    @Query(value = "update PasswordResetToken prt set prt.expirationDate=CURRENT_TIMESTAMP() where prt.token = :token")
    int updateExpirationDateForThisToken(@Param(value = "token") String token);
}

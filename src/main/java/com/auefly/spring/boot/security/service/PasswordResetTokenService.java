package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.entity.PasswordResetToken;
import org.springframework.transaction.annotation.Transactional;


public interface PasswordResetTokenService {
    PasswordResetToken save(PasswordResetToken passwordResetToken);

    PasswordResetToken findByTokenOrderByIdDesc(String token);

    PasswordResetToken findByToken(String token);

    @Transactional
    int expireThisToken(String token);
}

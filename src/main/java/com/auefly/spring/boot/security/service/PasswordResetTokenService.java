package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.entity.PasswordResetToken;


public interface PasswordResetTokenService {
    PasswordResetToken save(PasswordResetToken passwordResetToken);
    PasswordResetToken findByTokenOrderByIdDesc(String token);

    PasswordResetToken findByToken(String token);
}

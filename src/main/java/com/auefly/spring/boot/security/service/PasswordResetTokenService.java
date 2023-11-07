package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.entity.PasswordResetToken;


public interface PasswordResetTokenService {
    PasswordResetToken save(PasswordResetToken passwordResetToken);
    PasswordResetToken findByToken(String token);
}

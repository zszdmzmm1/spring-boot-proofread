package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.entity.PasswordResetToken;
import com.auefly.spring.boot.security.repository.PasswordResetTokenRepository;
import com.auefly.spring.boot.security.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Autowired
    PasswordResetTokenRepository repository;

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return repository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken findByTokenOrderByIdDesc(String token) {
        Optional<PasswordResetToken> optionalPasswordResetToken = repository.findByTokenOrderByIdDesc(token);
        return optionalPasswordResetToken.orElse(null);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return repository.findByToken(token);
    }
}

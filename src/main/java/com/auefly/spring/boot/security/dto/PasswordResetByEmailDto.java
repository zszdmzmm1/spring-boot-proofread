package com.auefly.spring.boot.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordResetByEmailDto {
    @NotEmpty(message = "邮箱不可为空！")
    @Email
    private String email;
}

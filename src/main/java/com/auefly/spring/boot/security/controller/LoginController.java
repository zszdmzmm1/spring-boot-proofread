package com.auefly.spring.boot.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableMethodSecurity
public class LoginController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/dashboard")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public String users() {
        return "user";
    }
}

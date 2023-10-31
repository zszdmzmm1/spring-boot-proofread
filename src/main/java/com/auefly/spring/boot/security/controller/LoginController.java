package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String email, @RequestParam String password, HttpSession httpSession) {
        httpSession.setAttribute("user", new User(email, password));
        return "user";
    }
}

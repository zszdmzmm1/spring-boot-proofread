package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.config.BackendProperties;
import com.auefly.spring.boot.security.config.Menus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class BackendController {
    @Autowired
    private BackendProperties backendProperties;

    @ModelAttribute("menus")
    public List<Menus> getMenus(){
        return backendProperties.getMenus();
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "backend/dashboard";
    }

    @GetMapping("/admin/users")
    public String adminUsers() {
        return "backend/dashboard-users";
    }

    @GetMapping("/admin/posts")
    public String adminPosts() {
        return "backend/dashboard-posts";
    }
}
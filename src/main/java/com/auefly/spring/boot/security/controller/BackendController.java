package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.config.BackendProperties;
import com.auefly.spring.boot.security.config.Menus;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@EnableMethodSecurity
public class BackendController {
    @Autowired
    private BackendProperties backendProperties;

    @Autowired
    UserService userService;

    @ModelAttribute("menus")
    public List<Menus> getMenus(){
        return backendProperties.getMenus();
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('admin')")
    public String adminDashboard() {
        return "backend/dashboard";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('admin')")
    public String adminUsers(@RequestParam Optional<Integer> page,
                             @RequestParam Optional<Integer> size,
                             Model model) {
        Page<User> pageContent = userService.findAll(page.orElse(1), size.orElse(10));
        model.addAttribute("page", pageContent);
        return "backend/dashboard-users";
    }

    @GetMapping("/admin/posts")
    @PreAuthorize("hasRole('admin')")
    public String adminPosts() {
        return "backend/dashboard-posts";
    }
}

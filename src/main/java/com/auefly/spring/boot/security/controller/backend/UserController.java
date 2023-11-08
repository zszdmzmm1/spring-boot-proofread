package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller("backendUserController")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('admin')")
    String adminUsers(@RequestParam Optional<Integer> page,
                             @RequestParam Optional<Integer> size,
                             Model model) {
        Page<User> pageContent = userService.findAll(page.orElse(1), size.orElse(10));
        model.addAttribute("page", pageContent);
        return "backend/user/index";
    }
}

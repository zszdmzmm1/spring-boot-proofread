package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.dto.PasswordResetByEmailDto;
import com.auefly.spring.boot.security.dto.UserDto;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/users/dashboard")
    @PreAuthorize("isAuthenticated()")
    String users() {
        return "user";
    }

    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    String registration(@Valid @ModelAttribute("user") UserDto userDto,
                        BindingResult result,
                        Model model,
                        HttpServletRequest httpServletRequest) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "exist", "该邮箱已被注册");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);

        try {
            httpServletRequest.login(userDto.getEmail(), userDto.getPassword());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/users/dashboard";
    }

    @GetMapping("/users/password-reset")
    String passwordReset(Model model) {
        model.addAttribute("resetPassword", new PasswordResetByEmailDto());
        return "password-reset";
    }

    @PostMapping("/users/password-reset")
    String postPasswordReset(@Valid @ModelAttribute("resetPassword") PasswordResetByEmailDto passwordResetByEmailDto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User exisetUser = userService.findUserByEmail(passwordResetByEmailDto.getEmail());
        if(exisetUser == null) {
            result.rejectValue("email", "non-existent", "找不到该邮箱！");
        }

        if(result.hasErrors()) {
            model.addAttribute("resetPassword", passwordResetByEmailDto);
            return "password-reset";
        }

        redirectAttributes.addFlashAttribute("success", "密码重置邮件已发送，请注意查收");
        return "redirect:/users/password-reset";
    }
}

package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.dto.PasswordResetDto;
import com.auefly.spring.boot.security.dto.PasswordResetEmailDto;
import com.auefly.spring.boot.security.dto.UserDto;
import com.auefly.spring.boot.security.entity.PasswordResetToken;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.service.PasswordResetTokenService;
import com.auefly.spring.boot.security.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private JavaMailSenderImpl sender;

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
        model.addAttribute("resetPassword", new PasswordResetEmailDto());
        return "password-reset";
    }

    @PostMapping("/users/password-reset")
    String postPasswordReset(@Valid @ModelAttribute("resetPassword") PasswordResetEmailDto passwordResetEmailDto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User existUser = userService.findUserByEmail(passwordResetEmailDto.getEmail());
        if (existUser == null) {
            result.rejectValue("email", "non-existent", "找不到该邮箱！");
        }

        if (result.hasErrors()) {
            model.addAttribute("resetPassword", passwordResetEmailDto);
            return "password-reset";
        }

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(existUser);
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setExpirationDate(LocalDateTime.now().plusMinutes(30));

        try {
            passwordResetTokenService.save(passwordResetToken);
        } catch (Exception e) {
            result.rejectValue("email", null, "未知错误");
            model.addAttribute("passwordResetEmail", passwordResetEmailDto);
            return "password-reset";
        }

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(new InternetAddress("admin@example.com", "Admin"));
        helper.setSubject("重置密码");
        assert existUser != null;
        helper.setTo(existUser.getEmail());
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        helper.setText("<html><body><p>点击以下链接进行密码重置</p><a href='" + baseUrl + "/users/do-password-reset?token=" + passwordResetToken.getToken() + "'>重置密码</a><p>链接将在 30 分钟后失效，请尽快操作。</p></body></html>", true);
        sender.send(message);


        redirectAttributes.addFlashAttribute("success", "密码重置邮件已发送，请注意查收");
        return "redirect:/users/password-reset";
    }

    @GetMapping("users/do-password-reset")
    String getPasswordReset(Model model,
                            @RequestParam(required = false) String token) {
        PasswordResetToken pswToken = passwordResetTokenService.findByTokenOrderByIdDesc(token);
        if (pswToken == null) {
            model.addAttribute("error", "密码token不存在。");
        } else if (pswToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "密码token已过期。");
        }
        PasswordResetDto passwordResetDto = new PasswordResetDto();
        passwordResetDto.setToken(token);
        model.addAttribute("passwordResetDto", passwordResetDto);
        return "do-password-reset";
    }

    @PostMapping("users/do-password-reset")
    String postPasswordReset(@Valid @ModelAttribute("passwordResetDto") PasswordResetDto passwordResetDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword())) {
            result.rejectValue("password", "confirm-fail", "两次密码不一致！");
            return "do-password-reset";
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordResetDto", passwordResetDto);
            return "redirect:/users/do-password-reset";
        }

        PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetDto.getToken());
        User user = token.getUser();
        user.setPassword(passwordResetDto.getPassword());
        userService.updatePassword(user);

        passwordResetTokenService.expireThisToken(passwordResetDto.getToken());

        return "redirect:/login";
    }
}

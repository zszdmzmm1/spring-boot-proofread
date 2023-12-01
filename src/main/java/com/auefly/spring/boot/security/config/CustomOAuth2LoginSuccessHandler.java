package com.auefly.spring.boot.security.config;


import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    @Lazy
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String id = oAuth2User.getAttribute("id") + "";
        User user = userService.findUserBySocialId(id);

        if (user == null) {
            String name = oAuth2User.getAttribute("name");
            if (name == null || name.isEmpty()) {
                name = id;
            }
            user = new User();
            user.setSocialId(id);
            user.setName(name);
            user.setEmail(oAuth2User.getAttribute("email"));
            user.setAvatar(oAuth2User.getAttribute("avatar_url"));
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            user.setSocialProvider(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
            userService.saveUser(user);
        }

        response.sendRedirect("/");
    }
}

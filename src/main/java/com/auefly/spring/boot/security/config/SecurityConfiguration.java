package com.auefly.spring.boot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //look at this
                .csrf((csrf) -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("name")
                        .passwordParameter("password")
//                        .defaultSuccessUrl("/")
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                )
                .rememberMe(rm -> rm
                        .rememberMeParameter("remember")
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/login", "/user", "/build/**", "/vendor/**", "/img/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("admin")
                        .anyRequest().authenticated()
                )
        ;
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("123456"))
                .roles("user")
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder().encode("secret"))
                .roles("admin")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

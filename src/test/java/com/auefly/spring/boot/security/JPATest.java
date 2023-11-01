package com.auefly.spring.boot.security;

import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class JPATest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("java User类与数据库实现对接")
    void UserTableInitTest(){
        User user = new User();
        user.setName("admin");
        user.setPassword(passwordEncoder.encode("secret"));
        User returnUser = userRepository.saveAndFlush(user);
        Assertions.assertEquals(user, returnUser);

        userRepository.delete(returnUser);
    }

}

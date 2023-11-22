package com.auefly.spring.boot.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(userDetailsServiceBeanName = "jpaUserDetailsService", value = "admin@example.com")
public class WithMockUserForAdminBaseTest {
    @Autowired
    public MockMvc mockMvc;
}
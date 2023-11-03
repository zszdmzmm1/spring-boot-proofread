package com.auefly.spring.boot.security.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void indexTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name("index")
                )
        ;
    }

    @Test
    void loginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("登录"))
                )
        ;
    }

    @Test
    @DisplayName("测试添加cookie")
    void loginCookieTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "user")
                        .param("password", "password")
                        .param("remember", "on")
                )
                .andExpect(MockMvcResultMatchers.cookie().exists("remember-me"))
        ;
    }

    @Test
    @DisplayName("用户无邮箱登录失败跳转")
    void loginByNameExpectFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "user")
                        .param("password", "password")
                )
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error"))
        ;
    }

    @Test
    @DisplayName("用户用邮箱登录成功")
    void loginByEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "user@example.com")
                        .param("password", "password")
                )
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
        ;
    }

    @Test
    @WithMockUser(roles = "visitor")
    @DisplayName("非user, 和admin的身份无法访问该页面")
    void userPageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/dashboard"))
                .andExpect(MockMvcResultMatchers.status().is(403))
        ;
    }
}
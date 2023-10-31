package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.config.BackendProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "admin")
class BackendControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BackendProperties backendProperties;

    @Test
    @DisplayName("测试添加进page的model是否生效")
    void backendModelTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/dashboard"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("menus"))
                .andExpect(MockMvcResultMatchers.model().attribute("menus", backendProperties.getMenus()))
        ;
    }
}

package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.entity.Block;
import com.auefly.spring.boot.security.repository.BlockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class LectureControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BlockRepository blockRepository;

    @Test
    @DisplayName("正常登录docs详情页面")
    @WithMockUser(username="user",roles={"user"})
    void showNotContainsString() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20"))
                .andExpect(MockMvcResultMatchers.content().string(not(containsString("校对<i class=\"bi bi-spellcheck pl-1\"></i>"))))
        ;
    }

    @Test
    @DisplayName("未登录跳转登陆界面")
    void showProofreadReturn3xxWithoutLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @DisplayName("无权限禁止访问")
    @WithMockUser(username="not-admin",roles={"not-admin"})
    void showProofreadReturnForbiddenWithoutAdminRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
        ;
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    @DisplayName("有权限正常进入页面。")
    void showProofreadWithAdminRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("<i class=\"bi bi-arrow-return-left pl-1\"></i>返回正常页面")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("自动翻译<i class=\"bi bi-translate pl-1\"></i>")))
        ;
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void autoTranslateBlocks() throws Exception {
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContentTranslation = block.getContentTranslation();

        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/" + lectureId + "/auto-translate"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/docs/lecture/" + lectureId + "/proofread"))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertNotNull(block.getContentTranslation());

        // rollback
        block.setContentTranslation(originContentTranslation);
        blockRepository.save(block);
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    @DisplayName("更新内容字段")
    void updateBlockWithContentColumn() throws Exception {
        long collectionId = 2L;
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContent = block.getContent();
        String updatedContent = "content-column__updated";
        mockMvc.perform(MockMvcRequestBuilders.put("/docs/lecture/block/proofread")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", blockId + "")
                        .param("content", updatedContent)
                        .param("lecture_id", lectureId + "")
                        .param("collection_id", collectionId + "")
                )
                .andExpect(MockMvcResultMatchers.content().string(is("SUCCESS")))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertEquals(updatedContent, block.getContent());

        // rollback
        block.setContent(originContent);
        blockRepository.save(block);
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    @DisplayName("跟新翻译字段 ")
    void updateBlockWithContentTranslationColumn() throws Exception {
        long collectionId = 2L;
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContentTranslation = block.getContentTranslation();
        String updatedContentTranslation = "contentTranslation-column__updated";
        mockMvc.perform(MockMvcRequestBuilders.put("/docs/lecture/block/proofread")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", blockId + "")
                        .param("contentTranslation", updatedContentTranslation)
                        .param("lecture_id", lectureId + "")
                        .param("collection_id", collectionId + "")
                )
                .andExpect(MockMvcResultMatchers.content().string(is("SUCCESS")))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertEquals(updatedContentTranslation, block.getContentTranslation());

        // rollback
        block.setContentTranslation(originContentTranslation);
        blockRepository.save(block);
    }
}

package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.controller.WithMockUserForAdminBaseTest;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.repository.CollectionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.function.RequestPredicates.param;

public class CollectionControllerTest extends WithMockUserForAdminBaseTest {
    @Test
    @DisplayName("collections")
    void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/collections"))
                .andExpect(MockMvcResultMatchers.view().name("backend/collection/index"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("collections")))
        ;
    }

    @Test
    @DisplayName("删除文件")
    void deleteById(@Autowired CollectionRepository collectionRepository) throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));

        collectionRepository.save(collection);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/collections/destroy/" + collection.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> byId = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(byId.isEmpty());
    }


    @Test
    @DisplayName("批量删除文件")
    void batchDelete(@Autowired CollectionRepository collectionRepository) throws Exception {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Collection collection = new Collection();
            collection.setTitle(UUID.randomUUID().toString());
            collection.setSlug(UUID.randomUUID().toString());
            collection.setType("doc");
            collection.setUser(new User(1L));

            Collection entity = collectionRepository.save(collection);
            ids.add(entity.getId());
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/collections/destroy")
                        .param("ids[]", ids.get(0).toString())
                        .param("ids[]", ids.get(1).toString())
                        .param("ids[]", ids.get(2).toString())
                        .param("ids[]", ids.get(3).toString())
                        .param("ids[]", ids.get(4).toString())
                )
                .andExpect(MockMvcResultMatchers.content().string("DONE"))
        ;

        for(int i = 0; i < 5; i++) {
            Optional<Collection> byId = collectionRepository.findById(Long.valueOf(ids.get(i).toString()));
            Assertions.assertTrue(byId.isEmpty());
        }
    }
}

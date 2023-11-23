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
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CollectionControllerTest extends WithMockUserForAdminBaseTest {
    @Autowired
    CollectionRepository collectionRepository;

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
    void batchDelete() throws Exception {
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

        for (int i = 0; i < 5; i++) {
            Optional<Collection> byId = collectionRepository.findById(Long.valueOf(ids.get(i).toString()));
            Assertions.assertTrue(byId.isEmpty());
        }
    }

    @Test
    @DisplayName("新建文件")
    void create() throws Exception {
        String title = UUID.randomUUID().toString();
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/collections")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", title)
                        .param("slug", title + UUID.randomUUID())
                        .param("content", UUID.randomUUID().toString())
                        .param("description", UUID.randomUUID().toString())
                        .param("type", "doc")
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"));

        Optional<Collection> co = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(co.isPresent());

        collectionRepository.delete(co.get());

    }


    @Test
    @DisplayName("下载图片")
    void storeWithCoverImage(@Autowired Environment env) throws Exception {
        String title = "title-" + UUID.randomUUID();
        MockMultipartFile coverFile = new MockMultipartFile("coverFile", "cover.png", MediaType.IMAGE_PNG_VALUE, new byte[]{1, 2, 3});
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/admin/collections")
                        //.contentType(MediaType.MULTIPART_FORM_DATA)
                        .file(coverFile)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", UUID.randomUUID().toString())
                        .param("type", "doc")
                        .param("description", "content-" + UUID.randomUUID())
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> co = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(co.isPresent());

        String cover = co.get().getCover();
        File coverOnDisk = new File(env.getProperty("custom.upload.base-path") + cover);
        Assertions.assertTrue(Files.exists(coverOnDisk.toPath()));
        Assertions.assertTrue(coverOnDisk.delete());

        collectionRepository.delete(co.get());
    }


    @Test
    @DisplayName("update")
    void update() throws Exception {
        String title = UUID.randomUUID().toString();
        Collection collection = new Collection();
        collection.setTitle(title);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));

        collectionRepository.save(collection);

        Optional<Collection> optionalCollection = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(optionalCollection.isPresent());

        String updateTitle = title + "_update";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/collections")
                        .param("id", collection.getId().toString())
                        .param("title", updateTitle)
                        .param("slug", collection.getSlug())
                        .param("type", collection.getType())
                        .param("description", collection.getDescription())
                        .param("user_id", collection.getUser().getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"));
    }

    @Test
    @DisplayName("文章上线下线")
    void togglePublished() throws Exception {
        boolean isPublished = false;
        String title = UUID.randomUUID().toString();
        Collection collection = new Collection();
        collection.setTitle(title);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/collections/togglePublished/" + collection.getId()))
                .andExpect(MockMvcResultMatchers.content().string("SUCCESS"));

        Optional<Collection> optionalCollection = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(optionalCollection.isPresent());
        Assertions.assertEquals(optionalCollection.get().isPublished(), !isPublished);

        collectionRepository.delete(optionalCollection.get());
    }

    @Test
    @DisplayName("自定义slug验证")
    void storeWithCustomUniqueValidator(@Autowired CollectionRepository collectionRepository) throws Exception {
        String title = "title-" + UUID.randomUUID();
        String slug = "slug-" + UUID.randomUUID();

        Collection collection = new Collection();
        collection.setTitle(title);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setSlug(slug);
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/collections")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", slug)
                        .param("type", "doc")
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("collection", "slug", "CustomUnique"))
        ;

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/collections")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", collection.getId().toString())
                        .param("title", title + "--updated")
                        .param("slug", slug)
                        .param("type", "doc")
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> co = collectionRepository.findFirstByTitle(title + "--updated");
        Assertions.assertTrue(co.isPresent());

        collectionRepository.delete(co.get());
    }
}

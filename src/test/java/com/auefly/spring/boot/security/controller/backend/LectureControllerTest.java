package com.auefly.spring.boot.security.controller.backend;


import com.auefly.spring.boot.security.controller.WithMockUserForAdminBaseTest;
import com.auefly.spring.boot.security.entity.*;
import com.auefly.spring.boot.security.repository.BlockRepository;
import com.auefly.spring.boot.security.repository.CollectionRepository;
import com.auefly.spring.boot.security.repository.LectureRepository;
import com.auefly.spring.boot.security.repository.SectionRepository;
import com.auefly.spring.boot.security.service.BlockService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class LectureControllerTest extends WithMockUserForAdminBaseTest {
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    BlockService blockService;
    @Autowired
    BlockRepository blockRepository;

    @Test
    @DisplayName("新增一个lecture")
    void store() throws Exception {
        String collectionTitle = UUID.randomUUID().toString();
        Collection collection = new Collection();
        collection.setTitle(collectionTitle);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        String sectionTitle = UUID.randomUUID().toString();
        Section section = new Section();
        section.setTitle(sectionTitle);
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);

        String lectureTitle = "title" + UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/lectures")
                        .param("id", "")
                        .param("title", lectureTitle)
                        .param("sortOrder", "0")
                        .param("section_id", section.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Optional<Lecture> le = lectureRepository.findFirstByTitle(lectureTitle);
        Assertions.assertTrue(le.isPresent());
        lectureRepository.delete(le.get());

        Optional<Section> se = sectionRepository.findFirstByTitle(sectionTitle);
        Assertions.assertTrue(se.isPresent());
        sectionRepository.delete(se.get());

        Optional<Collection> co = collectionRepository.findFirstByTitle(collectionTitle);
        Assertions.assertTrue(co.isPresent());
        collectionRepository.delete(co.get());
    }

    @Test
    @DisplayName("更新lecture")
    void update() throws Exception {
        String collectionTitle = UUID.randomUUID().toString();
        Collection collection = new Collection();
        collection.setTitle(collectionTitle);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        String sectionTitle = UUID.randomUUID().toString();
        Section section = new Section();
        section.setTitle(sectionTitle);
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);

        String lectureTitle = UUID.randomUUID().toString();
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureTitle);
        lecture.setSection(new Section(section.getId()));
        lecture.setCollection(new Collection(collection.getId()));
        lectureRepository.save(lecture);

        String illegalSortOrderValue = "100000";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/lectures")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", lecture.getId().toString())
                        .param("title", lecture.getTitle())
                        .param("sortOrder", illegalSortOrderValue)
                        .param("section_id", section.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("lecture", "sortOrder", "Digits"))
        ;

        String descriptionUpdated = "description--updated";
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/lectures")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", lecture.getId().toString())
                        .param("title", lecture.getTitle())
                        .param("sortOrder", "0")
                        .param("description", descriptionUpdated)
                        .param("section_id", section.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Lecture lectureUpdated = lectureRepository.findFirstByTitle(lectureTitle).get();
        Assertions.assertEquals(descriptionUpdated, lectureUpdated.getDescription());

        lectureRepository.delete(lecture);
        sectionRepository.delete(section);
        collectionRepository.delete(collection);

        Assertions.assertTrue(lectureRepository.findById(lecture.getId()).isEmpty());
        Assertions.assertTrue(sectionRepository.findById(section.getId()).isEmpty());
        Assertions.assertTrue(collectionRepository.findById(collection.getId()).isEmpty());
    }


    @Test
    @DisplayName("删除lectures")
    void destroy() throws Exception {
        String collectionTitle = UUID.randomUUID().toString();
        Collection collection = new Collection();
        collection.setTitle(collectionTitle);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        String sectionTitle = UUID.randomUUID().toString();
        Section section = new Section();
        section.setTitle(sectionTitle);
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);

        String lectureTitle = UUID.randomUUID().toString();
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureTitle);
        lecture.setSection(new Section(section.getId()));
        lecture.setCollection(new Collection(collection.getId()));
        lectureRepository.save(lecture);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/lectures/destroy/" + lecture.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Optional<Lecture> byId = lectureRepository.findById(lecture.getId());
        Assertions.assertTrue(byId.isEmpty());

        sectionRepository.delete(section);
        Assertions.assertTrue(sectionRepository.findById(section.getId()).isEmpty());
        collectionRepository.delete(collection);
        Assertions.assertTrue(collectionRepository.findById(collection.getId()).isEmpty());
    }

    @Test
    @DisplayName("自动保存blocks")
    void autoSaveBlocksOnCreateLecture(@Autowired Environment environment) throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);


        String lectureTitle = "title-" + UUID.randomUUID();
        String blockSeparator = environment.getProperty("custom.block.separator");
        String lectureContent = """
                ## block-1-title
                                                
                block-1-content
                                                
                %s
                                                
                ## block-2-title
                                                
                block-2-content
                                                
                %s
                                                
                ## block-3-title
                                                
                block-3-content
                """.formatted(blockSeparator, blockSeparator);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/lectures")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("title", lectureTitle)
                        .param("content", lectureContent)
                        .param("sortOrder", "0")
                        .param("section_id", section.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Optional<Lecture> le = lectureRepository.findFirstByTitle(lectureTitle);
        Assertions.assertTrue(le.isPresent());

        Lecture lecture = le.get();
        List<Block> blocks = lecture.getBlocks();
        Assertions.assertEquals(3, blocks.size());
        blockService.destroyAllById(blocks.stream().map(Block::getId).collect(Collectors.toList()));

        lectureRepository.deleteById(lecture.getId());

        Optional<Section> se = sectionRepository.findById(section.getId());
        Assertions.assertTrue(se.isPresent());
        sectionRepository.delete(se.get());

        Optional<Collection> co = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(co.isPresent());
        collectionRepository.delete(co.get());
    }

    @Test
    @DisplayName("在文章详情也页面切换语言")
    void showLectureWithCtParam() throws Exception {
        long blockId = 16L;
        Block block = blockRepository.findById(blockId).orElseThrow();

        String ctParam = "c";
        String expected = getExpectedString(block, ctParam);
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20?ct=" + ctParam))
                .andExpect(MockMvcResultMatchers.model().attribute("content", expected))
        ;

        ctParam = "t";
        expected = getExpectedString(block, "t");
        mockMvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20?ct=" + ctParam))
                .andExpect(MockMvcResultMatchers.model().attribute("content", expected))
        ;
    }

    private String getExpectedString(Block block, String ctParam) {
        Lecture lecture = block.getLecture();

        StringBuilder allBlocks = new StringBuilder();
        for (Block b : lecture.getBlocks()) {
            String bc = "t".equals(ctParam) ? b.getContentTranslation() : b.getContent();
            if (bc != null) {
                allBlocks.append(bc).append(System.lineSeparator());
            }
        }

        return allBlocks.toString();
    }
}

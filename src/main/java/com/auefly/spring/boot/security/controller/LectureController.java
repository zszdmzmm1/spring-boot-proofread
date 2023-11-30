package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.dto.BlockDto;
import com.auefly.spring.boot.security.entity.Block;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.service.BlockService;
import com.auefly.spring.boot.security.service.LectureService;
import com.auefly.spring.boot.security.service.TextTranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LectureController {
    @Autowired
    LectureService lectureService;

    @Autowired
    TextTranslatorService textTranslatorService;

    @Autowired
    BlockService blockService;

    @GetMapping("/docs/lecture/{id}")
    String show(@PathVariable Long id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("lecture", lecture);

        StringBuilder allBlocks = new StringBuilder();
        if (lecture.getBlocks().isEmpty()) {
            allBlocks.append(lecture.getContent());
        } else {
            for (Block block : lecture.getBlocks()) {
                allBlocks.append(block.getContent()).append(System.lineSeparator());
            }
        }
        model.addAttribute("content", allBlocks);

        return "collection/doc/lecture/show";
    }

    @GetMapping("/docs/lecture/{id}/proofread")
    @PreAuthorize("hasRole('admin')")
    String showProofread(@PathVariable Long id,
                         Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("lecture", lecture);
        return "collection/doc/lecture/show-proofread";
    }

    @GetMapping("/docs/lecture/{id}/auto-translate")
    @PreAuthorize("hasRole('admin')")
    String autoTranslateBlocks(@PathVariable Long id,
                               Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        model.addAttribute("lecture", lecture);
        if(!lecture.getBlocks().isEmpty()) {
            for (Block block : lecture.getBlocks()) {
                block.setContentTranslation(textTranslatorService.translateText(block.getContent(), "en", "zh"));
                blockService.save(mapBlockToBlockDto(block));
            }
        }
        return "redirect:/docs/lecture/" + id + "/proofread";
    }

    private BlockDto mapBlockToBlockDto(Block block) {
        BlockDto blockDto = new BlockDto();
        blockDto.setId(block.getId());
        blockDto.setContent(block.getContent());
        blockDto.setContentTranslation(block.getContentTranslation());
        blockDto.setSortOrder(block.getSortOrder());
        blockDto.setPublished(block.isPublished());
        blockDto.setLecture_id(block.getLecture().getId());
        blockDto.setCollection_id(block.getCollection().getId());
        return blockDto;
    }
}

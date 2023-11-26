package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.entity.Block;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LectureController {
    @Autowired
    LectureService lectureService;

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
}

package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.LectureDto;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.service.CollectionService;
import com.auefly.spring.boot.security.service.LectureService;
import com.auefly.spring.boot.security.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/lectures")
public class LectureController {
    @Autowired
    LectureService lectureService;
    @Autowired
    SectionService sectionService;
    @Autowired
    CollectionService collectionService;

    @GetMapping("/create")
    String create(Model model,
                  @RequestParam("collection_id")Long collectionId,
                  @RequestParam("section_id")Long sectionId) {
        model.addAttribute("collection", collectionService.findById(collectionId).get());
        model.addAttribute("section", sectionService.findById(sectionId).get());
        model.addAttribute("lecture", new Lecture());
        return "backend/lecture/create";
    }

    @PostMapping("")
    String store(@Valid @ModelAttribute("lecture") LectureDto lectureDto,
                 BindingResult result) {
        if(result.hasErrors()) {
            return "backend/lecture/create";
        }
        lectureService.save(lectureDto);
        return "redirect:/admin/collections/edit/" + lectureDto.getCollection_id();
    }
}

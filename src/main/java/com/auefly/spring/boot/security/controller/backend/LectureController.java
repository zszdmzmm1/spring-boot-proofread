package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.LectureDto;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.service.CollectionService;
import com.auefly.spring.boot.security.service.LectureService;
import com.auefly.spring.boot.security.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @GetMapping("/edit/{id}")
    String edit(
            @PathVariable Long id,
            @RequestParam("collection_id") Long collectionId,
            @RequestParam("section_id") Long sectionId,
            Model model
    ) {
        Optional<Lecture> optionalLecture = lectureService.findById(id);
        if (optionalLecture.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found");
        } else {
            Lecture lecture = optionalLecture.get();
            model.addAttribute("collection", collectionService.findById(collectionId).get());
            model.addAttribute("section", sectionService.findById(sectionId).get());
            model.addAttribute("lecture", lecture);
            return "backend/lecture/edit";
        }
    }

    @PutMapping("")
    String update(
            @Validated @ModelAttribute("lecture") LectureDto lectureDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("lecture", lectureDto);
            model.addAttribute("collection", collectionService.findById(lectureDto.getCollection_id()).get());
            model.addAttribute("section", sectionService.findById(lectureDto.getSection_id()).get());

            return "backend/lecture/edit";
        }

        lectureService.save(lectureDto);

        return "redirect:/admin/collections/edit/" + lectureDto.getCollection_id();
    }

    @DeleteMapping("/destroy/{id}")
    String destroy (@PathVariable Long id) {
        Lecture lecture = lectureService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecture Not Found"));
        lectureService.destroy(id);
        return "redirect:/admin/collections/edit/" + lecture.getCollection().getId();
    }
}

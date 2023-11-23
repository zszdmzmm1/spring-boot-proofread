package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.SectionDto;
import com.auefly.spring.boot.security.entity.Section;
import com.auefly.spring.boot.security.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sections")
public class SectionController {
    @Autowired
    SectionService sectionService;

    @GetMapping("/create")
    String create(Model model) {
        model.addAttribute("section", new Section());
        return "backend/section/create";
    }

    @PostMapping("")
    String store(@Valid @ModelAttribute("section") SectionDto sectionDto,
                 BindingResult result) {
        if(result.hasErrors()) {
            return "backend/section/create";
        }
        sectionService.save(sectionDto);
        return "redirect:/admin/collections/edit/" + sectionDto.getCollection_id();
    }
}
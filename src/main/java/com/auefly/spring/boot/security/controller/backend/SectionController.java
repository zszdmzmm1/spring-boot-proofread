package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.SectionDto;
import com.auefly.spring.boot.security.entity.Section;
import com.auefly.spring.boot.security.service.CollectionService;
import com.auefly.spring.boot.security.service.SectionService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/admin/sections")
public class SectionController {
    @Autowired
    SectionService sectionService;

    @Autowired
    CollectionService collectionService;

    @GetMapping("/create")
    String create(Model model,
                  @RequestParam("collection_id")Long collectionId) {
        model.addAttribute("collection", collectionService.findById(collectionId).get());
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


    @GetMapping("edit/{id}")
    String edit(
            @PathVariable Long id,
            @RequestParam("collection_id") Long collectionId,
            Model model
    ) {
        Optional<Section> optionalSection = sectionService.findById(id);
        if (optionalSection.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            Section section = optionalSection.get();
            model.addAttribute("collection", collectionService.findById(collectionId).get());
            model.addAttribute("section", section);
            return "backend/section/edit";
        }
    }

    @PutMapping("")
    String update(
            @Validated @ModelAttribute("section") SectionDto sectionDto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("collection", collectionService.findById(sectionDto.getCollection_id()).get());
            model.addAttribute("section", sectionDto);

            return "backend/section/edit";
        }

        sectionService.save(sectionDto);

        return "redirect:/admin/collections/edit/" + sectionDto.getCollection_id();
    }

    @DeleteMapping("destroy/{id}")
    @Transactional
    public String destroy(@PathVariable Long id) {
        Section section = sectionService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sectionService.destroy(id);
        if (!section.getLectures().isEmpty()) {
            //lectureService.destroyAllById(section.getLectures().stream().map(Lecture::getId).collect(Collectors.toList()));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are also lectures under the section, which are not allowed to be deleted directly.");
        }
        return "redirect:/admin/collections/edit/" + section.getCollection().getId();
    }
}

package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.CollectionDto;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.service.CollectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/collections")
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @GetMapping("")
    String adminUsers(@RequestParam Optional<Integer> page,
                      @RequestParam Optional<Integer> size,
                      Model model) {
        Page<Collection> pageContent = collectionService.findAll(page.orElse(1), size.orElse(5));
        model.addAttribute("page", pageContent);
        return "backend/collection/index";
    }

    @DeleteMapping("/destroy/{id}")
    String destroy(@PathVariable Long id) {
        collectionService.destroy(id);
        return "redirect:/admin/collections";
    }

    @DeleteMapping("/destroy")
    @ResponseBody
    String destroyBatch(@RequestParam(value = "ids[]")List<Long> ids) {
        collectionService.destroyAllByIds(ids);
        return "DONE";
    }

    @GetMapping("create")
    String create(Model model) {
        model.addAttribute("collection", new Collection());
        return "backend/collection/create";
    }

    @PostMapping("")
    String store(@Valid @ModelAttribute("collection") CollectionDto collectionDto,
                 BindingResult bindingResul) {
        if(bindingResul.hasErrors()) {
            return "backend/collection/create";
        }
        collectionService.save(collectionDto);
        return "redirect:/admin/collections";
    }
}

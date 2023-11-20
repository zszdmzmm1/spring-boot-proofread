package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @GetMapping("/admin/collections")
    String adminUsers(@RequestParam Optional<Integer> page,
                      @RequestParam Optional<Integer> size,
                      Model model) {
        Page<Collection> pageContent = collectionService.findAll(page.orElse(1), size.orElse(5));
        model.addAttribute("page", pageContent);
        return "backend/collection/index";
    }

    @DeleteMapping("/admin/collections/destroy/{id}")
    String destroy(@PathVariable Long id) {
        collectionService.destroy(id);
        return "redirect:/admin/collections";
    }
}

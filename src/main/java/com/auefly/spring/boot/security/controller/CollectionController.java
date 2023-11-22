package com.auefly.spring.boot.security.controller;

import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @GetMapping("/docs")
    String index(Model model,
                 @RequestParam("page")Optional<Integer> page,
                 @RequestParam("size")Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Collection> pageContent = collectionService.findAllPublishedDocs(currentPage, pageSize);
        model.addAttribute("page", pageContent);
        return "collection/doc/index";
    }

    @GetMapping("/docs/{id}")
    String show(@PathVariable Long id, Model model) {
        Optional<Collection> optionalPost = collectionService.findById(id);

        if (optionalPost.isEmpty()
                || !"doc".equals(optionalPost.get().getType())
        ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doc Not Found");
        }

        model.addAttribute("doc", optionalPost.get());
        return "collection/doc/show";
    }
}

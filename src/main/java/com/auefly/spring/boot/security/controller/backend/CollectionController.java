package com.auefly.spring.boot.security.controller.backend;

import com.auefly.spring.boot.security.dto.CollectionDto;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.service.CollectionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("backendCollectionController")
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
    String destroyBatch(@RequestParam(value = "ids[]") List<Long> ids) {
        collectionService.destroyAllByIds(ids);
        return "DONE";
    }

    @GetMapping("create")
    String create(Model model) {
        model.addAttribute("collection", new Collection());
        return "backend/collection/create";
    }

    @PostMapping("")
    String store(@RequestParam(value = "coverFile", required = false) MultipartFile file,
                 @Valid @ModelAttribute("collection") CollectionDto collectionDto,
                 BindingResult bindingResul) throws IOException {
        if (bindingResul.hasErrors()) {
            return "backend/collection/create";
        }
        doCover(file, collectionDto);
        collectionService.save(collectionDto);
        return "redirect:/admin/collections";
    }

    @Value("${custom.upload.base-path}")
    String uploadBasePath;
    @Value("${custom.upload.collection-cover-dir-under-base-path}")
    String postCoverDirUnderBasePath;

    private void doCover(MultipartFile file, CollectionDto collectionDto) throws IOException {
        if (file != null && !file.isEmpty()) {
            File dir = new File(uploadBasePath + File.separator + postCoverDirUnderBasePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + suffix;
            file.transferTo(new File(dir.getAbsolutePath() + File.separator + newFilename));
            collectionDto.setCover("/" + postCoverDirUnderBasePath + File.separator + newFilename);
        }
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id,
                Model model) {
        Optional<Collection> optionalCollection = collectionService.findById(id);
        if (optionalCollection.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found");
        } else {
            model.addAttribute("collection", optionalCollection.get());
            return "backend/collection/edit";
        }
    }

    @PutMapping("")
    String update(@Valid @ModelAttribute("collection")CollectionDto collectionDto,
                  BindingResult bindingResult, Model model,
                  @RequestParam(value = "coverFile", required = false)MultipartFile file) throws IOException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("collection", collectionDto);
            return "backend/collection/edit";
        }

        doCover(file, collectionDto);
        collectionService.save(collectionDto);

        return "redirect:/admin/collections";
    }

    @PostMapping("togglePublished/{id}")
    @ResponseBody
    @Transactional
    public String togglePublished(@PathVariable Long id) {
        Optional<Collection> optionalCollection = collectionService.findById(id);

        if(optionalCollection.isEmpty() || !"doc".equals(optionalCollection.get().getType())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doc Not Found");
        }

        collectionService.togglePublished(id);
        return "SUCCESS";
    }
}

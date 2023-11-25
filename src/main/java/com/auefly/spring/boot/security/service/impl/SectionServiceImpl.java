package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.dto.SectionDto;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.entity.Section;
import com.auefly.spring.boot.security.repository.SectionRepository;
import com.auefly.spring.boot.security.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    SectionRepository repository;


    @Override
    public void save(SectionDto sectionDto) {
        Section section = new Section();

        section.setTitle(sectionDto.getTitle());
        section.setSortOrder(sectionDto.getSortOrder());
        section.setDescription(sectionDto.getDescription());
        section.setCollection(new Collection(sectionDto.getCollection_id()));
        section.setCreatedAt(LocalDateTime.now());
        repository.save(section);
    }

    @Override
    public Optional<Section> findById(Long id) {
        return repository.findById(id);
    }
}

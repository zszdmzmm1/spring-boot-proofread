package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.dto.SectionDto;
import com.auefly.spring.boot.security.entity.Section;
import org.springframework.beans.MutablePropertyValues;

import java.util.Optional;

public interface SectionService {
    void save(SectionDto sectionDto);

    Optional<Section> findById(Long id);
}

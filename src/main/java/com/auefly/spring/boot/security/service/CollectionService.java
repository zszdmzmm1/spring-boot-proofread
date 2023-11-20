package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.dto.CollectionDto;
import com.auefly.spring.boot.security.entity.Collection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CollectionService {

    Page<Collection> findAll(int pageNumber, int pageSize);

    void destroy(Long id);

    void destroyAllByIds(List<Long> ids);

    void save(CollectionDto collectionDto);
}

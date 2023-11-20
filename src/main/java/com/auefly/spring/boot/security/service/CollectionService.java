package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.entity.Collection;
import org.springframework.data.domain.Page;

public interface CollectionService {

    Page<Collection> findAll(int pageNumber, int pageSize);

    void destroy(Long id);
}

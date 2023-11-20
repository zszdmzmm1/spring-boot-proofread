package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.repository.CollectionRepository;
import com.auefly.spring.boot.security.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    CollectionRepository repository;

    @Override
    public Page<Collection> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        return repository.findAll(pageable);
    }

    @Override
    public void destroy(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void destroyAllByIds(List<Long> ids) {
        repository.deleteAllById(ids);
    }
}


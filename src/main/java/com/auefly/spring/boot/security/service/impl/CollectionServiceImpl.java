package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.dto.CollectionDto;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.entity.User;
import com.auefly.spring.boot.security.repository.CollectionRepository;
import com.auefly.spring.boot.security.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void save(CollectionDto collectionDto) {
        Collection collection = new Collection();

        if (collectionDto.getId() != null) {
            collection = repository.findById(collectionDto.getId()).get();
            collection.setUpdatedAt(LocalDateTime.now());
        } else {
            collection.setCreatedAt(LocalDateTime.now());
        }

        collection.setTitle(collectionDto.getTitle());
        collection.setSlug(collectionDto.getSlug());
        collection.setType(collectionDto.getType());
        collection.setDescription(collectionDto.getDescription());
        collection.setPublished(collectionDto.isPublished());
        collection.setCover(collectionDto.getCover());
        collection.setUser(new User(collectionDto.getUser_id()));
        collection.setCreatedAt(LocalDateTime.now());
        repository.save(collection);
    }

    @Override
    public Optional<Collection> findById(Long id) {
        return repository.findById(id);
    }
}


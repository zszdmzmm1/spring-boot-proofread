package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.dto.BlockDto;
import com.auefly.spring.boot.security.entity.Block;

import java.util.List;
import java.util.Optional;

public interface BlockService {
    void save(BlockDto blockDto);

    Optional<Block> findById(Long id);

    void destroy(Long id);

    void destroyAllById(List<Long> ids);
}

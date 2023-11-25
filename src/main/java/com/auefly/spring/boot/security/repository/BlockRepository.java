package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findFirstByLectureId(Long lectureId);
}

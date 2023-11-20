package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Optional<Collection> findFirstByTitle(String title);
}

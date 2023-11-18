package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

}

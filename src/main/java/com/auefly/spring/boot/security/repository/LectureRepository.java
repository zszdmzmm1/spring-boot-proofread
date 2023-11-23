package com.auefly.spring.boot.security.repository;

import com.auefly.spring.boot.security.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findFirstByTitle(String title);
}

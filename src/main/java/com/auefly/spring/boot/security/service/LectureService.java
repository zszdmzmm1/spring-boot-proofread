package com.auefly.spring.boot.security.service;

import com.auefly.spring.boot.security.dto.LectureDto;
import com.auefly.spring.boot.security.entity.Lecture;

import java.util.Optional;

public interface LectureService {
    Lecture save(LectureDto lectureDto);

    Optional<Lecture> findById(Long id);

    void destroy(Long id);

    void saveBlocks(Long lectureId, LectureDto lectureDto);
}

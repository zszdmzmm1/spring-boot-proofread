package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.dto.LectureDto;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.entity.Section;
import com.auefly.spring.boot.security.repository.LectureRepository;
import com.auefly.spring.boot.security.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public void save(LectureDto lectureDto) {
        Lecture lecture = new Lecture();

        lecture.setId(lectureDto.getId());
        lecture.setTitle(lectureDto.getTitle());
        lecture.setContent(lectureDto.getContent());
        lecture.setPublished(lectureDto.isPublished());
        lecture.setFree(lectureDto.isFree());
        lecture.setRequiresLogin(lectureDto.isRequiresLogin());
        lecture.setSortOrder(lectureDto.getSortOrder());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setSection(new Section(lectureDto.getSection_id()));
        lecture.setCollection(new Collection(lectureDto.getCollection_id()));
        lecture.setCreatedAt(LocalDateTime.now());
        lectureRepository.save(lecture);
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }
}

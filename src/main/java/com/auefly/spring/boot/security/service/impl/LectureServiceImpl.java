package com.auefly.spring.boot.security.service.impl;

import com.auefly.spring.boot.security.dto.LectureDto;
import com.auefly.spring.boot.security.entity.Block;
import com.auefly.spring.boot.security.entity.Collection;
import com.auefly.spring.boot.security.entity.Lecture;
import com.auefly.spring.boot.security.entity.Section;
import com.auefly.spring.boot.security.repository.BlockRepository;
import com.auefly.spring.boot.security.repository.LectureRepository;
import com.auefly.spring.boot.security.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    BlockRepository blockRepository;

    @Value("${custom.block.separator}")
    String blockSeparator;

    @Override
    public Lecture save(LectureDto lectureDto) {
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
        return lectureRepository.save(lecture);
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }

    @Override
    public void destroy(Long id) {
        lectureRepository.deleteById(id);
    }

    @Override
    public void saveBlocks(Long lectureId, LectureDto lectureDto) {
        String[] ss = lectureDto.getContent().split(blockSeparator);
        for (String s : ss) {
            Block block = new Block();
            block.setContent(s.trim());
            block.setLecture(new Lecture(lectureId));
            block.setCollection(new Collection(lectureDto.getCollection_id()));
            blockRepository.save(block);
        }
    }
}

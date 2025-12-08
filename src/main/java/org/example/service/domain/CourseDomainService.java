package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.CourseEntity;
import org.example.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepository courseRepository;

    public List<Long> getMandatoryCourseIds(Long studyProgramId, Integer semester) {
        return courseRepository.findByStudyProgram_IdAndSemesterAndMandatoryTrue(studyProgramId, semester)
                .stream()
                .map(CourseEntity::getId)
                .toList();
    }
}
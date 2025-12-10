package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.repository.CompletedCourseRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletedCourseDomainService {

    private final CompletedCourseRepository completedCourseRepository;

    public boolean hasCompletedCourse(Long studentId, Long courseId) {
        return completedCourseRepository.existsByStudent_IdAndCourse_Id(studentId, courseId);
    }
}
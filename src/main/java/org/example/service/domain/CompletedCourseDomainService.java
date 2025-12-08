package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.repository.CompletedCourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletedCourseDomainService {

    private final CompletedCourseRepository completedCourseRepository;

    public boolean hasCompletedCourse(Long studentId, Long courseId) {
        return completedCourseRepository.existsByStudent_IdAndCourse_Id(studentId, courseId);
    }

    public List<Long> getCompletedCourseIds(Long studentId) {
        return completedCourseRepository.findByStudent_Id(studentId)
                .stream()
                .map(c -> c.getCourse().getId())
                .toList();
    }
}
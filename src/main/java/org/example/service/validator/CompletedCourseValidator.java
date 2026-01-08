package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.service.domain.CompletedCourseDomainService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompletedCourseValidator {

    private final CompletedCourseDomainService completedCourseDomainService;

    public void validateCreate(CompletedCourseCreateRequest request) {
        Optional.of(request.studentId())
                .filter(v -> v > 0)
                .orElseThrow(() -> new IllegalArgumentException("studentId must be > 0"));

        Optional.of(request.courseId())
                .filter(v -> v > 0)
                .orElseThrow(() -> new IllegalArgumentException("courseId must be > 0"));

        Optional.of(request.grade())
                .filter(v -> v >= 1 && v <= 5)
                .orElseThrow(() -> new IllegalArgumentException("grade must be in range [1..5]"));

        Optional.ofNullable(request.completionDate())
                .orElseThrow(() -> new IllegalArgumentException("completionDate must not be null"));

        Optional.of(completedCourseDomainService.hasCompletedCourse(request.studentId(), request.courseId()))
                .filter(exists -> !exists)
                .orElseThrow(() -> new IllegalStateException("Course already marked as completed for this student"));
    }
}

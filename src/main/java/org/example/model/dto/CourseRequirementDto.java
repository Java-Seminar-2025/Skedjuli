package org.example.model.dto;

import java.time.LocalDateTime;

public record CourseRequirementDto(
        Long id,
        Long courseId,
        Long requiredCourseId,
        LocalDateTime createdAt
) {}

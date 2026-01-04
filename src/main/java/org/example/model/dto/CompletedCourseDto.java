package org.example.model.dto;

import java.time.LocalDate;

public record CompletedCourseDto(
        Long studentId,
        Long courseId,
        int grade,
        LocalDate completionDate,
        Long academicYearId
) {}
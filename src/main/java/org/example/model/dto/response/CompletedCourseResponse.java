package org.example.model.dto.response;

import java.time.LocalDate;

public record CompletedCourseResponse(
        Long id,
        Long studentId,
        Long courseId,
        int grade,
        LocalDate completionDate,
        Long academicYearId
) {}
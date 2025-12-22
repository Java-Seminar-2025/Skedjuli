package org.example.model.dto;

import java.time.LocalDate;

public record CompletedCourseDto(
        int id,
        int studentId,
        int courseId,
        int grade,
        LocalDate completionDate,
        int academicYearId
) {}
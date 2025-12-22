package org.example.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CompletedCourseCreateRequest(
        @Min(1) Long studentId,
        @Min(1) Long courseId,
        @NotNull @Min(1) @Max(5) int grade,
        @NotNull LocalDate completionDate
) {}
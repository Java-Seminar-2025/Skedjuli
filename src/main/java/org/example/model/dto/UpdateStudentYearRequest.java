package org.example.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStudentYearRequest(
        @NotNull Long studentId,
        @Min(1) int newYear
) {}

package org.example.model.dto.request.create;

import jakarta.validation.constraints.Min;

public record StudentCreateRequest(
        @Min(1) Long userId,
        @Min(1) Long studyProgramId,
        @Min(value = 1900)
        Integer enrollmentYear,
        @Min(1) Integer currentYear
) {}

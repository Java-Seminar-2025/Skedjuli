package org.example.model.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AcademicYearCreateRequest(
        @NotBlank String yearCode,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        LocalDate enrollmentStart,
        LocalDate enrollmentEnd,
        boolean isActive
) {}
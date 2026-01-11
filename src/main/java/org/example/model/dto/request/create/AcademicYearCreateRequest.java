package org.example.model.dto.request.create;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AcademicYearCreateRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        LocalDate enrollmentStart,
        LocalDate enrollmentEnd,
        boolean isActive
) {}
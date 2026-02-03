package org.example.model.dto.request.patch;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AcademicYearPatchRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        LocalDate enrollmentStart,
        LocalDate enrollmentEnd,
        Boolean isActive
) {}

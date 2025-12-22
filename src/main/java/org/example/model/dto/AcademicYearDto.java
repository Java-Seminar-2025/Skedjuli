package org.example.model.dto;

import java.time.LocalDate;

public record AcademicYearDto(
        Long id,
        String yearCode,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate enrollmentStart,
        LocalDate enrollmentEnd,
        boolean isActive
) {}
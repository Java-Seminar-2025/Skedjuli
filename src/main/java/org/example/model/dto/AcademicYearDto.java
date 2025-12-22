package org.example.model.dto;

import java.time.LocalDate;

public record AcademicYearDto(
        int id,
        String yearCode,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate enrollmentStart,
        LocalDate enrollmentEnd,
        boolean isActive
) {}
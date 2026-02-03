package org.example.model.dto.response;

import java.time.LocalDate;

public record AcademicYearResponse(
    Long id,
    String yearCode,
    LocalDate startDate,
    LocalDate endDate,
    LocalDate enrollmentStart,
    LocalDate enrollmentEnd,
    Boolean isActive
) {}

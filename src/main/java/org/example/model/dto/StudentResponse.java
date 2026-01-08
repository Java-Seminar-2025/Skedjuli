package org.example.model.dto;

public record StudentResponse(
        Integer enrollmentYear,
        Integer currentYear,
        Double averageGrade,
        Double totalEctsEarned,
        Boolean isActive
) {}

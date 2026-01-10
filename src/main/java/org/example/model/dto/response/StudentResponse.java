package org.example.model.dto.response;

public record StudentResponse(
        Long id,
        Long userId,
        Long studyProgramId,
        Integer enrollmentYear,
        Integer currentYear,
        Double averageGrade,
        Double totalEctsEarned,
        Boolean isActive
) {}

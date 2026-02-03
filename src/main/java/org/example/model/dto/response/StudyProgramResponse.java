package org.example.model.dto.response;

public record StudyProgramResponse(
        Long id,
        String code,
        String name,
        String description,
        Integer durationYears,
        Integer totalEcts,
        Boolean active
) {}

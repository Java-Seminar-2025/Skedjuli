package org.example.model.dto.request.patch;

public record StudyProgramPatchRequest (
        String code,
        String name,
        String description,
        Integer durationYears,
        Integer totalEcts,
        Boolean active
) {}

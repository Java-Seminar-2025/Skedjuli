package org.example.model.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

public record StudyProgramCreateRequest(
        @NotBlank String name,
        String description,
        @NotNull Integer durationYears,
        Integer totalEcts,
        Boolean active
) {}

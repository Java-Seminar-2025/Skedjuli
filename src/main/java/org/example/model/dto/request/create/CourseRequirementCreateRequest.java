package org.example.model.dto.request.create;

import jakarta.validation.constraints.Min;

public record CourseRequirementCreateRequest(
        @Min(1) Long courseId,
        @Min(1) Long requiredCourseId
) {}

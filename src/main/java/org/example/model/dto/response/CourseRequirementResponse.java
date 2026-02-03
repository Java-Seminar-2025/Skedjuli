package org.example.model.dto.response;

public record CourseRequirementResponse(
        Long id,
        Long courseId,
        Long requiredCourseId
) {}

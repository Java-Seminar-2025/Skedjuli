package org.example.model.dto.response;

import org.example.model.enums.StudentCourseStatus;

public record StudentCourseWithStatusResponse(
        CourseResponse course,
        StudentCourseStatus status
) {
}

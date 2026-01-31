package org.example.model.dto.response;

import java.util.List;

public record SemesterCoursesResponse(
        Integer semester,
        List<StudentCourseWithStatusResponse> courses
) {
}

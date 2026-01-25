package org.example.model.dto.response;

public record CourseGradeResponse(
        Long courseId,
        String courseCode,
        String courseName,
        Integer grade,
        Long academicYearId,
        String academicYearCode
) {
}

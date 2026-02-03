package org.example.model.dto.response;

import java.util.List;

public record StudentAnalyticsResponse(
        Long studentId,

        Double averageThisAcademicYear,
        Double averageOverall,

        Double cohortAverageThisAcademicYear,
        Double cohortAverageOverall,

        Double deltaVsCohortThisAcademicYear,
        Double deltaVsCohortOverall,

        List<CourseGradeResponse> gradedCourses,
        List<CourseResponse> notPassedCourses
) {
}

package org.example.domain.mapper;

import org.example.domain.dto.EnrollmentCourseResponse;
import org.example.domain.entity.CourseEntity;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public static EnrollmentCourseResponse toDto(CourseEntity course) {
        return new EnrollmentCourseResponse(
                course.getId(),
                course.getName(),
                course.getEcts(),
                course.getSemester()
        );
    }
}
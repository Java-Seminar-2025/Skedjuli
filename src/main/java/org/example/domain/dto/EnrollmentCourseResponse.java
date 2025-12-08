package org.example.domain.dto;

public record EnrollmentCourseResponse(
        Long id,
        String name,
        int ects,
        int semester
) {}
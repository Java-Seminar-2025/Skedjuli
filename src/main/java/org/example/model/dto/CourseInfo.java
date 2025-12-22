package org.example.model.dto;

public record CourseInfo(
        Long id,
        String name,
        int ects,
        int semester
) {}

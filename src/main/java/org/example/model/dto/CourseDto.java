package org.example.model.dto;

public record CourseDto(
        int id,
        String name,
        int ects,
        int semester
) {}
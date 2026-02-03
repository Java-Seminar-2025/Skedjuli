package org.example.model.dto.unsorted;

public record CourseInfo(
        Long id,
        String name,
        int ects,
        int semester
) {}
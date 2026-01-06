package org.example.model.dto;

public record CourseUpdateRequest(

        Long id,

        String name,
        String description,
        Integer ects,
        Integer semester,
        Boolean mandatory,
        Integer enrollmentLimit,
        Boolean active,

        Long lecturerId,
        Long studyProgramId,
        Long academicYearId
) {}

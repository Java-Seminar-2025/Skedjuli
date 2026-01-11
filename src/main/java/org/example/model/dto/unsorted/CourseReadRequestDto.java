package org.example.model.dto.unsorted;

public record CourseReadRequestDto(

        Long id,
        String code,
        String name,
        String description,
        Integer ects,
        Integer semester,
        Boolean mandatory,
        Boolean active,
        Integer enrollmentLimit,

        Long studyProgramId,
        Long academicYearId,
        Long lecturerId
) {}
package org.example.model.dto;

import java.util.Set;

public record CourseAdminReadDto(

        Long id,
        String code,
        String name,
        String description,
        Integer ects,
        Integer semester,
        Boolean mandatory,
        Boolean active,
        Integer enrollmentLimit,

        IdNameDto studyProgram,
        IdNameDto academicYear,
        IdNameDto lecturer,

        Set<IdNameDto> prerequisites

) {}

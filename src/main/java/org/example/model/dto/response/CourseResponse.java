package org.example.model.dto.response;

import java.util.Set;

public record CourseResponse(
        Long id,
        String code,
        String name,
        String description,
        Integer ects,
        Boolean mandatory,
        Integer enrollmentLimit,
        Long lecturerId,
        Long studyProgramId,
        Long academicYearId,
        Integer semester,
        Boolean active,
        Set<Long> prerequisiteIds
) {}

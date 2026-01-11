package org.example.model.dto.request.patch;

import java.util.Set;

public record CoursePatchRequest(
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
        Long lecturerId,
        Set<Long> prerequisiteCourseIds
) {}

package org.example.model.dto.request.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CourseCreateRequest(
        @Size(max = 20) String code,
        @NotNull @Size(max = 100) String name,
        String description,
        @Positive Integer ects,
        Integer semester,
        Boolean mandatory,
        Integer enrollmentLimit,
        @NotNull Long lecturerId,
        @NotNull Long studyProgramId,
        @NotNull Long academicYearId,
        Boolean active,
        Set<Long> prerequisiteCourseIds
) {}

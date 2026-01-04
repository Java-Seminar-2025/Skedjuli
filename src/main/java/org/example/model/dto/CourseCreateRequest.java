package org.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CourseCreateRequest(

        @NotBlank
        @Size(max = 20)
        String code,

        @NotBlank
        @Size(max = 100)
        String name,

        String description,

        @NotNull
        @Positive
        Integer ects,

        @NotNull
        Integer semester,

        @NotNull
        Boolean mandatory,

        Integer enrollmentLimit,

        @NotNull
        Long studyProgramId,

        @NotNull
        Long academicYearId,

        Long lecturerId,

        Set<Long> prerequisiteCourseIds

) {}

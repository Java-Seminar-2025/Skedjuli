package org.example.model.dto.request.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CourseGradeCreateRequest(
        @NotNull Long lecturerId,
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull @Min(1) @Max(5) Integer grade,
        LocalDate completionDate
) {
}

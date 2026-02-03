package org.example.model.dto.request.create;

import jakarta.validation.constraints.NotNull;

public record LecturerCourseAddStudentRequest(
        @NotNull Long lecturerId,
        @NotNull Long courseId,
        @NotNull Long studentId
) {
}

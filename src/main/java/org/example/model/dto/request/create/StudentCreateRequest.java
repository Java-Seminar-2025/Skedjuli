package org.example.model.dto.request.create;

public record StudentCreateRequest(
        long userId,

        long studyProgramId,

        int enrollmentYear,

        int currentYear
) {
}

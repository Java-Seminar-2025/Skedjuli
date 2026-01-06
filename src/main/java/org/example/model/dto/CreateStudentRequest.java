package org.example.model.dto;

public record CreateStudentRequest(
        long userId,

        long studyProgramId,

        int enrollmentYear,

        int currentYear
) {
}

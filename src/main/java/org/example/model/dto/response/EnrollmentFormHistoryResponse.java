package org.example.model.dto.response;

import java.time.LocalDateTime;

public record EnrollmentFormHistoryResponse(
        Long formId,
        Long academicYearId,
        String academicYearCode,
        Integer semester,
        LocalDateTime approvedAt,
        Boolean locked,
        LocalDateTime createdAt
) {
}

package org.example.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EnrollmentFormLockedResponse(
        Long formId,
        String studentFirstName,
        String studentLastName,
        String studentUsername,
        Long academicYearId,
        Integer semester,
        LocalDateTime submittedAt,
        List<EnrollmentFormItemReviewResponse> items
) {
}

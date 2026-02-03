package org.example.model.dto.pdf;

import java.time.LocalDateTime;
import java.util.List;

public record EnrollmentFormPdfDto(
        String studentFullName,
        String studyProgramName,
        String academicYearCode,
        Integer semester,
        String formStatus,
        LocalDateTime submittedAt,
        LocalDateTime approvedAt,
        List<EnrollmentFormItemPdfDto> items
) {}

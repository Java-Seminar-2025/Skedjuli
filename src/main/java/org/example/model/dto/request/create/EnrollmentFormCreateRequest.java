package org.example.model.dto.request.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EnrollmentFormCreateRequest(
        @Min(1) Long studentId,
        @Min(1) Long academicYearId,
        @Min(1) @Max(10) Integer semester
) {
}

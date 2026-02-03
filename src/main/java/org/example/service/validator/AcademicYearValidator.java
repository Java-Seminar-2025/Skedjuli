package org.example.service.validator;

import org.example.model.dto.request.create.AcademicYearCreateRequest;
import org.example.model.dto.request.patch.AcademicYearPatchRequest;

import java.time.LocalDate;

public class AcademicYearValidator {

    private AcademicYearValidator() {}

    public static void validateCreate(AcademicYearCreateRequest request) {
        require(request != null, "Request must not be null");
        require(request.startDate() != null, "startDate must not be null");
        require(request.endDate() != null, "endDate must not be null");

        validateDates(
                request.startDate(),
                request.endDate(),
                request.enrollmentStart(),
                request.enrollmentEnd()
        );
    }

    public static void validatePatch(AcademicYearPatchRequest request) {
        require(request != null, "Request must not be null");

        validateDates(
                request.startDate(),
                request.endDate(),
                request.enrollmentStart(),
                request.enrollmentEnd()
        );

        if ((request.startDate() != null && request.endDate() == null) ||
                (request.startDate() == null && request.endDate() != null)) {
            throw new IllegalArgumentException("Both startDate and endDate must be provided together");
        }
    }

    private static void validateDates(
            LocalDate start,
            LocalDate end,
            LocalDate enrollmentStart,
            LocalDate enrollmentEnd
    ) {
        require(start.isBefore(end), "startDate must be before endDate");

        if (enrollmentStart != null)
            require(!enrollmentStart.isBefore(start),
                    "enrollmentStart must be on or after startDate");

        if (enrollmentEnd != null)
            require(!enrollmentEnd.isAfter(end),
                    "enrollmentEnd must be on or before endDate");

        if (enrollmentStart != null && enrollmentEnd != null)
            require(!enrollmentStart.isAfter(enrollmentEnd),
                    "enrollmentStart must be before or equal to enrollmentEnd");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}

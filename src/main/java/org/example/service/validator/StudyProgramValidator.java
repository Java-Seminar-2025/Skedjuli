package org.example.service.validator;

import org.example.model.dto.request.create.StudyProgramCreateRequest;
import org.example.model.dto.request.patch.StudyProgramPatchRequest;

public class StudyProgramValidator {

    private StudyProgramValidator() {}

    public static void validateCreate(StudyProgramCreateRequest request) {
        require(request != null, "Request must not be null");
        requireNotBlank(request.name(), "name must not be blank");
        require(request.durationYears() != null && request.durationYears() > 0, "durationYears must be positive");
    }

    public static void validatePatch(StudyProgramPatchRequest request) {
        require(request != null, "Patch request must not be null");
        if (request.durationYears() != null) {
            require(request.durationYears() > 0, "durationYears must be positive");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    private static void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
    }
}

package org.example.model.dto;
import java.util.List;

public record EnrollmentSelectionRequest(
        List<Long> selectedCourseIds,
        boolean allowHigherYearSelection
) {}

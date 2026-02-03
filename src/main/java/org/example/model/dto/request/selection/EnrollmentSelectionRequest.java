package org.example.model.dto.request.selection;
import java.util.List;

public record EnrollmentSelectionRequest(
        List<Long> selectedCourseIds,
        boolean allowHigherYearSelection
) {}

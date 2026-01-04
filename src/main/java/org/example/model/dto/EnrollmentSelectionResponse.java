package org.example.model.dto;

import java.util.List;

public record EnrollmentSelectionResponse(
        Long formId,
        List<CourseInfo> selectedCourses
) {}

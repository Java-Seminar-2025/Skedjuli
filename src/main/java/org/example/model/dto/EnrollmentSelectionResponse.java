package org.example.model.dto;

import org.example.model.dto.response.CourseResponse;

import java.util.List;

public record EnrollmentSelectionResponse(
        Long formId,
        List<CourseResponse> selectedCourses
) {}

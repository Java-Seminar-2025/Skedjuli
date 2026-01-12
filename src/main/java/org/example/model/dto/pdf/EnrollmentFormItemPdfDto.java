package org.example.model.dto.pdf;

public record EnrollmentFormItemPdfDto(
        String courseCode,
        String courseName,
        Integer ects,
        Integer semester,
        String status
) {}

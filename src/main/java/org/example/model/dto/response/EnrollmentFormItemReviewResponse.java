package org.example.model.dto.response;

public record EnrollmentFormItemReviewResponse(
        String courseCode,
        String courseName,
        Integer ects,
        Integer status
) {

}

package org.example.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.model.dto.CourseInfo;
import org.example.model.dto.EnrollmentResult;
import org.example.model.dto.request.selection.EnrollmentSelectionRequest;
import org.example.model.dto.EnrollmentSelectionResponse;
import org.example.service.business.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollment")
@Validated
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<CourseInfo>> getEnrolledCourses(@PathVariable Long studentId) {
        var courses = enrollmentService.getEnrolledCoursesForYear(studentId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/student/{studentId}/selection")
    public ResponseEntity<EnrollmentSelectionResponse> getSelection(@PathVariable Long studentId) {
        var selected = enrollmentService.getSelection(studentId);
        var formId = enrollmentService.getCurrentEnrollmentFormId(studentId);
        return ResponseEntity.ok(new EnrollmentSelectionResponse(formId, selected));
    }

    @PostMapping("/student/{studentId}/selection")
    public ResponseEntity<EnrollmentSelectionResponse> saveSelection(
            @PathVariable Long studentId,
            @Valid @RequestBody EnrollmentSelectionRequest request) {

        var selectedIds = Optional.ofNullable(request.selectedCourseIds()).orElse(List.of());
        var formId = enrollmentService.saveSelection(studentId, selectedIds, request.allowHigherYearSelection());
        var selectedCourses = enrollmentService.getSelection(studentId);

        return ResponseEntity.ok(new EnrollmentSelectionResponse(formId, selectedCourses));
    }

    @PostMapping("/student/{studentId}/enroll")
    public ResponseEntity<EnrollmentResult> enrollStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody(required = false) EnrollmentSelectionRequest request) {

        var selectedIds = Optional.ofNullable(request)
                .map(EnrollmentSelectionRequest::selectedCourseIds)
                .orElse(List.of());

        var allowHigher = Optional.ofNullable(request)
                .map(EnrollmentSelectionRequest::allowHigherYearSelection)
                .orElse(false);

        enrollmentService.enrollYear(studentId, selectedIds, allowHigher);

        return ResponseEntity.ok(new EnrollmentResult("OK", "Enrollment successful"));
    }
}

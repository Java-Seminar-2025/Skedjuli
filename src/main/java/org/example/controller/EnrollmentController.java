package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.domain.dto.EnrollmentCourseResponse;
import org.example.service.business.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<EnrollmentCourseResponse>> getEnrolledCourses(@PathVariable Long studentId) {
        List<EnrollmentCourseResponse> enrolledCourses = enrollmentService.getEnrolledCoursesForYear(studentId);
        return ResponseEntity.ok(enrolledCourses);
    }

    @PostMapping("/student/{studentId}/enroll")
    public ResponseEntity<String> enrollStudent(@PathVariable Long studentId) {
        try {
            enrollmentService.enrollYear(studentId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("First-year student enrolled successfully");
    }
}
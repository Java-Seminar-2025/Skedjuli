package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.Course;
import org.example.model.Student;
import org.example.service.EnrollmentService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;

    @GetMapping("/student/{userId}/courses")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long userId) {
        Student student = userService.findStudentByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Course> enrolledCourses = enrollmentService.getEnrolledCourses(student);
        return ResponseEntity.ok(enrolledCourses);
    }

    @PostMapping("/student/{userId}/enroll")
    public ResponseEntity<String> enrollStudent(@PathVariable Long userId) {
        Student student = userService.findStudentByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getCurrentYear() != 1) {
            return ResponseEntity.badRequest().body("Only first-year students can be enrolled using this endpoint");
        }

        try {
            enrollmentService.enrollFirstYear(student);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("First-year student enrolled successfully");
    }
}
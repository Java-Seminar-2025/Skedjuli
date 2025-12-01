package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.Course;
import org.example.model.EnrollmentForm;
import org.example.model.EnrollmentFormItem;
import org.example.model.Student;
import org.example.repository.EnrollmentFormRepository;
import org.example.service.EnrollmentService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enrollment")
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final EnrollmentFormRepository enrollmentFormRepository;

    /**
     * Get enrolled courses for a student (first-year)
     * If the student has no enrollment yet, automatically enrolls first-year courses
     */
    @GetMapping("/student/{studentId}/first-year")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long studentId) {

        /*Student student = userService.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if first-year enrollment exists; if not, enroll
        boolean sem1Exists = enrollmentFormRepository.existsByStudentIdAndSemester(studentId, 1);
        boolean sem2Exists = enrollmentFormRepository.existsByStudentIdAndSemester(studentId, 2);

        if (!sem1Exists || !sem2Exists) {
            enrollmentService.enrollFirstYear(studentId);
        }

        // Fetch enrollment forms for first-year
        List<EnrollmentForm> forms = enrollmentFormRepository.findByStudentId(studentId)
                .stream()
                .filter(f -> f.getSemester() == 1 || f.getSemester() == 2)
                .collect(Collectors.toList());

        // Flatten items to get all enrolled courses
        List<Course> courses = forms.stream()
                .flatMap(f -> f.getItems().stream().map(EnrollmentFormItem::getCourse))
                .collect(Collectors.toList());

        return ResponseEntity.ok(courses);

        */
        return null;
    }
}

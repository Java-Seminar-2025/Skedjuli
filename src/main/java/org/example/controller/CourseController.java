package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.response.StudentResponse;
import org.example.model.dto.unsorted.CourseDto;
import org.example.model.dto.unsorted.CourseReadRequestDto;
import org.example.service.business.CourseTrialService;
import org.example.service.business.LecturerCourseService;
import org.example.service.business.StudentCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {
    private final StudentCourseService service;

    private final CourseTrialService courseTrialService;

    private final LecturerCourseService lecturerCourseService;

    @GetMapping("/enrolled/{id}")
    public List<CourseResponse> getStudentEnrolledCourses(@PathVariable Long id) {
        return service.getEnrolledCourses(id);
    }

    @GetMapping("/{id}")
    public CourseReadRequestDto getCourseReadRequestDto(@PathVariable Long id) {
        return courseTrialService.getCourseReadRequestDtoById(id);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CourseDto>> mine(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        return ResponseEntity.ok(courseTrialService.getMyCourses(email));
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(
            @Valid @RequestBody CourseCreateRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        CourseDto created = courseTrialService.createCourseAsLecturer(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        return ResponseEntity.ok(courseTrialService.updateMyCourse(email, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        courseTrialService.deleteMyCourse(email, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/{courseId}/students")
    public List<StudentResponse> getStudentsForCourse(@PathVariable Long courseId) {
        return lecturerCourseService.getStudentsForCourseLockedAndApproved(courseId);
    }
}

package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.create.CourseGradeCreateRequest;
import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.response.StudentResponse;
import org.example.model.dto.unsorted.CourseDto;
import org.example.model.dto.unsorted.CourseReadRequestDto;
import org.example.service.business.CourseTrialService;
import org.example.service.business.LecturerCourseService;
import org.example.service.business.LecturerGradingService;
import org.example.service.business.StudentCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final LecturerGradingService lecturerGradingService;

    @GetMapping("/enrolled/{id}")
    public List<CourseResponse> getStudentEnrolledCourses(@PathVariable Long id) {
        return service.getEnrolledCourses(id);
    }

    @GetMapping("/{id}")
    public CourseReadRequestDto getCourseReadRequestDto(@PathVariable Long id) {
        return courseTrialService.getCourseReadRequestDtoById(id);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CourseDto>> mine(@RequestParam Long lecturerId) {
        return ResponseEntity.ok(courseTrialService.getCoursesByLecturerId(lecturerId));
    }

    @PostMapping
    public ResponseEntity<CourseDto> create(@Valid @RequestBody CourseCreateRequest request) {
        CourseDto created = courseTrialService.createCourseByLecturerId(request.lecturerId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateRequest request
    ) {
        return ResponseEntity.ok(courseTrialService.updateCourseByLecturerId(request.lecturerId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long lecturerId
    ) {
        courseTrialService.deleteCourseByLecturerId(lecturerId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/students")
    public List<StudentResponse> getStudentsForCourse(@PathVariable Long courseId) {
        return lecturerCourseService.getStudentsForCourseLockedAndApproved(courseId);
    }

    @GetMapping("/students/count")
    public long getStudentCountForCourse(@RequestParam Long courseId) {
        return lecturerCourseService.getStudentCountForCourseLockedAndApproved(courseId);
    }

    @GetMapping("/my-course/students")
    public List<StudentResponse> getMyCourseStudents(@RequestParam Long courseId, @RequestParam Long lecturerId) {
        return lecturerCourseService.getStudentsForCourseLockedAndApprovedForLecturer(lecturerId, courseId);
    }

    @GetMapping("/my-course/count")
    public long getMyCourseCountForCourse(@RequestParam Long courseId, @RequestParam Long lecturerId) {
        return lecturerCourseService.getStudentCountForCourseLockedAndApprovedForLecturer(lecturerId, courseId);
    }

    @PostMapping("/my-course/grade")
    public ResponseEntity<CompletedCourseResponse> gradeStudent(@Valid @RequestBody CourseGradeCreateRequest request) {
        return ResponseEntity.ok(lecturerGradingService.upsertGrade(request));
    }
}

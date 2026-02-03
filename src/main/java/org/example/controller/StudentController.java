package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.dto.request.patch.StudentPatchRequest;
import org.example.model.dto.response.SemesterCoursesResponse;
import org.example.model.dto.response.StudentAnalyticsResponse;
import org.example.model.dto.response.StudentResponse;
import org.example.service.business.StudentAnalyticsService;
import org.example.service.business.StudentCourseOverviewService;
import org.example.service.business.StudentCourseService;
import org.example.service.domain.StudentDomainService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentDomainService service;
    private final StudentAnalyticsService analyticsService;
    private final StudentCourseOverviewService studentCourseOverviewService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        var created = service.createStudent(request);
        return ResponseEntity
                .created(URI.create("/api/students/" + created.id()))
                .body(created);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudent(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> patchStudent(@PathVariable Long id, @Valid @RequestBody StudentPatchRequest request) {
        return ResponseEntity.ok(service.patchStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) Long studyProgramId,
            @RequestParam(required = false) Integer enrollmentYear,
            @RequestParam(required = false) Integer currentYear,
            @RequestParam(required = false) Double totalEctsEarned,
            @RequestParam(required = false) Boolean isActive
            ) {
        return ResponseEntity.ok(service.getStudents(page, size, sortBy, sortOrder,studyProgramId, enrollmentYear, currentYear, totalEctsEarned, isActive));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<StudentAnalyticsResponse> getStudentAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getStudentAnalytics(id));
    }

    @GetMapping("/semester/courses")
    public List<SemesterCoursesResponse> getCoursesBySemesterForStudent(@RequestParam Long studentId) {
        return studentCourseOverviewService.getCoursesBySemesterWithStatus(studentId);
    }

    @GetMapping("/semester/courses/completed")
    public List<SemesterCoursesResponse> getCompletedCourses(
            @RequestParam Long studentId
    ) {
        return studentCourseOverviewService.getCompletedCoursesBySemester(studentId);
    }

    @GetMapping("/semester/courses/enrolled")
    public List<SemesterCoursesResponse> getEnrolledCourses(
            @RequestParam Long studentId
    ) {
        return studentCourseOverviewService.getEnrolledCoursesBySemester(studentId);
    }

    @GetMapping("/semester/courses/available")
    public List<SemesterCoursesResponse> getAvailableCourses(
            @RequestParam Long studentId
    ) {
        return studentCourseOverviewService.getAvailableCoursesBySemester(studentId);
    }

}

package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.model.dto.request.patch.CompletedCoursePatchRequest;
import org.example.service.domain.CompletedCourseDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/completedCourses")
@RequiredArgsConstructor
public class CompletedCourseController {
    private final CompletedCourseDomainService service;

    @PostMapping
    public ResponseEntity<CompletedCourseResponse> createCompletedCourse(@Valid @RequestBody CompletedCourseCreateRequest request) {
        var created = service.createCompletedCourse(request);
        return ResponseEntity
                .created(URI.create("/api/completedCourses/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompletedCourseResponse> getCompletedCourse(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompletedCourseResponse> patchCompletedCourse(@PathVariable Long id, @Valid @RequestBody CompletedCoursePatchRequest request) {
        return ResponseEntity.ok(service.patchCompletedCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompletedCourse(@PathVariable Long id) {
        service.deleteCompletedCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student")
    public ResponseEntity<List<CompletedCourseResponse>> getAllCompletedCoursesByStudent(@RequestParam Long studentId) {
        return ResponseEntity.ok(service.getByStudentId(studentId));
    }
}

package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.model.dto.CourseInfo;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.service.domain.CourseDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {
    private final CourseDomainService courseDomainService;

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        if (request == null)
            throw new IllegalArgumentException("CourseCreateRequest is null");
        courseDomainService.createCourse(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseInfo> getCourse(@PathVariable Long id) {
        if (id == null)
            throw new IllegalArgumentException("CourseId is null");
        return ResponseEntity.ok(courseDomainService.getCourseInfoById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchCourse(@PathVariable Long id, @Valid @RequestBody CoursePatchRequest request) {
        if (request == null)
            throw new IllegalArgumentException("CoursePatchRequest is null");
        courseDomainService.patchCourse(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseDomainService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

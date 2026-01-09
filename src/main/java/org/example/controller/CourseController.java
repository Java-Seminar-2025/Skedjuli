package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.dto.response.CourseResponse;
import org.example.service.domain.CourseDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {
    private final CourseDomainService service;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        var created = service.createCourse(request);
        return ResponseEntity
                .created(URI.create("/api/courses/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCourseInfoById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponse> patchCourse(@PathVariable Long id, @Valid @RequestBody CoursePatchRequest request) {
        return ResponseEntity.ok(service.patchCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        service.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

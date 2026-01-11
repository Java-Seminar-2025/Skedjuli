package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseCreateRequest;
import org.example.model.dto.CourseDto;
import org.example.model.dto.CourseReadRequestDto;
import org.example.service.domain.CourseDomainService;
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

    private final CourseDomainService courseDomainService;

    @GetMapping("/{id}")
    public CourseReadRequestDto getCourseReadRequestDto(@PathVariable Long id) {
        return courseDomainService.getCourseReadRequestDtoById(id);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CourseDto>> mine(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        return ResponseEntity.ok(courseDomainService.getMyCourses(email));
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
        CourseDto created = courseDomainService.createCourseAsLecturer(email, request);
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
        return ResponseEntity.ok(courseDomainService.updateMyCourse(email, id, request));
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
        courseDomainService.deleteMyCourse(email, id);
        return ResponseEntity.noContent().build();
    }
}

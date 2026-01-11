package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.create.CourseRequirementCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.dto.request.patch.CourseRequirementPatchRequest;
import org.example.model.dto.response.CourseRequirementResponse;
import org.example.model.dto.response.CourseResponse;
import org.example.service.business.CourseManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/coursesManagement")
@Validated
@RequiredArgsConstructor
public class CourseManagementController {

    private final CourseManagementService service;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourseWithRequirements(@Valid @RequestBody CourseCreateRequest request) {
        var created = service.createCourseWithRequirements(request);
        return ResponseEntity
                .created(URI.create("/api/coursesManagement" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseWithRequirements(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCourseWithRequirements(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponse> patchCourseWithRequirements(@PathVariable Long id, @Valid @RequestBody CoursePatchRequest request) {
        return ResponseEntity.ok(service.patchCourseWithRequirements(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseWithRequirements(@PathVariable Long id) {
        service.deleteCourseWithRequirements(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requirements")
    public ResponseEntity<CourseRequirementResponse> createCourseRequirement(@Valid @RequestBody CourseRequirementCreateRequest request) {
        var created = service.createCourseRequirement(request);
        return ResponseEntity
                .created(URI.create("/api/coursesManagement/requirements/" + created.id()))
                .body(created);
    }

    @GetMapping("/{courseId}/requirements")
    public ResponseEntity<List<CourseRequirementResponse>> getCourseRequirements(@PathVariable Long courseId) {
        var requirements = service.getCourseRequirements(courseId);
        return ResponseEntity.ok(requirements);
    }

    @PatchMapping("/requirements/{id}")
    public ResponseEntity<CourseRequirementResponse> patchCourseRequirement(@PathVariable Long id,
                                                                            @Valid @RequestBody CourseRequirementPatchRequest request) {
        return ResponseEntity.ok(service.patchCourseRequirement(id, request));
    }

    @DeleteMapping("/requirements/{id}")
    public ResponseEntity<Void> deleteCourseRequirement(@PathVariable Long id) {
        service.deleteCourseRequirement(id);
        return ResponseEntity.noContent().build();
    }
}

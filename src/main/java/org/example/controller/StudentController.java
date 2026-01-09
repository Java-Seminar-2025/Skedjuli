package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.dto.request.patch.StudentPatchRequest;
import org.example.model.dto.response.StudentResponse;
import org.example.service.domain.StudentDomainService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentDomainService service;

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
}

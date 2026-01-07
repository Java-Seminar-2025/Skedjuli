package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CreateStudentRequest;
import org.example.model.dto.StudentPatchRequest;
import org.example.model.dto.StudentResponse;
import org.example.service.domain.StudentDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentDomainService studentDomainService;

    @PatchMapping("/{studentId}")
    public ResponseEntity<Void> patchStudent(@PathVariable Long studentId, @Valid @RequestBody StudentPatchRequest request) {
        studentDomainService.patchStudent(studentId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        studentDomainService.createStudent(request.userId(), request.studyProgramId(), request.enrollmentYear(),request.currentYear());
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/{studentId}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentDomainService.getStudent(studentId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentDomainService.deleteStudent(studentId);
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
        return ResponseEntity.ok(studentDomainService.getStudents(page, size, sortBy, sortOrder,studyProgramId, enrollmentYear, currentYear, totalEctsEarned, isActive));
    }
}

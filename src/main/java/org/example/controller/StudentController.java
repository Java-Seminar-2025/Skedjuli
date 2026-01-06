package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudentPatchRequest;
import org.example.service.business.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PatchMapping("/{studentId}")
    public ResponseEntity<Void> patchStudent(@PathVariable Long studentId, @Valid @RequestBody StudentPatchRequest request) {
        studentService.patchStudent(studentId, request);
        return ResponseEntity.noContent().build();
    }


}

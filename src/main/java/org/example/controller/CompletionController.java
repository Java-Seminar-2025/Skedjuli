package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.model.dto.CompletedCourseDto;
import org.example.service.business.CompletionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/completedCourses")
@Validated
@RequiredArgsConstructor
public class CompletionController {

    private final CompletionService completionService;

    @PostMapping
    public ResponseEntity<CompletedCourseDto> markCompleted(@Valid @RequestBody CompletedCourseCreateRequest request) {
        var dto = completionService.markCompleted(request);
        return ResponseEntity.ok(dto);
    }
}
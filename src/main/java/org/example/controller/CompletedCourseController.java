package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CompletedCourseDto;
import org.example.service.domain.CompletedCourseDomainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/completedCourse")
@RequiredArgsConstructor
public class CompletedCourseController {
    private final CompletedCourseDomainService service;

    @PostMapping
    public ResponseEntity<CompletedCourseDto> create(@RequestBody CompletedCourseDto dto) {
        var created = service.createCompletedCourse(
                dto.studentId(),
                dto.courseId(),
                dto.grade(),
                dto.completionDate(),
                dto.academicYearId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompletedCourseDto> get(@PathVariable Long id) {
        var dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompletedCourseDto> update(@PathVariable Long id, @RequestBody CompletedCourseDto dto) {
        var updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

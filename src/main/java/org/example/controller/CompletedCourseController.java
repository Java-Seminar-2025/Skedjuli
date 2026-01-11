package org.example.controller;

<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CompletedCourseDto;
import org.example.service.domain.CompletedCourseDomainService;
import org.springframework.http.HttpStatus;
=======
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.model.dto.request.patch.CompletedCoursePatchRequest;
import org.example.service.domain.CompletedCourseDomainService;
>>>>>>> origin/enrollment
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
@RestController
@Validated
@RequestMapping("/api/completedCourse")
=======
import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/completedCourses")
>>>>>>> origin/enrollment
@RequiredArgsConstructor
public class CompletedCourseController {
    private final CompletedCourseDomainService service;

    @PostMapping
<<<<<<< HEAD
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
=======
    public ResponseEntity<CompletedCourseResponse> createCompletedCourse(@Valid @RequestBody CompletedCourseCreateRequest request) {
        var created = service.createCompletedCourse(request);
        return ResponseEntity
                .created(URI.create("/api/completedCourses/" + created.id()))
                .body(created);
    }

    @GetMapping("{id}")
    public ResponseEntity<CompletedCourseResponse> getCompletedCourse(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CompletedCourseResponse> patchCompletedCourse(@PathVariable Long id, @Valid @RequestBody CompletedCoursePatchRequest request) {
        return ResponseEntity.ok(service.patchCompletedCourse(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCompletedCourse(@PathVariable Long id) {
        service.deleteCompletedCourse(id);
>>>>>>> origin/enrollment
        return ResponseEntity.noContent().build();
    }
}

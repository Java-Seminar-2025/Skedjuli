package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.LecturerCreateRequest;
import org.example.model.dto.request.patch.LecturerPatchRequest;
import org.example.model.dto.response.LecturerResponse;
import org.example.service.domain.LecturerDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lecturers")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerDomainService service;

    @PostMapping
    public ResponseEntity<LecturerResponse> createLecturer(@Valid @RequestBody LecturerCreateRequest request) {
        var created = service.createLecturer(request);
        return ResponseEntity
                .created(URI.create("/lecturers/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturerResponse> getLecturerById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLecturerById(id));
    }

    @GetMapping
    public ResponseEntity<List<LecturerResponse>> getAllLecturers() {
        return ResponseEntity.ok(service.getAllLecturers());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LecturerResponse> patchLecturer(@PathVariable Long id, @Valid @RequestBody LecturerPatchRequest request) {
        return ResponseEntity.ok(service.patchLecturer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        service.deleteLecturer(id);
        return ResponseEntity.noContent().build();
    }
}

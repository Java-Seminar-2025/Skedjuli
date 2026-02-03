package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.AcademicYearCreateRequest;
import org.example.model.dto.request.patch.AcademicYearPatchRequest;
import org.example.model.dto.response.AcademicYearResponse;
import org.example.service.domain.AcademicYearDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/academicYears")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearDomainService service;

    @PostMapping
    public ResponseEntity<AcademicYearResponse> createAcademicYear(@Valid @RequestBody AcademicYearCreateRequest request) {

        var created = service.createAcademicYear(request);
        return ResponseEntity
                .created(URI.create("/api/academicYears/" + created.id()))
                .body(created);
    }
    
    @GetMapping("/active")
    public ResponseEntity<AcademicYearResponse> getActiveAcademicYear() {
        return ResponseEntity.ok(service.getActiveAcademicYear());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearResponse> getAcademicYearById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAcademicYearById(id));
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearResponse>> getAllAcademicYears() {
        return ResponseEntity.ok(service.getAllAcademicYears());
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<AcademicYearResponse> patchAcademicYear(@PathVariable Long id, @Valid @RequestBody AcademicYearPatchRequest request) {
        return ResponseEntity.ok(service.patchAcademicYear(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicYear(@PathVariable Long id) {
        service.deleteAcademicYear(id);
        return ResponseEntity.noContent().build();
    }
}

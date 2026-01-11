package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.StudyProgramCreateRequest;
import org.example.model.dto.request.patch.StudyProgramPatchRequest;
import org.example.model.dto.response.StudyProgramResponse;
import org.example.service.domain.StudyProgramDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/studyPrograms")
@RequiredArgsConstructor
public class StudyProgramController {

    private final StudyProgramDomainService service;

    @PostMapping
    public ResponseEntity<StudyProgramResponse> createStudyProgram(@Valid @RequestBody StudyProgramCreateRequest request) {
        var created = service.createStudyProgram(request);

        return ResponseEntity
                .created(URI.create("/studyPrograms/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyProgramResponse> getStudyProgramById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudyProgramById(id));
    }

    @GetMapping("/allStudyPrograms")
    public ResponseEntity<List<StudyProgramResponse>> getAllStudyPrograms() {
        return ResponseEntity.ok(service.getAllStudyPrograms());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudyProgramResponse> patchStudyProgram(@PathVariable Long id, @Valid @RequestBody StudyProgramPatchRequest request) {
        return ResponseEntity.ok(service.patchStudyProgram(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyProgram(@PathVariable Long id) {
        service.deleteStudyProgram(id);
        return ResponseEntity.noContent().build();
    }
}

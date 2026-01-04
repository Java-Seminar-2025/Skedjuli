package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.AcademicYearCreateRequest;
import org.example.model.dto.AcademicYearDto;
import org.example.service.business.ActiveYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academicYears")
@Validated
@RequiredArgsConstructor
public class ActiveYearController {

    private final ActiveYearService activeYearService;

    @PostMapping
    public ResponseEntity<AcademicYearDto> add(@Valid @RequestBody AcademicYearCreateRequest request) {
        var dto = activeYearService.addAcademicYear(request);
        return ResponseEntity.ok(dto);
    }
}
package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseReadRequestDto;
import org.example.service.domain.CourseDomainService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {
    private final CourseDomainService courseDomainService;
    @GetMapping("/{id}")
    public CourseReadRequestDto getCourseReadRequestDto(@PathVariable Long id) {
        return courseDomainService.getCourseReadRequestDtoById(id);
    }
}

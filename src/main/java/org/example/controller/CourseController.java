package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CourseResponse;
import org.example.service.business.StudentCourseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Validated
@RequiredArgsConstructor
public class CourseController {
    private final StudentCourseService service;

    @GetMapping("/enrolled/{id}")
    public List<CourseResponse> getStudentEnrolledCourses (@PathVariable Long id){
        return service.getEnrolledCourses(id);
    }
}

package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.UpdateStudentYearRequest;
import org.example.service.business.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PutMapping("/year")
    public void updateYear(@RequestBody UpdateStudentYearRequest request) {
        studentService.updateCurrentYear(request.studentId(), request.newYear());
    }
}
